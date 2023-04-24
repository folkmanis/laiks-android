package com.folkmanis.laiks.ui.screens.prices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.INCLUDE_AVERAGE_DAYS
import com.folkmanis.laiks.data.domain.AppliancesCostsUseCase
import com.folkmanis.laiks.data.domain.LastDaysPricesUseCase
import com.folkmanis.laiks.model.NpPrice
import com.folkmanis.laiks.model.PowerApplianceHour
import com.folkmanis.laiks.utilities.ext.*
import com.folkmanis.laiks.utilities.hourTicks
import com.folkmanis.laiks.utilities.minuteTicks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class PricesViewModel @Inject constructor(
    lastDaysPrices: LastDaysPricesUseCase,
    private val appliancesCosts: AppliancesCostsUseCase,
) : ViewModel() {

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
            appliancesCosts(prices, minute)
        }


    companion object {
        @Suppress("unused")
        private const val TAG = "PricesViewModel"
    }
}