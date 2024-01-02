package com.folkmanis.laiks.ui.screens.clock

import com.folkmanis.laiks.model.UserPowerAppliance
import com.folkmanis.laiks.utilities.ext.hoursString
import com.folkmanis.laiks.utilities.ext.minutesString
import java.time.LocalTime

data class ClockUiState(
    val isPricesAllowed: Boolean = true,
    val appliances: List<UserPowerAppliance> = emptyList(),
    val offset: Int = 0,
    val currentTime: LocalTime = LocalTime.now(),
) {

    private val targetTime: LocalTime = currentTime
        .plusHours(offset.toLong())

    val minutes: String
        get() = targetTime.minutesString

    val hours: String
        get() = targetTime.hoursString
}
