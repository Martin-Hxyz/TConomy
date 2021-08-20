package xyz.handshot.tconomy.commands

import org.bukkit.ChatColor

fun String.color() = ChatColor.translateAlternateColorCodes('&', this)