package com.folkmanis.laiks.data.fake

import com.folkmanis.laiks.data.PricesService
import com.folkmanis.laiks.model.NpPrice
import com.folkmanis.laiks.model.PowerAppliance
import com.folkmanis.laiks.model.PowerApplianceCycle
import com.folkmanis.laiks.utilities.ext.toLocalDateTime
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class FakePricesService : PricesService {

    override suspend fun addAppliance(appliance: PowerAppliance): String {
        return "12ABF34"
    }

    override val allAppliancesFlow: Flow<List<PowerAppliance>>
        get() = flowOf(testAppliances)

    override suspend fun getAppliance(id: String): PowerAppliance? {
       return testAppliances.find { it.id == id }
    }

    override suspend fun updateAppliance(appliance: PowerAppliance) {

    }

    override suspend fun deleteAppliance(id: String) {

    }

    override suspend fun activeAppliances(): List<PowerAppliance> {
        return testAppliances
    }

    override fun lastDaysPricesFlow(days: Long): Flow<List<NpPrice>> {
       return flowOf(testPrices(LocalDateTime.now()))
    }

    override suspend fun npPrices(startTimestamp: Timestamp): List<NpPrice> {
        return testPrices(startTimestamp.toLocalDateTime())
    }

    companion object {

        val dishWasher = PowerAppliance(
            cycles = listOf(
                PowerApplianceCycle(
                    consumption = 100,
                    length = 5 * 60 * 1000, // 5 min
                ),
                PowerApplianceCycle(
                    consumption = 2000,
                    length = 30 * 60 * 1000, // 30 min
                ),
                PowerApplianceCycle(
                    consumption = 150,
                    length = 40 * 60 * 1000, // 40 min
                ),
            ),
            name = "Trauku mašīna",
            delay = "start",
            minimumDelay = 0, // hours
            enabled = true,
            color = "#ff00ff",
            id="12AFE34",
        )

        val washer = dishWasher.copy(
            id="12AFE35",
            delay = "end",
            minimumDelay = 3L,
            name = "Veļasmašīna"
        )


        val testAppliances = listOf(
            dishWasher,
            washer
        )

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