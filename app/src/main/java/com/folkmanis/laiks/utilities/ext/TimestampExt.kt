package com.folkmanis.laiks.utilities.ext

import com.folkmanis.laiks.utilities.hoursUntilTimestamp
import com.google.firebase.Timestamp
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
