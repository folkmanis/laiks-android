package com.folkmanis.laiks.data.domain

import android.util.Log
import com.folkmanis.laiks.data.NpRepository
import com.folkmanis.laiks.data.PricesService
import com.folkmanis.laiks.model.toNpPrices
import com.folkmanis.laiks.utilities.ext.toInstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NpUpdate @Inject constructor(
    private val pricesService: PricesService,
    private val npRepository: NpRepository,
) {

    suspend operator fun invoke(): Int {
        val npPrices = npRepository.getNpData().toNpPrices()
        Log.d(TAG, "Retrieved ${npPrices.size} hourly prices")

        if (npPrices.isEmpty()) {
            return 0
        }

        val lastDbUpdate = pricesService.lastUpdate()

        val newData = npPrices.filter { it.startTime.toInstant() > lastDbUpdate }

        Log.d(TAG, "${newData.size} new records")

        if (newData.isNotEmpty()) {
            withContext(Dispatchers.IO) {
                pricesService.uploadPrices(newData)
            }
        }

        return newData.size

    }

    companion object {
        const val TAG = "NpUpdate"
    }

}