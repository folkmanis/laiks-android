package com.folkmanis.laiks.model.np_data

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

private object NpDateSerializer : KSerializer<ZonedDateTime> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("np_data.date", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): ZonedDateTime {
        val string = decoder.decodeString()
        return LocalDateTime.parse(string).atZone(ZoneId.of("CET"))
    }

    override fun serialize(encoder: Encoder, value: ZonedDateTime) {
        encoder.encodeString(value.toLocalDateTime().toString())
    }
}

@Serializable
data class Row(

    @SerialName("Columns")
    val columns: List<Column> = listOf(),

    @SerialName("StartTime")
    @Serializable(with = NpDateSerializer::class)
    val startTime: ZonedDateTime = ZonedDateTime.now(),

    @SerialName("EndTime")
    @Serializable(with = NpDateSerializer::class)
    val endTime: ZonedDateTime = ZonedDateTime.now(),

    @SerialName("IsExtraRow") val isExtraRow: Boolean = false,

    ) {

    fun toZonedNpPrices(): List<ZonedNpPrice> =
        columns.map { column ->
            ZonedNpPrice(
                value = column.value,
                startTime = startTime.minusDays(column.index),
                endTime = endTime.minusDays(column.index)
            )
        }

}