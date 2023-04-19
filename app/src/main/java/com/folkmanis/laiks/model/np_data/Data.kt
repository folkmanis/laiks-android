package com.folkmanis.laiks.model.np_data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Data(
    @SerialName("Rows") val rows: List<Row> = listOf(),
) {
    fun toZonedNpPrices(): List<ZonedNpPrice> = rows.map { it.toZonedNpPrices() }.flatten()

}