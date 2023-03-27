package com.folkmanis.laiks.ui.screens.prices

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.folkmanis.laiks.LaiksApplication
import com.folkmanis.laiks.SHOW_HOURS_BEFORE
import com.folkmanis.laiks.VAT
import com.folkmanis.laiks.data.PricesService
import com.folkmanis.laiks.data.UserPreferencesRepository
import com.folkmanis.laiks.model.NpPrice
import com.folkmanis.laiks.model.PowerAppliance
import com.folkmanis.laiks.model.PowerApplianceHour
import com.folkmanis.laiks.model.PowerHour
import com.folkmanis.laiks.utilities.bestOffset
import com.folkmanis.laiks.utilities.ext.eurMWhToCentsKWh
import com.folkmanis.laiks.utilities.ext.hoursFrom
import com.folkmanis.laiks.utilities.ext.toLocalDateTime
import com.folkmanis.laiks.utilities.ext.withVat
import com.folkmanis.laiks.utilities.hourTicks
import com.folkmanis.laiks.utilities.minuteTicks
import com.folkmanis.laiks.utilities.offsetCosts
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

fun List<NpPrice>.addVat(amount: Double): List<NpPrice> =
    map { it.copy(value = it.value.withVat(amount)) }

@OptIn(ExperimentalCoroutinesApi::class)
class PricesViewModel(
    private val pricesService: PricesService,
    userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {

    private val vatAmount = userPreferencesRepository.includeVat
        .map { if (it) VAT else 1.0 }

    val uiState: Flow<PricesUiState> = hourTicks()
        .flatMapLatest { localDateTime ->
            pricesService.allNpPrices(startTime(localDateTime))
        }
        .flatMapLatest { prices ->
            vatAmount.map {
                prices.addVat(it)
            }
        }
        .combine(pricesService.activeAppliances) { prices, appliances ->
            Pair(prices, appliances)
        }
        .combine(minuteTicks()) { pricesAppliancesPair, minute ->
            Log.d(TAG, "pricesAppliancesPair: $pricesAppliancesPair")
            calculateCosts(
                prices = pricesAppliancesPair.first,
                minute = minute,
                appliances = pricesAppliancesPair.second,
            )
        }
        .map { powerHour ->
            PricesUiState.Success(
                groupedPrices = powerHour.groupBy {
                    it.startTime.toLocalDate()
                }
            )
        }
//        .catch {
//            if (it is FirebaseFirestoreException)
//                PricesUiState.Error(it.message ?: "Firebase error", it)
//            else
//                throw it
//        }


    private fun calculateCosts(
        prices: List<NpPrice>,
        minute: LocalDateTime,
        appliances: List<PowerAppliance>,
    ): List<PowerHour> {

        val appliancesAllCosts = buildMap {
            appliances.forEach { appliance ->
                val costs = offsetCosts(prices, minute, appliance)
                put(appliance, costs)
                Log.d(TAG, "Best offset: ${appliance.name}, max offset ${costs.minBy { it.key }}")
            }
        }
        val bestOffsets = buildMap {
            appliancesAllCosts.forEach { (powerAppliance, costs) ->
                val bestOffset = costs.bestOffset()
                Log.d(TAG, "Best offset: $bestOffset")
                if (bestOffset != null) {
                    put(powerAppliance, bestOffset.toInt())
                }
            }
        }


        return prices.map { price ->
            val offset = price.startTime.hoursFrom(minute)
            val appliancesHours = buildList {
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
        private const val TAG = "PricesViewModel"
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LaiksApplication)
                PricesViewModel(
                    application.pricesService,
                    application.userPreferencesRepository,
                )
            }
        }
    }
}