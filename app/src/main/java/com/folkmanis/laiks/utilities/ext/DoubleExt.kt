package com.folkmanis.laiks.utilities.ext

import com.folkmanis.laiks.CURRENCY_DECIMALS
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.Locale

const val EUR_MWH_TO_CENTS_KWH = 1.0 / 1000.0 * 100.0

fun Double.eurMWhToCentsKWh(): Double {
    return this * EUR_MWH_TO_CENTS_KWH
}

fun Double.sWtoMWh(): Double =
    this / 1000.0 / 1000.0 / 60.0 / 60.0

fun Double.eurToCents(): Double = this * 100.0

fun Double.withVat(amount: Double): Double {
    return this * amount
}

fun Double.toFormattedDecimals(
    locale: Locale = Locale.getDefault(Locale.Category.FORMAT),
    digits: Int = CURRENCY_DECIMALS,
): String {
    val formatter = DecimalFormat
        .getNumberInstance(locale).apply {
            maximumFractionDigits = digits
            minimumFractionDigits = digits
            roundingMode = RoundingMode.HALF_UP
        }
    return formatter.format(this)
}