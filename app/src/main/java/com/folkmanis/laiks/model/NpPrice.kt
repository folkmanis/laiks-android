package com.folkmanis.laiks.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class NpPrice(
    @DocumentId val id: String = "",
    val startTime: Timestamp = Timestamp.now(),
    val endTime: Timestamp = Timestamp.now(),
    val value: Double = 0.0,
)
