package net.vulcanmc.vulcaneconomy.vault

import net.milkbowl.vault.economy.AbstractEconomy
import net.milkbowl.vault.economy.EconomyResponse
import net.vulcanmc.vulcaneconomy.VulcanEconomy
import net.vulcanmc.vulcaneconomy.rest.Account
import net.vulcanmc.vulcaneconomy.rest.Currency
import net.vulcanmc.vulcaneconomy.rest.User
import net.vulcanmc.vulcaneconomy.rest.Users
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.event.server.PluginEnableEvent
import org.bukkit.plugin.Plugin

import java.util.ArrayList
import java.util.logging.Logger

class Economy_VulcanEco(plugin: Plugin) : AbstractEconomy() {
    private val name = "VulcanEconomy"
    private var vulcaneco: VulcanEconomy? = VulcanEconomy.getPlugin()
    private var plugin : Plugin = VulcanEconomy.getPlugin()
    private var currency = VulcanEconomy.getPlugin().currencies.defaultCurrency

    init {
        this.plugin = plugin
        Bukkit.getServer().pluginManager.registerEvents(EconomyServerListener(this), plugin)

        // Load Plugin in case it was loaded before
        if (vulcaneco == null) {
            val essentials = plugin.server.pluginManager.getPlugin("VulcanEconomy")
            if (essentials != null && essentials.isEnabled) {
                vulcaneco = essentials as VulcanEconomy
                log.info(String.format("[%s][Economy] %s hooked.", plugin.description.name, name))
            }
        }
        //add dynamic currencies later
        //currency = Currency()
    }

    override fun isEnabled(): Boolean {
        return if (vulcaneco == null) {
            false
        } else {
            vulcaneco!!.isEnabled
        }
    }

    override fun getName(): String {
        return name
    }

    override fun hasBankSupport(): Boolean {
        return false
    }

    override fun fractionalDigits(): Int {
        return 0
    }

    //continue here
    override fun format(v: Double): String? {
        return null
    }

    override fun currencyNamePlural(): String {
        return currency.namePlural
    }

    override fun currencyNameSingular(): String {
        return currency.nameSingle
    }

    override fun hasAccount(playername: String): Boolean {
        val time1 = System.currentTimeMillis()

        val offlinePlayer = Bukkit.getOfflinePlayer(playername) ?: return false
        val user = User(offlinePlayer.uniqueId)
        val acc = user.getAccount(vulcaneco!!.currencies.defaultCurrency)
      //  println("VaultHasAccount" + System.currentTimeMillis().minus(time1))

        return acc != null
    }

    override fun hasAccount(playername: String, worldname: String): Boolean {
        return hasAccount(playername)
    }

    override fun getBalance(playername: String): Double {
        val time1 = System.currentTimeMillis()

        val user = User(Bukkit.getOfflinePlayer(playername).uniqueId)

        if (user.getAccount(currency) != null) {
            //plugin.getLogger().info("getAccount not null");
            var value = user.getAccount(currency)!!.getBalance(true).toLong().toDouble()
         //   println("GetBalanceVault" + System.currentTimeMillis().minus(time1))
            return value
        } else {
            //plugin.getLogger().info("getAccount null");
        }
        return -1.0
    }

    override fun getBalance(offlinePlayer: OfflinePlayer, worldName: String): Double {
        return getBalance(offlinePlayer)
    }

    override fun getBalance(playername: String, worldname: String): Double {
        return getBalance(playername)
    }

    override fun has(playername: String, amount: Double): Boolean {
        return getBalance(playername) >= amount
    }

    override fun has(playername: String, worldname: String, amount: Double): Boolean {
        return has(playername, amount)
    }


    override fun withdrawPlayer(playername: String, amount: Double): EconomyResponse {
        return withdraw(playername, amount)
    }

    override fun withdrawPlayer(playername: String, world: String, amount: Double): EconomyResponse {
        return withdraw(playername, amount)
    }


    override fun depositPlayer(playername: String, amount: Double): EconomyResponse {
        return deposit(playername, amount)
    }

    override fun depositPlayer(playername: String, world: String, amount: Double): EconomyResponse {
        return deposit(playername, amount)
    }


    override fun createPlayerAccount(playername: String): Boolean {
        val time1 = System.currentTimeMillis()
        val user = User(Bukkit.getOfflinePlayer(playername).uniqueId)
        val account = user.getAccount(currency)//.getUser(Bukkit.getOfflinePlayer(playername)).getAccount(currency);
        if (account != null) {
            account.updateBalanceAsync()
            println("VaultCreatePlayerAccount" + System.currentTimeMillis().minus(time1))

            return true
        } else {
            return false
        }
    }

