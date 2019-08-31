package net.vulcanmc.vulcaneconomy.commands

import net.vulcanmc.vulcaneconomy.VulcanEconomy
import net.vulcanmc.vulcaneconomy.rest.Account
import net.vulcanmc.vulcaneconomy.rest.Currency
import net.vulcanmc.vulcaneconomy.rest.Transaction
import net.vulcanmc.vulcaneconomy.rest.User
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.time.format.DateTimeFormatter

import java.util.ArrayList

class Transactions(private val plugin: VulcanEconomy)// Store the plugin in situations where you need it.
    : CommandExecutor {

    fun isNumeric(strNum: String): Boolean {
        try {
            val d = java.lang.Double.parseDouble(strNum)
        } catch (nfe: NumberFormatException) {
            return false
        } catch (nfe: NullPointerException) {
            return false
        }
        return true
    }

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        var player: OfflinePlayer
        var quantity : Int = 10
        var currencyPos : Int = 1
        if (args.size >= 1) {
            if(isNumeric(args[0]) && sender is Player) {
              player=sender;
              quantity = Integer.valueOf(args[0]);
            } else {
                player= Bukkit.getOfflinePlayer(args[0]);
            }
        } else if(sender is Player) {
            player = sender;
        } else {
            return false;
        }
        var currency : Currency = plugin.currencies.defaultCurrency;
        if(args.size >=2) {
            var key = args[1];
            if(isNumeric(key)) {
                quantity = Integer.valueOf(key)
                currencyPos = 2;
            }
            if(args.size >= currencyPos+1) {
                key = args[currencyPos]
                if(plugin.currencies.getCurrency(key) != null) {
                    currency = plugin.currencies.getCurrency(key)!!;
                } else {
                    sender.sendMessage(plugin.prefix + ChatColor.RED + "Invalid currency. Type /eco currencies")
                    return false;
                }
            }
        }

            sender.sendMessage(ChatColor.GOLD.toString() + "===== " + player.name + "'s Transactions (${currency.name}) =====")
        val inputFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss")
        val user = User(player.uniqueId)
        val account = user.getAccount(currency)
        val transactions = account!!.getTransactions(quantity)
        plugin.logger.info(transactions.size.toString())

        for (transaction in transactions) {
            //println(account.id.toString() + transaction.creditPlayer?.id.toString())
            if(account.id.equals(transaction.creditPlayer?.id)) {
                sender.sendMessage(ChatColor.RED.toString() + "$${transaction.amount.toString()} | ${transaction.time} | ${transaction.plugin} | ${transaction.description}")
            } else {
                sender.sendMessage(ChatColor.GREEN.toString() + "$${transaction.amount.toString()} | ${transaction.time} | ${transaction.plugin} | ${transaction.description}")
            }
        }
            return true
    }
}
