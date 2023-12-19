package com.folkmanis.laiks.ui.screens.user_settings.appliance_edit

import com.folkmanis.laiks.model.MSW_TO_KWH
import com.folkmanis.laiks.model.PowerAppliance
import com.folkmanis.laiks.model.PowerApplianceCycle
import com.folkmanis.laiks.model.PowerApplianceDelay
import com.folkmanis.laiks.model.UserPowerAppliance

data class NullablePowerApplianceCycle(
    val consumption: Long? = 0,
    val length: Long? = 0, // milliseconds
) {
    fun toPowerApplianceCycle() =
        PowerApplianceCycle(
            consumption = consumption ?: 0,
            length = length ?: 0,
        )

    val isValid: Boolean
        get() = consumption != null && length != null

    val kWh: Double
        get() = (consumption?.toDouble() ?: 0.0) * (length?.toDouble() ?: 0.0) * MSW_TO_KWH

}

val List<NullablePowerApplianceCycle>.isValid: Boolean
    get() = this.all { it.isValid }

fun List<NullablePowerApplianceCycle>.toPowerApplianceCycles() =
    map { it.toPowerApplianceCycle() }

fun PowerApplianceCycle.toNullablePowerApplianceCycle() =
    NullablePowerApplianceCycle(consumption, length)

fun List<PowerApplianceCycle>.toNullablePowerApplianceCycles() =
    map { it.toNullablePowerApplianceCycle() }

data class ApplianceUiState(

    val isLoading: Boolean = false,
    val isSaving: Boolean = false,

    val idx: Int? = null,
    val name: String = "",
    val color: String = "#CCCCCC",
    val delay: String = "start",
    val minimumDelay: Long? = 0,
    val cycles: List<NullablePowerApplianceCycle> = emptyList(),

    ) {

    val isEnabled: Boolean
        get() = !isSaving && !isLoading

    val isNameValid: Boolean
        get() = name.isNotBlank()

    val isMinimumDelayValid: Boolean
        get() = minimumDelay != null && minimumDelay >= 0

    private val isDelayValid: Boolean
        get() = PowerApplianceDelay.entries.map { it.name }.contains(delay)

    private val isCyclesValid: Boolean
        get() = cycles.isValid

    val isValid: Boolean
        get() = isNameValid && isMinimumDelayValid && isDelayValid && isCyclesValid

    fun setAppliance(appliance: PowerAppliance): ApplianceUiState {
        return copy(
            name = appliance.name,
            color = appliance.color,
            delay = appliance.delay,
            minimumDelay = appliance.minimumDelay,
            cycles = appliance.cycles.toNullablePowerApplianceCycles(),
        )
    }

    fun toPowerAppliance(): UserPowerAppliance {
        return UserPowerAppliance(
            name = name,
            color = color,
            delay = delay,
            minimumDelay = minimumDelay ?: 0,
            cycles = cycles.toPowerApplianceCycles(),
        )
    }

}

