package net.vulcanmc.vulcaneconomy.rest;

import com.mashape.unirest.http.HttpResponse;
import org.json.JSONArray;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.vulcanmc.vulcaneconomy.VulcanEconomy;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

public class User {
    private UUID uuid;
    private Player player;
    private Integer playerid;

    public List<Account> getAccounts() {
        return Accounts.getAccountsByPlayer(player);
    }
    public Player getPlayer() {
        return null;
    }
    public User(Integer playerid) {
        this.playerid = playerid;
    }


    public Account getAccount(Currency currency) {
        Long accountid = -1L;
        try {
            JsonNode response =  Unirest.get(VulcanEconomy.apiURL + "players/" + playerid + "/accounts").asJson().getBody();
            JSONArray jsonArray = response.getArray();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                //ADD currency checks later...
                /*String currencyobject = object.getString("currency");
                if(currencyobject.equals(currency.getName())) {
                    accountid = object.getLong("id");
                }*/
                accountid = object.getLong("id");
                return new Account(accountid);
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Account createAccount(Currency currency) {
            try {
                JsonNode response = Unirest.post(VulcanEconomy.apiURL + "accounts/").queryString("accountOwner", playerid).queryString("server", VulcanEconomy.serverid).queryString("currency", new Currency().getNameSingle()).asJson().getBody();
                JSONArray jsonArray = response.getArray();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    Long accountid = object.getLong("id");
                    return new Account(accountid);
                }
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    public boolean hasAccount(Currency currency) {
        JsonNode response = null;
        try {
            response = Unirest.get(VulcanEconomy.apiURL + "accounts/").asJson().getBody();
            JSONArray jsonArray = response.getArray();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                Integer account_owner = object.getInt("account_owner");
                if(account_owner == playerid) {
                    return true;
                }
            }
        } catch (UnirestException e) {
            VulcanEconomy.plugin.getLogger().info(e.getMessage());
        }
        return false;
    }
}
