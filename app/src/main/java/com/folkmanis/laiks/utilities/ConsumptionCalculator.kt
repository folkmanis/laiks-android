package com.folkmanis.laiks.utilities

import com.folkmanis.laiks.model.NpPrice
import com.folkmanis.laiks.model.PowerAppliance
import com.folkmanis.laiks.model.PowerApplianceCycle
import com.folkmanis.laiks.utilities.ext.toEpochMilli
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun msWtoMWh(msWh: Double): Double =
    msWh / 1000.0 / 1000.0 / 60.0 / 60.0 / 1000.0

val PowerAppliance.cycleLength: Long
    get() = this.cycles.fold(0) { acc, cycle -> acc + cycle.length }

fun LocalDateTime.toLocalMilli(): Long =
    this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

fun PowerAppliance.startTime(time: LocalDateTime): Long {
    return if (this.delay == "end") {
        time.toLocalMilli() - this.cycleLength
    } else {
        time.toLocalMilli()
    }

}

data class OffsetPrice(
    val offset: Long,
    val price: Double,
)

fun priceTime(
    prices: List<NpPrice>,
    time: LocalDateTime,
    appliance: PowerAppliance
): Double? {

    var t: Long = appliance.startTime(time)

    var totalCons = 0.0

    for (cycle in appliance.cycles) {

        val consumption = cycleConsumption(cycle, prices, t)

        if (consumption === null) {
            return null
        }

        totalCons += consumption
        t += cycle.length

    }

    return msWtoMWh(totalCons) // EUR

}

fun offsetPrices(
    npPrices: List<NpPrice>,
    startTime: LocalDateTime,
    appliance: PowerAppliance
): List<OffsetPrice> {

    val prices: MutableList<OffsetPrice> = mutableListOf()

    var time = if (appliance.delay === "end") {
        startTime.plusHours(appliance.minimumDelay)
    } else startTime

    val lastTime = npPrices.fold(Instant.MIN) { last, pr ->
        val end = pr.endTime.toDate().toInstant()
        if (end.isAfter(last))
            end
        else
            last
    }
        .atZone(ZoneId.systemDefault()).toLocalDateTime()

//    var time = startTime
    var offset = appliance.minimumDelay

    while (time.isBefore(lastTime)) {

        val price = priceTime(npPrices, time, appliance)

        if (price !== null) {
            prices.add(OffsetPrice(offset, price))
        }

        offset++
        time = time.plusHours(1)

    }

    return prices

}

fun List<OffsetPrice>.bestOffset(): OffsetPrice? {
    var best: OffsetPrice? = null
    for (offsetPrice in this) {
        best = if (best == null || offsetPrice.price < best.price)
            offsetPrice else best
    }
    return best
}

fun cycleConsumption(
    cycle: PowerApplianceCycle,
    npPrices: List<NpPrice>,
    start: Long
): Double? {

    val end = start + cycle.length

    var total = 0.0
    var pos = start

    while (pos != end) {

        val price = npPrices
            .find { pr -> pos >= pr.startTime.toEpochMilli() && pos < pr.endTime.toEpochMilli() }
            ?: return null

        val l = price.endTime.toEpochMilli().coerceAtMost(end) -
                price.startTime.toEpochMilli().coerceAtLeast(pos)
        total += l.toDouble() * cycle.consumption.toDouble() * price.value

        pos = price.endTime.toEpochMilli().coerceAtMost(end)

    }

    return total

}

