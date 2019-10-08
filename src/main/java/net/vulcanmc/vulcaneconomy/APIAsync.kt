package net.vulcanmc.vulcaneconomy

import net.vulcanmc.vulcaneconomy.rest.Account

class APIAsync {
    fun withdraw(account: Account, amount: Long, success: Runnable, failure: Runnable) {
        Thread () {
            if (account.withdraw(amount)) {
                success.run()
            } else {
                failure.run();
            }
        }.run();
    }
    fun deposit(account: Account, amount: Long, message : String, success: Runnable, failure:
    Runnable) {
        Thread () {
            if (account.deposit(amount)) {
                success.run()
            } else {
                failure.run();
            }
        }.run();
    }
    fun has(account : Account, amount:Long, hasAmount : Runnable, doesNot : Runnable) {
        Thread() {
            if(account.has(amount)) {
                hasAmount.run();
            } else {
                doesNot.run();
            }
        }
    }
}
