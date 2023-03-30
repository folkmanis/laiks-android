package com.folkmanis.laiks.data

import com.folkmanis.laiks.model.AveragePrices
import com.folkmanis.laiks.model.NpPrice
import com.folkmanis.laiks.model.PowerAppliance
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.Flow

interface PricesService {
    fun allNpPrices(startTimestamp: Timestamp): Flow<List<NpPrice>>
    fun lastDaysPrices(days: Long): Flow<List<NpPrice>>
    val activeAppliances: Flow<List<PowerAppliance>>
}