package com.folkmanis.laiks.model

data class PowerApplianceCycle(
    val consumption: Long = 100,
    val length: Long = 60000, // milliseconds
) {
    val seconds: Long
        get() = length / 1000
}
