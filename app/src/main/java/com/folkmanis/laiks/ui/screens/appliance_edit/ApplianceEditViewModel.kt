package com.folkmanis.laiks.ui.screens.appliance_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.data.PricesService
import com.folkmanis.laiks.model.PowerAppliance
import com.folkmanis.laiks.model.PowerApplianceCycle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface EditActions {
    data class Edit(val id: String) : EditActions
    data class Copy(val id: String) : EditActions
    object CreateNew : EditActions
}

@HiltViewModel
class ApplianceEditViewModel @Inject constructor(
    private val pricesService: PricesService,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ApplianceUiState())
    val uiState = _uiState.asStateFlow()

    fun setName(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun setMinimumDelay(time: Long?) {
        _uiState.update { it.copy(minimumDelay = time) }
    }

    fun setDelay(value: String) {
        if (value == "start" || value == "end") {
            _uiState.update { it.copy(delay = value) }
        }
    }

    fun setEnabled(enabled: Boolean) {
        _uiState.update { it.copy(enabled = enabled) }
    }

    fun setColor(color: String) {
        _uiState.update { it.copy(color = color) }
    }

    fun setCycles(cycles: List<PowerApplianceCycle>) {
        _uiState.update { it.copy(cycles = cycles) }
    }

    fun save(popUpScreen: () -> Unit) {
        _uiState.update {
            it.copy(isSaving = true)
        }
        viewModelScope.launch {
            val appliance = _uiState.value.toPowerAppliance()
            if (appliance.id.isBlank()) {
                pricesService.addAppliance(appliance)
            } else {
                pricesService.updateAppliance(appliance)
            }
            _uiState.update {
                it.copy(isSaving = false)
            }
            popUpScreen()
        }
    }

    fun loadAppliance(action: EditActions) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when (action) {
                is EditActions.Edit -> {
                    val appliance = pricesService.getAppliance(action.id)
                    if(appliance != null)
                    _uiState.update { state ->
                        state.copy(appliance)
                    }

                }
                is EditActions.Copy -> {
                    val appliance = pricesService.getAppliance(action.id)
                    if(appliance != null)
                        _uiState.update { state ->
                            state.copy(appliance).copy(id = "", name = "")
                        }

                }
                is EditActions.CreateNew -> {
                    _uiState.update { state ->
                        state.copy(PowerAppliance())
                    }
                }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

}