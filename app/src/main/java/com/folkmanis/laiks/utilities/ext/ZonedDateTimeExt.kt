package com.folkmanis.laiks.utilities.ext

import com.google.firebase.Timestamp
import java.time.ZonedDateTime
import java.util.Date

fun ZonedDateTime.toTimestamp() =
    Timestamp(Date.from(toInstant()))