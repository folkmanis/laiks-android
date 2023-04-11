package com.folkmanis.laiks.ui.screens.appliance_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.APPLIANCE_ID
import com.folkmanis.laiks.data.PricesService
import com.folkmanis.laiks.model.PowerAppliance
import com.folkmanis.laiks.model.PowerApplianceCycle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class ApplianceEditViewModel @Inject constructor(
    private val pricesService: PricesService,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val applianceId: String? = savedStateHandle[APPLIANCE_ID]

    private val _uiState = MutableStateFlow(ApplianceUiState())
    val uiState = _uiState.asStateFlow()

    init {
        if (applianceId != null) {
            loadAppliance(applianceId)
        }
    }

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
            popUpScreen()
        }
    }

    private fun loadAppliance(id: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val appliance = pricesService.getAppliance(id)
            if (appliance != null) {
                _uiState.update { state ->
                    state.copy(appliance)
                }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

}