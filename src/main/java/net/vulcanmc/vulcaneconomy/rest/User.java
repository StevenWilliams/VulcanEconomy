package net.vulcanmc.vulcaneconomy.rest;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.vulcanmc.vulcaneconomy.VulcanEconomy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

public class User {
    private UUID uuid;
    private Player player;
    private Integer playerid;
    private AccountCache accountcache;

    public List<Account> getAccounts() {
        return Accounts.getAccountsByPlayer(player);
    }
    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(this.uuid);
    }
    public User(Integer playerid, UUID uuid) {
        this.playerid = playerid;
        this.uuid = uuid;
        if(!VulcanEconomy.getPlugin().getAccountcache().containsKey(this.playerid)) {
            //VulcanEconomy.plugin.getLogger().info("account cache does not contain key...");
            this.accountcache = new AccountCache(this);
            VulcanEconomy.getPlugin().getAccountcache().put(this.playerid, this.accountcache);
        } else {
            //VulcanEconomy.plugin.getLogger().info("account cache does contain key...");
            this.accountcache = VulcanEconomy.getPlugin().getAccountcache().get(this.playerid);
        }
    }
    public Account getAccount(Currency currency) {
        return this.accountcache.getAccount(currency);
    }
    public Account createAccount(Currency currency) {
            try {
                JsonNode response = Unirest.post(VulcanEconomy.getApiURL() + "accounts/").basicAuth(VulcanEconomy.getPlugin().getApiUser(), VulcanEconomy.getPlugin().getApiPass()).queryString("accountOwner", playerid).queryString("server", VulcanEconomy.getServerid()).queryString("currency", new Currency().getNameSingle()).asJson().getBody();

                if (response.getObject().isNull("data")) {
                    VulcanEconomy.getPlugin().getAccountcache().get(this.playerid).lookupHasAccount(new Currency());
                    VulcanEconomy.getPlugin().getLogger().info(response.toString());
                    return VulcanEconomy.getPlugin().getAccountcache().get(this.playerid).lookupGetAccount(new Currency());

                }
                JSONObject data = response.getObject().getJSONObject("data");
                    Long accountid = data.getLong("id");
                     VulcanEconomy.getPlugin().getLogger().info("createdacccid: " + accountid);
                    Account account = new Account(accountid);
                    VulcanEconomy.getPlugin().getAccountcache().get(this.playerid).setAccount(account);
                    return account;
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    public boolean hasAccount(Currency currency)
    {
        if(!VulcanEconomy.getPlugin().getAccountcache().containsKey(this.playerid)) {
            this.accountcache = new AccountCache(this);
            VulcanEconomy.getPlugin().getAccountcache().put(this.playerid, this.accountcache);
            //VulcanEconomy.plugin.getLogger().info("account cache has does not contain key...");
            return this.accountcache.hasAccount(currency);
        } else {
            //VulcanEconomy.plugin.getLogger().info("account cache has does contain key...");
            this.accountcache = VulcanEconomy.getPlugin().getAccountcache().get(this.playerid);
            return this.accountcache.hasAccount(currency);
        }
    }
    public Long getId()
    {
        return Long.valueOf(this.playerid);
    }
}
