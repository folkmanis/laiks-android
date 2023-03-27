package com.folkmanis.laiks.utilities

import com.folkmanis.laiks.model.NpPrice
import com.folkmanis.laiks.model.PowerAppliance
import com.folkmanis.laiks.model.PowerApplianceCycle
import com.folkmanis.laiks.utilities.ext.toEpochMilli
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun Double.msWtoMWh(): Double =
    this / 1000.0 / 1000.0 / 60.0 / 60.0 / 1000.0

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

fun timeCost(
    prices: List<NpPrice>,
    time: LocalDateTime,
    appliance: PowerAppliance
): Double? {

    var t: Long = appliance.startTime(time)

    var totalCons = 0.0

    for (cycle in appliance.cycles) {

        val consumption = cycleCost(cycle, prices, t)

        if (consumption === null) {
            return null
        }

        totalCons += consumption
        t += cycle.length

    }

    return totalCons.msWtoMWh() // EUR

}

fun offsetCosts(
    npPrices: List<NpPrice>,
    startTime: LocalDateTime,
    appliance: PowerAppliance
): Map<Long, Double> {

    val costs: MutableMap<Long, Double> = mutableMapOf()

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

    var offset = appliance.minimumDelay

    while (time.isBefore(lastTime)) {

        val price = timeCost(npPrices, time, appliance)

        if (price !== null) {
            costs[offset] = price
        }

        offset++
        time = time.plusHours(1)

    }

    return costs

}

fun Map<Long, Double>.bestOffset(): Long? {
    return minByOrNull { pair -> pair.value }?.key
}

fun cycleCost(
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

