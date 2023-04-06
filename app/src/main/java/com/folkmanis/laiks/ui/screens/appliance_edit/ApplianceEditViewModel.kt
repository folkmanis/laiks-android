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

data class ApplianceUiState(
//    val appliance: PowerAppliance = PowerAppliance(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val editMode: Boolean = false,
) {
    val enabled: Boolean
        get() = !isSaving && editMode && !isLoading
}

@HiltViewModel
class ApplianceEditViewModel @Inject constructor(
    private val pricesService: PricesService,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val applianceId: String? = savedStateHandle[APPLIANCE_ID]

    private val _uiState = MutableStateFlow(ApplianceUiState())
    val uiState = _uiState.asStateFlow()

    private val _appliance = MutableStateFlow(PowerAppliance())
    val appliance = _appliance.asStateFlow()

    init {
        if (applianceId != null) {
            loadAppliance(applianceId)
            _uiState.update { it.copy(editMode = false) }
        } else {
            _uiState.update { it.copy(editMode = true) }
        }
    }

    fun setName(name: String) {
        _appliance.update { it.copy(name = name) }
    }

    fun setMinimumDelay(time: Long) {
        _appliance.update { it.copy(minimumDelay = time) }
    }

    fun setDelay(delay: String) {
        _appliance.update { it.copy(delay = delay) }
    }

    fun setEnabled(enabled: Boolean) {
        _appliance.update { it.copy(enabled = enabled) }
    }

    fun setColor(color: String) {
        _appliance.update { it.copy(color=color) }
    }

    fun setCycles(cycles: List<PowerApplianceCycle>) {
        _appliance
    }

    fun startEdit() {
        _uiState.update { it.copy(editMode = true) }
    }

    fun save(popUpScreen: () -> Unit) {
        _uiState.update {
            it.copy(editMode = false, isSaving = true)
        }
        viewModelScope.launch {
            val appliance = _appliance.value
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
                _appliance.update { appliance }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

}