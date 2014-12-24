package net.vulcanmc.vulcaneconomy.rest;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class User {
    private UUID uuid;
    private Player player;
    public List<Account> getAccounts() {
        return Accounts.getAccountsByPlayer(player);
    }
    public Player getPlayer() {
        return null;
    }
    public User(Player player) {
        this.uuid = player.getUniqueId();
        this.player = player;
    }
    public User(OfflinePlayer player) {
        this.uuid = player.getUniqueId();
        this.player = (Player) player;
    }
    public Account getAccount(Currency currency) {
        return null;
    }
    public boolean hasAccount(Currency currency) {
        return true;
    }
}
