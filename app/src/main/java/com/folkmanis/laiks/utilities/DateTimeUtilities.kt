package com.folkmanis.laiks.utilities

import com.google.firebase.Timestamp
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.temporal.TemporalField
import java.util.Date
import kotlin.math.ceil

fun delayToNextSecond(fromDateTime: LocalDateTime): Long {
    val nextSecond = fromDateTime
        .plusSeconds(1)
        .withNano(0)
    return Duration
        .between(fromDateTime, nextSecond)
        .toMillis()
}

fun delayToNextMinute(fromDateTime: LocalDateTime): Long {
    val nextMinute = fromDateTime
        .plusMinutes(1)
        .withSecond(0)
        .withNano(0)
    return Duration
        .between(fromDateTime, nextMinute)
        .toMillis()
}

fun hoursUntilTimestamp(dateNow: Instant, time: Timestamp): Long {
    val hours = Duration.between(dateNow, time.toDate().toInstant()).toMillis() / (60 * 60 * 1000)
return hours
}