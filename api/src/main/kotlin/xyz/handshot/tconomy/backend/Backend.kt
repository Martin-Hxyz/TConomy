package xyz.handshot.tconomy.backend

import xyz.handshot.tconomy.account.Account
import java.util.*

interface Backend
{
	fun exists(id: UUID): Boolean
	fun load(id: UUID): Account?
	fun save(account: Account)
	fun count(): Int
}