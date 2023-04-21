package com.folkmanis.laiks.model.np_data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NpServerData(
    @SerialName("data") val data: Data = Data(),
)
