package com.folkmanis.laiks.utilities.ext

import com.folkmanis.laiks.model.NpPrice

val List<NpPrice>.average: Double
    get() = map { it.value }.average()

fun List<NpPrice>.stDev(): Double =
    map { it.value }.stDev()

fun List<NpPrice>.addVat(amount: Double): List<NpPrice> =
    map { it.copy(value = it.value.withVat(amount)) }

fun List<NpPrice>.addExtraCost(amount: Double): List<NpPrice> =
    map { it.copy(value = it.value + amount) }

fun List<NpPrice>.eurMWhToCentsKWh(): List<NpPrice> =
    map { it.copy(value = it.value.eurMWhToCentsKWh()) }