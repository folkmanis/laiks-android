package com.folkmanis.laiks.model

import java.time.LocalDateTime

data class ApplianceHourWithCosts(
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val value: Double,
    val offset: Int,
)
