package com.folkmanis.laiks.data.fake

import com.folkmanis.laiks.data.PricesService
import com.folkmanis.laiks.model.NpPrice
import com.folkmanis.laiks.utilities.ext.toLocalDateTime
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

class FakePricesService : PricesService {

    override fun lastDaysPricesFlow(days: Long): Flow<List<NpPrice>> {
       return flowOf(testPrices(LocalDateTime.now()))
    }

    override suspend fun npPrices(startTimestamp: Timestamp): List<NpPrice> {
        return testPrices(startTimestamp.toLocalDateTime())
    }

    override fun pricesFromDateTime(dateTime: LocalDateTime): Flow<List<NpPrice>> {
        return flowOf(testPrices(dateTime))
    }

    override suspend fun lastUpdate(): Instant {
        return Instant.MIN
    }

    override suspend fun uploadPrices(prices: List<NpPrice>) {

    }

    companion object {


    private fun LocalDateTime.toTimestamp(): Timestamp {
        val instant = this.atZone(ZoneId.systemDefault()).toInstant()
        return Timestamp(Date.from(instant))
    }

    fun testPrices(pricesStart: LocalDateTime): List<NpPrice> = listOf(
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