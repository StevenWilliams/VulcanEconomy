package net.vulcanmc.vulcaneconomy.commands

import net.vulcanmc.vulcaneconomy.UUIDConverter
import net.vulcanmc.vulcaneconomy.VulcanEconomy
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import kotlin.math.min

class BalanceTop(private val plugin: VulcanEconomy)// Store the plugin in situations where you need it.
    : CommandExecutor {

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        var currency = plugin.currencies.defaultCurrency
        var amount = 15
        if(args.size >= 2) {
            val key = args[0]
            if(plugin.currencies.getCurrency(key) != null) {
                currency = plugin.currencies.getCurrency(key)!!
            } else {
                sender.sendMessage(plugin.prefix + ChatColor.RED + "Invalid currency. Type /eco currencies")
            }
            amount = Integer.valueOf(args[1])
        } else if (args.size == 1) {
            amount = Integer.valueOf(args[0])
        }


        sender.sendMessage(ChatColor.GOLD.toString() + "Balance Top: " + currency.name)
        var top = plugin.accounts!!.getTop(currency)
        top = top.subList(0, min(amount, top.size))
        var i = 1
        for (acc in top) {
            try {
                sender.sendMessage(org.bukkit.ChatColor.AQUA.toString() + "$i. ${UUIDConverter.getNameFromUUID(acc.owner!!.getID()!!)}, $${acc.getBalance().toLong()}")

            } catch (e:Exception) {

            }
            i++
        }
        return true
    }
}
