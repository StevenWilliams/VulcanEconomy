package net.vulcanmc.vulcaneconomy.commands

import net.vulcanmc.vulcaneconomy.VulcanEconomy
import net.vulcanmc.vulcaneconomy.rest.Currency
import net.vulcanmc.vulcaneconomy.rest.User
import net.vulcanmc.vulcaneconomy.rest.Users
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import java.math.BigDecimal

import java.util.UUID

//CODES A MESS. FIX
class Economy(private val plugin: VulcanEconomy)// Store the plugin in situations where you need it.
    : CommandExecutor {

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {

        if (args.size >= 3) {
            val subcommand = args[0]
            //target uuid
            val uuid = VulcanEconomy.getPlugin().getUUID(args[1])
            if (uuid == null) {
                sender.sendMessage(ChatColor.RED.toString() + "Null profile lookup for " + args[1])
                return false
            }
            val user = User(uuid)
            val amount = java.lang.Long.valueOf(args[2])

            if (subcommand.toLowerCase() == "give") {
                try {
                    if (amount < 0) {
                        sender.sendMessage(ChatColor.RED.toString() + "Amount must be greater than 0!")
                        return false
                    }
                    sender.sendMessage("Giving user $amount")
                    if (user.getAccount(plugin.currencies.defaultCurrency)!!.deposit(amount, "Economy Manage Command: " + sender.name)) {
                        sender.sendMessage(ChatColor.AQUA.toString() + "Successfully set balance of " + user.offlinePlayer.name + " to " + user.getAccount(plugin.currencies.defaultCurrency)!!.getBalance().toLong())
                        return true
                    } else {
                        sender.sendMessage(ChatColor.RED.toString() + "Error occurred when trying to set balance.")
                        sender.sendMessage(ChatColor.RED.toString() + user.offlinePlayer.name + "'s balance: " + user.getAccount(plugin.currencies.defaultCurrency)!!.getBalance().toLong())
                        return false
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                return true
            } else if (subcommand.toLowerCase() == "take") {
                try {
                    if (amount < 0) {
                        sender.sendMessage(ChatColor.RED.toString() + "Amount must be greater than 0!")
                        return false
                    }
                    if (user!!.getAccount(plugin.currencies.defaultCurrency)!!.withdraw(amount, "Economy Manage Command: " + sender.name) != null) {
                        sender.sendMessage(ChatColor.AQUA.toString() + "Successfully set balance of " + user.offlinePlayer.name + " to " + user.getAccount(plugin.currencies.defaultCurrency)!!.getBalance().toLong())
                        return true
                    } else {
                        sender.sendMessage(ChatColor.RED.toString() + "Error occurred when trying to set balance.")
                        sender.sendMessage(ChatColor.RED.toString() + user.offlinePlayer.name + "'s balance: " + user.getAccount(plugin.currencies.defaultCurrency)!!.getBalance().toLong())
                        return false
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return true
            } else if (subcommand.toLowerCase() == "set") {
                var acc = user.getAccount(plugin.currencies.defaultCurrency)!!;
                var balance = acc.getBalance(false)
                if(balance.compareTo(BigDecimal(amount)) > 0) {
                    //balance is greater than amount
                    val difference = balance.subtract(BigDecimal(amount));
                    acc.withdraw(difference.toLong(), "Balance set to $amount")
                } else if (balance.compareTo(BigDecimal(amount)) < 0) {
                    //balance is less than than amount
                    val difference = BigDecimal(amount).subtract(balance);
                    acc.deposit(difference.toLong(), "Balance set to $amount")

                }
                sender.sendMessage(ChatColor.AQUA.toString() + "Set balance of " + user.offlinePlayer.name + " to " + user.getAccount(plugin.currencies.defaultCurrency)!!.getBalance(false))

            }
        }
        return false
    }

}
