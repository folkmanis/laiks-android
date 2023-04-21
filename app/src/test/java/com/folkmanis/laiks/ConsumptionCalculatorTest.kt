package com.folkmanis.laiks

import com.folkmanis.laiks.data.fake.FakeAppliancesService.Companion.dishWasher
import com.folkmanis.laiks.data.fake.FakeAppliancesService.Companion.washer
import com.folkmanis.laiks.data.fake.FakePricesService.Companion.testPrices
import com.folkmanis.laiks.model.NpPrice
import com.folkmanis.laiks.utilities.bestOffset
import com.folkmanis.laiks.utilities.cycleCost
import com.folkmanis.laiks.utilities.cycleLengthSeconds
import com.folkmanis.laiks.utilities.ext.sWtoMWh
import com.folkmanis.laiks.utilities.offsetCosts
import com.folkmanis.laiks.utilities.timeCost
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId


private val pricesStart = LocalDateTime.of(2023, 3, 24, 23, 0)
val prices: List<NpPrice> = testPrices(pricesStart)

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
            prices,
            after30min.epochSecond
        )
        val expected = 600000.0
        assertEquals(expected, cost)
    }

    @Test
    fun consumptionCalculator_consumption_timeCost() {
        val cost = timeCost(
            prices,
            after30min.epochSecond,
            dishWasher
        )
        val expected = 0.0195
        assertEquals(expected, cost!!, 0.0001)
    }

    @Test
    fun consumptionCalculator_consumption_dishWasherCosts() {
        val costs = offsetCosts(
            prices,
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
            prices,
            after30min,
            dishWasher
        )
        assertEquals(3L,costs.bestOffset())
    }

    @Test
    fun consumptionCalculator_consumption_washerCosts() {
        val costs = offsetCosts(
            prices,
            after30min,
            washer
        )
        assertEquals(washerCosts[0], costs[0])
        assertEquals(washerCosts[1], costs[1])
    }

    @Test
    fun consumptionCalculator_consumption_notCalculateDishWasher() {
        val costs = offsetCosts(
            prices,
            after4h,
            dishWasher
        )
        assertTrue(costs.isEmpty())
    }


}
