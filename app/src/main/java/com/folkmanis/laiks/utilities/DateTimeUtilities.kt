package com.folkmanis.laiks.utilities

import java.time.Duration
import java.time.LocalDateTime

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

