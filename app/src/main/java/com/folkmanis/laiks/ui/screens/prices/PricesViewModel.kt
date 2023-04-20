package com.folkmanis.laiks.ui.screens.prices

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.INCLUDE_AVERAGE_DAYS
import com.folkmanis.laiks.VAT
import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.data.AppliancesService
import com.folkmanis.laiks.data.NpUpdateService
import com.folkmanis.laiks.data.PricesService
import com.folkmanis.laiks.data.UserPreferencesRepository
import com.folkmanis.laiks.model.NpPrice
import com.folkmanis.laiks.model.PowerAppliance
import com.folkmanis.laiks.model.PowerApplianceHour
import com.folkmanis.laiks.model.PowerHour
import com.folkmanis.laiks.utilities.bestOffset
import com.folkmanis.laiks.utilities.ext.*
import com.folkmanis.laiks.utilities.hourTicks
import com.folkmanis.laiks.utilities.minuteTicks
import com.folkmanis.laiks.utilities.offsetCosts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

fun List<NpPrice>.addVat(amount: Double): List<NpPrice> =
    map { it.copy(value = it.value.withVat(amount)) }

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

    val uiState: Flow<PricesUiState> = pricesService
        .lastDaysPricesFlow(INCLUDE_AVERAGE_DAYS)
        .combine(vatAmount) { prices, vat ->
            prices.addVat(vat)
        }
        .map { prices ->
            Log.d(TAG, "Prices ${prices.size} records")
            if (prices.isNotEmpty()) {
                PricesUiState.Success(
                    npPrices = prices,
                    average = prices.average,
                    stDev = prices.stDev(),
                )
            } else {
                PricesUiState.Success(npPrices = prices)
            }
        }
        .combine(hourTicks()) { state, hour ->
            val npPrices = state.npPrices
                .filter { it.startTime.toLocalDateTime() >= hour }
            state.copy(npPrices = npPrices)
        }
        .map { state ->
            state.copy(appliances = appliancesService.activeAppliances())
        }
        .combine(minuteTicks()) { state, minute ->
            state.copy(minute = minute)
        }
        .map { state ->
            val powerHours = appliancesCostsFromMinute(
                prices = state.npPrices,
                minute = state.minute,
                appliances = state.appliances,
            )
            state.copy(
                groupedCosts = powerHours.groupBy {
                    it.startTime.toLocalDate()
                }
            )
        }

    private fun appliancesCostsFromMinute(
        prices: List<NpPrice>,
        minute: LocalDateTime,
        appliances: List<PowerAppliance>,
    ): List<PowerHour> {

        val startTime = minute.atZone(ZoneId.systemDefault()).toInstant()
        val appliancesAllCosts = buildMap {
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

        val powerHours = prices.map { price ->
            val offset = price.startTime.hoursFrom(minute)
            val appliancesHours = appliances.toPowerApplianceHour(
                appliancesAllCosts,
                offset,
                bestOffsets
            )
            PowerHour(
                id = price.id,
                offset = offset,
                minute = minute,
                price = price.value,
                startTime = price.startTime.toLocalDateTime(),
                endTime = price.endTime.toLocalDateTime(),
                appliancesHours = appliancesHours,
            )
        }

        return powerHours
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
                npUpdateService.updateNpPrices()
            } catch (err: Throwable) {
                Log.e(TAG, "Error: $err")
            }
        }
    }

    companion object {
        @Suppress("unused")
        private const val TAG = "PricesViewModel"
    }
}