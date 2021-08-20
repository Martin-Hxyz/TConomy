package xyz.handshot.tconomy.account

import kotlinx.serialization.Serializable
import xyz.handshot.tconomy.backend.kotlinx.IdSerializer
import xyz.handshot.tconomy.transaction.Transaction
import java.util.*

@Serializable
class Account(@Serializable(with = IdSerializer::class) val id: UUID, var balance: Double, val transactions: MutableList<Transaction>)