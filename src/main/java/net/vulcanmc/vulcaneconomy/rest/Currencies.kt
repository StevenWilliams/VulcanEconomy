package net.vulcanmc.vulcaneconomy.rest

import net.vulcanmc.vulcaneconomy.VulcanEconomy
import org.bukkit.configuration.ConfigurationSection
import java.util.*

/**
 * Created by steven on 23/12/14.
 */
class Currencies {
    fun getCurrency(key: String): Currency? {
        for (currency in currencies) {
            if (key == currency.key) {
                return currency
            }
        }
        return null
    }
    private fun getCurrencyFromConfig(key : String) : Currency {
        val currenciesSect: ConfigurationSection? = VulcanEconomy.plugin!!.config.getConfigurationSection("currencies")
        val uuidString: String? = currenciesSect!!.getString("$key.id")
        val uuid: UUID? = UUID.fromString(uuidString)
        val name: String? = currenciesSect.getString("$key.name")
        val symbol: String? = currenciesSect.getString("$key.symbol")
        val currency = Currency(uuid!!, name, symbol)
        currency.key = key
        return currency
    }

    val defaultCurrency: Currency
        get() {
            val defKey: String? = VulcanEconomy.plugin!!.config.getString("default")
            val currency = getCurrency(defKey!!)
            return currency!!
        }
    val currencies: ArrayList<Currency>
        get() {
            val currenciesSect: ConfigurationSection? = VulcanEconomy.plugin!!.config.getConfigurationSection("currencies")
            val keys: MutableSet<String> = currenciesSect!!.getKeys(false)
            val currenciesList: ArrayList<Currency> = ArrayList<Currency>()

            for (key in keys) {
                currenciesList.add(getCurrencyFromConfig(key))
            }
            return currenciesList
        }
}
