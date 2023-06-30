package com.folkmanis.laiks.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude


enum class ApplianceType(val type: Int) {
    SYSTEM(0), USER(1),
}

data class ApplianceRecord(
    val type: Int = ApplianceType.USER.type,
    val applianceId: String = "",
)

data class LaiksUser(

    @DocumentId val id: String = "",
    val email: String = "",

    val name: String = "",
    val npUploadAllowed: Boolean = false,
    val verified: Boolean = false,
    val appliances: List<ApplianceRecord> = emptyList(),
    val marketZoneId: String = "LV",
    val includeVat: Boolean = true,
    val vatAmount: Double = 0.21,
    val locale: String = "lv",
) {
    @get:Exclude
    val tax: Double
        get() = if (includeVat) 1.0 + vatAmount else 1.0

}
