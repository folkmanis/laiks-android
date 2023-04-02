package com.folkmanis.laiks.utilities.ext

import com.folkmanis.laiks.model.NpPrice
import kotlin.math.pow
import kotlin.math.sqrt

val List<NpPrice>.average: Double
    get() = sumOf { it.value } / size.toDouble()

fun List<NpPrice>.stDev(): Double {
    val avg = average
    val sum = map { it.value }.reduce { sum, price -> sum + (price - avg).pow(2) }
    return sqrt(sum / size)
}
