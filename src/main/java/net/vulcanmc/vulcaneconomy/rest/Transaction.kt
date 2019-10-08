package net.vulcanmc.vulcaneconomy.rest

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.gson.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import kong.unirest.Unirest
import kong.unirest.UnirestException
import net.vulcanmc.vulcaneconomy.VulcanEconomy
import org.joda.time.DateTime
import org.json.JSONException
import org.json.JSONObject
import org.json.simple.parser.JSONParser
import java.math.BigDecimal
import java.time.DateTimeException
import java.util.*


/**
 * Created by steven on 23/12/14.
 */
class Transaction(val uuid : UUID? = null, val amount : Long, val creditPlayer : User? = null, val debitPlayer : User? =null, val description : String?, val plugin : String?, val currency : Currency) {
    private var id: UUID? = uuid ?: UUID.randomUUID();
    private var url = VulcanEconomy.apiURL;
     var time = DateTime(System.currentTimeMillis()).toString()


    fun createRequest() : Boolean {
        val parser = JSONParser()
        var obj = JSONObject()
        obj.put("id", id.toString());
        obj.put("credit_player", creditPlayer?.getID())
        obj.put("credit", amount)

        obj.put("description", description)
        obj.put("plugin", plugin)
        obj.put("server", VulcanEconomy.serverID.toString())
        obj.put("debit_player", debitPlayer?.getID())

        obj.put("debit", amount)
        obj.put("currency", currency.id.toString())

        println(obj);
        val (request, response, result) = (url + "currency/${currency.id}/transactions").httpPost().authentication().basic(VulcanEconomy.username, VulcanEconomy.password).body(obj.toString()).header("Content-Type", "application/json").header("Accept", "application/json").responseJson()
        when (result) {
            is Result.Failure -> {
                val ex = result.getException()
                println(ex.message);
                println(ex.response);
                println(ex.stackTrace);
                return false;
            }
            is Result.Success -> {
                val data = result.get()

                if (response.statusCode == 200) {
                   this.updateBalance();
                    return true;
                } else {
                    println(data.obj().toString(2));
                    return false;
                }
            }
        }


    }
    private fun updateBalance() {
        if(debitPlayer!= null) {
            var debitAccount = VulcanEconomy.plugin!!.accounts!!.getAccount(player = debitPlayer!!.offlinePlayer, currency = currency);
            debitAccount!!.balanceDecimal = debitAccount!!.balanceDecimal!!.add(amount.toBigDecimal());
          //  debitAccount!!.getBalance(false);
        }
        if(creditPlayer != null) {
            var creditAccount = VulcanEconomy.plugin!!.accounts!!.getAccount(player = creditPlayer!!.offlinePlayer, currency = currency);
            creditAccount!!.balanceDecimal!!.subtract(amount.toBigDecimal());
           // creditAccount!!.getBalance(false);

        }
    }


    enum class TransactionType private constructor(private val value: String) {
        CREDIT("CREDIT"), DEBIT("DEBIT")
    }
}
