package com.folkmanis.laiks.model.np_data

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class NpServerData(
    @JsonNames("data") val data: Data = Data(),
)