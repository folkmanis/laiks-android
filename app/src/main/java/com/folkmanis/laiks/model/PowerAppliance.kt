package com.folkmanis.laiks.model

import com.google.firebase.firestore.DocumentId


data class PowerAppliance(
    @DocumentId val id: String = "",
    val color: String = "#CCCCCC",
    val cycles: List<PowerApplianceCycle> = emptyList(),
    val delay: String = "start",
    val enabled: Boolean = true,
    val minimumDelay: Long = 0,
    val name: String = "",
)
