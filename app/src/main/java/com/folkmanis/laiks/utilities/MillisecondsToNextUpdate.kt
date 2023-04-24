package com.folkmanis.laiks.utilities

import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit

fun millisecondsToNextUpdate(
    now: Instant,
    lastUpdate: Instant,
    hour: Int,
    minute: Int,
    zoneString: String
): Long {

    require(now >= lastUpdate)

    val zoneId = ZoneId.of(zoneString)

    val todayUpdateTime = now
        .atZone(zoneId)
        .withHour(hour)
        .withMinute(minute)
        .truncatedTo(ChronoUnit.MINUTES)

    val pastDateTime = if (now.atZone(zoneId) > todayUpdateTime)
        todayUpdateTime
    else
        todayUpdateTime.minusDays(1)

    return if (lastUpdate < pastDateTime.toInstant())
        0
    else {
        pastDateTime.plusDays(1).toInstant().toEpochMilli() - now.toEpochMilli()
    }


}

