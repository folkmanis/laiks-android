package com.folkmanis.laiks.model

import java.time.Period

data class AveragePrices(
    val lowest: NpPrice,
    val highest: NpPrice,
    val average: Double,
    val averageDay: Double,
    val averageNight: Double,
    val period: Period
)