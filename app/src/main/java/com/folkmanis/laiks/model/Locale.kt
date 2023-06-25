package com.folkmanis.laiks.model

import com.google.firebase.firestore.DocumentId

data class Locale(
    @DocumentId
    val id: String = "",
    val language: String = "",
)
