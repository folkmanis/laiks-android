package com.folkmanis.laiks.ui.screens.prices

import android.util.Log
import androidx.lifecycle.ViewModel
import com.folkmanis.laiks.INCLUDE_AVERAGE_DAYS
import com.folkmanis.laiks.SHOW_HOURS_BEFORE
import com.folkmanis.laiks.VAT
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
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import javax.inject.Inject

fun List<NpPrice>.addVat(amount: Double): List<NpPrice> =
    map { it.copy(value = it.value.withVat(amount)) }

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PricesViewModel @Inject constructor(
    private val pricesService: PricesService,
    userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {

    private val vatAmount = userPreferencesRepository.includeVat
        .map { if (it) VAT else 1.0 }

    val uiState: Flow<PricesUiState> = pricesService
        .lastDaysPrices(INCLUDE_AVERAGE_DAYS)
        .combine(vatAmount) { prices, vat ->
            prices.addVat(vat)
        }
        .map { prices ->
            PricesUiState.Success(
                npPrices = prices,
                average = prices.average,
                stDev = prices.stDev(),
            )
        }
        .combine(hourTicks()) { state, hour ->
            val npPrices = state.npPrices
                .filter { it.startTime.toLocalDateTime() >= hour }
            state.copy(npPrices = npPrices)
        }
        .combine(pricesService.activeAppliances) { state, appliances ->
            state.copy(appliances = appliances)
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

    private fun startTime(localDateTime: LocalDateTime): Timestamp {
        val instant: Instant = localDateTime
            .minusHours(SHOW_HOURS_BEFORE)
            .truncatedTo(ChronoUnit.HOURS)
            .atZone(ZoneId.systemDefault())
            .toInstant()
        return Timestamp(instant.epochSecond, 0)
    }

    companion object {
        @Suppress("unused")
        private const val TAG = "PricesViewModel"
    }
}