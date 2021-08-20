package xyz.handshot.tconomy.transaction

import kotlinx.serialization.Serializable
import xyz.handshot.tconomy.backend.kotlinx.IdSerializer
import java.util.*

/**
 * @property vendor What plugin caused the transaction
 * @property change How much the balance changed
 * @property summary The new account balance after said change
 */
@Serializable
data class Transaction(@Serializable(with = IdSerializer::class) val id: UUID = UUID.randomUUID(), var vendor: String = "Unknown Vendor", val timestamp: Long = System.currentTimeMillis(), val change: Double = 0.0, val summary: Double = 0.0)
