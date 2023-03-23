package com.folkmanis.laiks

import com.folkmanis.laiks.utilities.delayToNextHour
import com.folkmanis.laiks.utilities.delayToNextMinute
import com.folkmanis.laiks.utilities.delayToNextSecond
import com.folkmanis.laiks.utilities.ext.hoursString
import com.folkmanis.laiks.utilities.ext.minutesString
import com.folkmanis.laiks.utilities.ext.toLocalDateString
import com.folkmanis.laiks.utilities.hoursUntilTimestamp
import com.google.firebase.Timestamp
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

class TimeTest {

    @Test
    fun dateTimeUtils_timeToMinutes_twoDigits() {
        val testTime = LocalTime.of(5, 25)
        val expectedTime = "25"
        assertEquals(expectedTime, testTime.minutesString)
    }

    @Test
    fun dateTimeUtils_timeToMinutes_oneDigit() {
        val testTime = LocalTime.of(5, 5)
        val expectedTime = "05"
        assertEquals(expectedTime, testTime.minutesString)
    }

    @Test
    fun dateTimeUtils_timeToMinutes_zero() {
        val testTime = LocalTime.of(5, 0)
        val expectedTime = "00"
        assertEquals(expectedTime, testTime.minutesString)
    }

    @Test
    fun dateTimeUtils_timeToHours_twoDigits() {
        val testTime = LocalTime.of(23, 10)
        val expectedHour = "23"
        assertEquals(expectedHour, testTime.hoursString)
    }

    @Test
    fun dateTimeUtils_timeToHours_oneDigit() {
        val testTime = LocalTime.of(8, 10)
        val expectedHour = "8"
        assertEquals(expectedHour, testTime.hoursString)
    }

    @Test
    fun dateTimeUtils_timeToHours_zero() {
        val testTime = LocalTime.of(0, 10)
        val expectedHour = "0"
        assertEquals(expectedHour, testTime.hoursString)
    }

    @Test
    fun clockTick_nextMinute_fromDate() {
        val expectedDelay = 1000L
        val dateNow = LocalDateTime.of(
            2023,
            3,
            19,
            23,
            59,
            59,
        )
        assertEquals(expectedDelay, delayToNextMinute(dateNow))
    }

    @Test
    fun clockTick_nextHour_fromDateTime() {
        val expectedDelay = 60000L
        val dateNow = LocalDateTime.of(
            2023,
            3,
            19,
            23,
            59,
            0,
        )
        assertEquals(expectedDelay, delayToNextHour(dateNow))
    }

    @Test
    fun clockTick_nextMinute_fromZero() {
        val expectedDelay = 60000L
        val dateNow = LocalDateTime.of(
            2023,
            3,
            19,
            23,
            59,
            0,
        )
        assertEquals(expectedDelay, delayToNextMinute(dateNow))
    }

    @Test
    fun clockTick_nextSecond_fromDate() {
        val expectedDelay = 500L
        val dateNow = LocalDateTime.of(
            2023,
            3,
            19,
            23,
            59,
            0,
            500_000_000
        )
        assertEquals(expectedDelay, delayToNextSecond(dateNow))
    }

    @Test
    fun dateTimeUtils_hoursUntilTime_shouldCalculate() {
        val startDate = LocalDateTime
            .of(2023, 3, 22, 23, 59, 59)
            .atZone(ZoneId.systemDefault())
            .toInstant()
        val timestampTo = Timestamp(
            Date(startDate.toEpochMilli() + 1000)
        )
        val interval = hoursUntilTimestamp(startDate, timestampTo)
        assertEquals(1, interval)
    }

    @Test
    fun extTimestamp_ToLocalDate_localDate() {
        val dateNow = ZonedDateTime.of(2023, 3, 22, 23, 0, 0, 0, ZoneId.systemDefault())
            .toInstant()

        val dateText = Timestamp(dateNow.epochSecond, dateNow.nano).toLocalDateString()
        assertEquals("22.Mar", dateText)
    }

}
