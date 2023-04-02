package com.folkmanis.laiks.data

import com.folkmanis.laiks.model.NpPrice
import com.folkmanis.laiks.model.PowerAppliance
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.Flow

interface PricesService {
    suspend fun npPrices(startTimestamp: Timestamp): List<NpPrice>
    fun lastDaysPricesFlow(days: Long): Flow<List<NpPrice>>
    suspend fun activeAppliances(): List<PowerAppliance>

}