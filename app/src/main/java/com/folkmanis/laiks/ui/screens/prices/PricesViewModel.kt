package com.folkmanis.laiks.ui.screens.prices

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.data.domain.AppliancesCostsUseCase
import com.folkmanis.laiks.data.domain.CurrentMarketZoneUseCase
import com.folkmanis.laiks.data.domain.HourlyPricesUseCase
import com.folkmanis.laiks.data.domain.StatisticsUseCase
import com.folkmanis.laiks.model.NpPrice
import com.folkmanis.laiks.model.PowerApplianceHour
import com.folkmanis.laiks.model.PricesStatistics
import com.folkmanis.laiks.utilities.MarketZoneNotSetException
import com.folkmanis.laiks.utilities.ext.eurMWhToCentsKWh
import com.folkmanis.laiks.utilities.ext.hoursFrom
import com.folkmanis.laiks.utilities.ext.toLocalDateTime
import com.folkmanis.laiks.utilities.hourTicks
import com.folkmanis.laiks.utilities.minuteTicks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PricesViewModel @Inject constructor(
    appliancesCosts: AppliancesCostsUseCase,
    private val statistics: StatisticsUseCase,
    private val hourlyPrices: HourlyPricesUseCase,
    marketZone: CurrentMarketZoneUseCase,
) : ViewModel() {

    val currentMarketZoneId: Flow<String> = marketZone()
        .map { it.id }

    var pricesStatistics by mutableStateOf<PricesStatistics?>(null)
        private set

    var uiState by mutableStateOf<PricesUiState>(PricesUiState.Loading)
        private set

    fun initialize() {
        uiState = PricesUiState.Loading
        viewModelScope.launch(CoroutineExceptionHandler { _, exception ->
            if (exception !is MarketZoneNotSetException) {
                Log.e(TAG, "pricesStatistics error ${exception.message}")
            }
        }) {
            statistics().collect {
                pricesStatistics = it
            }
        }

        viewModelScope.launch(CoroutineExceptionHandler { _, exception ->
            uiState = if (exception is MarketZoneNotSetException) {
                PricesUiState.MarketZoneMissing
            } else {
                Log.e(TAG, "uiState error ${exception.message}")
                PricesUiState.Error(exception.message, exception)
            }
        }) {
            hourTicks()
                .flatMapLatest { hour ->
                    hourlyPrices(
                        hour.truncatedTo(ChronoUnit.DAYS)
                    )
                        .map { prices ->
                            val groupedPrices = prices
                                .eurMWhToCentsKWh()
                                .groupBy { it.startTime.toLocalDateTime().toLocalDate() }
                            PricesUiState.Success(
                                groupedPrices = groupedPrices,
                                hour = hour,
                                currentOffsetIndex = currentOffsetIndex(groupedPrices, hour)
                            )
                        }
                }.collect {
                    uiState = it
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
        .catch { emit(emptyMap()) }

    companion object {

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