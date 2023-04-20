package com.folkmanis.laiks.model.np_data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

@Serializable
data class NpServerData(
    @SerialName("data") val data: Data = Data(),
)