    override fun createPlayerAccount(playername: String, world: String): Boolean {
        return createPlayerAccount(playername)
    }


    private fun deposit(playername: String, amount: Double): EconomyResponse {
        val time1 = System.currentTimeMillis()
        val user = User(Bukkit.getOfflinePlayer(playername).uniqueId)
        val account = user.getAccount(currency)
                ?: return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Account doesn't exist")//.getUser(Bukkit.getOfflinePlayer(playername)).getAccount(currency);

        val roundedamount = Math.round(amount)

        account.deposit(roundedamount, "VaultAPI deposit")
        val balance = account.getBalance(true).toLong().toDouble()
        println("VaultDeposit" + System.currentTimeMillis().minus(time1))

        return EconomyResponse(roundedamount.toDouble(), balance, EconomyResponse.ResponseType.SUCCESS, "")
    }


    private fun withdraw(playername: String, amount: Double): EconomyResponse {
      //  println("vaultapi withdraw1")
        val time1 = System.currentTimeMillis()

        if (amount < 0) {
        //    println("vaultapi withdraw2")

            return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Cannot withdraw negative funds")
        }
        //println("vaultapi withdraw3")

        val user = User(Bukkit.getOfflinePlayer(playername).uniqueId)
        val account = user.getAccount(currency)
                ?: return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Account doesn't exist")//.getUser(Bukkit.getOfflinePlayer(playername)).getAccount(currency);
        //println("vaultapi withdraw4")

        val roundedamount = Math.round(amount)

        //println("vaultapi withdraw5 $roundedamount")

        if (account.has(roundedamount)) {
            println("vaultapi withdraw6")

            account.withdraw(roundedamount, "VaultAPI withdrawal")
            val balance = account.getBalance(false).toLong().toDouble()
            println("VaultWithdrawSuccess" + System.currentTimeMillis().minus(time1))

            return EconomyResponse(amount, balance , EconomyResponse.ResponseType.SUCCESS, "")
        } else {
            //println("vaultapi withdraw7")

            println("VaultWithdrawfail" + System.currentTimeMillis().minus(time1))
            return EconomyResponse(0.0, account.getBalance(false).toLong().toDouble(), EconomyResponse.ResponseType.FAILURE, "Insufficient funds")
        }
    }

    //Ignore
    override fun createBank(playername: String, world: String): EconomyResponse {
        return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "VulcanEconomy does not support bank accounts!")
    }

    override fun deleteBank(playername: String): EconomyResponse {
        return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "VulcanEconomy does not support bank accounts!")
    }

    override fun bankBalance(playername: String): EconomyResponse {
        return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "VulcanEconomy does not support bank accounts!")
    }

    override fun bankHas(playername: String, amount: Double): EconomyResponse {
        return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "VulcanEconomy does not support bank accounts!")
    }

    override fun bankWithdraw(playername: String, amount: Double): EconomyResponse {
        return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "VulcanEconomy does not support bank accounts!")
    }

    override fun bankDeposit(playername: String, amount: Double): EconomyResponse {
        return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "VulcanEconomy does not support bank accounts!")
    }

    override fun isBankOwner(player: String, worldName: String): EconomyResponse {
        return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "VulcanEconomy does not support bank accounts!")
    }

    override fun isBankMember(player: String, worldName: String): EconomyResponse {
        return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "VulcanEconomy does not support bank accounts!")
    }

    override fun getBanks(): List<String> {
        return ArrayList()
    }
    //upto here


    inner class EconomyServerListener(economy: Economy_VulcanEco) : Listener {
        internal var economy: Economy_VulcanEco? = null

        init {
            this.economy = economy
        }

        @EventHandler(priority = EventPriority.MONITOR)
        fun onPluginEnable(event: PluginEnableEvent) {
            if (economy!!.vulcaneco == null) {
                val vulcanecoplugin = event.plugin

                if (vulcanecoplugin.description.name == "VulcanEconomy") {
                    economy!!.vulcaneco = vulcanecoplugin as VulcanEconomy
                    log.info(String.format("[%s][Economy] %s hooked.", plugin.description.name, economy!!.name))
                }
            }
        }

        @EventHandler(priority = EventPriority.MONITOR)
        fun onPluginDisable(event: PluginDisableEvent) {
            if (economy!!.vulcaneco != null) {
                if (event.plugin.description.name == "VulcanEconomy") {
                    economy!!.vulcaneco = null
                    log.info(String.format("[%s][Economy] %s unhooked.", plugin.description.name, economy!!.name))
                }
            }
        }
    }

    companion object {

        private val log = VulcanEconomy.getPlugin().logger
    }

}
