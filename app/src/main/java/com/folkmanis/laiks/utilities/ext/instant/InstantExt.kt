package com.folkmanis.laiks.utilities.ext.instant

import com.google.firebase.Timestamp
import java.time.Instant
import java.util.Date

fun Instant.toTimestamp(): Timestamp =
    Timestamp(Date.from(this))
