package com.folkmanis.laiks.utilities.ext

import com.folkmanis.laiks.model.NpPrice
import com.folkmanis.laiks.model.NpPriceLocal
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import kotlin.math.pow
import kotlin.math.sqrt

val List<NpPrice>.min: NpPrice
    get() = minBy { it.value }

val List<NpPrice>.max: NpPrice
    get() = maxBy { it.value }

val List<NpPrice>.average: Double
    get() = sumOf { it.value } / size.toDouble()

fun NpPrice.isBetween(start: Long, end: Long): Boolean {
    val stDate = startTime
        .toLocalDateTime()
        .withHour(start.toInt())
    val enDate = endTime
        .toLocalDateTime()
        .plusDays(if (end > start) 0 else 1)
        .withHour(end.toInt())
    return stDate >= startTime.toLocalDateTime() &&
            enDate < endTime.toLocalDateTime()
}

fun List<NpPrice>.averageBetween(start: Long, end: Long): Double =
    filter { it.isBetween(start, end) }.average

fun List<NpPrice>.stDev(): Double {
    val avg = average
    val sum = map { it.value }.reduce { sum, price -> sum + (price - avg).pow(2) }
    return sqrt(sum / size)
}
