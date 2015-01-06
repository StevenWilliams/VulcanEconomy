package net.vulcanmc.vulcaneconomy.commands;

import net.vulcanmc.vulcaneconomy.VulcanEconomy;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Transactions implements CommandExecutor {
    private final VulcanEconomy plugin;

    public Transactions(VulcanEconomy plugin) {
        this.plugin = plugin; // Store the plugin in situations where you need it.
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 1) {
             sender.sendMessage(ChatColor.GOLD + "===== {PLAYERS}'s Transactions =====");
             sender.sendMessage(ChatColor.AQUA + "7 | DEC23 11:59 | D-$500 | Payment to That1Guy2");
             sender.sendMessage(ChatColor.AQUA + "43 | DEC24 1:54 | D-$530 | Payment to That1Guy2");
             sender.sendMessage(ChatColor.AQUA + "198 | DEC27 16:23 | C-$53 | Payment from Notch");
             sender.sendMessage(ChatColor.AQUA + "44885 | DEC29 17:42 | D-$56 | Payment to Notch");
            sender.sendMessage(ChatColor.AQUA + "425893 | DEC31 5:34 | C-$993 | Payment from puppy3276");
             sender.sendMessage(ChatColor.GOLD + "Page 1 out of 5");
            return true;
        }
        return false;
    }
}
