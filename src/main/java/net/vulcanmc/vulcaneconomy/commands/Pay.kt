package net.vulcanmc.vulcaneconomy.commands

import net.vulcanmc.vulcaneconomy.VulcanEconomy
import net.vulcanmc.vulcaneconomy.rest.*
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

import java.util.UUID

class Pay(private val plugin: VulcanEconomy)// Store the plugin in situations where you need it.
    : CommandExecutor {

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        //todo check against negative
        if (args.size >= 2) {
            val uuid = Bukkit.getOfflinePlayer(args[0]).uniqueId
            val target = plugin.accounts.getAccount(uuid, plugin.currencies.defaultCurrency)
            val amount = java.lang.Long.valueOf(args[1])
            if (sender is Player) {
                val user = plugin.accounts.getAccount(sender.uniqueId, plugin.currencies.defaultCurrency)
                user!!.transferTo(plugin.currencies.defaultCurrency, target!!, amount, "Transfer", "VulcanEconomy")
                sender.sendMessage("Payment sent. Your balance is now: ${user.getBalance()}.");
            } else if (sender is ConsoleCommandSender) {
                //is console
                target!!.deposit(amount, "Console deposit /pay")
            }
            return true;

        }
    return false;
    }
}
