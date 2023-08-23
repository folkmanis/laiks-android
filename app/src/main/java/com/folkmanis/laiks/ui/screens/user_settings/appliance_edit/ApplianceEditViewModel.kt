package com.folkmanis.laiks.ui.screens.user_settings.appliance_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.model.PowerAppliance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApplianceEditViewModel @Inject constructor(
    private val laiksUserService: LaiksUserService,
) : ViewModel() {

    var uiState = MutableStateFlow(ApplianceUiState())
        private set

    fun setName(name: String) {
        uiState.update { it.copy(name = name) }
    }

    fun setMinimumDelay(time: Long?) {
        uiState.update { it.copy(minimumDelay = time) }
    }

    fun setDelay(value: String) {
        if (value == "start" || value == "end") {
            uiState.update { it.copy(delay = value) }
        }
    }

    fun setEnabled(enabled: Boolean) {
        uiState.update { it.copy(enabled = enabled) }
    }

    fun setColor(color: String) {
        uiState.update { it.copy(color = color) }
    }

    fun setCycles(cycles: List<NullablePowerApplianceCycle>) {
        uiState.update { it.copy(cycles = cycles) }
    }

    fun save(popUpScreen: () -> Unit) {
        uiState.update {
            it.copy(isSaving = true)
        }
        viewModelScope.launch {
            val appliance = uiState.value.toPowerAppliance()
            val userAppliances = laiksUserService.laiksUser().appliances.toMutableList()
            uiState.value.idx.also {
                if (it == null) {
                    userAppliances.add(appliance)
                } else {
                    userAppliances[it] = (appliance)
                }
            }
            laiksUserService.updateLaiksUser("appliances", userAppliances)
            uiState.update {
                it.copy(isSaving = false)
            }
            popUpScreen()
        }
    }

    fun loadAppliance(idx: Int?) {
        uiState.update {
            it.copy(
                isLoading = true,
                idx = idx,
            )
        }
        viewModelScope.launch {

            val appliance = if (idx != null) {
                laiksUserService.laiksUser().appliances[idx]
            } else {
                PowerAppliance()
            }

            setAppliance(appliance)

            uiState.update { it.copy(isLoading = false) }
        }
    }

    fun setAppliance(appliance: PowerAppliance) {
        uiState.update { state -> state.setAppliance(appliance) }
    }

}