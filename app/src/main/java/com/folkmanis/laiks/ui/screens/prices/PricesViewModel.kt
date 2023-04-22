package com.folkmanis.laiks.ui.screens.prices

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.INCLUDE_AVERAGE_DAYS
import com.folkmanis.laiks.R
import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.data.AppliancesService
import com.folkmanis.laiks.data.NpUpdateService
import com.folkmanis.laiks.data.PricesService
import com.folkmanis.laiks.data.domain.LastDaysPricesUseCase
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
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PricesViewModel @Inject constructor(
//    pricesService: PricesService,
    appliancesService: AppliancesService,
    private val npUpdateService: NpUpdateService,
    accountService: AccountService,
    lastDaysPrices: LastDaysPricesUseCase,
) : ViewModel() {

    val npUploadAllowed: Flow<Boolean> = accountService
        .laiksUserFlow
        .map { it?.npUploadAllowed ?: false }

    private val lastPricesFlow: Flow<List<NpPrice>> =
        lastDaysPrices(INCLUDE_AVERAGE_DAYS)
            .shareIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000L)
            )

    val pricesStatistics: Flow<PricesStatistics> =
        lastPricesFlow
            .map { prices ->
                PricesStatistics(
                    average = prices.average,
                    stDev = prices.stDev(),
                )
            }

    val uiState: Flow<PricesUiState> = combine(hourTicks(), lastPricesFlow) { hour, prices ->
        val groupedPrices = prices
            .filter { it.startTime.toLocalDateTime() >= hour }
            .groupBy { price ->
                price.startTime.toLocalDateTime().toLocalDate()
            }
        PricesUiState.Success(groupedPrices, hour)
    }

    val appliancesState: Flow<Map<Int, List<PowerApplianceHour>>> =
        combine(minuteTicks(), lastPricesFlow) { minute, prices ->
            val appliances = appliancesService.activeAppliances()
            appliancesCostsFromMinute(
                prices,
                minute,
                appliances
            )
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