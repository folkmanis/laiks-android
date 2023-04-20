package com.folkmanis.laiks.utilities.ext

import com.folkmanis.laiks.utilities.hoursUntilTimestamp
import com.google.firebase.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun Timestamp.hoursFrom(dateNow: LocalDateTime): Int {
    return hoursUntilTimestamp(dateNow, this).toInt()
}

fun Timestamp.toLocalDateTime(): LocalDateTime {
    return this.toDate()
        .toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
}

fun Timestamp.minusDays(days: Long): Timestamp =
    Timestamp(seconds - days * 24 * 60 * 60, nanoseconds)

fun Timestamp.toInstant(): Instant = toDate().toInstant()