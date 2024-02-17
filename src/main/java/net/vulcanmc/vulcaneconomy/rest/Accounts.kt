package net.vulcanmc.vulcaneconomy.rest

import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.json.responseJson
import net.vulcanmc.vulcaneconomy.VulcanEconomy
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.file.YamlConfiguration

import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap

import com.github.kittinunf.result.Result;
import org.bukkit.configuration.file.FileConfiguration
import java.math.BigDecimal
import java.util.logging.Level

class Accounts {
    private var cache: YamlConfiguration
    private val cacheMap = HashMap<String, Account>();
    private var f : File;
    init {
        f = File(VulcanEconomy.plugin!!.dataFolder.toString() + "/accounts.yml")
        if (!f.exists()) {
            try {
                f.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        cache = YamlConfiguration.loadConfiguration(f)

    }

    fun saveCache() {
        if (cache == null || f == null) {
            return;
        }
        try {
            getCache().save(f);
        } catch (ex : IOException) {
            VulcanEconomy.plugin!!.getLogger().log(Level.SEVERE, "Could not save config to " + f, ex);
        }

    }
    fun getCache() : FileConfiguration {
            if (cache == null) {
                reloadCache();
            }
            return cache;
    }
    fun reloadCache() {
        if (cache == null) {
            f =         f = File(VulcanEconomy.plugin!!.dataFolder.toString() + "/accounts.yml")
        }
        cache = YamlConfiguration.loadConfiguration(f)
    }
    fun getAccount(player: OfflinePlayer, currency: Currency): Account? {
      return  getAccount(player.uniqueId, currency)
        //  Unirest.get(VulcanEconomy.getApiURL() + "/");
    }

    fun removeAccountsFromCache(playerID : UUID) {
        val currencies: ArrayList<Currency> = VulcanEconomy.plugin!!.currencies.currencies;
        for(currency in currencies) {
            removeAccountFromCache(playerID, currency)
        }
    }
    fun removeAccountFromCache(playerID: UUID, currency : Currency) {
        if(cacheMap.containsKey("${playerID}.${currency.id}")) {
            val acc: Account? = cacheMap.get("${playerID}.${currency.id}");
            this.getCache().set("${playerID}.${currency.id}.bal", acc!!.getBalance(true));
            cacheMap.remove("${playerID}.${currency.id}")
            saveCache()
        }
;
    }
    fun getAccount(uniqueId : UUID, currency : Currency) : Account? {
        val time1 = System.currentTimeMillis();

        //use local cache
        if(cacheMap.containsKey("${uniqueId}.${currency.id}") && cacheMap.get("${uniqueId}.${currency.id}") != null) {
         //   println("cacheMap")
            return cacheMap["${uniqueId}.${currency.id}"]!!;
        }
        var user = User(uniqueId);
        var acc: Account? = null;
        if(getCache().contains("${uniqueId}.${currency.id}.id")) {
  //          println("cache contains $uniqueId")
            var accID = UUID.fromString(getCache().getString("${uniqueId}.${currency.id}.id"));
            var bal = getCache().getLong("${uniqueId}.${currency.id}.bal", 0L);
            acc = Account(accID, user, currency);
            acc.updateBalanceAsync();
            cacheMap.put("${uniqueId}.${currency.id}", acc)
        } else {
            //cache.set();
            val time2 = System.currentTimeMillis();

            val (request, response, result) =  (VulcanEconomy.apiURL + "/player/${uniqueId}/currency/${currency.id}").httpGet().authentication().basic(VulcanEconomy.username, VulcanEconomy.password).responseJson()
            when (result) {
                is Result.Failure-> {
                    val ex = result.getException()
                    println(ex)
                    throw Exception("request failure for getAccount()")
                }
                is Result.Success -> {
                    val data = result.get()
                    data.obj().getString("id");

                    var id = UUID.fromString(data.obj().getString("id"));
                    var balance: BigDecimal = data.obj().getBigDecimal("value");
                   // var balance = balanceStr.toBigDecimal();
                  //  println(data)
                    acc = Account(id, user, currency, balance);
                    cacheMap.put("${uniqueId}.${currency.id}", acc)
                    getCache().set("${uniqueId}.${currency.id}.id", id.toString())
                    saveCache();
                 //   println("getAccountHttp2" + time2.minus(System.currentTimeMillis()))


                }
            }
//            println("getAccountHttp1" + time2.minus(System.currentTimeMillis()))


        }
      //  println(time1)
    //    println(System.currentTimeMillis())
  //      println("getAccountTime" + time1.minus(System.currentTimeMillis()).toLong())

        return acc
    }
    fun getTop(currency: Currency) : List<Account>{
        var accountsTop = ArrayList<Account>();
        val (request, response, result) =  (VulcanEconomy.apiURL + "/accounts/top/${currency.id}/").httpGet().authentication().basic(VulcanEconomy.username, VulcanEconomy.password).responseJson()
        when (result) {
            is Result.Failure-> {
                val ex = result.getException()
                println(ex)

            }
            is Result.Success -> {
                val data = result.get()
              //  println(data.toString())

                val accounts = data.array();
                for (i in 0 until accounts.length()) {
                    val jsonObject = accounts.getJSONObject(i)
                //    println(jsonObject.toString());

                    val id = UUID.fromString(jsonObject.getString("id"));
                    val balance = jsonObject.getBigDecimal("value");
                    val ownerUUID = UUID.fromString(jsonObject.getString("account_owner"));
                  //  println(ownerUUID)
                    val owner = User(ownerUUID);

                    accountsTop.add(Account(id, owner, currency, balance));
                }
               // data.obj().getString("id");

            }
        }
        return accountsTop;
    }
    companion object {
        val topAccounts: List<Account>?
            get() = null

        fun getAccountsByPlayer(player: OfflinePlayer): List<Account>? {
            return null
        }
    }

}
