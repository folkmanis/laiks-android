package com.folkmanis.laiks.model

data class PowerApplianceRecord(
    val appliance: PowerAppliance,
    val type: Int,
) {
    val name: String
        get() = appliance.localName
    val id: String
        get() = appliance.id
}
