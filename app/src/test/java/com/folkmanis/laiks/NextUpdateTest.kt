package com.folkmanis.laiks

import com.folkmanis.laiks.utilities.NextUpdate
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

const val H = 13
const val M = 0
const val TZ = "CET"

class NextUpdateTest {

    private val zoneId = ZoneId.of(TZ)
    private val testDay = LocalDate.of(2023, 4, 23)

    @Test
    fun nextUpdate_pastTime_runNow() {
        val testHour = LocalTime.of(13, 10)
        val now = ZonedDateTime.of(
            testDay, testHour, zoneId
        ).toInstant()
        val lastUpdate = ZonedDateTime.of(
            testDay.minusDays(1), testHour, zoneId
        ).toInstant()
        val nextUpdate = NextUpdate.build(now, lastUpdate, H, M, TZ)
        assertTrue(nextUpdate is NextUpdate.Now)
    }

    @Test
    fun nextUpdate_futureTime_runNextDay() {
        val now = ZonedDateTime.of(
            testDay, LocalTime.of(23, 0), zoneId
        ).toInstant()
        val lastUpdate = ZonedDateTime.of(
            testDay, LocalTime.of(13, 10), zoneId
        ).toInstant()
        val nextTestUpdate = ZonedDateTime.of(
            testDay.plusDays(1), LocalTime.of(H, M), zoneId
        ).toInstant()
        val nextUpdate = NextUpdate.build(now, lastUpdate, H, M, TZ)
        assertTrue(
            nextUpdate is NextUpdate.At &&
                    nextUpdate.instant == nextTestUpdate
        )
    }

    @Test
    fun nextUpdate_futureTime_runToday() {
        val now = ZonedDateTime.of(
            testDay.plusDays(1), LocalTime.of(12, 0), zoneId
        ).toInstant()
        val lastUpdate = ZonedDateTime.of(
            testDay, LocalTime.of(13, 10), zoneId
        ).toInstant()
        val nextTestUpdate = ZonedDateTime.of(
            testDay.plusDays(1), LocalTime.of(H, M), zoneId
        ).toInstant()
        val nextUpdate = NextUpdate.build(now, lastUpdate, H, M, TZ)
        assertTrue(
            nextUpdate is NextUpdate.At &&
                    nextUpdate.instant == nextTestUpdate
        )
    }
    @Test
    fun nextUpdate_oldData_runToday() {
        val now = ZonedDateTime.of(
            testDay.plusDays(1), LocalTime.of(12, 0), zoneId
        ).toInstant()
        val lastUpdate = ZonedDateTime.of(
            testDay.minusDays(2), LocalTime.of(13, 10), zoneId
        ).toInstant()
        val nextUpdate = NextUpdate.build(now, lastUpdate, H, M, TZ)
        assertTrue(
            nextUpdate is NextUpdate.Now
        )
    }
}

