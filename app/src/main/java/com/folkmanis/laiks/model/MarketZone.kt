package com.folkmanis.laiks.model

import com.google.firebase.firestore.DocumentId

data class MarketZone(

    @DocumentId
    override val id: String = "",

    val dbName: String = "np-data",
    override val description: String = "Latvija",
    val locale: String = "lv",
    val tax: Double = 0.21,
    val url: String = "",
) : DataWithIdAndDescription