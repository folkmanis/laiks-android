package com.folkmanis.laiks.ui.screens.prices

import com.folkmanis.laiks.model.NpPrice
import com.folkmanis.laiks.utilities.ext.average
import com.folkmanis.laiks.utilities.ext.stDev
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Flow<List<NpPrice>>.statisticsFlow: Flow<PricesStatistics>
    get() = map {
        PricesStatistics.from(it)
    }

data class PricesStatistics(
    val average: Double? = null,
    val stDev: Double? = null,
) {
    companion object {
        fun from(prices: List<NpPrice>): PricesStatistics =
            PricesStatistics(
                average = prices.average,
                stDev = prices.stDev(),
            )

    }
}
