package com.folkmanis.laiks.data.domain

import android.util.Log
import com.folkmanis.laiks.INCLUDE_AVERAGE_DAYS
import com.folkmanis.laiks.REFRESH_AT_TZ
import com.folkmanis.laiks.data.NpRepository
import com.folkmanis.laiks.data.PricesService
import com.folkmanis.laiks.model.NpPricesDocument
import com.folkmanis.laiks.model.toNpPrices
import com.folkmanis.laiks.utilities.ext.average
import com.folkmanis.laiks.utilities.ext.stDev
import com.folkmanis.laiks.utilities.ext.toInstant
import java.time.ZoneId
import javax.inject.Inject

class NpUpdateUseCase @Inject constructor(
    private val pricesService: PricesService,
    private val npRepository: NpRepository,
) {

    suspend operator fun invoke(): Int {
        val npPrices = npRepository.getNpData().toNpPrices()
        Log.d(TAG, "Retrieved ${npPrices.size} hourly prices")

        if (npPrices.isEmpty()) {
            return 0
        }

        val lastDbUpdate = pricesService.latestPriceStartTime()

        val newData = npPrices.filter { it.startTime.toInstant() > lastDbUpdate }

        val daysInStatistics = lastDbUpdate
            .atZone(ZoneId.of(REFRESH_AT_TZ))
            .minusDays(INCLUDE_AVERAGE_DAYS)
            .toInstant()

        val dataForStatistics = npPrices.filter { it.startTime.toInstant() >= daysInStatistics }

        val npPricesDocument = NpPricesDocument(
            average = dataForStatistics.average,
            stDev = dataForStatistics.stDev(),
        )

        Log.d(TAG, "${newData.size} new records")

        pricesService.uploadPrices(newData, npPricesDocument)

        return newData.size

    }

    companion object {
        const val TAG = "NpUpdate"
    }

}