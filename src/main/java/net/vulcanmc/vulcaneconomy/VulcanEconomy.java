package net.vulcanmc.vulcaneconomy;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sk89q.squirrelid.Profile;
import com.sk89q.squirrelid.cache.SQLiteCache;
import com.sk89q.squirrelid.resolver.HttpRepositoryService;
import com.sk89q.squirrelid.resolver.ProfileService;
import net.vulcanmc.vulcaneconomy.commands.*;
import net.vulcanmc.vulcaneconomy.listeners.PlayerListener;
import net.vulcanmc.vulcaneconomy.rest.AccountCache;
import net.vulcanmc.vulcaneconomy.rest.BalanceCache;
import net.vulcanmc.vulcaneconomy.rest.UserCache;
import net.vulcanmc.vulcaneconomy.vault.Economy_VulcanEco;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import static net.vulcanmc.vulcaneconomy.vault.ScoreboardLoader.load;

public class VulcanEconomy extends JavaPlugin{
    public static VulcanEconomy plugin;
    public static String apiURL;
    public static Integer serverid;
    public static ProfileService resolver;
    public HashMap<Long, BalanceCache> balancecache = new HashMap<>();
    public HashMap<Integer, AccountCache> accountcache = new HashMap<>();
    public HashMap<UUID, UserCache> usercache = new HashMap<>();
    public  SQLiteCache cache;
    public String apiUser;
    public String apiPass;

    @Override
    public void onEnable() {
        plugin = this;
        this.saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        //remember to disable commands in essentials config.
        this.getCommand("balance").setExecutor(new Balance(this));
        this.getCommand("balancetop").setExecutor(new BalanceTop(this));
        this.getCommand("pay").setExecutor(new Pay(this));
        this.getCommand("economy").setExecutor(new Economy(this));
        this.getCommand("transactions").setExecutor(new Transactions(this));
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        this.serverid = this.getConfig().getInt("server-id");
        this.apiURL = this.getConfig().getString("api-url");
        this.apiUser = this.getConfig().getString("api-username");
        this.apiPass = this.getConfig().getString("api-password");
        setupVault();
        this.resolver = HttpRepositoryService.forMinecraft();
        File file = new File("uuidcache.sqlite");

        if(plugin.getServer().getPluginManager().getPlugin("ScoreboardStats") != null) {
            load(this);
        }

    }
    public UUID getUUIDIfExists(String playername) {
        if(Bukkit.getPlayer(playername) != null)
        {
            return Bukkit.getPlayer(playername).getUniqueId();
        }

        Integer playerid;
        UUID uuid = null;
        //this.player = (Player) player;
        try {
            JsonNode response = Unirest.get(VulcanEconomy.apiURL + "players/").basicAuth(this.apiUser, this.apiPass).asJson().getBody();

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
        }
        return uuid;
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
                    Profile profile = this.resolver.findByName(playername);
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
}
