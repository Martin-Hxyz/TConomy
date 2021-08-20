package xyz.handshot.tconomy.backend

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import xyz.handshot.tconomy.backend.kotlinx.KotlinxBackend
import java.io.File
import java.util.*

@OptIn(ExperimentalSerializationApi::class)
class JsonBackend(private val root: File) : KotlinxBackend(Json)
{
	override fun file(id: UUID): File
	{
		return File(root, "accounts/$id.json")
	}
}