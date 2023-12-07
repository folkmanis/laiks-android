package com.folkmanis.laiks.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude


data class LaiksUser(

    @DocumentId val id: String = "",
    val email: String = "",

    val name: String = "",
    val npUploadAllowed: Boolean = false,
    val verified: Boolean = false,
    val appliances: List<UserPowerAppliance> = emptyList(),
    val marketZoneId: String? = null,
    val includeVat: Boolean = true,
    val vatAmount: Double = 0.0,
    val locale: String? = null,
) {
    @get:Exclude
    val tax: Double
        get() = if (includeVat) 1.0 + vatAmount else 1.0

}
