package com.folkmanis.laiks.model.np_data

import com.folkmanis.laiks.model.NpPrice
import com.google.firebase.Timestamp
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonNames
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

object NpDateSerializer : KSerializer<ZonedDateTime> {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-d'T'HH:mm:ss")

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("np_data.date", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): ZonedDateTime {
        val string = decoder.decodeString()
        return LocalDateTime.parse(string, formatter).atZone(ZoneId.of("CET"))
    }

    override fun serialize(encoder: Encoder, value: ZonedDateTime) {
        encoder.encodeString(value.toString())
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

    fun toNpPrices(): List<NpPrice> =
        columns.map { column ->
            NpPrice(
                value = column.value,
                startTime = Timestamp(Date.from(startTime.minusDays(column.index).toInstant())),
                endTime = Timestamp(Date.from(endTime.minusDays(column.index).toInstant()))
            )
        }

}