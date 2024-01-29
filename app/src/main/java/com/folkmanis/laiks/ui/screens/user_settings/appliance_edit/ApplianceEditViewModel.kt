package com.folkmanis.laiks.ui.screens.user_settings.appliance_edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.model.PowerAppliance
import com.folkmanis.laiks.model.UserPowerAppliance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApplianceEditViewModel @Inject constructor(
    private val laiksUserService: LaiksUserService,
) : ViewModel() {

    var uiState by mutableStateOf(ApplianceUiState())
        private set

    private var busy: Boolean
        set(value) {
            uiState = uiState.copy(isBusy = value)
        }
        get() = uiState.isBusy

    fun setName(name: String) {
        uiState = uiState.copy(name = name)
    }

    fun setMinimumDelay(time: Long?) {
        uiState = uiState.copy(minimumDelay = time)
    }

    fun setDelay(value: String) {
        if (value == "start" || value == "end") {
            uiState = uiState.copy(delay = value)
        }
    }


    fun setColor(color: String) {
        uiState = uiState.copy(color = color)
    }


    fun setCycles(cycles: List<NullablePowerApplianceCycle>) {
        uiState = uiState.copy(cycles = cycles)
    }

    fun save(popUpScreen: () -> Unit) {
        busy = true

        viewModelScope.launch {
            val appliance = uiState.toPowerAppliance()
            laiksUserService.laiksUser()?.also { laiksUser ->
                val userAppliances = laiksUser.appliances.toMutableList()
                uiState.idx.also { idx ->
                    if (idx == null) {
                        userAppliances.add(appliance)
                    } else {
                        userAppliances[idx] = (appliance)
                    }
                }
                laiksUserService.updateLaiksUser("appliances", userAppliances)
            }
            busy = false
            popUpScreen()
        }
    }

    suspend fun loadAppliance(idx: Int?) {
        busy = true
        uiState = uiState.copy(
            idx = idx,
        )

        val laiksUser = laiksUserService.laiksUser()
        val appliance = if (idx != null && laiksUser != null) {
            laiksUser.appliances[idx]
        } else {
            UserPowerAppliance()
        }

        setAppliance(appliance)

        busy = false
    }

    fun setAppliance(appliance: PowerAppliance) {
        uiState = uiState.setAppliance(appliance)
    }

}