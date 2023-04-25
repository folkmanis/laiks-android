package com.folkmanis.laiks.ui.screens.prices

import com.folkmanis.laiks.data.domain.AppliancesCostsUseCase
import com.folkmanis.laiks.data.domain.DelayToNextNpUpdateUseCase
import com.folkmanis.laiks.data.domain.HourlyPricesUseCase
import com.folkmanis.laiks.data.domain.NpUpdateUseCase
import com.folkmanis.laiks.data.domain.StatisticsUseCase
import com.folkmanis.laiks.model.PowerApplianceHour
import com.folkmanis.laiks.utilities.ext.*
import com.folkmanis.laiks.utilities.hourTicks
import com.folkmanis.laiks.utilities.minuteTicks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PricesViewModel @Inject constructor(
    appliancesCosts: AppliancesCostsUseCase,
    statistics: StatisticsUseCase,
    hourlyPrices: HourlyPricesUseCase,
    npUpdate: NpUpdateUseCase,
    delayToNextNpUpdate: DelayToNextNpUpdateUseCase,
) : PricesUpdateViewModel(npUpdate, delayToNextNpUpdate) {


    val pricesStatistics: Flow<PricesStatistics> = statistics()

    val uiState: Flow<PricesUiState> = hourTicks()
        .flatMapLatest { hour ->
            hourlyPrices(hour)
                .map { prices ->
                    if (prices.isEmpty())
                        PricesUiState.Loading
                    else
                        PricesUiState.Success(
                            prices.groupBy { it.startTime.toLocalDateTime().toLocalDate() },
                            hour
                        )

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
    }
}