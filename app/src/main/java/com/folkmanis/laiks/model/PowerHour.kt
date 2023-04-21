package com.folkmanis.laiks.model

import com.folkmanis.laiks.utilities.ext.toLocalDateTime
import java.time.LocalDateTime

data class PowerHour(
    val id: String ="",
    val offset: Int = 0,
//    val minute: LocalDateTime = LocalDateTime.now(),
    val price: Double = 0.0,
    val startTime: LocalDateTime = LocalDateTime.now(),
    val endTime: LocalDateTime = LocalDateTime.now().plusHours(1),
//    val appliancesHours: List<PowerApplianceHour> = emptyList()
)