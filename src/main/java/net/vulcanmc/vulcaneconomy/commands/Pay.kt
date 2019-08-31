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
import java.math.BigDecimal

import java.util.UUID

class Pay(private val plugin: VulcanEconomy)// Store the plugin in situations where you need it.
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
        //todo check against negative
        if (args.size >= 2) {
            val uuid = Bukkit.getOfflinePlayer(args[0]).uniqueId
            var currency = plugin.currencies.defaultCurrency;
            var amount :Long = 0;
            if(isNumeric(args[1])) {
                 amount = java.lang.Long.valueOf(args[1])
            } else {
                if(args.size < 3) {
                    return false;
                } else {
                    var key = args[1];
                    if(plugin.currencies.getCurrency(key) != null) {
                        currency = plugin.currencies.getCurrency(key)!!;
                    } else {
                        sender.sendMessage("Invalid currency. Type /eco currencies")
                        return false;
                    }
                    if(!isNumeric(args[2])) {
                        return false;
                    }
                    amount = java.lang.Long.valueOf(args[2]);
                }
            }
            val target = plugin.accounts.getAccount(uuid, currency)

            if(amount < 0) {
                sender.sendMessage("Amount cannot be negative.")
                return false;
            }

            if (sender is Player) {
                val user = plugin.accounts.getAccount(sender.uniqueId, currency)
                val balance = user!!.getBalance();
                if(balance.compareTo(BigDecimal(amount)) < 0) {
                    sender.sendMessage(plugin.prefix + ChatColor.RED + "You do not have enough money.")
                    return false;
                }
                user!!.transferTo(currency, target!!, amount, "Payment from ${sender.name} to ${target.owner?.offlinePlayer?.name}", "VulcanEconomy")
                sender.sendMessage(plugin.prefix + "Payment sent. Your balance is now: ${user.getBalance()} ${currency.name}.");
                if(target.owner?.offlinePlayer!!.isOnline) {
                    val targetPlayer = target.owner?.offlinePlayer.player;
                    targetPlayer.sendMessage(plugin.prefix + "You have received a payment of $amount ${currency.name} from ${sender.name}")
                }
                target!!.updateBalanceAsync();
            } else if (sender is ConsoleCommandSender) {
                //is console
                target!!.deposit(amount, "Console deposit /pay")
            }
            return true;

        }
    return false;
    }
}
