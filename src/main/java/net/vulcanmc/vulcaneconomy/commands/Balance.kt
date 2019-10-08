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

         var posCounter = 0;
        var useCache = true;
        if(args.size>=1 && (args[0].equals("false") )){
            useCache=false;
            posCounter++;
        }
        if (args.size >= (posCounter+1)) {
            val username = args[posCounter]
            val uuid = Bukkit.getOfflinePlayer(username).uniqueId
            val acc = plugin.accounts!!.getAccount(uuid, defCurrency)
            val currencies: ArrayList<Currency> = plugin.currencies.currencies
            for(currency in currencies) {
                val acc = plugin.accounts!!.getAccount(uuid, currency)
                if (currency.id.equals(plugin.currencies.defaultCurrency.id)) {
                    sender.sendMessage(plugin.prefix + "Main Balance: " + acc?.getBalance(useCache)?.toLong() + " "+ acc?.currency?.symbol)

                } else {
                    sender.sendMessage(plugin.prefix + acc?.getBalance(useCache)?.toLong() + " "+ acc?.currency?.symbol)

                }
            }
        } else if (sender is Player && args.size < (posCounter+1)) {

            val acc = plugin.accounts!!.getAccount(sender, defCurrency)


            val currencies: ArrayList<Currency> = plugin.currencies.currencies
            for(currency in currencies) {
                val acc = plugin.accounts!!.getAccount(sender, currency)

                if (currency.id.equals(plugin.currencies.defaultCurrency.id)) {
                    sender.sendMessage(plugin.prefix + "Main Balance: " + acc?.getBalance(useCache)?.toLong() + " "+ acc?.currency?.symbol)

                } else {
                    sender.sendMessage(plugin.prefix + acc?.getBalance(useCache)?.toLong() + " "+ acc?.currency?.symbol)

                }
            }


        }
        return true
    }
}
