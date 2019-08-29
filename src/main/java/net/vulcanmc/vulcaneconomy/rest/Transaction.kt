package net.vulcanmc.vulcaneconomy.rest

import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.gson.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.json.responseJson
import kong.unirest.Unirest
import kong.unirest.UnirestException
import net.vulcanmc.vulcaneconomy.VulcanEconomy
import org.json.JSONException
import org.json.JSONObject
import org.json.simple.parser.JSONParser
import java.util.*


/**
 * Created by steven on 23/12/14.
 */
class Transaction(val uuid : UUID? = null, val amount : Long, val creditPlayer : User? = null, val debitPlayer : User? =null, val description : String?, val plugin : String?, val currency : Currency) {
    private var id: UUID? = uuid ?: UUID.randomUUID();
    private var url = VulcanEconomy.getApiURL();

    fun createRequest() {
        val parser = JSONParser()
        var obj = JSONObject()
        obj.put("id", id.toString());
        obj.put("credit_player", creditPlayer?.getID())
        obj.put("credit", amount)

        obj.put("description", description)
        obj.put("plugin", plugin)
        obj.put("server", VulcanEconomy.getServerID().toString())
        obj.put("debit_player", debitPlayer?.getID())

        obj.put("debit", amount)
        obj.put("currency", currency.id.toString())

        println(obj);
        val responseJson = (url + "currency/${currency.id}/transactions").httpPost().authentication().basic(VulcanEconomy.getUsername(), VulcanEconomy.getPassword()).body(obj.toString()).header("Content-Type", "application/json").header("Accept", "application/json").responseJson() { request, response, result ->
            //do something with response
            println("test1");
            result.fold({ d ->
                println("test");
                println(d.obj().toString(2));
                var data = d.obj();
                println(data.toString());
               // println(res.toString())
                //do something with data
            }, { err ->
                val errjson = parser.parse(err.response.toString())
                println(err.message)
                println("error")
                println(errjson.toString())
                //do something with error
            })
        }
//println(responseJson.request.headers)
        responseJson.join();


/*        val request = Unirest.post(url + "currency/${currency.id}/transactions").header("Content-Type", "application/json").header("Accept", "application/json").body(JsonNode(obj.toString()));

        println(request.httpRequest.headers);
//      println(request.asJson().body);

        try {
            val get = request.asJson();
            if(get.status != 200) {
                revertBalance()
            }
            println(get.body)
        } catch (e : JSONException) {

        } catch (e : UnirestException) {

        }


*/

      //  val get = asJsonAsync.get();
        //println(get.body.toString())

    }
    fun revertBalance() {
        if(debitPlayer!= null) {
           var debitAccount = VulcanEconomy.getPlugin().accounts.getAccount(player = debitPlayer!!.offlinePlayer, currency = currency);
            debitAccount!!.balanceDecimal!!.add(amount.toBigDecimal());
            debitAccount!!.getBalance(false);
        }
        else {
            var creditAccount = VulcanEconomy.getPlugin().accounts.getAccount(player = creditPlayer!!.offlinePlayer, currency = currency);
            creditAccount!!.balanceDecimal!!.add(amount.toBigDecimal());
            creditAccount!!.getBalance(false);

        }

    }

    enum class TransactionType private constructor(private val value: String) {
        CREDIT("CREDIT"), DEBIT("DEBIT")
    }
}
