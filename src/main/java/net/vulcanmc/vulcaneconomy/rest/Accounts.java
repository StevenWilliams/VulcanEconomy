package net.vulcanmc.vulcaneconomy.rest;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.vulcanmc.vulcaneconomy.VulcanEconomy;
import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class Accounts {
    public static List<Account> getTopAccounts() {
        return null;
    }
    public static List<Account> getAccountsByPlayer(Player player) {
        return null;
    }
    public static Account getAccount(Player player, Currency currency) {
        return new Account(-1L);
    }

}
