package net.vulcanmc.vulcaneconomy.rest;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.vulcanmc.vulcaneconomy.VulcanEconomy;
import org.json.JSONObject;

/**
 * Created by steven on 24/12/14.
 */
public class BalanceCache {
    private Long accountid;
    private Long balance;
    private Long lastLookup = null;
    public BalanceCache(Account account) {
        this.accountid = account.getId();
        this.balance = getBalance();
    }
    public Long setBalance(Long newbalance) {
        this.balance = newbalance;
        return this.balance;
    }
    public Long withdraw(Long amount) {
        this.balance = this.balance - amount;
        return this.balance;
    }
    public Long deposit(Long amount) {
        this.balance = this.balance + amount;
        return this.balance;
    }
    public Long getBalance() {
        if(lastLookup == null) {
            //VulcanEconomy.plugin.getLogger().info("No cache " + this.accountid);
            return lookupBalance();
        } else {
            //30 second cache
            if(lastLookup > System.currentTimeMillis() - 45000) {
                //VulcanEconomy.plugin.getLogger().info("Using bal cache: " + this.accountid);
                return this.balance;
            } else {
                //VulcanEconomy.plugin.getLogger().info("bal cache outdated! " + this.accountid);
                return lookupBalance();
            }
        }


    }
    public Long lookupBalance() {
        Long balance = -1L;
        try {
            JsonNode response = Unirest.get(VulcanEconomy.getApiURL() + "accounts/" + this.accountid).basicAuth(VulcanEconomy.getPlugin().getApiUser(), VulcanEconomy.getPlugin().getApiPass()).asJson().getBody();

            JSONObject data = response.getObject().getJSONObject("data");
                    balance = data.getLong("balance");
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        this.balance = balance;
        this.lastLookup = System.currentTimeMillis();
        return balance;
    }
}