package xyz.handshot.tconomy

import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.economy.EconomyResponse
import org.bukkit.OfflinePlayer
import xyz.handshot.tconomy.account.Account
import xyz.handshot.tconomy.backend.Backend
import xyz.handshot.tconomy.transaction.Transaction
import xyz.handshot.tconomy.transaction.TransactionManager
import java.text.DecimalFormat
import java.util.*
import java.util.logging.Logger

class EconomyService(private val backend: Backend, private val transactionManager: TransactionManager) : Economy
{
	private val logger = Logger.getLogger(EconomyService::class.java.simpleName)
	private val format = DecimalFormat.getCurrencyInstance(Locale.US)

	fun account(id: UUID): Account
	{
		val backendAccount = backend.load(id)

		if (backendAccount == null)
		{
			logger.warning("Could not fetch player data for $id")
		}

		// TODO Allow configuration of default balance
		return backend.load(id) ?: Account(id, 0.0, LinkedList())
	}

	override fun isEnabled(): Boolean
	{
		return true
	}

	override fun getName(): String
	{
		return "TConomy"
	}

	override fun hasBankSupport(): Boolean
	{
		return false
	}

	override fun fractionalDigits(): Int
	{
		return -1
	}

	override fun format(amount: Double): String
	{
		return format.format(amount)
	}

	override fun currencyNamePlural(): String
	{
		return "dollars"
	}

	override fun currencyNameSingular(): String
	{
		return "dollar"
	}

	override fun hasAccount(playerName: String): Boolean
	{
		TODO("Not yet implemented")
	}

	override fun hasAccount(player: OfflinePlayer): Boolean
	{
		return backend.exists(player.uniqueId)
	}

	override fun hasAccount(playerName: String, worldName: String): Boolean
	{
		TODO("Not yet implemented")
	}

	override fun hasAccount(player: OfflinePlayer, worldName: String): Boolean
	{
		return hasAccount(player)
	}

	override fun getBalance(playerName: String): Double
	{
		TODO("Not yet implemented")
	}

	override fun getBalance(player: OfflinePlayer): Double
	{
		return account(player.uniqueId).balance
	}

	override fun getBalance(playerName: String, world: String): Double
	{
		TODO("Not yet implemented")
	}

	override fun getBalance(player: OfflinePlayer, world: String): Double
	{
		return getBalance(player)
	}

	override fun has(playerName: String, amount: Double): Boolean
	{
		TODO("Not yet implemented")
	}

	override fun has(player: OfflinePlayer, amount: Double): Boolean
	{
		return getBalance(player) >= amount
	}

	override fun has(playerName: String, worldName: String, amount: Double): Boolean
	{
		TODO("Not yet implemented")
	}

	override fun has(player: OfflinePlayer, worldName: String, amount: Double): Boolean
	{
		return has(player, amount)
	}

	override fun withdrawPlayer(playerName: String, amount: Double): EconomyResponse
	{
		TODO("Not yet implemented")
	}

	override fun withdrawPlayer(player: OfflinePlayer, amount: Double): EconomyResponse
	{
		if (!hasAccount(player))
		{
			createPlayerAccount(player)
		}

		val account = account(player.uniqueId)

		if (account.balance < amount)
		{
			return EconomyResponse(0.0, account.balance, EconomyResponse.ResponseType.FAILURE, "Insufficient balance")
		}

		val transaction = Transaction(change = amount * -1, summary = account.balance - amount, vendor="", timestamp = System.currentTimeMillis())
		transactionManager.process(transaction, account)

		return EconomyResponse(amount, transaction.summary, EconomyResponse.ResponseType.SUCCESS, "")
	}

	override fun withdrawPlayer(playerName: String, worldName: String, amount: Double): EconomyResponse
	{
		TODO("Not yet implemented")
	}

	override fun withdrawPlayer(player: OfflinePlayer, worldName: String, amount: Double): EconomyResponse
	{
		return withdrawPlayer(player, amount)
	}

	override fun depositPlayer(playerName: String, amount: Double): EconomyResponse
	{
		TODO("Not yet implemented")
	}

	override fun depositPlayer(player: OfflinePlayer, amount: Double): EconomyResponse
	{
		if (!hasAccount(player))
		{
			createPlayerAccount(player)
		}

		val account = account(player.uniqueId)
		val transaction = Transaction(change = amount, summary = account.balance + amount, vendor = "", timestamp = System.currentTimeMillis())
		transactionManager.process(transaction, account)

		return EconomyResponse(amount, transaction.summary, EconomyResponse.ResponseType.SUCCESS, "")
	}

	override fun depositPlayer(playerName: String, worldName: String, amount: Double): EconomyResponse
	{
		TODO("Not yet implemented")
	}

	override fun depositPlayer(player: OfflinePlayer, worldName: String, amount: Double): EconomyResponse
	{
		return depositPlayer(player, amount)
	}

	override fun createBank(name: String, player: String): EconomyResponse
	{
		TODO("Not yet implemented")
	}

	override fun createBank(name: String, player: OfflinePlayer): EconomyResponse
	{
		TODO("Not yet implemented")
	}

	override fun deleteBank(name: String): EconomyResponse
	{
		TODO("Not yet implemented")
	}

	override fun bankBalance(name: String): EconomyResponse
	{
		TODO("Not yet implemented")
	}

	override fun bankHas(name: String, amount: Double): EconomyResponse
	{
		TODO("Not yet implemented")
	}

	override fun bankWithdraw(name: String, amount: Double): EconomyResponse
	{
		TODO("Not yet implemented")
	}

	override fun bankDeposit(name: String, amount: Double): EconomyResponse
	{
		TODO("Not yet implemented")
	}

	override fun isBankOwner(name: String, playerName: String): EconomyResponse
	{
		TODO("Not yet implemented")
	}

	override fun isBankOwner(name: String, player: OfflinePlayer): EconomyResponse
	{
		TODO("Not yet implemented")
	}

	override fun isBankMember(name: String, playerName: String): EconomyResponse
	{
		TODO("Not yet implemented")
	}

	override fun isBankMember(name: String, player: OfflinePlayer): EconomyResponse
	{
		TODO("Not yet implemented")
	}

	override fun getBanks(): MutableList<String>
	{
		TODO("Not yet implemented")
	}

	override fun createPlayerAccount(playerName: String): Boolean
	{
		TODO("Not yet implemented")
	}

	override fun createPlayerAccount(player: OfflinePlayer): Boolean
	{
		if (hasAccount(player))
		{
			return true
		}
		account(player.uniqueId)
		return true
	}

	override fun createPlayerAccount(playerName: String, worldName: String): Boolean
	{
		TODO("Not yet implemented")
	}

	override fun createPlayerAccount(player: OfflinePlayer, worldName: String): Boolean
	{
		return createPlayerAccount(player)
	}
}