package net.vulcanmc.vulcaneconomy.commands

import net.vulcanmc.vulcaneconomy.rest.Account
import org.bukkit.ChatColor
import net.vulcanmc.vulcaneconomy.VulcanEconomy
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import kotlin.math.min

class BalanceTop(private val plugin: VulcanEconomy)// Store the plugin in situations where you need it.
    : CommandExecutor {

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        sender.sendMessage(ChatColor.GOLD.toString() + "Balance Top")
        var top = plugin.accounts.getTop(plugin.currencies.defaultCurrency)
        top.subList(0, min(10, top.size))
        var i : Int = 0;
        for (acc in top) {
            sender.sendMessage(ChatColor.AQUA.toString() + "$i. ${Bukkit.getOfflinePlayer(acc.owner.getID()).name}, $${acc.getBalance().toLong()}")
            i++;
        }
        return true
    }
}
