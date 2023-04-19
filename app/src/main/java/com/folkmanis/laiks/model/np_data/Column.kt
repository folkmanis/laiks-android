package com.folkmanis.laiks.model.np_data

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonNames

object ValueSerializer : KSerializer<Double> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(
            "kotlinx.serialization.primitive",
            PrimitiveKind.DOUBLE
        )

    override fun deserialize(decoder: Decoder): Double {
        return decoder.decodeString()
            .replace(',', '.')
            .toDouble()
    }

    override fun serialize(encoder: Encoder, value: Double) {
        val string = value.toString().replace('.', ',')
        encoder.encodeString(string)
    }
}


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class Column(

    @SerialName("Index") val index: Long = 0,

    @SerialName("Value")
    @Serializable(with = ValueSerializer::class)
    val value: Double = 0.0,

    )