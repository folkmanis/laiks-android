package com.folkmanis.laiks.data

import com.folkmanis.laiks.model.NpPrice
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import java.time.LocalDateTime

interface PricesService {
    suspend fun npPrices(startTimestamp: Timestamp): List<NpPrice>
    fun lastDaysPricesFlow(days: Long): Flow<List<NpPrice>>

   suspend fun lastUpdate(): Instant

   suspend fun uploadPrices(prices: List<NpPrice>)

}