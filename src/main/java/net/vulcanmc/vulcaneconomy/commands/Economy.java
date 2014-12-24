package net.vulcanmc.vulcaneconomy.commands;

import net.vulcanmc.vulcaneconomy.VulcanEconomy;
import net.vulcanmc.vulcaneconomy.rest.Currency;
import net.vulcanmc.vulcaneconomy.rest.Users;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
//CODES A MESS. FIX
public class Economy implements CommandExecutor {
    private final VulcanEconomy plugin;

    public Economy(VulcanEconomy plugin) {
        this.plugin = plugin; // Store the plugin in situations where you need it.
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length >= 3) {
            String subcommand = args[0];
            if (subcommand.toLowerCase().equals("give")) {
                sender.sendMessage("Giving user money...");
                OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
                if (Long.valueOf(args[2]) < 0) {
                    //cant give negative...
                    sender.sendMessage("Can't give negative...");
                    return false;
                }
                Long amount = Long.valueOf(args[2]);
                sender.sendMessage("Giving user " + amount.toString());
                if (Users.getUser(player).getAccount(new Currency()).deposit(amount, "Economy Manage Command") == null) {
                    sender.sendMessage("Money failed!");
                } else {
                    sender.sendMessage("Gave user money!");
                }
                return true;
            } else if (subcommand.toLowerCase().equals("take")) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
                if (Long.valueOf(args[2]) < 0) {
                    //cant give negative...
                    sender.sendMessage("Can't take negative...");
                    return false;
                }
                Long amount = Long.valueOf(args[2]);
                sender.sendMessage("Giving user " + amount.toString());
                if (Users.getUser(player).getAccount(new Currency()).deposit(amount, "Economy Manage Command") == null) {
                    sender.sendMessage("Money failed!");
                } else {
                    sender.sendMessage("Gave user money!");
                }
                return true;
            } else if (subcommand.toLowerCase().equals("set")) {
                sender.sendMessage("Not implemented yet...");
            }
        }

        return false;
    }
}
