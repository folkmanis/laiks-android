package com.folkmanis.laiks.ui.screens.appliance_costs

import com.folkmanis.laiks.model.ApplianceHourWithCosts
import com.folkmanis.laiks.model.PricesStatistics


sealed interface ApplianceCostsUiState {
    data class Loading(
        val name: String?,
    ) : ApplianceCostsUiState
    data class Error(
        val reason: String,
        val exception: Throwable,
    ) : ApplianceCostsUiState
    data class Success(
        val name: String,
        val hoursWithCosts: List<ApplianceHourWithCosts>,
        val statistics: PricesStatistics?,
    ) : ApplianceCostsUiState
}