package com.folkmanis.laiks.model.np_data

import java.time.ZonedDateTime

data class ZonedNpPrice(
    val value: Double,
    val startTime: ZonedDateTime,
    val endTime: ZonedDateTime,
)

//startTime = Timestamp(Date.from(startTime.minusDays(column.index).toInstant())),
