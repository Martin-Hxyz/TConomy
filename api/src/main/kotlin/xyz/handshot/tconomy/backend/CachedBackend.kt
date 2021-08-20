package xyz.handshot.tconomy.backend

import xyz.handshot.tconomy.account.Account
import java.util.*

class CachedBackend(private val backend: Backend) : Backend
{
	private val cache = mutableMapOf<UUID, Account>()

	override fun exists(id: UUID): Boolean
	{
		return cache.containsKey(id) || backend.exists(id)
	}

	override fun load(id: UUID): Account?
	{
		if (cache.containsKey(id))
		{
			return cache[id]!!
		}

		val account = backend.load(id) ?: Account(id, 0.0, LinkedList())
		cache[id] = account
		return account
	}

	override fun save(account: Account)
	{
		backend.save(account)
	}

	override fun count(): Int
	{
		return backend.count()
	}

	fun cached(): Set<Account>
	{
		return cache.values.toSet()
	}

	fun invalidate(id: UUID)
	{
		cache.remove(id)
	}
}
