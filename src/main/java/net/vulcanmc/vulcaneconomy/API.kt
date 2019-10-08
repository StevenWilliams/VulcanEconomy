package net.vulcanmc.vulcaneconomy

import net.vulcanmc.vulcaneconomy.rest.Account
import net.vulcanmc.vulcaneconomy.rest.Currency
import net.vulcanmc.vulcaneconomy.rest.User
import org.bukkit.OfflinePlayer
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

import java.util.ArrayList

class API(plugin: Plugin) : IAPI {
    override fun getCurrency(key: String): Currency? {
        return vulcanEconomy!!.currencies.getCurrency(key);
    }

    private var plugin: Plugin? = null

    init {
        this.plugin = plugin
    }


    override fun getBalance(player: OfflinePlayer, currency: Currency): Long {
        val account = getPlayerAccount(player, currency)
        return account!!.getBalance().toLong()
    }

    override fun getBalance(player: OfflinePlayer) : Long{
        return getBalance(player, defaultCurrency);
    }
    override fun has(player: OfflinePlayer, currency: Currency, amount: Long?): Boolean {
        return getBalance(player, currency) >= amount!!
    }

    override fun has(player: OfflinePlayer, amount: Long?): Boolean {
        return has(player, defaultCurrency, amount)
    }

    override fun deposit(player: OfflinePlayer, currency: Currency, amount: Long?, message: String): Boolean {
       try {
           val account = getPlayerAccount(player, currency)
           return account!!.deposit(amount!!, message, plugin!!.name)
       } catch (e : Exception) {
           println(e.message + e.stackTrace)
           return false;
       }

    }

    override fun deposit(player: OfflinePlayer, amount: Long?, message: String ): Boolean {
        return deposit(player, defaultCurrency, amount, message)
    }

    override fun deposit(player: OfflinePlayer, amount: Long? ): Boolean {
        return deposit(player, defaultCurrency, amount, "")
    }

    override fun withdraw(player: OfflinePlayer, currency: Currency, amount: Long?, message: String): Boolean {
       try {
           val account = getPlayerAccount(player, currency)
           return account!!.withdraw(amount!!, message, plugin!!.name)
       } catch (e : Exception) {
           println(e.message + e.stackTrace)
           return false;
       }
    }

    override fun withdraw(player: OfflinePlayer, amount: Long?, message: String): Boolean {
        return withdraw(player, defaultCurrency, amount, message)
    }

    override fun withdraw(player: OfflinePlayer, amount: Long?): Boolean {
        return withdraw(player, defaultCurrency, amount, "")
    }

    private fun getPlayerAccount(player: OfflinePlayer, currency: Currency = defaultCurrency): Account? {
        val user = User(player.uniqueId)
        return user!!.getAccount(currency)
    }

    fun withdraw(player : OfflinePlayer, amount : Long, currency : Currency = defaultCurrency, success:
    BukkitRunnable,
                 failure : BukkitRunnable) {
        if(withdraw(player, amount)) {
            success.runTaskAsynchronously(plugin)
        } else {
            failure.runTaskAsynchronously(plugin);
        }
    }
    companion object {
        private val vulcanEconomy = VulcanEconomy.plugin
        val defaultCurrency: Currency
            get() = vulcanEconomy!!.currencies.defaultCurrency
        val currencies: ArrayList<Currency>
            get() = vulcanEconomy!!.currencies.currencies
    }
}
