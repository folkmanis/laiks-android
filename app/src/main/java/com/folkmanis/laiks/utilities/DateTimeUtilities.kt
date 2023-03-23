package com.folkmanis.laiks.utilities

import com.google.firebase.Timestamp
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

fun delayToNextSecond(fromDateTime: LocalDateTime): Long {
    val nextSecond = fromDateTime
        .plusSeconds(1)
        .truncatedTo(ChronoUnit.SECONDS)
    return Duration
        .between(fromDateTime, nextSecond)
        .toMillis()
}

fun delayToNextMinute(fromDateTime: LocalDateTime): Long {
    val nextMinute = fromDateTime
        .plusMinutes(1)
        .truncatedTo(ChronoUnit.MINUTES)
    return Duration
        .between(fromDateTime, nextMinute)
        .toMillis()
}

fun delayToNextHour(fromDateTime: LocalDateTime): Long {
    val nextHour = fromDateTime
        .plusHours(1)
        .truncatedTo(ChronoUnit.HOURS)
    return Duration
        .between(fromDateTime, nextHour)
        .toMillis()
}

fun hoursUntilTimestamp(dateNow: Instant, time: Timestamp): Long {
    return Duration
        .between(
            dateNow.truncatedTo(ChronoUnit.HOURS),
            time.toDate().toInstant().truncatedTo(ChronoUnit.HOURS)
        )
        .toHours()
}