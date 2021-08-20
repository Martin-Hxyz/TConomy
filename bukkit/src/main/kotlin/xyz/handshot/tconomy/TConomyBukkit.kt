package xyz.handshot.tconomy

import io.github.slimjar.app.builder.ApplicationBuilder
import net.milkbowl.vault.economy.Economy
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.java.JavaPlugin
import xyz.handshot.tconomy.backend.CachedBackend
import xyz.handshot.tconomy.commands.*

class TConomyBukkit : JavaPlugin()
{
	private val app = ApplicationBuilder.appending("TConomy").build()
	private val tconomyConfig = buildConfig()
	private val tconomy: TConomy by lazy { TConomy.build(tconomyConfig) }
	private val economy by lazy { EconomyService(tconomy.backend, tconomy.transactionManager) }

	override fun onEnable()
	{
		server.servicesManager.register(Economy::class.java, economy, this, ServicePriority.Normal)
		getCommand("balance")?.setExecutor(CommandBalance(economy))
		getCommand("pay")?.setExecutor(CommandPay(economy, tconomy.transactionManager))

		CommandTConomy(economy, tconomy.backend, tconomy.transactionManager).let { command ->
			getCommand("tconomy")?.setExecutor(command)
			getCommand("tconomy")?.setTabCompleter(command)
		}

		server.pluginManager.registerEvents(JoinLeaveListener(tconomy.backend), this)
	}

	override fun onDisable()
	{
		tconomy.backend.let { backend ->
			if (backend is CachedBackend)
			{
				backend.cached().forEach(backend::save)
			}
		}
	}

	private fun buildConfig(): Config
	{
		saveDefaultConfig()

		return Config(
			backend = config.getString("backend") ?: "json",
			transactionHistory = config.getInt("transaction-history"),
			accountsDir = dataFolder
		)
	}
}