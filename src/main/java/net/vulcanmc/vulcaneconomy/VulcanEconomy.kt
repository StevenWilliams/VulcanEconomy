package net.vulcanmc.vulcaneconomy


import com.sk89q.squirrelid.Profile
import com.sk89q.squirrelid.cache.SQLiteCache
import com.sk89q.squirrelid.resolver.HttpRepositoryService
import com.sk89q.squirrelid.resolver.ProfileService
import kong.unirest.Unirest
import net.vulcanmc.vulcaneconomy.commands.*
import net.vulcanmc.vulcaneconomy.listeners.PlayerListener
import net.vulcanmc.vulcaneconomy.rest.Accounts
import net.vulcanmc.vulcaneconomy.rest.Currencies
import net.vulcanmc.vulcaneconomy.rest.Currency
import net.vulcanmc.vulcaneconomy.vault.Economy_VulcanEco
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.JavaPluginLoader

import java.io.File
import java.io.IOException
import java.util.Arrays
import java.util.UUID

//import static net.vulcanmc.vulcaneconomy.vault.ScoreboardLoader.load;

class VulcanEconomy : JavaPlugin {
  //  private var apiURL: String = ""
    private var serverID: UUID? = null
    private lateinit var resolver : ProfileService
    val cache: SQLiteCache? = null
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

    /*   public RequestsQueue getQueue() {
        return queue;
    }
*/


    fun reload() {
        val serverIDstr = this.config.getString("server-id")
        this.serverID = UUID.fromString(serverIDstr)
        apiURL = this.config.getString("api-url")
        this.apiUser = this.config.getString("api-username")
        this.apiPass = this.config.getString("api-password")
        username = this.apiUser
        password = this.apiPass
        accounts!!.getCache()
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

    fun getUUIDIfExists(playername: String): UUID? {
        return if (Bukkit.getPlayer(playername) != null) {
            Bukkit.getPlayer(playername).uniqueId
        } else null
        /*

        Integer playerid;
        UUID uuid = null;
        //this.player = (Player) player;
        try {
            JsonNode response = Unirest.get(VulcanEconomy.getApiURL() + "players/").basicAuth(this.getApiUser(), this.getApiPass()).asJson().getBody();

            JSONArray data = response.getObject().getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {
                JSONObject object = data.getJSONObject(i);
                String usernameobject = object.getString("username");
                if(usernameobject.equals(playername)) {
                    playerid = object.getInt("id");
                    uuid = UUID.fromString(object.getString("uuid"));
                    return uuid;
                }
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }*/

    }

    fun getUUID(playername: String): UUID? {
        if (Bukkit.getPlayer(playername) != null) {
            return Bukkit.getPlayer(playername).uniqueId
        }

        val playerid: Int?
        var uuid = getUUIDIfExists(playername)
        if (uuid == null) {
            try {
                val profile = this.resolver!!.findByName(playername)

                if (profile != null) {
                    uuid = profile!!.getUniqueId()
                    return uuid
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        }
        return Bukkit.getOfflinePlayer(playername).uniqueId
        //lookup with usernmae instead of uuid. fall back with mojang api
    }

    override fun onDisable() {
        for (player in server.onlinePlayers) {
            accounts!!.removeAccountsFromCache(player.uniqueId)
        }
        Unirest.shutDown()

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
