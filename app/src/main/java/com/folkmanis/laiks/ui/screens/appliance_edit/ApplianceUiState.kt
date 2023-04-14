package com.folkmanis.laiks.ui.screens.appliance_edit

import com.folkmanis.laiks.model.PowerAppliance
import com.folkmanis.laiks.model.PowerApplianceCycle
import com.folkmanis.laiks.model.PowerApplianceDelay

data class ApplianceUiState(

    val isLoading: Boolean = false,
    val isSaving: Boolean = false,

    val id: String = "",
    val name: String = "",
    val color: String = "#CCCCCC",
    val delay: String = "start",
    val enabled: Boolean = true,
    val minimumDelay: Long = 0,
    val cycles: List<PowerApplianceCycle> = emptyList(),

    ) {

    val isNew: Boolean
        get() = id.isBlank()

    val isEnabled: Boolean
        get() = !isSaving && !isLoading

    val isNameValid: Boolean
        get() = name.isNotBlank()

    val isMinimumDelayValid: Boolean
        get() = minimumDelay >= 0

    private val isDelayValid: Boolean
        get() = PowerApplianceDelay.values().map { it.name }.contains(delay)

    private val isCyclesValid: Boolean
        get() = cycles.isValid

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

