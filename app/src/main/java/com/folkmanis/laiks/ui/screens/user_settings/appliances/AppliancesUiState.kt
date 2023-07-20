package com.folkmanis.laiks.ui.screens.user_settings.appliances

import com.folkmanis.laiks.model.PowerAppliance

data class AppliancesUiState(
    val appliances: List<PowerAppliance> = emptyList(),
    val saving:Boolean = false,
    val loading: Boolean=true,
    ) {
    fun removeRecordAt(idx: Int): AppliancesUiState {
        val updated = appliances.toMutableList()
            .apply { removeAt(idx) }
        return copy(appliances = updated)
    }

    fun addRecord(appliance: PowerAppliance): AppliancesUiState {
        val updated = appliances.toMutableList()
            .apply { add(appliance) }
        return copy(appliances = updated)
    }
}
