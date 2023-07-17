package com.folkmanis.laiks.ui.screens.appliance_select

import com.folkmanis.laiks.model.PowerAppliance
import com.folkmanis.laiks.model.PowerApplianceRecord

sealed interface ApplianceSelectUiState {

    object Loading: ApplianceSelectUiState

    data class Error(
        val exception: Throwable,
        val reason: String? = null,
        ):ApplianceSelectUiState

    data class Success(
        val userAppliances: List<PowerAppliance>,
        val systemAppliances: List<PowerAppliance>,
        val selected: PowerApplianceRecord? = null,
    ):ApplianceSelectUiState

}