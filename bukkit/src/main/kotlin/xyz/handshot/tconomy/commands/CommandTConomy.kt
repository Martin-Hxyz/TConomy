package xyz.handshot.tconomy.commands

import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import xyz.handshot.tconomy.backend.Backend
import xyz.handshot.tconomy.transaction.TransactionManager
import java.text.DateFormat
import java.util.*
import kotlin.math.abs

class CommandTConomy(private val economy: Economy, private val backend: Backend, private val transactionManager: TransactionManager) : CommandExecutor, TabCompleter
{
	private val timeFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.US)

	override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean
	{
		if (!sender.hasPermission("tconomy.admin"))
		{
			return false
		}

		if (args.isEmpty())
		{
			help(sender)
			return false
		}

		when (args[0])
		{
			"transactions" -> transactions(sender, args)
			"reverse" -> reverse(sender, args)
			"give" -> give(sender, args)
			"take" -> take(sender, args)
			else -> help(sender)
		}

		return false
	}

	private fun transactions(sender: CommandSender, args: Array<String>)
	{
		if (args.size != 2)
		{
			sender.sendMessage("&cUsage: /tconomy transactions <player>".color())
			return
		}

		val target = Bukkit.getOfflinePlayer(args[1])

		if (!target.hasPlayedBefore())
		{
			sender.sendMessage("&c${target.name} has not played on this server".color())
			return

		}

		backend.load(target.uniqueId)?.transactions?.forEachIndexed { index, transaction ->
			val color = when (index % 2 == 0)
			{
				false -> ChatColor.GRAY
				true -> ChatColor.WHITE
			}

			val id = transaction.id.toString().substring(0, 7)
			val vendor = transaction.vendor
			val time = timeFormat.format(Date(transaction.timestamp))
			val change = economy.format(abs(transaction.change))
			val summary = economy.format(transaction.summary)

			val format = when (transaction.change > 0)
			{
				true -> "%s - Received %s from %s at %s. New balance: %s"
				false -> "%s - Spent %s with %s at %s. New balance: %s"
			}

			val message = String.format(format, id, change, vendor, time, summary)
			sender.sendMessage("$color$message")
		}
	}

	private fun reverse(sender: CommandSender, args: Array<String>)
	{
		if (args.size != 3)
		{
			sender.sendMessage("&cUsage: /tconomy reverse <player> <transaction>".color())
			return
		}

		val target = Bukkit.getOfflinePlayer(args[1])
		val transactionId = args[2]
		val account = backend.load(target.uniqueId)

		if (!target.hasPlayedBefore())
		{
			sender.sendMessage("&c${target.name} has not played on this server".color())
			return
		}

		val transaction = account?.transactions?.firstOrNull { transaction -> transaction.id.toString().startsWith(transactionId) }

		if (transaction == null)
		{
			sender.sendMessage("&cCould not find transaction '$transactionId'".color())
			return
		}

		transactionManager.reverse(transaction, account)
		sender.sendMessage("&aReversed transaction ${transaction.id}".color())
	}

	private fun give(sender: CommandSender, args: Array<String>)
	{
		if (args.size != 3)
		{
			sender.sendMessage("&cUsage: /tconomy give <player> <amount>".color())
			return
		}

		val target = Bukkit.getOfflinePlayer(args[1])
		val amount = args[2].toDoubleOrNull()
		val account = backend.load(target.uniqueId)

		if (amount == null)
		{
			sender.sendMessage("&cInvalid amount '${args[2]}'".color())
			return
		}

		if (account == null || amount < 0)
		{
			return
		}

		val amountFormatted = economy.format(amount)
		transactionManager.deposit(account, amount, "${sender.name} [admin]")
		sender.sendMessage("&aSuccessfully added $amountFormatted to ${target.name}'s account".color())
	}

	private fun take(sender: CommandSender, args: Array<String>)
	{
		if (args.size != 3)
		{
			sender.sendMessage("&cUsage: /tconomy take <player> <amount>".color())
			return
		}

		val target = Bukkit.getOfflinePlayer(args[1])
		val amount = args[2].toDoubleOrNull()
		val account = backend.load(target.uniqueId)

		if (amount == null || amount < 0)
		{
			sender.sendMessage("&cInvalid amount '${args[2]}'")
			return
		}

		if (account == null)
		{
			return
		}

		val amountFormatted = economy.format(amount)
		transactionManager.withdraw(account, amount, "${sender.name} [admin]")
		sender.sendMessage("&aSuccessfully removed $amountFormatted from ${target.name}'s account".color())
	}

	private fun help(sender: CommandSender)
	{
		sender.sendMessage("""&7
/tconomy transactions <player>
/tconomy reverse <player> <id>
/tconomy give <player> <amount>
/tconomy take <player> <amount>
		""".trimIndent())
	}

	override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String>): MutableList<String>
	{
		return when (args.size)
		{
			1 -> mutableListOf("transactions", "reverse", "give", "take")
			2 -> Bukkit.getOnlinePlayers().map(Player::getName).toMutableList()
			else -> mutableListOf()
		}
	}

}