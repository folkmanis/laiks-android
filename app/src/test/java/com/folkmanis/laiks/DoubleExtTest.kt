package com.folkmanis.laiks

import com.folkmanis.laiks.utilities.ext.toFormattedDecimals
import org.junit.Test
import org.junit.Assert.assertEquals
import java.util.*

class DoubleExtTest {

    @Test
    fun doubleExt_toDecimalsString_small() {
        val result = 0.0.toFormattedDecimals(
           locale= Locale.forLanguageTag("lv"),
            digits = 2,
        )
        assertEquals("0,00", result)
    }

    @Test
    fun doubleExt_toDecimalsString_large() {
        val result = 0.125.toFormattedDecimals(
           locale= Locale.forLanguageTag("lv"),
            digits = 2,
        )
        assertEquals("0,13", result)
    }

    @Test
    fun doubleExt_toDecimalsString_negative() {
        val result = (-0.125).toFormattedDecimals(
           locale= Locale.forLanguageTag("lv"),
            digits = 2,
        )
        assertEquals("-0,13", result)
    }


}