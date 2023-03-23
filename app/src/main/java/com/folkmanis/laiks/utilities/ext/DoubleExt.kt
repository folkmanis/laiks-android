package com.folkmanis.laiks.utilities.ext

import com.folkmanis.laiks.utilities.EUR_MWH_TO_CENTS_KWH

fun Double.eurMWhToCentsKWh(): Double {
    return this * EUR_MWH_TO_CENTS_KWH
}

fun Double.withVat(amount: Double): Double {
    return this * amount
}