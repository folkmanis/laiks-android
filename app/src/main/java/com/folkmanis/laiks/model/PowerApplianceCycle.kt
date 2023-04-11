package com.folkmanis.laiks.model

import com.folkmanis.laiks.MSW_TO_KWH

data class PowerApplianceCycle(
    val consumption: Long = 0,
    val length: Long = 0, // milliseconds
) {
    val seconds: Long
        get() = length / 1000

    val kWh: Double
        get() = consumption.toDouble() * length.toDouble() * MSW_TO_KWH

    companion object {
        val zeroCycle: PowerApplianceCycle
            get() = PowerApplianceCycle(0, 0)
    }


}
