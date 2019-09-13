package net.vulcanmc.vulcaneconomy.rest

import net.vulcanmc.vulcaneconomy.VulcanEconomy
import org.bukkit.configuration.ConfigurationSection
import java.util.UUID

/**
 * Created by steven on 23/12/14.
 */
class Currencies {
    fun getCurrency(key:String) : Currency? {
        for(currency in currencies) {
            if(key.equals(currency.key)) {
                return currency;
            }
        }
        return null;
    }
    val defaultCurrency: Currency
        get() {
            val defKey : String? = VulcanEconomy.getPlugin().config.getString("default");
            val currenciesSect: ConfigurationSection? = VulcanEconomy.getPlugin().config.getConfigurationSection("currencies")
            val uuidString : String? = currenciesSect!!.getString("$defKey.id");
            val uuid : UUID? = UUID.fromString(uuidString);
            val name : String? = currenciesSect?.getString("$defKey.name")
            val symbol : String? = currenciesSect?.getString("$defKey.symbol")
            var currency = Currency(uuid!!, name, symbol);
            currency.key = defKey;
            return currency;
        }
    val currencies: ArrayList<Currency>
        get() {
            val currenciesSect: ConfigurationSection? = VulcanEconomy.getPlugin().config.getConfigurationSection("currencies")
            val keys: MutableSet<String> = currenciesSect!!.getKeys(false);
            val currenciesList : ArrayList<Currency> = ArrayList<Currency>();

            for(key in keys) {
                val uuidStr :String? = currenciesSect.getString("${key}.id")
                val uuid : UUID? = UUID.fromString(uuidStr);
                val name :String? = currenciesSect.getString("$key.name");
                val symbol:String? = currenciesSect.getString("$key.symbol")
                val currency: Currency = Currency(uuid!!, name, symbol)
                currency.key = key
                currenciesList.add(currency)
            }
            return currenciesList;
        }
}
