package xyz.handshot.tconomy

import xyz.handshot.tconomy.backend.Backend
import xyz.handshot.tconomy.backend.CachedBackend
import xyz.handshot.tconomy.backend.CborBackend
import xyz.handshot.tconomy.backend.JsonBackend
import xyz.handshot.tconomy.transaction.TransactionManager
import xyz.handshot.tconomy.transaction.TransactionManagerImpl

interface TConomy
{
	companion object
	{
		fun build(config: Config): TConomy
		{
			val backend = when (config.backend)
			{
				"cbor" -> CachedBackend(CborBackend(config.accountsDir))
				else -> CachedBackend(JsonBackend(config.accountsDir))
			}

			return object : TConomy
			{
				override val backend: Backend
					get() = backend
				override val config: Config
					get() = config
				override val transactionManager: TransactionManager
					get() = TransactionManagerImpl(config)
			}
		}
	}

	val backend: Backend
	val config: Config
	val transactionManager: TransactionManager
}