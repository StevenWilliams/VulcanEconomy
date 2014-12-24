package net.vulcanmc.vulcaneconomy.rest;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.vulcanmc.vulcaneconomy.VulcanEconomy;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.UUID;

public class Users {
    public static User getUser(OfflinePlayer player) {
        return getUser(player.getPlayer());
    }
    public static User getUser(Player player) {
        UUID uuid = player.getUniqueId();
        Integer playerid;
        //this.player = (Player) player;
        try {
            JsonNode response = Unirest.get(VulcanEconomy.apiURL + "players/").asJson().getBody();
            JSONArray jsonArray = response.getArray();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String uuidobject = object.getString("uuid");
                if(uuidobject.equals(uuid.toString())) {
                    playerid = object.getInt("id");
                    return new User(playerid);
                }
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static boolean userExists(String uuid) {
        JsonNode response = null;
        try {
            response = Unirest.get(VulcanEconomy.apiURL + "players/").asJson().getBody();
            JSONArray jsonArray = response.getArray();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String uuidobject = object.getString("uuid");
                if(uuidobject.equals(uuid.toString())) {
                    return true;
                }
            }
        } catch (UnirestException e) {
            return false;
            //VulcanEconomy.plugin.getLogger().info(e.getMessage());
        }
        return false;
    }
    public static User createUser(String uuid, String username) {
        try {
            JsonNode response = Unirest.post(VulcanEconomy.apiURL + "players").queryString("uuid", uuid).queryString("username", username).asJson().getBody();
            JSONArray jsonArray = response.getArray();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                Integer playerid = object.getInt("id");
                return new User(playerid);
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }
}
