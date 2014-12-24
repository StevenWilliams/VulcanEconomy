package net.vulcanmc.vulcaneconomy.rest;

import org.bukkit.entity.Player;

import java.util.List;

public class Accounts {
    public static List<Account> getTopAccounts() {
        return null;
    }
    public static List<Account> getAccountsByPlayer(Player player) {
        return null;
    }
    public static Account getAccount(Player player, Currency currency) {
        return null;
    }
    public static Account createAccount(Player player, Currency currency) {
        return new Account();
    }
}
