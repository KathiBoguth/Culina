package com.example.culina.dailyCooking.database.dto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.UUID

@Serializable
data class UpdateDailyCookingDto(

    @SerialName("name")
    val name: String,

    @SerialName("ingredients")
    val ingredients: Set<String>,

    @SerialName("image")
    val image: String?,

    @SerialName("rating")
    val rating: Int,

    @SerialName("id")
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
)

object UUIDSerializer : KSerializer<UUID> {
    override val descriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): UUID {
        return UUID.fromString(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeString(value.toString())
    }
}