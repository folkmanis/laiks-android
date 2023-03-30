package com.folkmanis.laiks.model

import com.folkmanis.laiks.utilities.ext.toLocalDateTime
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

fun NpPrice.toLocalDateTime():NpPriceLocal =
    NpPriceLocal(
        id = id,
        value = value,
        startTime = startTime.toLocalDateTime(),
        endTime = endTime.toLocalDateTime(),
    )

fun NpPriceLocal.isBetween(start: Long, end: Long):Boolean {
val stDate = startTime.truncatedTo(ChronoUnit.DAYS).withHour(start.toInt())
    val enDate = endTime.truncatedTo(ChronoUnit.DAYS).withHour(end.toInt())
   return (startTime.isAfter(stDate) || startTime.isEqual(stDate)) &&
            endTime.isBefore(enDate)
}

fun List<NpPrice>.toLocalDateTime(): List<NpPriceLocal> =
    map { it.toLocalDateTime() }

data class NpPriceLocal(
    val id : String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val value: Double,
    )
