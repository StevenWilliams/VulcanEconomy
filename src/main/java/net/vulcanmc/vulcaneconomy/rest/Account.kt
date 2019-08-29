package net.vulcanmc.vulcaneconomy.rest

import com.github.kittinunf.fuel.core.awaitResponse
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.core.requests.CancellableRequest
import com.github.kittinunf.fuel.coroutines.awaitResponse
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.json.responseJson

import kotlinx.coroutines.delay

import net.vulcanmc.vulcaneconomy.VulcanEconomy
import org.json.JSONArray
import org.json.JSONObject

import java.math.BigDecimal
import java.util.UUID


class Account(val id: UUID, val owner: User, val currency: Currency, value: BigDecimal? = null) {
    constructor(id: UUID, owner: User, currency: Currency, value: Long) : this(id, owner, currency, BigDecimal.valueOf(value))
    //constructor(id: UUID?, user: User, currency: Currency, balance: Int) : this(id, user, currency, BigDecimal.valueOf(balance.toLong()))
    //constructor(id: UUID, owner: User, currency: Currency) : this()

    var balanceDecimal: BigDecimal? = null
        private set
   /* val balance: Long
        get() = balanceDecimal!!.toLong()*/
    val transactions: JSONArray?
        get() = null

    val isActive: Boolean
        get() = true

    init {
        if (value == null) {
            this.balanceDecimal = BigDecimal(0);
            this.updateBalanceAsync();
        } else {
            this.balanceDecimal = value
        }
    }

    fun createTransaction(type: Transaction.TransactionType, currency: Currency, amount: Long, description: String, plugin: String): Transaction? {
        if (type == Transaction.TransactionType.CREDIT) {
            return Transaction(currency = currency, amount = amount, description = description, plugin = plugin, creditPlayer = this.owner)
        } else {
            return Transaction(currency = currency, amount = amount, description = description, plugin = plugin, debitPlayer = this.owner)

        }
        // return null;
    }

    fun transferTo(currency: Currency, account: Account, amount: Long, description: String, plugin: String){
        balanceDecimal = balanceDecimal?.subtract(BigDecimal(amount))
        account.balanceDecimal = account.balanceDecimal?.add(BigDecimal(amount))
        val transaction = Transaction(currency = currency, amount = amount, description = description, plugin = plugin, creditPlayer = this.owner, debitPlayer = account.owner)
        transaction.createRequest()
    }

    //todo: insead of submitting a request for every transaction, group them and process them every 5 minutes (or 10 transactions, whichever comes first)
    fun createTransaction(type: Transaction.TransactionType, currency: Currency, amount: Long, description: String, plugin: String?, queue: Boolean): Transaction? {
        val obj = JSONObject()

        if (type == Transaction.TransactionType.CREDIT) {
            return Transaction(currency = currency, amount = amount, description = description, plugin = plugin, creditPlayer = this.owner);


        } else if (type == Transaction.TransactionType.DEBIT) {
            return Transaction(currency = currency, amount = amount, description = description, plugin = plugin, debitPlayer = this.owner);


        }

        obj.put("description", description)
        obj.put("plugin", plugin)
        obj.put("server", VulcanEconomy.getServerID())
        obj.put("currency", currency.id)
        //val request = Request(VulcanEconomy.getApiURL() + "accounts/" + this.id + "/transactions", "POST", obj)

        return null
    }

    @JvmOverloads
    fun withdraw(amount: Long, description: String = "No description"): Transaction? {
        val time1 = System.currentTimeMillis()
        if (has(amount)) {
            val transaction = createTransaction(Transaction.TransactionType.CREDIT, currency, amount, description, null, true)
            if (transaction != null) {
                balanceDecimal = balanceDecimal!!.subtract(BigDecimal.valueOf(amount))
                transaction.createRequest();
                //transaction.execute()
            }
        }
        println("withdraw: " + System.currentTimeMillis().minus( time1))
        //todo: check null checks
        return null
    }


    @JvmOverloads
    fun withdrawAsync(amount: Long, description: String = "No description"): Transaction? {
        return null
        //todo
    }



    @JvmOverloads
    fun deposit(amount: Long, description: String = "No description"): Boolean {
        val transaction = createTransaction(Transaction.TransactionType.DEBIT, currency, amount, description, null, true)
        if (transaction != null) {
            balanceDecimal = balanceDecimal!!.add(BigDecimal.valueOf(amount))
            transaction.createRequest();
            // transaction.execute()
        }
        return true
    }

    fun has(amount: Long): Boolean {
        return this.balanceDecimal!!.compareTo(BigDecimal.valueOf(amount)) >= 0
    }

    fun getBalance(): BigDecimal {
        return getBalance(true);
    }
    fun getBalance(useCache: Boolean = true) : BigDecimal {
        println("getBalance : " + this.balanceDecimal)

        if (useCache) {
            if(this.balanceDecimal!!.equals(BigDecimal(0))) {
                updateBalance();
            }
            return this!!.balanceDecimal!!;
        } else {
            updateBalance();
            return this!!.balanceDecimal!!;
        }
    }

    fun updateBalance() {
        updateBalanceAsync().join();
        println("updateBalance: " + this.balanceDecimal)
    }

    fun updateBalanceAsync(): CancellableRequest {
        var httpAsync =( VulcanEconomy.getApiURL() + "/player/${owner.getID()}/currency/${currency.id}").httpGet().authentication().basic(VulcanEconomy.getUsername(), VulcanEconomy.getPassword()).responseJson() { request, response, result ->
            //do something with response
           // println("test1");
            result.fold({ d ->
               // println("test");
             //   println(d.obj().toString(2));
                var response = d.obj();
                val bigDecimal = response.getBigDecimal("value");
                this.balanceDecimal = bigDecimal
                println("updated $balanceDecimal");
                //do something with data
            }, { err ->
                println(err.message)
                //do something with error
            })


        }
        return httpAsync;

    }
}
