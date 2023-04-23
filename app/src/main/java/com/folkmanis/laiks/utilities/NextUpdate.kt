package com.folkmanis.laiks.utilities

import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit

sealed interface NextUpdate {
    object Now : NextUpdate
    data class At(val instant: Instant) : NextUpdate

    companion object {

        fun build(
            now: Instant,
            lastUpdate: Instant,
            hour: Int,
            minute: Int,
            zoneString: String
        ): NextUpdate {

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

            return if (lastUpdate.atZone(zoneId) < pastDateTime)
                Now
            else
                At(pastDateTime.plusDays(1).toInstant())
        }

    }
}
