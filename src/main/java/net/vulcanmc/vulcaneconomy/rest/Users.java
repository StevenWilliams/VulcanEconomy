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
    public static User getUser(Player user) {
        return getUser(user.getUniqueId());
    }
    public static User getUser(UUID uuid) {
        if(!VulcanEconomy.getPlugin().getUsercache().containsKey(uuid)) {
            VulcanEconomy.getPlugin().getUsercache().put(uuid, new UserCache(uuid.toString()));
            return VulcanEconomy.getPlugin().getUsercache().get(uuid).getUser();
        } else {
            return VulcanEconomy.getPlugin().getUsercache().get(uuid).getUser();
        }
    }
    public static User getUser(String playername) {
        Integer playerid;
        //this.player = (Player) player;
        try {
            JsonNode response = Unirest.get(VulcanEconomy.getApiURL() + "players/").basicAuth(VulcanEconomy.getPlugin().getApiUser(), VulcanEconomy.getPlugin().getApiPass()).asJson().getBody();
            JSONArray jsonArray = response.getArray();
            JSONArray data = response.getObject().getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = data.getJSONObject(i);
                String usernameobject = object.getString("username");
                if(usernameobject.equals(playername)) {
                    playerid = object.getInt("id");
                    UUID uuid = UUID.fromString(object.getString("uuid"));
                    User user1 = new User(playerid, uuid);
                    return user1;
                }
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
        //lookup with usernmae instead of uuid. fall back with mojang api
    }
    public static boolean userExists(UUID uuid) {
        if(!VulcanEconomy.getPlugin().getUsercache().containsKey(uuid)) {
            VulcanEconomy.getPlugin().getUsercache().put(uuid, new UserCache(uuid.toString()));
            return VulcanEconomy.getPlugin().getUsercache().get(uuid).userExists();
        } else {
            return VulcanEconomy.getPlugin().getUsercache().get(uuid).userExists();
        }
    }
    public static User createUser(UUID uuid, String username) {
        try {
            JsonNode response = Unirest.post(VulcanEconomy.getApiURL() + "players").basicAuth(VulcanEconomy.getPlugin().getApiUser(), VulcanEconomy.getPlugin().getApiPass()).queryString("uuid", uuid.toString()).queryString("username", username).asJson().getBody();

            JSONObject data = response.getObject().getJSONObject("data");

                Integer playerid = data.getInt("id");
                User user = new User(playerid, uuid);
                VulcanEconomy.getPlugin().getUsercache().get(uuid).setUser(user);
                return user;
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }
}
