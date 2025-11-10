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
import com.folkmanis.laiks.utilities.ext.toLocalTime
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
                            val listItemsData = createListItemsData(prices.eurMWhToCentsKWh())
                            val currentOffsetIndex = currentOffsetIndex(listItemsData, hour)

                            PricesUiState.Success(
                                listItemsData,
                                hour,
                                currentOffsetIndex
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
            listItemsData: List<PricesListItemData>,
            hour: LocalDateTime,
        ): Int {
            val idx = listItemsData.indexOfFirst {
                it is PricesListItemData.HourlyPrice && it.npPrices.first().startTime.hoursFrom(hour) == 0
            }
            return if (idx == -1) 0 else idx
        }

        fun createListItemsData(npPrices: List<NpPrice>): List<PricesListItemData> =
            buildList {
                npPrices.groupBy { it.startTime.toLocalDateTime().toLocalDate() }
                    .forEach { (date, prices) ->
                        add(PricesListItemData.DateHeader(date))
                        prices.groupBy { it.startTime.toLocalTime().hour }.forEach { (_, prices) ->
                            add(PricesListItemData.HourlyPrice(prices))
                        }
                    }
            }


    }
}