package xyz.handshot.tconomy.backend

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.cbor.Cbor
import xyz.handshot.tconomy.backend.kotlinx.KotlinxBackend
import java.io.File
import java.util.*

@OptIn(InternalSerializationApi::class, kotlinx.serialization.ExperimentalSerializationApi::class, ExperimentalUnsignedTypes::class)
class CborBackend(private val root: File): KotlinxBackend(Cbor)
{
	override fun file(id: UUID): File
	{
		return File(root, "accounts/$id.cbor")
	}
}