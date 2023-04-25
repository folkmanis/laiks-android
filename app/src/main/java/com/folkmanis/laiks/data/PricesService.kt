package com.folkmanis.laiks.data

import com.folkmanis.laiks.model.NpPrice
import com.folkmanis.laiks.model.NpPricesDocument
import kotlinx.coroutines.flow.Flow
import java.time.Instant

interface PricesService {
    suspend fun npPrices(start: Instant): List<NpPrice>
    fun lastDaysPricesFlow(days: Long): Flow<List<NpPrice>>

    fun pricesFromDateTimeFlow(dateTime: Instant): Flow<List<NpPrice>>

    suspend fun latestPriceStartTime(): Instant

    suspend fun uploadPrices(prices: List<NpPrice>, npPricesDocument: NpPricesDocument)

    suspend fun npPricesDocument(): NpPricesDocument?

    fun npPricesDocumentFlow(): Flow<NpPricesDocument?>

}