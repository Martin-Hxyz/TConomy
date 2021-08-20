package xyz.handshot.tconomy.backend.kotlinx

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = UUID::class)
object IdSerializer : KSerializer<UUID>
{
	override val descriptor: SerialDescriptor
		get() = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

	override fun serialize(encoder: Encoder, value: UUID)
	{
		encoder.encodeString(value.toString())
	}

	override fun deserialize(decoder: Decoder): UUID
	{
		return UUID.fromString(decoder.decodeString())
	}
}