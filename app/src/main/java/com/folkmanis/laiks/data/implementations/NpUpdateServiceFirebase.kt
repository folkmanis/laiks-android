package com.folkmanis.laiks.data.implementations

import android.util.Log
import com.folkmanis.laiks.data.NpRepository
import com.folkmanis.laiks.data.NpUpdateService
import com.folkmanis.laiks.data.PricesService
import com.folkmanis.laiks.model.toNpPrices
import com.folkmanis.laiks.utilities.ext.toInstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NpUpdateServiceFirebase @Inject constructor(
    private val pricesService: PricesService,
    private val npRepository: NpRepository,
) : NpUpdateService {

    override suspend fun updateNpPrices(): Int {

        val npPrices = npRepository.getNpData()

        val lastDbUpdate = pricesService.lastUpdate()

        val newData = npPrices.toNpPrices().filter { it.startTime.toInstant() > lastDbUpdate }

        if (newData.isNotEmpty()) {

            Log.d(TAG, "New data on server! ${newData.size} records.")

            withContext(Dispatchers.IO) {
                pricesService.uploadPrices(newData)
            }
        }

        return newData.size

    }

    companion object {
        const val TAG = "NpUpdateServiceFirebase"
    }
}