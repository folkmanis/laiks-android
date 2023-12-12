package com.folkmanis.laiks.ui.screens.prices

import android.util.Log
import androidx.lifecycle.ViewModel
import com.folkmanis.laiks.R
import com.folkmanis.laiks.data.domain.AppliancesCostsUseCase
import com.folkmanis.laiks.data.domain.HourlyPricesUseCase
import com.folkmanis.laiks.data.domain.StatisticsUseCase
import com.folkmanis.laiks.model.NpPrice
import com.folkmanis.laiks.model.PowerApplianceHour
import com.folkmanis.laiks.model.PricesStatistics
import com.folkmanis.laiks.ui.snackbar.SnackbarManager
import com.folkmanis.laiks.utilities.MarketZoneNotSetException
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
    private val snackbarManager: SnackbarManager,
) : ViewModel() {

    lateinit var setMarketZone: ()-> Unit

    fun initialize(onSetMarketZone: ()->Unit) {
        setMarketZone = onSetMarketZone
    }

    val pricesStatistics: Flow<PricesStatistics?> = statistics()
        .catch { err ->
            if (err !is MarketZoneNotSetException) {
                Log.e(TAG, "pricesStatistics error ${err.message}")
            }
        }

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
        .catch { err->
            if (err is MarketZoneNotSetException) {
                snackbarManager.showMessage(R.string.market_zone_select_anonymous)
                setMarketZone()
            } else {
                Log.e(TAG, "uiState error ${err.message}")
                emit(PricesUiState.Error(err.message, err))
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