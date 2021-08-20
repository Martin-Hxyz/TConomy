package xyz.handshot.tconomy.commands

import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandBalance(private val economy: Economy) : CommandExecutor
{
	override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean
	{
		return when (args.size)
		{
			0 -> balanceSelf(sender)
			else -> balanceOther(sender, args)
		}
	}

	private fun balanceSelf(sender: CommandSender): Boolean
	{
		if (sender !is Player)
		{
			sender.sendMessage("&cTo view a players balance use /balance <name>".color())
			return true
		}
		sendBalance(sender, sender)
		return true
	}

	private fun balanceOther(sender: CommandSender, args: Array<out String>): Boolean
	{
		if (!sender.hasPermission("tconomy.balance.other"))
		{
			sender.sendMessage("&cYou do not have permission to view others' balance".color())
			return true
		}

		val target = Bukkit.getOfflinePlayer(args[0])

		if (!target.hasPlayedBefore())
		{
			sender.sendMessage("&c${target.name} has not played on this server".color())
			return true
		}

		sendBalance(sender, target)
		return true
	}

	private fun sendBalance(sender: CommandSender, target: OfflinePlayer)
	{
		val prefix = when (sender == target)
		{
			true -> "Your"
			false -> "${target.name}'s"
		}
		val balance = economy.getBalance(target)
		val balanceFormatted = economy.format(balance)
		sender.sendMessage("&7$prefix balance is $balanceFormatted".color())
	}
}