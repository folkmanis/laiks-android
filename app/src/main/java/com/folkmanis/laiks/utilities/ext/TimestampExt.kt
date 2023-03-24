package com.folkmanis.laiks.utilities.ext

import com.folkmanis.laiks.utilities.hoursUntilTimestamp
import com.google.firebase.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Timestamp.hoursFrom(dateNow: Instant): Int {
    return hoursUntilTimestamp(dateNow, this).toInt()
}

fun Timestamp.toLocalDateString(): String {
   val formatter = DateTimeFormatter.ofPattern("d.LLL")
    return this
        .toDate()
        .toInstant()
        .atZone(ZoneId.systemDefault())
        .format(formatter)
}

fun Timestamp.toLocalDateTime(): LocalDateTime {
   return this.toDate()
        .toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
}

fun Timestamp.toLocalTime(): LocalTime {
    return  this.toLocalDateTime().toLocalTime()
}

fun Timestamp.toEpochMilli(): Long {
    return this.seconds * 1000 + (this.nanoseconds / 1_000_000)
}