package xyz.handshot.tconomy.commands

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import xyz.handshot.tconomy.EconomyService
import xyz.handshot.tconomy.transaction.TransactionManager

class CommandPay(private val economy: EconomyService, private val transactionManager: TransactionManager) : CommandExecutor
{
	override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean
	{
		if (sender !is Player)
		{
			sender.sendMessage("&cPay is a player only command. Use /tconomy deposit <player> <amount>".color())
			return false
		}

		if (args.size < 2)
		{
			sender.sendMessage("&cUsage: /pay <player> <amount>".color())
			return false
		}

		val target = Bukkit.getOfflinePlayer(args[0])
		val amount = args[1].toDoubleOrNull()

		if (!target.hasPlayedBefore())
		{
			sender.sendMessage("&c${target.name} has not played on this server".color())
			return false
		}

		if (sender == target)
		{
			sender.sendMessage("&cYou can't send money to yourself")
			return false
		}

		if (amount == null || amount <= 0)
		{
			sender.sendMessage("&cInvalid amount '${args[1]}'".color())
			return false
		}

		val senderAccount = economy.account(sender.uniqueId)
		val targetAccount = economy.account(target.uniqueId)
		val amountFormatted = economy.format(amount)

		if (!transactionManager.withdraw(senderAccount, amount, "${target.name} [pay]"))
		{
			sender.sendMessage("&cCould not withdraw $amountFormatted from your account".color())
			return false
		}

		transactionManager.deposit(targetAccount, amount, "${sender.name} [pay]")
		sender.sendMessage("&a$amountFormatted has been sent to ${target.name}".color())

		if (target.isOnline)
		{
			target.player?.sendMessage("&aYou received $amountFormatted from ${sender.name}".color())
		}
		return false
	}
}