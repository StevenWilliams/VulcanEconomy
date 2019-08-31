package net.vulcanmc.vulcaneconomy.rest

import java.util.*

class TransactionModel(val uuid : UUID? = null, val amount : Long, val creditPlayer : Account? = null, val debitPlayer : Account? =null, val description : String?, val plugin : String?, val currency : Currency, val time : String) {

}