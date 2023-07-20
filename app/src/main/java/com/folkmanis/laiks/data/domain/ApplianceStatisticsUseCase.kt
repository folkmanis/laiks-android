package com.folkmanis.laiks.data.domain

import android.util.Log
import com.folkmanis.laiks.INCLUDE_AVERAGE_DAYS
import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.data.PricesService
import com.folkmanis.laiks.model.PowerAppliance
import com.folkmanis.laiks.model.PricesStatistics
import com.folkmanis.laiks.utilities.ext.addVat
import com.folkmanis.laiks.utilities.ext.stDev
import com.folkmanis.laiks.utilities.ext.toInstant
import com.folkmanis.laiks.utilities.offsetCosts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ApplianceStatisticsUseCase @Inject constructor(
    private val pricesService: PricesService,
    private val laiksUserService: LaiksUserService,
) {

    operator fun invoke(appliance: PowerAppliance): Flow<PricesStatistics> = combine(
        pricesService.lastDaysPricesFlow(INCLUDE_AVERAGE_DAYS),
        laiksUserService.vatAmountFlow,
    ) { prices, vat ->
        val startTime = prices.first().startTime.toInstant()
        offsetCosts(prices.addVat(vat), startTime, appliance).values
    }
        .filter(Collection<Double>::isNotEmpty)
        .map { values ->
            Log.d(TAG, "Average: ${values.average()}")
            Log.d(TAG, "StDev: ${values.stDev()}")
            PricesStatistics(
                average = values.average(),
                stDev = values.stDev(),
            )
        }
        .flowOn(Dispatchers.Default)

    companion object {
        const val TAG = "ApplianceStatisticsUseCase"
    }
}
