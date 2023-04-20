package com.folkmanis.laiks.utilities.ext

import com.folkmanis.laiks.model.NpPrice
import kotlin.math.pow
import kotlin.math.sqrt

val List<NpPrice>.average: Double
    get() = map { it.value }.average()

fun List<NpPrice>.stDev(): Double =
    if (isNotEmpty()) {
        val avg = average
        val sum = map { it.value }.reduce { sum, price -> sum + (price - avg).pow(2) }
        sqrt(sum / size)
    } else 0.0

