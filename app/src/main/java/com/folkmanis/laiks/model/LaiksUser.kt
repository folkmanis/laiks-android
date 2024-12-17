package com.folkmanis.laiks.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude


data class LaiksUser(

    @DocumentId val id: String = "",
    val email: String = "",

    val name: String = "",
    val appliances: List<UserPowerAppliance> = emptyList(),
    val marketZoneId: String? = null,
    val includeVat: Boolean = true,
    val vatAmount: Double? = null,
    val locale: String? = null,
    val fixedComponentEnabled: Boolean = false,
    val fixedComponentKwh: Double? = null,
    val tradeMarkupEnabled: Boolean = false,
    val tradeMarkupKwh: Double? = null,
) {
    @get:Exclude
    val tax: Double
        get() = if (includeVat && vatAmount != null) 1.0 + vatAmount else 1.0

    // EUR/MWh
    @get:Exclude
    val fixedComponent: Double
        get() = 1000.0 * if (fixedComponentEnabled) fixedComponentKwh ?: 0.0 else 0.0

    // EUR/MWh
    @get:Exclude
    val tradeMarkup: Double
        get() = 1000.0 * if (tradeMarkupEnabled) tradeMarkupKwh ?: 0.0 else 0.0

    // EUR/MWh
    @get: Exclude
    val extraCost: Double
        get() = fixedComponent + tradeMarkup
}
