package com.folkmanis.laiks.data.fake

import com.folkmanis.laiks.data.PricesService
import com.folkmanis.laiks.model.NpPrice
import com.folkmanis.laiks.model.NpPricesDocument
import com.folkmanis.laiks.utilities.ext.instant.toTimestamp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.Instant
import java.time.ZoneId

@Suppress("RedundantNullableReturnType")
class FakePricesService : PricesService {

    override fun lastDaysPricesFlow(days: Long): Flow<List<NpPrice>> {
        return flowOf(testPrices(Instant.now()))
    }

    override suspend fun npPrices(start: Instant): List<NpPrice> {
        return testPrices(start)
    }

    override fun pricesFromDateTimeFlow(dateTime: Instant): Flow<List<NpPrice>> {
        return flowOf(testPrices(dateTime))
    }

    override suspend fun latestPriceStartTime(): Instant {
        return Instant.MIN
    }

    override suspend fun uploadPrices(
        prices: List<NpPrice>, npPricesDocument: NpPricesDocument
    ) {

    }

    override suspend fun npPricesDocument(): NpPricesDocument? {
        return NpPricesDocument()
    }

    override fun npPricesDocumentFlow(): Flow<NpPricesDocument?> =
        flowOf(NpPricesDocument())

    companion object {


        fun testPrices(pricesStart: Instant): List<NpPrice> = listOf(
            NpPrice(
                startTime = pricesStart.toTimestamp(),
                endTime = pricesStart.plusHours(1).toTimestamp(),
                value = 20.0,
            ),
            NpPrice(
                startTime = pricesStart.plusHours(1).toTimestamp(),
                endTime = pricesStart.plusHours(2).toTimestamp(),
                value = 10.0,
            ),
            NpPrice(
                startTime = pricesStart.plusHours(2).toTimestamp(),
                endTime = pricesStart.plusHours(3).toTimestamp(),
                value = 5.0,
            ),
            NpPrice(
                startTime = pricesStart.plusHours(3).toTimestamp(),
                endTime = pricesStart.plusHours(4).toTimestamp(),
                value = 2.0,
            ),
            NpPrice(
                startTime = pricesStart.plusHours(4).toTimestamp(),
                endTime = pricesStart.plusHours(5).toTimestamp(),
                value = 1.5,
            ),
        )

    }


}

private fun Instant.plusHours(i: Long): Instant =
    atZone(ZoneId.systemDefault()).plusHours(i).toInstant()

