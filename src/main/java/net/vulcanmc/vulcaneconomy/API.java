package net.vulcanmc.vulcaneconomy;

import net.vulcanmc.vulcaneconomy.rest.Account;
import net.vulcanmc.vulcaneconomy.rest.Currency;
import net.vulcanmc.vulcaneconomy.rest.User;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class API {
    private static VulcanEconomy vulcanEconomy = VulcanEconomy.getPlugin();
    private Plugin plugin;

    public API(Plugin plugin) {
        plugin = plugin;
    }

    public long getBalance(OfflinePlayer player, Currency currency) {
        Account account = getPlayerAccount(player, currency);
        return account.getBalance().longValue();
    }
    public long getBalance(OfflinePlayer player) {
        return getBalance(player, getDefaultCurrency());
    }
    public boolean has(OfflinePlayer player, Currency currency, Long amount) {
        return getBalance(player, currency) >= amount;
    }
    public boolean has(OfflinePlayer player, Long amount) {
        return has(player, getDefaultCurrency(), amount);
    }

    public boolean deposit(OfflinePlayer player, Currency currency, Long amount, String message) {
        Account account = getPlayerAccount(player, currency);
        return account.deposit(amount, message, plugin.getName());
    }

    public boolean deposit(OfflinePlayer player, Currency currency, Long amount) {
        return deposit(player, currency, amount, "");
    }
    public boolean deposit(OfflinePlayer player, Long amount, String message) {
        return deposit(player, getDefaultCurrency(), amount, message);
    }
    public boolean deposit(OfflinePlayer player, Long amount) {
        return deposit(player, amount, "");
    }

    public boolean withdraw(OfflinePlayer player, Currency currency, Long amount, String message) {
        Account account = getPlayerAccount(player, currency);
        return account.deposit(amount, message, plugin.getName());
    }

    public boolean withdraw(OfflinePlayer player, Currency currency, Long amount) {
        return withdraw(player, currency, amount, "");
    }
    public boolean withdraw(OfflinePlayer player, Long amount, String message) {
        return withdraw(player, getDefaultCurrency(), amount, message);
    }
    public boolean withdraw(OfflinePlayer player, Long amount) {
        return withdraw(player, getDefaultCurrency(), amount, "");
    }
    private Account getPlayerAccount(OfflinePlayer player) {
        return getPlayerAccount(player, getDefaultCurrency());
    }
    private Account getPlayerAccount(OfflinePlayer player, Currency currency) {
        User user = new User(player.getUniqueId());
        return user.getAccount(currency);
    }
    public static Currency getDefaultCurrency() {
        return vulcanEconomy.getCurrencies().getDefaultCurrency();
    }
    public static ArrayList<Currency> getCurrencies() {
        return vulcanEconomy.getCurrencies().getCurrencies();
    }
}
