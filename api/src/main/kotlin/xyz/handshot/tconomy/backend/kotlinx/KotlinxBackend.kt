package xyz.handshot.tconomy.backend.kotlinx

import kotlinx.serialization.*
import xyz.handshot.tconomy.account.Account
import xyz.handshot.tconomy.backend.Backend
import java.io.File
import java.util.*

@ExperimentalSerializationApi
abstract class KotlinxBackend constructor(private val serializer: SerialFormat) : Backend
{
	abstract fun file(id: UUID): File

	override fun exists(id: UUID): Boolean
	{
		return file(id).exists()
	}

	override fun load(id: UUID): Account?
	{
		if (!exists(id))
		{
			return null
		}

		val file = file(id)

		if (!file.exists())
		{
			file.parentFile.mkdirs()
			file.createNewFile()
		}

		return when (serializer)
		{
			is BinaryFormat -> serializer.decodeFromByteArray<Account>(file.readBytes())
			is StringFormat -> serializer.decodeFromString<Account>(file.readText())
			else -> null
		}
	}

	override fun save(account: Account)
	{
		val file = file(account.id)

		if (!file.exists())
		{
			file.parentFile.mkdirs()
			file.createNewFile()
		}

		when (serializer)
		{
			is BinaryFormat -> file.writeBytes(serializer.encodeToByteArray(account))
			is StringFormat -> file.writeText(serializer.encodeToString(account))
			else -> return
		}
	}
}