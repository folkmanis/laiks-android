package com.folkmanis.laiks.ui.screens.appliance_edit

import com.folkmanis.laiks.DELAY_VALUES
import com.folkmanis.laiks.model.PowerAppliance
import com.folkmanis.laiks.model.PowerApplianceCycle

data class ApplianceUiState(

    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val editMode: Boolean = false,

    val id: String = "",
    val name: String = "",
    val color: String = "#CCCCCC",
    val delay: String = "start",
    val enabled: Boolean = true,
    val minimumDelay: Long? = 0,
    val cycles: List<PowerApplianceCycle> = emptyList(),

    ) {

    val isEnabled: Boolean
        get() = !isSaving && editMode && !isLoading

    val isNameValid: Boolean
        get() = name.isNotBlank()

    val isMinimumDelayValid: Boolean
        get() = minimumDelay != null && minimumDelay >= 0

    val isDelayValid: Boolean
        get() = DELAY_VALUES.contains(delay)

    val isCyclesValid: Boolean
    get() = true // TODO

    val isValid: Boolean
        get() = isNameValid && isMinimumDelayValid && isDelayValid && isCyclesValid

    fun copy(appliance: PowerAppliance): ApplianceUiState {
        return copy(
            id = appliance.id,
            name = appliance.name,
            color = appliance.color,
            delay = appliance.delay,
            enabled = appliance.enabled,
            minimumDelay = appliance.minimumDelay,
            cycles = appliance.cycles,
        )
    }

    fun toPowerAppliance(): PowerAppliance {
        return PowerAppliance(
            id = id,
            name = name,
            color = color,
            delay = delay,
            enabled = enabled,
            minimumDelay = minimumDelay ?: 0,
            cycles = cycles,
        )
    }

}
