package net.vulcanmc.vulcaneconomy.rest

import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.core.requests.CancellableRequest
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import net.vulcanmc.vulcaneconomy.VulcanEconomy
import java.math.BigDecimal
import java.util.*


class Account(val id: UUID, val owner: User?, val currency: Currency, value: BigDecimal? = null) {
    constructor(id: UUID, owner: User?, currency: Currency, value: Long) : this(id, owner, currency, BigDecimal.valueOf(value))

    var balanceDecimal: BigDecimal? = null

    init {
        if (value == null) {
            this.balanceDecimal = BigDecimal(0)
            this.updateBalanceAsync()
        } else {
            this.balanceDecimal = value
        }

    }

    //returns latest transactions
    fun getTransactions(quantity: Int): ArrayList<TransactionModel?> {
        val transactionList = ArrayList<TransactionModel?>()
        val (request, response, result) = (VulcanEconomy.apiURL + "/account/${id}/transactions/$quantity").httpGet().authentication().basic(VulcanEconomy.username, VulcanEconomy.password).responseJson()
        when (result) {
            is Result.Failure -> {
                val ex = result.getException()
                println(ex)

            }

            is Result.Success -> {
                // println(result.get().obj().toString(2));
                val transactions = result.get().obj().getJSONArray("transactions")
                //  println(data.toString())

                for (i in 0 until transactions.length()) {
                    try {
                        val jsonObject = transactions.getJSONObject(i)
                        //    println(jsonObject.toString());

                        val id = UUID.fromString(jsonObject.getString("id"))
                        val amount = jsonObject.getBigDecimal("debit_amount")
                        val currency = Currency(UUID.fromString(jsonObject.getString("currency")))

                        val debitPlayer = if (jsonObject.has("debit_account") && !jsonObject.isNull("debit_account")) Account(UUID.fromString(jsonObject.getString("debit_account")), null, currency, 0) else null
                        val creditPlayer = if (jsonObject.has("credit_account") && !jsonObject.isNull("credit_account")) Account(UUID.fromString(jsonObject.getString("credit_account")), null, currency, 0) else null
                        val desc = if (jsonObject.has("description") && !jsonObject.isNull("description")) jsonObject.getString("description") else null
                        val plugin = if (jsonObject.has("plugin") && !jsonObject.isNull("plugin")) jsonObject.getString("plugin") else null
                        val time = jsonObject.getString("transaction_time")
                        val transaction: TransactionModel = TransactionModel(id, amount.toLong(), creditPlayer, debitPlayer, desc, plugin, currency, time)
                        //var transactionTime = jsonObject.getString("transaction_time")
                        //transactionTime = transactionTime.substring(0, transactionTime.length - 3);
                        transactionList.add(transaction)

                    } catch (e: Exception) {
                        transactionList.add(null)
                    }

                    // data.obj().getString("id");

                }
            }
        }
        return transactionList
    }


    fun transferTo(currency: Currency, account: Account, amount: Long, description: String, plugin: String) {
        balanceDecimal = balanceDecimal?.subtract(BigDecimal(amount))
        account.balanceDecimal = account.balanceDecimal?.add(BigDecimal(amount))
        val transaction = Transaction(currency = currency, amount = amount, description = description, plugin = plugin, creditPlayer = this.owner, debitPlayer = account.owner)
        transaction.createRequest()
        updateBalanceAsync()
    }

    //todo: insead of submitting a request for every transaction, group them and process them every 5 minutes (or 10 transactions, whichever comes first)
    fun createTransaction(type: Transaction.TransactionType, currency: Currency, amount: Long, description: String, plugin: String?, queue: Boolean): Transaction? {

        if (type == Transaction.TransactionType.CREDIT) {
            return Transaction(currency = currency, amount = amount, description = description, plugin = plugin, creditPlayer = this.owner)

        } else if (type == Transaction.TransactionType.DEBIT) {
            return Transaction(currency = currency, amount = amount, description = description, plugin = plugin, debitPlayer = this.owner)


        }
        return null
    }


    @JvmOverloads
    fun withdraw(amount: Long, description: String = "No description", plugin: String = ""): Boolean {
        val time1 = System.currentTimeMillis()
        if (has(amount)) {
            var success = createTransaction(Transaction.TransactionType.CREDIT, currency, amount, description, plugin, true)?.createRequest()
            println("withdraw: " + System.currentTimeMillis().minus(time1))
            if (success == null) return false
            return success
        } else {
            println("withdraw: " + System.currentTimeMillis().minus(time1))
            //todo: check null checks
            return false
        }

    }


    @JvmOverloads
    fun withdrawAsync(amount: Long, description: String = "No description"): Transaction? {
        return null
        //todo
    }


    fun deposit(amount: Long, description: String): Boolean {
        return deposit(amount, description, "")
    }


    fun deposit(amount: Long, description: String = "No description", plugin: String = ""): Boolean {
        var result = createTransaction(Transaction.TransactionType.DEBIT, currency, amount, description, plugin, true)?.createRequest()
        if (result == null) return false
        return result
    }

    fun has(amount: Long): Boolean {
        return this.balanceDecimal!!.compareTo(BigDecimal.valueOf(amount)) >= 0
    }

    fun getBalance(): BigDecimal {
        return getBalance(true)
    }

    fun getBalance(useCache: Boolean = true): BigDecimal {
        //   println("getBalance : " + this.balanceDecimal)

        if (useCache) {
            if (this.balanceDecimal!!.equals(BigDecimal(0))) {
                updateBalance()
            }
            return this.balanceDecimal!!
        } else {
            updateBalance()
            return this.balanceDecimal!!
        }
    }

    fun updateBalance() {
        updateBalanceAsync().join()
        //println("updateBalance: " + this.balanceDecimal)
    }


    fun updateBalanceAsync(): CancellableRequest {
        var httpAsync = (VulcanEconomy.apiURL + "/player/${owner!!.getID()}/currency/${currency.id}").httpGet().authentication().basic(VulcanEconomy.username, VulcanEconomy.password).responseJson { request, response, result ->
            //do something with response
            // println("test1");
            result.fold({ d ->
                // println("test");
                //   println(d.obj().toString(2));
                var response = d.obj()
                val bigDecimal = response.getBigDecimal("value")
                this.balanceDecimal = bigDecimal
                //  println("updated $balanceDecimal");
                //do something with data
            }, { err ->
                println(err.message)
                //do something with error
            })


        }
        return httpAsync

    }
}
