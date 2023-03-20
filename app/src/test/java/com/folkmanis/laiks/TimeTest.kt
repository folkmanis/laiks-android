package com.folkmanis.laiks

import com.folkmanis.laiks.utilities.delayToNextMinute
import com.folkmanis.laiks.utilities.delayToNextSecond
import com.folkmanis.laiks.utilities.timeToHours
import com.folkmanis.laiks.utilities.timeToMinutes
import org.junit.Test
import java.time.LocalTime
import org.junit.Assert.assertEquals
import java.time.LocalDateTime

class TimeTest {

    @Test
    fun dateTimeUtils_timeToMinutes_twoDigits() {
        val testTime = LocalTime.of(5, 25)
        val expectedTime = "25"
        assertEquals(expectedTime, timeToMinutes(testTime))
    }

    @Test
    fun dateTimeUtils_timeToMinutes_oneDigit() {
        val testTime = LocalTime.of(5, 5)
        val expectedTime = "05"
        assertEquals(expectedTime, timeToMinutes(testTime))
    }

    @Test
    fun dateTimeUtils_timeToMinutes_zero() {
        val testTime = LocalTime.of(5, 0)
        val expectedTime = "00"
        assertEquals(expectedTime, timeToMinutes(testTime))
    }

    @Test
    fun dateTimeUtils_timeToHours_twoDigits() {
        val testTime = LocalTime.of(23, 10)
        val expectedHour = "23"
        assertEquals(expectedHour, timeToHours(testTime))
    }

    @Test
    fun dateTimeUtils_timeToHours_oneDigit() {
        val testTime = LocalTime.of(8, 10)
        val expectedHour = "8"
        assertEquals(expectedHour, timeToHours(testTime))
    }

    @Test
    fun dateTimeUtils_timeToHours_zero() {
        val testTime = LocalTime.of(0, 10)
        val expectedHour = "0"
        assertEquals(expectedHour, timeToHours(testTime))
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

}