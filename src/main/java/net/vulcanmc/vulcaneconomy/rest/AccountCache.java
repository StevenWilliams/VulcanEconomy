package net.vulcanmc.vulcaneconomy.rest;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.vulcanmc.vulcaneconomy.VulcanEconomy;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by steven on 25/12/14.
 */
public class AccountCache {
    private Long playerid;
    private Long HasLastLookup = null;
    private Long GetLastLookup = null;
    private boolean hasAccount = false;
    private Account account;

    public AccountCache(User user) {
        this.playerid = user.getId();
    }
    public boolean hasAccount(Currency currency) {
        if(GetLastLookup == null) {
            //VulcanEconomy.plugin.getLogger().info("No accounthas cache");
            //VulcanEconomy.plugin.getLogger().info("Player id =" + this.playerid);
            return lookupHasAccount(currency);
        } else {
            //30 minute cache
            if(GetLastLookup > System.currentTimeMillis() - 1800000) {
                if(this.hasAccount == true) {
                    //VulcanEconomy.plugin.getLogger().info("accounthas cache true!");
                    return this.hasAccount;
                } else {
                   // VulcanEconomy.plugin.getLogger().info("accounthas cache fale, checking again!");
                    return lookupHasAccount(currency);
                }
            } else {
                //VulcanEconomy.plugin.getLogger().info("accounthas cache outdated!");
                return lookupHasAccount(currency);
            }
        }
    }
    public void setAccount(Account newaccount) {
        this.hasAccount = true;
        this.account = newaccount;
    }
    public boolean lookupHasAccount(Currency currency) {
        JsonNode response = null;
        try {
            response = Unirest.get(VulcanEconomy.apiURL + "accounts/").basicAuth(VulcanEconomy.plugin.apiUser, VulcanEconomy.plugin.apiPass).asJson().getBody();
            JSONArray data = response.getObject().getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject object = data.getJSONObject(i);
                Long account_owner = object.getLong("account_owner");
                if(account_owner == playerid) {
                    this.hasAccount = true;
                    this.HasLastLookup = System.currentTimeMillis();
                    this.GetLastLookup = System.currentTimeMillis();
                    this.account = new Account(object.getLong("id"));
                    return true;
                }
            }
        } catch (UnirestException e) {
            VulcanEconomy.plugin.getLogger().info(e.getMessage());
        }
        this.hasAccount = false;
        return false;
    }
    public Account getAccount(Currency currency) {
        if(GetLastLookup == null) {
           // VulcanEconomy.plugin.getLogger().info("No accountget cache");
            return lookupGetAccount(currency);
        } else {
            //15 minute cache
            if(GetLastLookup > System.currentTimeMillis() - 900000) {
                //VulcanEconomy.plugin.getLogger().info("using accountget cache");
                return this.account;
            } else {
                //VulcanEconomy.plugin.getLogger().info("accountget cache outdated!");
                return lookupGetAccount(currency);
            }
        }
    }
    public Account lookupGetAccount(Currency currency) {
        Long accountid = -1L;
        try {
            JsonNode response =  Unirest.get(VulcanEconomy.apiURL + "players/" + playerid + "/accounts").basicAuth(VulcanEconomy.plugin.apiUser, VulcanEconomy.plugin.apiPass).asJson().getBody();
            JSONArray data = response.getObject().getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject object = data.getJSONObject(i);
                //ADD currency checks later...
                /*String currencyobject = object.getString("currency");
                if(currencyobject.equals(currency.getName())) {
                    accountid = object.getLong("id");
                }*/
                accountid = object.getLong("id");
                this.GetLastLookup = System.currentTimeMillis();
                Account account1 = new Account(accountid);
                this.account = account1;
                return new Account(accountid);
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }
}
