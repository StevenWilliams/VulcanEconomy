package net.vulcanmc.vulcaneconomy.commands

import net.vulcanmc.vulcaneconomy.VulcanEconomy
import net.vulcanmc.vulcaneconomy.rest.Currency
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Balance(private val plugin: VulcanEconomy)// Store the plugin in situations where you need it.
    : CommandExecutor {

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {


        val defCurrency = plugin.currencies.defaultCurrency;

        if (args.size >= 1) {
            val username = args[0]
            val uuid = Bukkit.getOfflinePlayer(username).uniqueId
            val acc = plugin.accounts.getAccount(uuid, defCurrency)
            sender.sendMessage("Balance" + acc?.getBalance())
            val currencies: ArrayList<Currency> = plugin.currencies.currencies
            for(currency in currencies) {
                val acc = plugin.accounts.getAccount(uuid, currency)
                sender.sendMessage("Balance "  + acc?.getBalance(true) + acc?.currency?.symbol)
            }
        } else if (sender is Player && args.size < 1) {

            val acc = plugin.accounts.getAccount(sender, defCurrency)

            sender.sendMessage("Balance" + acc?.getBalance(true) + acc?.currency?.symbol)

            val currencies: ArrayList<Currency> = plugin.currencies.currencies
            for(currency in currencies) {
                val acc = plugin.accounts.getAccount(sender, currency)
                sender.sendMessage("Balance "  + acc?.getBalance(true) + acc?.currency?.symbol)
            }

        }
        return true
    }
}
