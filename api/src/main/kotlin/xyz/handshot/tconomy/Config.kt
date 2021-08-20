package xyz.handshot.tconomy

import java.io.File

data class Config(val backend: String = "json", val transactionHistory: Int = 50, val accountsDir: File)
