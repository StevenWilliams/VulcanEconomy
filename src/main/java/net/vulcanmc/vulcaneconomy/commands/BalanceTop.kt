package net.vulcanmc.vulcaneconomy.commands

import net.vulcanmc.vulcaneconomy.UUIDConverter
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
        top = top.subList(0, min(15, top.size))
        var i : Int = 1;
        for (acc in top) {
            try {
                sender.sendMessage(org.bukkit.ChatColor.AQUA.toString() + "$i. ${net.vulcanmc.vulcaneconomy.UUIDConverter.getNameFromUUID(acc.owner.getID()!!)}, $${acc.getBalance().toLong()}")

            } catch (e:Exception) {

            }
            i++;
        }
        return true
    }
}
