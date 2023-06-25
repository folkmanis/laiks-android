package com.folkmanis.laiks.model

import com.google.firebase.firestore.DocumentId

data class MarketZone(

    @DocumentId
    val id: String = "",

    val dbName: String = "",
    val description: String = "",
    val locale: String = "lv",
    val tax: Double = 0.0,
    val url: String = "",
)