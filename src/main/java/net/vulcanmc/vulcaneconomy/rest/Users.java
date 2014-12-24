package net.vulcanmc.vulcaneconomy.rest;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Users {
    public static User getUser(Player player) {
        return new User(player);
    }
    public static User getUser(OfflinePlayer player) {
        return new User(player);
    }
}
