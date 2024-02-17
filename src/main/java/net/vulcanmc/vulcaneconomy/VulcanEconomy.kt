package net.vulcanmc.vulcaneconomy


import net.vulcanmc.vulcaneconomy.commands.*
import net.vulcanmc.vulcaneconomy.listeners.PlayerListener
import net.vulcanmc.vulcaneconomy.rest.Accounts
import net.vulcanmc.vulcaneconomy.rest.Currencies
import net.vulcanmc.vulcaneconomy.vault.Economy_VulcanEco
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.JavaPluginLoader
import org.enginehub.squirrelid.resolver.HttpRepositoryService
import org.enginehub.squirrelid.resolver.ProfileService

import java.io.File
import java.io.IOException
import java.util.UUID

//import static net.vulcanmc.vulcaneconomy.vault.ScoreboardLoader.load;

class VulcanEconomy : JavaPlugin {
  //  private var apiURL: String = ""
    private var serverID: UUID? = null
    private lateinit var resolver : ProfileService
    var apiUser: String = ""
        private set
    var apiPass: String = ""
        private set
    lateinit var uuid : UUID;
    //private RequestsQueue queue;
    val currencies = Currencies()
    var accounts: Accounts? = null
        private set// = new Accounts();
    val prefix: String
        get() = ChatColor.translateAlternateColorCodes('&', config.getString("prefix"))

    //For use in MockBukkit
    constructor() : super() {}

    protected constructor(loader: JavaPluginLoader, description: PluginDescriptionFile, dataFolder: File, file: File) : super(loader, description, dataFolder, file) {}


    fun reload() {
        val serverIDstr = this.config.getString("server-id")
        this.serverID = UUID.fromString(serverIDstr)
        apiURL = this.config.getString("api-url")
        this.apiUser = this.config.getString("api-username")
        this.apiPass = this.config.getString("api-password")
        username = this.apiUser
        password = this.apiPass
        accounts!!.cache
        accounts!!.reloadCache()

    }

    override fun onEnable() {
        plugin = this

        //  this.queue = new RequestsQueue();
        this.saveDefaultConfig()
        config.options().copyDefaults(true)
        //remember to disable commands in essentials config.
        this.getCommand("balance").executor = Balance(this)
        this.getCommand("balancetop").executor = BalanceTop(this)
        this.getCommand("pay").executor = Pay(this)
        this.getCommand("economy").executor = Economy(this)
        this.getCommand("transactions").executor = Transactions(this)

        this.server.pluginManager.registerEvents(PlayerListener(this), this)
        setupVault()
        this.resolver = HttpRepositoryService.forMinecraft()

        val file = File("uuidcache.sqlite")

        if (plugin!!.server.pluginManager.getPlugin("ScoreboardStats") != null) {
            //load(this);
        }

        accounts = Accounts()
        reload()


    }

    fun getUUIDIfExists(playerName: String): UUID? {
        return if (Bukkit.getPlayer(playerName) != null) {
            Bukkit.getPlayer(playerName).uniqueId
        } else null
    }

    fun getUUID(playerName: String): UUID? {
        if (Bukkit.getPlayer(playerName) != null) {
            return Bukkit.getPlayer(playerName).uniqueId
        }

        var uuid = getUUIDIfExists(playerName)
        if (uuid == null) {
            try {
                val profile = this.resolver!!.findByName(playerName)
                if (profile != null) {
                    uuid = profile!!.getUniqueId()
                    return uuid
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        } else {
            return uuid;
        }
        return Bukkit.getOfflinePlayer(playerName).uniqueId //todo: check if this is ever reached?
    }

    override fun onDisable() {
        for (player in server.onlinePlayers) {
            accounts!!.removeAccountsFromCache(player.uniqueId)
        }

        plugin = null
    }

    private fun setupVault() {
        val vault = server.pluginManager.getPlugin("Vault") ?: return

        server.servicesManager.register(net.milkbowl.vault.economy.Economy::class.java, Economy_VulcanEco(this), this, ServicePriority.Highest)
        //getServer().getServicesManager().unregister(net.milkbowl.vault.economy.Economy.class, net.milkbowl.vault.economy.plugins.Economy_Essentials.class);
    }

    companion object {
        var plugin: VulcanEconomy? = null
            private set

        var password: String = ""
            private set
        var username: String = ""
            private set

        var apiURL : String = ""
            private set
        var serverID: UUID? = null
            private set
    }
}
