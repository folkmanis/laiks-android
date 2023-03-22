package com.folkmanis.laiks.ui.screens.clock

import java.time.LocalTime

data class ClockUiState(
    val time: LocalTime = LocalTime.now(),
    val offset: Int = 0,
)
