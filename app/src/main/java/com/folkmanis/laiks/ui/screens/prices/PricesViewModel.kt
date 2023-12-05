package com.folkmanis.laiks.ui.screens.prices

import androidx.lifecycle.ViewModel
import com.folkmanis.laiks.data.domain.AppliancesCostsUseCase
import com.folkmanis.laiks.data.domain.HourlyPricesUseCase
import com.folkmanis.laiks.data.domain.StatisticsUseCase
import com.folkmanis.laiks.model.NpPrice
import com.folkmanis.laiks.model.PowerApplianceHour
import com.folkmanis.laiks.model.PricesStatistics
import com.folkmanis.laiks.utilities.ext.*
import com.folkmanis.laiks.utilities.hourTicks
import com.folkmanis.laiks.utilities.minuteTicks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PricesViewModel @Inject constructor(
    appliancesCosts: AppliancesCostsUseCase,
    statistics: StatisticsUseCase,
    hourlyPrices: HourlyPricesUseCase,
) : ViewModel()  {


    val pricesStatistics: Flow<PricesStatistics> = statistics()

    val uiState: Flow<PricesUiState> = hourTicks()
        .flatMapLatest { hour ->
           hourlyPrices(
                hour
                    .truncatedTo(ChronoUnit.DAYS)
            )
                .map { prices ->
                    if (prices.isEmpty()) {
                        PricesUiState.Loading
                    } else {
                        val groupedPrices = prices
                            .eurMWhToCentsKWh()
                            .groupBy { it.startTime.toLocalDateTime().toLocalDate() }
                        PricesUiState.Success(
                            groupedPrices = groupedPrices,
                            hour = hour,
                            currentOffsetIndex = currentOffsetIndex(groupedPrices, hour)
                        )
                    }
                }
        }

    val appliancesState: Flow<Map<Int, List<PowerApplianceHour>>> = minuteTicks()
        .flatMapLatest { minute ->
            hourlyPrices(minute)
                .map { prices ->
                    appliancesCosts(prices, minute)
                }
        }

    companion object {
        @Suppress("unused")
        private const val TAG = "PricesViewModel"

        fun currentOffsetIndex(
            groupedPrices: Map<LocalDate, List<NpPrice>>,
            hour: LocalDateTime,
        ): Int {
            var zeroIdx = 0
            groupedPrices.forEach { (_, prices) ->
                zeroIdx++
                val idx = prices.indexOfFirst { it.startTime.hoursFrom(hour) == 0 }
                if (idx != -1) {
                    zeroIdx += idx
                    return zeroIdx
                } else {
                    zeroIdx += prices.size
                }
            }
            return 0
        }

    }
}