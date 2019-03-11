package net.vulcanmc.vulcaneconomy.commands;

import net.vulcanmc.vulcaneconomy.VulcanEconomy;
import net.vulcanmc.vulcaneconomy.rest.Currency;
import net.vulcanmc.vulcaneconomy.rest.User;
import net.vulcanmc.vulcaneconomy.rest.Users;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Pay implements CommandExecutor {
    private final VulcanEconomy plugin;

    public Pay(VulcanEconomy plugin) {
        this.plugin = plugin; // Store the plugin in situations where you need it.
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length >= 2) {
                UUID uuid = VulcanEconomy.getPlugin().getUUIDIfExists(args[0]);
            if(uuid == null ) {
                sender.sendMessage(ChatColor.RED + "Null profile lookup for " + args[0]);
                return false;
            }
                User payeduser = Users.getUser(uuid);
                User payinguser = Users.getUser(((Player) sender).getUniqueId());
                if(!Users.userExists(uuid) || !payeduser.hasAccount(new Currency())) {
                    sender.sendMessage(ChatColor.RED + "Account does not exist!");
                    return false;
                }
                Long amount = Long.valueOf(args[1]);
                String description = "";
                if(args.length >= 3 && args[2] != null) {
                    description = args[2];
                }
                if(amount < 0) {
                    sender.sendMessage(ChatColor.RED + "Cannot pay lower than 0...");
                    return false;
                }
                if (amount == 0) {
                    sender.sendMessage(ChatColor.RED + "Cannot pay 0");
                    return false;
                }
                if(payinguser.getAccount(new Currency()).has(amount)) {
                    payinguser.getAccount(new Currency()).withdraw(amount, description + "(Payment to userid:" + payeduser.getId() + ")");
                    payeduser.getAccount(new Currency()).deposit(amount, description + "(Payment from userid:" + payeduser.getId() + ")");

                    sender.sendMessage(ChatColor.GREEN + "Payed " + payeduser.getOfflinePlayer().getName() + " " + new Currency().getSymbol() + amount);
                    if(payeduser.getOfflinePlayer().isOnline()) {
                        payeduser.getOfflinePlayer().getPlayer().sendMessage(ChatColor.GREEN + "Received " + new Currency().getSymbol() + amount + " from " + payinguser.getOfflinePlayer().getName());
                    }
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "You do not have enough money to complete this transaction!");
                }
        }
        return false;
    }
}
