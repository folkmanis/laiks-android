package com.folkmanis.laiks.model.np_data

import com.folkmanis.laiks.model.NpPrice
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Data(
    @SerialName("Rows") val rows: List<Row> = listOf(),
) {
    fun toNpPrices(): List<NpPrice> = rows.map { it.toNpPrices() }.flatten()

}