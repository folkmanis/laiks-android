package com.folkmanis.laiks.model.np_data

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class Data(
    @JsonNames("Rows") val rows: ArrayList<Row> = arrayListOf(),
)