package com.folkmanis.laiks

import com.folkmanis.laiks.utilities.timeToHours
import com.folkmanis.laiks.utilities.timeToMinutes
import org.junit.Test
import java.time.LocalTime
import org.junit.Assert.assertEquals

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

}