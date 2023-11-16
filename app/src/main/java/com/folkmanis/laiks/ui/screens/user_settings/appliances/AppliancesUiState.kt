package com.folkmanis.laiks.ui.screens.user_settings.appliances

import com.folkmanis.laiks.model.PowerAppliance

data class AppliancesUiState(
    val appliances: List<PowerAppliance> = emptyList(),
    val saving:Boolean = false,
    val loading: Boolean=true,
    )