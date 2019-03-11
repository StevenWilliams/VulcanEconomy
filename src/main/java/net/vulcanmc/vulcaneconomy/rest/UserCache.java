package net.vulcanmc.vulcaneconomy.rest;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.vulcanmc.vulcaneconomy.VulcanEconomy;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by steven on 25/12/14.
 */
public class UserCache {
    private String uuid;
    private boolean userExists;
    private Long ExistsLastLookup = null;
   // private Long GetLastLookup = null;
    private User user = null;

    public UserCache(String uuidstring) {
        this.uuid = uuidstring;
    }
    public void setUser(User newuser) {
        this.user = newuser;
    }
    public boolean userExists() {
        if(ExistsLastLookup == null) {
            //VulcanEconomy.plugin.getLogger().info("No user cache");
            return lookupUserExists();
        } else {
           // 5 minute cache
            if(ExistsLastLookup > System.currentTimeMillis() - 300000) {
               // VulcanEconomy.plugin.getLogger().info("Using user cache");
                return this.userExists;
            } else {
                //VulcanEconomy.plugin.getLogger().info("user cache outdated!");
                return lookupUserExists();
            }
        }
    }

    public boolean lookupUserExists() {
        JsonNode response = null;
        try {
            response = Unirest.get(VulcanEconomy.getApiURL() + "players/").basicAuth(VulcanEconomy.getPlugin().getApiUser(), VulcanEconomy.getPlugin().getApiPass()).asJson().getBody();
            JSONArray data = response.getObject().getJSONArray("data");
            if(data == null) {
                return false;
            }
            for (int i = 0; i < data.length(); i++) {
                JSONObject object = data.getJSONObject(i);
                String uuidobject = object.getString("uuid");
                if(uuidobject.equals(uuid.toString())) {
                    this.userExists = true;
                    this.ExistsLastLookup = System.currentTimeMillis();
                    return true;
                }
            }
        } catch (UnirestException e) {
            return false;
            //VulcanEconomy.plugin.getLogger().info(e.getMessage());
        }
        return false;
    }
    public User getUser() {
        if(ExistsLastLookup == null) {
            //VulcanEconomy.plugin.getLogger().info("No cache");
            return lookupGetUser();
        } else {
            //5 minute cache
            if(ExistsLastLookup > System.currentTimeMillis() - 300000) {
                //VulcanEconomy.plugin.getLogger().info("Using get user cache");
                if(this.user != null ) {
                    return this.user;
                } else {
                   // VulcanEconomy.plugin.getLogger().info("Using get cache null");
                    return lookupGetUser();
                }
            } else {
                //VulcanEconomy.plugin.getLogger().info("user cache outdated!");
                return lookupGetUser();
            }
        }
    }
    public User lookupGetUser() {
        Integer playerid;
        //this.player = (Player) player;
        try {
            JsonNode response = Unirest.get(VulcanEconomy.getApiURL() + "players/").basicAuth(VulcanEconomy.getPlugin().getApiUser(), VulcanEconomy.getPlugin().getApiPass()).asJson().getBody();
            JSONArray data = response.getObject().getJSONArray("data");


            for (int i = 0; i < data.length(); i++) {
                JSONObject object = data.getJSONObject(i);
                String uuidobject = object.getString("uuid");
                if(uuidobject.equals(uuid.toString())) {
                    playerid = object.getInt("id");
                    User user1 = new User(playerid, UUID.fromString(uuid));
                    this.user = user1;
                    this.userExists = true;
                    this.ExistsLastLookup = System.currentTimeMillis();
                    return user1;
                }
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }
}
