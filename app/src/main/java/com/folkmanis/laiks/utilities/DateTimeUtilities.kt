package com.folkmanis.laiks.utilities

import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun timeToMinutes(time: LocalTime): String {
    val formatter = DateTimeFormatter.ofPattern("mm")
    return time.format(formatter)
}

fun timeToHours(time: LocalTime): String {
    val formatter = DateTimeFormatter.ofPattern("H")
    return time.format(formatter)
}

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

