package com.folkmanis.laiks.ui.screens.appliance_costs

import com.folkmanis.laiks.model.ApplianceHourWithCosts


sealed interface ApplianceCostsUiState {
    object Loading : ApplianceCostsUiState
    data class Error(
        val reason: String,
        val exception: Throwable,
    ) : ApplianceCostsUiState
    data class Success(
        val hoursWithCosts: List<ApplianceHourWithCosts>,
    ) : ApplianceCostsUiState
}