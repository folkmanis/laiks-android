package com.folkmanis.laiks.model

import com.google.firebase.firestore.DocumentId

data class MarketZone(

    @DocumentId
    val id: String = "",

    val dbName: String = "np-data",
    val description: String = "Latvija",
    val locale: String = "lv",
    val tax: Double = 0.21,
    val url: String = "",
)