package com.folkmanis.laiks.data

import com.folkmanis.laiks.model.NpPrice
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.Flow

interface PricesService {
    fun allNpPrices(startTimestamp: Timestamp): Flow<List<NpPrice>>
}