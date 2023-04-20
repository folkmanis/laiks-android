package com.folkmanis.laiks.model

import com.folkmanis.laiks.model.np_data.NpServerData
import com.folkmanis.laiks.model.np_data.ZonedNpPrice
import com.folkmanis.laiks.utilities.ext.toTimestamp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId


fun ZonedNpPrice.toNpPrice(): NpPrice =
    NpPrice(
        id = id,
        startTime = startTime.toTimestamp(),
        endTime = endTime.toTimestamp(),
        value = value,
    )

fun NpServerData.toNpPrices(): List<NpPrice> =
    data.toZonedNpPrices()
        .map(ZonedNpPrice::toNpPrice)

data class NpPrice(
    @DocumentId val id: String = "",
    val startTime: Timestamp = Timestamp.now(),
    val endTime: Timestamp = Timestamp.now(),
    val value: Double = 0.0,
)
