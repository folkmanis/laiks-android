package com.folkmanis.laiks.utilities

import com.folkmanis.laiks.model.NpPrice
import com.folkmanis.laiks.model.PowerAppliance
import com.folkmanis.laiks.model.PowerApplianceCycle
import com.folkmanis.laiks.utilities.ext.sWtoMWh
import java.time.Instant

fun Map<Long, Double>.bestOffset(): Int? {
    return minByOrNull { mapEntry -> mapEntry.value }?.key?.toInt()
}

private fun PowerAppliance.startTime(time: Long): Long {
    return if (this.delay == "end") {
        time - this.cycleLengthSeconds
    } else {
        time
    }
}

internal val PowerAppliance.cycleLengthSeconds: Long
    get() = this.cycles.fold(0) { acc, cycle -> acc + cycle.seconds }

fun offsetCosts(
    npPrices: List<NpPrice>,
    startTime: Instant,
    appliance: PowerAppliance
): Map<Long, Double> {

    val costs: MutableMap<Long, Double> = mutableMapOf()

    var offset = appliance.minimumDelay

    var time = startTime.epochSecond + offset * 60 * 60
    if (appliance.delay === "end") {
        time -= appliance.cycleLengthSeconds
    }

    val lastTime = npPrices.maxOfOrNull { it.endTime.seconds } ?: Long.MIN_VALUE

    while (time < lastTime) {

        val price = timeCost(npPrices, time, appliance)

        if (price !== null) {
            costs[offset] = price
        }

        offset++
        time += 60 * 60 // 1h

    }

    return costs

}

fun timeCost(
    prices: List<NpPrice>,
    time: Long,
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
        t += cycle.seconds

    }

    return totalCons.sWtoMWh() // EUR

}

fun cycleCost(
    cycle: PowerApplianceCycle,
    npPrices: List<NpPrice>,
    start: Long
): Double? {

    val end = start + cycle.seconds

    var total = 0.0
    var pos = start

    while (pos != end) {

        val price = npPrices
            .find { pr -> pos >= pr.startTime.seconds && pos < pr.endTime.seconds }
            ?: return null

        val l = price.endTime.seconds.coerceAtMost(end) -
                price.startTime.seconds.coerceAtLeast(pos)
        total += l.toDouble() * cycle.consumption.toDouble() * price.value

        pos = price.endTime.seconds.coerceAtMost(end)

    }

    return total

}

