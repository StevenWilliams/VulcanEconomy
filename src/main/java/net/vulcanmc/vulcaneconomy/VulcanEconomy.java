package net.vulcanmc.vulcaneconomy;


import com.sk89q.squirrelid.Profile;
import com.sk89q.squirrelid.cache.SQLiteCache;
import com.sk89q.squirrelid.resolver.HttpRepositoryService;
import com.sk89q.squirrelid.resolver.ProfileService;
import kong.unirest.Unirest;
import net.vulcanmc.vulcaneconomy.commands.*;
import net.vulcanmc.vulcaneconomy.listeners.PlayerListener;
import net.vulcanmc.vulcaneconomy.rest.Accounts;
import net.vulcanmc.vulcaneconomy.rest.Currencies;
import net.vulcanmc.vulcaneconomy.rest.Currency;
import net.vulcanmc.vulcaneconomy.vault.Economy_VulcanEco;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

//import static net.vulcanmc.vulcaneconomy.vault.ScoreboardLoader.load;

public class VulcanEconomy extends JavaPlugin{
    private static VulcanEconomy plugin;
    private static String apiURL;
    private static UUID serverid;
    private static ProfileService resolver;
    private static String password;
    private static String username;

    private SQLiteCache cache;
    private String apiUser;
    private String apiPass;
    //private RequestsQueue queue;
    private Currencies currencies = new Currencies();
    private Accounts accounts;// = new Accounts();


    private static UUID serverID;

//For use in MockBukkit
    public VulcanEconomy()
    {
        super();
    }

    protected VulcanEconomy(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file)
    {
        super(loader, description, dataFolder, file);
    }

    public static VulcanEconomy getPlugin() {
        return plugin;
    }

    public static String getApiURL() {
        return apiURL;
    }

    public static UUID getServerid() {
        return serverid;
    }
    public static String getUsername() {
        return username;
    }
    public static String getPassword() {
        return password;
    }

    public static ProfileService getResolver() {
        return resolver;
    }

 /*   public RequestsQueue getQueue() {
        return queue;
    }
*/


    @Override

    public void onEnable() {
        plugin = this;

      //  this.queue = new RequestsQueue();
        this.saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        //remember to disable commands in essentials config.
        this.getCommand("balance").setExecutor(new Balance(this));
        this.getCommand("balancetop").setExecutor(new BalanceTop(this));
        this.getCommand("pay").setExecutor(new Pay(this));
        this.getCommand("economy").setExecutor(new Economy(this));
        this.getCommand("transactions").setExecutor(new Transactions(this));

        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        String serverIDstr = this.getConfig().getString("server-id");
        this.serverID = UUID.fromString(serverIDstr);
        this.apiURL = this.getConfig().getString("api-url");
        this.apiUser = this.getConfig().getString("api-username");
        this.apiPass = this.getConfig().getString("api-password");
        username = apiUser;
        password = apiPass;
        setupVault();
        this.resolver = HttpRepositoryService.forMinecraft();
        File file = new File("uuidcache.sqlite");

        if(getPlugin().getServer().getPluginManager().getPlugin("ScoreboardStats") != null) {
            //load(this);
        }

accounts = new Accounts();


    }
    public UUID getUUIDIfExists(String playername) {
        if(Bukkit.getPlayer(playername) != null)
        {
            return Bukkit.getPlayer(playername).getUniqueId();
        }
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

        return null;
    }
    public UUID getUUID(String playername) {
        if(Bukkit.getPlayer(playername) != null)
        {
            return Bukkit.getPlayer(playername).getUniqueId();
        }

        Integer playerid;
        UUID uuid = getUUIDIfExists(playername);
            if(uuid == null) {
                try {
                    Profile profile = this.getResolver().findByName(playername);
                    if(profile != null) {
                        uuid = profile.getUniqueId();
                        return uuid;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return Bukkit.getOfflinePlayer(playername).getUniqueId();
            //lookup with usernmae instead of uuid. fall back with mojang api
    }
    @Override
    public void onDisable() {
        for(Player player : getServer().getOnlinePlayers()){
            accounts.removeAccountsFromCache(player.getUniqueId());
        }
        Unirest.shutDown();

        plugin = null;
    }

    private void setupVault() {
        Plugin vault = getServer().getPluginManager().getPlugin("Vault");

        if (vault == null) {
            return;
        }

        getServer().getServicesManager().register(net.milkbowl.vault.economy.Economy.class, new Economy_VulcanEco(this), this, ServicePriority.Highest);
        //getServer().getServicesManager().unregister(net.milkbowl.vault.economy.Economy.class, net.milkbowl.vault.economy.plugins.Economy_Essentials.class);
    }


    public SQLiteCache getCache() {
        return cache;
    }

    public String getApiUser() {
        return apiUser;
    }

    public String getApiPass() {
        return apiPass;
    }

    public static UUID getServerID() {
        return serverID;
    }

    public Accounts getAccounts() {
        return accounts;
    }

    public Currencies getCurrencies() {
        return currencies;
    }
}
