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

    override suspend fun npPricesDocument(): NpPricesDocument? {
        return NpPricesDocument()
    }

    override fun npPricesDocumentFlow(): Flow<NpPricesDocument?> =
        flowOf(NpPricesDocument())

    companion object {

        private val TEST_PRICES_LIST = listOf(
            20.0, 10.0, 5.0, 2.0, 1.5, 20.0, 10.0, 5.0, 2.0, 1.5,
            20.0, 10.0, 5.0, 2.0, 1.5, 20.0, 10.0, 5.0, 2.0, 1.5
        )

        fun testPrices(pricesStart: Instant): List<NpPrice> = List(TEST_PRICES_LIST.size) {
            val startOffset: Long = (it.toLong() * 15)
            NpPrice(
                startTime = pricesStart.plusMinutes(startOffset).toTimestamp(),
                endTime = pricesStart.plusMinutes(startOffset + 15).toTimestamp(),
                value = TEST_PRICES_LIST[it]
            )
        }
    }

}

private fun Instant.plusMinutes(i: Long): Instant =
    atZone(ZoneId.systemDefault()).plusMinutes(i).toInstant()

