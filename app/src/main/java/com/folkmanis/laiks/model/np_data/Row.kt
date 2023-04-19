package com.folkmanis.laiks.model.np_data

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonNames
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object NpDateSerializer : KSerializer<ZonedDateTime> {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dTHH:mm:ss")

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(
            "com.folkmanis.laiks.model.np_data.date",
            PrimitiveKind.STRING
        )

    override fun deserialize(decoder: Decoder): ZonedDateTime {
        val string = decoder.decodeString()
        return ZonedDateTime.parse(string, formatter)
    }

    override fun serialize(encoder: Encoder, value: ZonedDateTime) {
        encoder.encodeString(value.toString())
    }
}

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class Row(

    @JsonNames("Columns")
    val columns: ArrayList<Column> = arrayListOf(),

    @JsonNames("StartTime")
    @Serializable(with = NpDateSerializer::class)
    val startTime: ZonedDateTime = ZonedDateTime.now(),

    @JsonNames("EndTime")
    val endTime: String = "",

    @JsonNames("IsExtraRow") val isExtraRow: Boolean = false,

    ) {

    fun toNpPrices() {
        // TODO
    }
}