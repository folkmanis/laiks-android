package com.folkmanis.laiks.ui.screens.appliance_costs

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.data.domain.ApplianceHourlyCostsUseCase
import com.folkmanis.laiks.data.domain.ApplianceStatisticsUseCase
import com.folkmanis.laiks.model.PricesStatistics
import com.folkmanis.laiks.ui.snackbar.SnackbarManager
import com.folkmanis.laiks.ui.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ApplianceCostsViewModel @Inject constructor(
    private val applianceHourlyCosts: ApplianceHourlyCostsUseCase,
    applianceStatistics: ApplianceStatisticsUseCase,
    private val snackbarManager: SnackbarManager,
    private val laiksUserService: LaiksUserService,
    savedStateHandle: SavedStateHandle,
//    npUpdate: NpUpdateUseCase,
//    delayToNextNpUpdate: DelayToNextNpUpdateUseCase,
) : ViewModel() { // : PricesUpdateViewModel(npUpdate, delayToNextNpUpdate, snackbarManager)

    private val initialName: String? = savedStateHandle[APPLIANCE_NAME]

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
        .catch { err ->
            Log.e(TAG, "Statistics error", err)
            snackbarManager.showMessage(err.toSnackbarMessage())
        }

    private val hourlyCostsFlow = applianceFlow
        .flatMapLatest { appliance ->
            applianceHourlyCosts(appliance)
        }

    val uiState =
        combine(hourlyCostsFlow, statisticsFlow, nameFlow) { hourlyCosts, statistics, name ->
            ApplianceCostsUiState.Success(
                name = name,
                hoursWithCosts = hourlyCosts,
                statistics = statistics,
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = ApplianceCostsUiState.Loading(initialName),
            )

    companion object {
        const val TAG = "ApplianceCostsViewModel"
    }

}

