package net.vulcanmc.vulcaneconomy.commands;

import org.bukkit.ChatColor;
import net.vulcanmc.vulcaneconomy.VulcanEconomy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BalanceTop implements CommandExecutor {
    private final VulcanEconomy plugin;

    public BalanceTop(VulcanEconomy plugin) {
        this.plugin = plugin; // Store the plugin in situations where you need it.
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        sender.sendMessage(ChatColor.GOLD + "Balance Top");
        sender.sendMessage(ChatColor.AQUA + "1. puppy3276, $5000");
        sender.sendMessage(ChatColor.AQUA + "2. That1Guy2, $3000");
        return true;
    }
}
