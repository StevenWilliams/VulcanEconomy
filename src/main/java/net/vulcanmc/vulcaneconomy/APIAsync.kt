package net.vulcanmc.vulcaneconomy

import net.vulcanmc.vulcaneconomy.rest.Account
import org.bukkit.plugin.java.JavaPlugin

class APIAsync (val plugin : JavaPlugin) {
    fun withdraw(account: Account, amount: Long, message : String, success: Runnable, failure: Runnable) {
        Thread () {
            if (account.withdraw(amount, message, plugin.name)) {
                success.run()
            } else {
                failure.run();
            }
        }.run();
    }
    fun deposit(account: Account, amount: Long, message : String, success: Runnable, failure:
    Runnable) {
        Thread () {
            if (account.deposit(amount, message, plugin.name)) {
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
