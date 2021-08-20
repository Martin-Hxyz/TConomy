package xyz.handshot.tconomy.transaction

import xyz.handshot.tconomy.account.Account

interface TransactionManager
{
	fun process(transaction: Transaction, account: Account): Boolean
	fun reverse(transaction: Transaction, account: Account): Boolean
	fun withdraw(account: Account, amount: Double, vendor: String = "Unknown Vendor"): Boolean
	fun deposit(account: Account, amount: Double, vendor: String = "Unknown Vendor"): Boolean
}