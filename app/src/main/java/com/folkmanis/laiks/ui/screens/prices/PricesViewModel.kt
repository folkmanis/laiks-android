package com.folkmanis.laiks.ui.screens.prices

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.INCLUDE_AVERAGE_DAYS
import com.folkmanis.laiks.R
import com.folkmanis.laiks.VAT
import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.data.AppliancesService
import com.folkmanis.laiks.data.NpUpdateService
import com.folkmanis.laiks.data.PricesService
import com.folkmanis.laiks.data.UserPreferencesRepository
import com.folkmanis.laiks.model.NpPrice
import com.folkmanis.laiks.model.PowerAppliance
import com.folkmanis.laiks.model.PowerApplianceHour
import com.folkmanis.laiks.utilities.bestOffset
import com.folkmanis.laiks.utilities.ext.*
import com.folkmanis.laiks.utilities.hourTicks
import com.folkmanis.laiks.utilities.minuteTicks
import com.folkmanis.laiks.utilities.offsetCosts
import com.folkmanis.laiks.utilities.snackbar.SnackbarManager
import com.folkmanis.laiks.utilities.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

fun List<NpPrice>.addVat(amount: Double): List<NpPrice> =
    map { it.copy(value = it.value.withVat(amount)) }

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PricesViewModel @Inject constructor(
    pricesService: PricesService,
    userPreferencesRepository: UserPreferencesRepository,
    appliancesService: AppliancesService,
    private val npUpdateService: NpUpdateService,
    accountService: AccountService,
) : ViewModel() {

    private val vatAmount = userPreferencesRepository.includeVat
        .map { if (it) VAT else 1.0 }

    val npUploadAllowed: Flow<Boolean> = accountService
        .laiksUserFlow
        .map { it?.npUploadAllowed ?: false }

    val pricesStatistics: Flow<PricesStatistics> = pricesService
        .lastDaysPricesFlow(INCLUDE_AVERAGE_DAYS)
        .filter(List<NpPrice>::isNotEmpty)
        .combine(vatAmount) { prices, vat ->
            prices.addVat(vat)
        }
        .map { prices ->
            PricesStatistics(
                average = prices.average,
                stDev = prices.stDev(),
            )
        }

    val appliancesState: Flow<Map<Int, List<PowerApplianceHour>>> = hourTicks()
        .flatMapLatest { hour ->
            pricesService.pricesFromDateTime(hour)
        }
        .combine(vatAmount) { prices, vat ->
            prices.addVat(vat)
        }
        .combine(minuteTicks()) { prices, minute ->
            val appliances = appliancesService.activeAppliances()
            appliancesCostsFromMinute(
                prices, minute, appliances
            )
        }

    val uiState: Flow<PricesUiState> = hourTicks()
        .flatMapLatest { hour ->
            pricesService.pricesFromDateTime(hour.minusHours(2))
                .combine(vatAmount) { prices, vat ->
                    prices.addVat(vat)
                }
                .map { prices ->
                    val groupedPrices = prices.groupBy { price ->
                        price.startTime.toLocalDateTime().toLocalDate()
                    }
                    PricesUiState.Success(groupedPrices, hour)
                }
        }

    private fun appliancesCostsFromMinute(
        prices: List<NpPrice>,
        minute: LocalDateTime,
        appliances: List<PowerAppliance>,
    ): Map<Int, List<PowerApplianceHour>> {

        val startTime = minute.atZone(ZoneId.systemDefault()).toInstant()
        val appliancesAllCosts: Map<PowerAppliance, Map<Long, Double>> =
            buildMap {
                appliances.forEach { appliance ->
                    val costs = offsetCosts(prices, startTime, appliance)
                    put(appliance, costs)
                }
            }

        val bestOffsets = buildMap {
            appliancesAllCosts.forEach { (powerAppliance, costs) ->
                val bestOffset = costs
                    .bestOffset()
                if (bestOffset != null) {
                    put(powerAppliance, bestOffset)
                }
            }
        }

        val hourlyCosts: Map<Int, List<PowerApplianceHour>> = buildMap {
            prices.forEach { npPrice ->
                val offset = npPrice.startTime.hoursFrom(minute)
                val appliancesHour = appliances.toPowerApplianceHour(
                    appliancesAllCosts,
                    offset,
                    bestOffsets
                )
                put(offset, appliancesHour)
            }
        }

        return hourlyCosts

    }

    private fun List<PowerAppliance>.toPowerApplianceHour(
        appliancesAllCosts: Map<PowerAppliance, Map<Long, Double>>,
        offset: Int,
        bestOffsets: Map<PowerAppliance, Int>
    ): List<PowerApplianceHour> {
        val appliances = this
        return buildList {
            appliances.forEach { appliance ->
                val applianceHourCost = appliancesAllCosts[appliance]
                    ?.get(offset.toLong())
                if (applianceHourCost != null) {
                    add(
                        PowerApplianceHour(
                            name = appliance.name,
                            cost = applianceHourCost,
                            color = appliance.color,
                            isBest = bestOffsets[appliance] == offset,
                        )
                    )
                }
            }
        }
    }

    fun updateNpPrices() {
        Log.d(TAG, "NpUpdate requested")
        viewModelScope.launch {
            try {
                val newRecords = npUpdateService.updateNpPrices()
                Log.d(TAG, "$newRecords retrieved")
                SnackbarManager.showMessage(
                    R.plurals.prices_retrieved,
                    newRecords,
                    newRecords
                )
            } catch (err: Throwable) {
                SnackbarManager.showMessage(
                    err.toSnackbarMessage()
                )
                Log.e(TAG, "Error: $err")
            }
        }
    }

    companion object {
        @Suppress("unused")
        private const val TAG = "PricesViewModel"
    }
}