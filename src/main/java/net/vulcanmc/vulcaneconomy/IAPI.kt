package net.vulcanmc.vulcaneconomy

import net.vulcanmc.vulcaneconomy.rest.Currency
import org.bukkit.OfflinePlayer
import org.bukkit.plugin.Plugin

interface IAPI {

        fun getBalance(player: OfflinePlayer, currency: Currency): Long
        fun getBalance(player: OfflinePlayer) : Long


        fun has(player: OfflinePlayer, currency: Currency, amount: Long?): Boolean
        fun has(player: OfflinePlayer, amount: Long?): Boolean

        fun deposit(player: OfflinePlayer, currency: Currency, amount: Long?, message: String = ""): Boolean
        fun deposit(player: OfflinePlayer, amount: Long?, message: String = ""): Boolean
        fun deposit(player: OfflinePlayer, amount: Long?): Boolean


        fun withdraw(player: OfflinePlayer, currency: Currency, amount: Long?, message: String = ""): Boolean
        fun withdraw(player: OfflinePlayer, amount: Long?, message: String): Boolean
        fun withdraw(player: OfflinePlayer, amount: Long?): Boolean

        fun getCurrency(key:String) : Currency?
        // fun addCurrency(UUID id, String name, String symbol
}
