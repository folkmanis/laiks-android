package com.folkmanis.laiks.model

const val MSW_TO_KWH = 1.0 / 1000.0 / 1000.0 / 3600.0

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
