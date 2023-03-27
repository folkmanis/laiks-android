package com.folkmanis.laiks

import com.folkmanis.laiks.model.NpPrice
import com.folkmanis.laiks.model.PowerAppliance
import com.folkmanis.laiks.model.PowerApplianceCycle
import com.folkmanis.laiks.utilities.*
import com.folkmanis.laiks.utilities.ext.sWtoMWh
import com.google.firebase.Timestamp
import org.junit.Assert.*
import org.junit.Test
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


private fun LocalDateTime.toTimestamp(): Timestamp {
    val instant = this.atZone(ZoneId.systemDefault()).toInstant()
    return Timestamp(Date.from(instant))
}

private val pricesStart = LocalDateTime.of(2023, 3, 24, 23, 0)
val testPrices: List<NpPrice> = listOf(
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
    delay = "start",
    minimumDelay = 0, // hours
    enabled = true,
    color = "#ff00ff",
)

private val dishWasherCosts = mapOf(
    0L to 0.0195,
    1L to 0.00975,
    2L to 17070000.0.sWtoMWh(),
    3L to 7500000.0.sWtoMWh(),
)
private val washerCosts = mapOf(
    3L to 19140000.0.sWtoMWh(),
    4L to 7845000.0.sWtoMWh(),
)

val washer = dishWasher.copy(delay = "end", minimumDelay = 3L)

val after30min: Instant = pricesStart
    .plusMinutes(30)
    .atZone(ZoneId.systemDefault())
    .toInstant()
val after4h: Instant = pricesStart
    .plusHours(4)
    .atZone(ZoneId.systemDefault())
    .toInstant()

class ConsumptionCalculatorTest {

    @Test
    fun consumptionCalculator_consumption_cyclesLength() {
        val total = dishWasher.cycleLengthSeconds
        val expected = (5 + 30 + 40) * 60 // 5 + 30 + 40 min
        assertEquals(expected.toLong(), total)
    }

    @Test
    fun consumptionCalculator_consumption_oneCycleCost() {
        val cost = cycleCost(
            dishWasher.cycles[0],
            testPrices,
            after30min.epochSecond
        )
        val expected = 600000.0
        assertEquals(expected, cost)
    }

    @Test
    fun consumptionCalculator_consumption_timeCost() {
        val cost = timeCost(
            testPrices,
            after30min.epochSecond,
            dishWasher
        )
        val expected = 0.0195
        assertEquals(expected, cost!!, 0.0001)
    }

    @Test
    fun consumptionCalculator_consumption_dishWasherCosts() {
        val costs = offsetCosts(
            testPrices,
            after30min,
            dishWasher
        )
        assertEquals(dishWasherCosts[0]!!, costs[0]!!, 0.0001)
        assertEquals(dishWasherCosts[1]!!, costs[1]!!, 0.0001)
        assertEquals(dishWasherCosts[2]!!, costs[2]!!, 0.0001)
        assertEquals(dishWasherCosts[3]!!, costs[3]!!, 0.0001)
    }

    @Test
    fun consumptionCalculator_consumption_dishWasherBestOffset() {
        val costs = offsetCosts(
            testPrices,
            after30min,
            dishWasher
        )
        assertEquals(3L,costs.bestOffset())
    }

    @Test
    fun consumptionCalculator_consumption_washerCosts() {
        val costs = offsetCosts(
            testPrices,
            after30min,
            washer
        )
        assertEquals(washerCosts[0], costs[0])
        assertEquals(washerCosts[1], costs[1])
    }

    @Test
    fun consumptionCalculator_consumption_notCalculateDishWasher() {
        val costs = offsetCosts(
            testPrices,
            after4h,
            dishWasher
        )
        assertTrue(costs.isEmpty())
    }


}
