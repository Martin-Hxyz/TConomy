package xyz.handshot.tconomy.transaction

import xyz.handshot.tconomy.Config
import xyz.handshot.tconomy.account.Account
import java.util.logging.Logger

class TransactionManagerImpl(private val config: Config) : TransactionManager
{
	private val logger = Logger.getLogger(TransactionManager::class.simpleName)

	override fun process(transaction: Transaction, account: Account): Boolean
	{
		if (account.transactions.contains(transaction))
		{
			logger.warning("Tried to process ${transaction.id} twice for account ${account.id}")
			return false
		}

		if (transaction.vendor.isBlank())
		{
			transaction.vendor = "Unknown Vendor"
		}

		if (transaction.summary < 0)
		{
			return false
		}

		account.transactions.add(transaction)
		account.balance = transaction.summary

		while (account.transactions.size > config.transactionHistory)
		{
			logger.info("Removing transaction due to history limit")
			account.transactions.removeFirst()
		}

		logger.info("Processed transaction [${transaction.change}]. New balance [${transaction.summary}]")
		return true
	}

	override fun reverse(transaction: Transaction, account: Account): Boolean
	{
		if (!account.transactions.contains(transaction))
		{
			logger.warning("Tried to reverse a transaction that doesn't belong to ${account.id}")
			return false
		}

		if (transaction.summary < 0)
		{
			return false
		}

		account.transactions.remove(transaction)
		account.balance -= transaction.change
		return true
	}

	override fun withdraw(account: Account, amount: Double, vendor: String): Boolean
	{
		val transaction = Transaction(
			vendor = vendor,
			change = amount * -1,
			summary = account.balance - amount
		)
		return process(transaction, account)
	}

	override fun deposit(account: Account, amount: Double, vendor: String): Boolean
	{
		val transaction = Transaction(
			vendor = vendor,
			change = amount,
			summary = account.balance + amount
		)
		return process(transaction, account)
	}
}