package com.folkmanis.laiks.ui.screens.appliance_costs

import androidx.lifecycle.ViewModel
import com.folkmanis.laiks.data.AppliancesService
import com.folkmanis.laiks.data.domain.ApplianceHourlyCostsUseCase
import com.folkmanis.laiks.data.domain.DelayToNextNpUpdateUseCase
import com.folkmanis.laiks.data.domain.NpUpdateUseCase
import com.folkmanis.laiks.utilities.PricesUpdateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ApplianceCostsViewModel @Inject constructor(
    private val appliancesService: AppliancesService,
    private val applianceHourlyCosts: ApplianceHourlyCostsUseCase,
    npUpdate: NpUpdateUseCase,
    delayToNextNpUpdate: DelayToNextNpUpdateUseCase,
    ) : PricesUpdateViewModel(npUpdate, delayToNextNpUpdate) {

    private val idFlow = MutableStateFlow<String?>(null)

    val uiState = idFlow
        .filterNotNull()
        .map { id ->
            appliancesService.getAppliance(id)
        }
        .filterNotNull()
        .flatMapLatest { appliance ->
            applianceHourlyCosts(appliance)
                .map { costs ->
                    ApplianceCostsUiState.Success(costs, appliance.name)
                }
        }

    fun setApplianceId(applianceId: String?) {
        idFlow.value = applianceId
    }

}