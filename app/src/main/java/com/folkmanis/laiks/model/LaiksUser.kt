package com.folkmanis.laiks.model

import com.google.firebase.firestore.DocumentId


enum class ApplianceType(val type: Int) {
    SYSTEM(0), USER(1),
}

data class ApplianceRecord(
    val type: ApplianceType,
    val applianceId: String,
)

data class LaiksUser(

    @DocumentId val id: String = "",
    val email: String = "",

    val name: String = "",
    val npUploadAllowed: Boolean = false,
    val verified: Boolean = false,
    val appliances: List<ApplianceRecord> = emptyList()
)
