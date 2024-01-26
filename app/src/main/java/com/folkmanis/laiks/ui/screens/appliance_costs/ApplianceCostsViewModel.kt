package com.folkmanis.laiks.ui.screens.appliance_costs

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.data.domain.ApplianceHourlyCostsUseCase
import com.folkmanis.laiks.data.domain.ApplianceStatisticsUseCase
import com.folkmanis.laiks.data.domain.CurrentMarketZoneUseCase
import com.folkmanis.laiks.model.PricesStatistics
import com.folkmanis.laiks.utilities.MarketZoneNotSetException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ApplianceCostsViewModel @Inject constructor(
    private val applianceHourlyCosts: ApplianceHourlyCostsUseCase,
    applianceStatistics: ApplianceStatisticsUseCase,
    private val laiksUserService: LaiksUserService,
    savedStateHandle: SavedStateHandle,
    marketZone: CurrentMarketZoneUseCase,
) : ViewModel() {

    private val initialName: String? = savedStateHandle[APPLIANCE_NAME]

    var uiState by mutableStateOf<ApplianceCostsUiState>(
        ApplianceCostsUiState.Loading(initialName        )
    )

    private val applianceFlow = savedStateHandle
        .getStateFlow<String?>(APPLIANCE_IDX, null)
        .map { it?.toInt() }
        .filterNotNull()
        .flatMapLatest { idx ->
            Log.d(TAG, "Appliance idx $idx")
            laiksUserService.laiksUserFlow()
                .filterNotNull()
                .map { laiksUser -> laiksUser.appliances[idx] }
        }
        .shareIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            1
        )

    private val nameFlow = applianceFlow.map { it.name }

    private val statisticsFlow: Flow<PricesStatistics> = applianceFlow
        .flatMapLatest { appliance ->
            applianceStatistics(appliance)
        }

    private val hourlyCostsFlow = applianceFlow
        .flatMapLatest { appliance ->
            applianceHourlyCosts(appliance)
        }

    private val uiStateFlow =
        combine(
            hourlyCostsFlow,
            statisticsFlow,
            nameFlow,
            marketZone()
        ) { hourlyCosts, statistics, name, marketZone ->
            ApplianceCostsUiState.Success(
                name = name,
                hoursWithCosts = hourlyCosts,
                statistics = statistics,
                marketZoneId = marketZone.id
            )
        }

    fun initialize() {
        uiState = ApplianceCostsUiState.Loading(initialName)
        viewModelScope.launch(
            CoroutineExceptionHandler { _, exception ->
                uiState = if (exception is MarketZoneNotSetException) {
                    ApplianceCostsUiState.MarketZoneMissing
                } else {
                    Log.e(TAG, "Error ${exception.message}")
                    ApplianceCostsUiState.Error(exception.message, exception)
                }
            }
        ) {
            uiStateFlow.collect { uiState = it }
        }
    }

    companion object {
        const val TAG = "ApplianceCostsViewModel"
    }

}

