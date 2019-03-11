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

public class Balance implements CommandExecutor {
    private final VulcanEconomy plugin;

    public Balance(VulcanEconomy plugin) {
        this.plugin = plugin; // Store the plugin in situations where you need it.
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        Currency currency = new Currency();
        if(args.length == 1) {
            try {

                UUID uuid = VulcanEconomy.getPlugin().getUUIDIfExists(args[0]);
                if(uuid == null ) {
                    sender.sendMessage(ChatColor.RED + "Null profile lookup for " + args[0]);
                    return false;
                }

                if(Users.userExists(uuid) && Users.getUser(uuid).hasAccount(new Currency()))
                {
                    User user = Users.getUser(uuid);
                        Long balance = user.getAccount(currency).getBalance();
                        sender.sendMessage(ChatColor.GOLD + args[0] + "'s Balance: " + new Currency().getSymbol() + balance);
                        return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "User " + args[0] + " does not have an account!");
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (Users.userExists(player.getUniqueId())) {
                if (Users.getUser(player).hasAccount(currency)) {
                    Long balance = Users.getUser(player).getAccount(currency).getBalance();
                    sender.sendMessage(ChatColor.GOLD + "Balance: " + new Currency().getSymbol() + balance);
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "User " + player.getName() + " does not have an account!");
                    return false;
                }
            } else {
                sender.sendMessage(ChatColor.RED + "User " + player.getName() + " does not exist!");
                return false;
            }
        }
        return false;
    }
}
