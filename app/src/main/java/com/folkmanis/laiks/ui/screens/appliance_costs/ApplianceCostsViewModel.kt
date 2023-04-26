package com.folkmanis.laiks.ui.screens.appliance_costs

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.data.AppliancesService
import com.folkmanis.laiks.data.domain.ApplianceHourlyCostsUseCase
import com.folkmanis.laiks.data.domain.ApplianceStatisticsUseCase
import com.folkmanis.laiks.data.domain.DelayToNextNpUpdateUseCase
import com.folkmanis.laiks.data.domain.NpUpdateUseCase
import com.folkmanis.laiks.model.PricesStatistics
import com.folkmanis.laiks.utilities.PricesUpdateViewModel
import com.folkmanis.laiks.utilities.snackbar.SnackbarManager
import com.folkmanis.laiks.utilities.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ApplianceCostsViewModel @Inject constructor(
    private val appliancesService: AppliancesService,
    private val applianceHourlyCosts: ApplianceHourlyCostsUseCase,
    applianceStatistics: ApplianceStatisticsUseCase,
    npUpdate: NpUpdateUseCase,
    delayToNextNpUpdate: DelayToNextNpUpdateUseCase,
) : PricesUpdateViewModel(npUpdate, delayToNextNpUpdate) {

    private val idFlow = MutableStateFlow<String?>(null)

    private val applianceFlow = idFlow
        .filterNotNull()
        .map { id ->
            appliancesService.getAppliance(id)
        }
        .filterNotNull()
        .shareIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000L)
        )

    val statistics: Flow<PricesStatistics> = applianceFlow
        .flatMapLatest { appliance ->
            applianceStatistics(appliance)
        }
        .catch { err ->
            Log.e(TAG, "Statistics error", err)
            SnackbarManager.showMessage(err.toSnackbarMessage())
        }

    val uiState = applianceFlow
        .flatMapLatest { appliance ->
            applianceHourlyCosts(appliance)
                .map { costs ->
                    ApplianceCostsUiState.Success(costs, appliance.name)
                }
        }

    fun setApplianceId(applianceId: String?) {
        idFlow.value = applianceId
    }

    companion object {
        const val TAG = "ApplianceCostsViewModel"
    }

}