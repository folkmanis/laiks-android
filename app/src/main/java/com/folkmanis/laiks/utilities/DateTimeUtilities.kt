package com.folkmanis.laiks.utilities

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