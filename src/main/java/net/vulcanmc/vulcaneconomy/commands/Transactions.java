package net.vulcanmc.vulcaneconomy.commands;

import net.vulcanmc.vulcaneconomy.VulcanEconomy;
import net.vulcanmc.vulcaneconomy.rest.Currency;
import net.vulcanmc.vulcaneconomy.rest.Users;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Transactions implements CommandExecutor {
    private final VulcanEconomy plugin;

    public Transactions(VulcanEconomy plugin) {
        this.plugin = plugin; // Store the plugin in situations where you need it.
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        Currency currency = new Currency();
        if(args.length == 1) {
            Long balance = Users.getUser(Bukkit.getOfflinePlayer(args[0])).getAccount(currency).getBalance();
            sender.sendMessage(args[0] + "'s Balance: " + new Currency().getSymbol()+ balance);
            return true;
        }
        Long balance = Users.getUser(player).getAccount(currency).getBalance();
        sender.sendMessage("Balance: " + new Currency().getSymbol()+ balance);
        return true;
    }
}
