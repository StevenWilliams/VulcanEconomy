package net.vulcanmc.vulcaneconomy.rest

import kong.unirest.Unirest
import net.vulcanmc.vulcaneconomy.VulcanEconomy
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.file.YamlConfiguration

import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap

class Accounts {
    private val cache: YamlConfiguration
    private val cacheMap = HashMap<String, Account>();

    init {
        val f = File(VulcanEconomy.getPlugin().dataFolder.toString() + "accounts.yml")
        if (!f.exists()) {
            try {
                f.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        cache = YamlConfiguration.loadConfiguration(f)
    }

    fun getAccount(player: OfflinePlayer, currency: Currency): Account {
      return  getAccount(player.uniqueId, currency)
        //  Unirest.get(VulcanEconomy.getApiURL() + "/");
    }

    fun getAccount(uniqueId : UUID, currency : Currency) : Account {
        //use local cache
        if(cacheMap.containsKey("${uniqueId}.${currency.id}") && cacheMap.get("${uniqueId}.${currency.id}") != null) {
            println("cacheMap")
            return cacheMap["${uniqueId}.${currency.id}"]!!;
        }
        var user = User(uniqueId);
        var acc: Account;
        if(cache.contains("${uniqueId}.${currency.id}")) {
            println("cache contains $uniqueId")
            var accID = UUID.fromString(cache.getString("players.${uniqueId}.${currency.id}.id"));
            acc = Account(accID, user, currency);
        } else {
            //cache.set();
            val get = Unirest.get(VulcanEconomy.getApiURL() + "/player/${uniqueId}/currency/${currency.id}")
            //   println(get.asString().body);
            println(get.asString().body)
            val body = get.asJson().body;
            println(body.toString());
            println(body.`object`.toString(4));

            var id = UUID.fromString(body.`object`.getString("id"));
            var balanceStr: String = body.`object`.getString("value");
            var balance = balanceStr.toBigDecimal();

            acc = Account(id, user, currency, balance);
        }
        cacheMap.put("${uniqueId}.${currency.id}", acc)

        return acc;
    }
    companion object {
        val topAccounts: List<Account>?
            get() = null

        fun getAccountsByPlayer(player: OfflinePlayer): List<Account>? {
            return null
        }
    }

}
