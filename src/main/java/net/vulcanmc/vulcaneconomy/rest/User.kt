package net.vulcanmc.vulcaneconomy.rest

import net.vulcanmc.vulcaneconomy.VulcanEconomy
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import java.math.BigDecimal
import java.util.UUID

class User(private val uuid: UUID) {
    val player: OfflinePlayer = VulcanEconomy.plugin!!.server.getOfflinePlayer(uuid);
    private var account: Account? = null;
    private var id : UUID? = null;

    val accounts: List<Account>?
        get() = Accounts.getAccountsByPlayer(player)
    val offlinePlayer: OfflinePlayer
        get() = Bukkit.getOfflinePlayer(this.uuid)

    init {
        id = uuid;
    }

    fun getAccount(currency: Currency): Account? {
        return VulcanEconomy.plugin!!.accounts!!.getAccount(player, currency)

    }

    fun createAccount(currency: Currency): Account? {
        return getAccount(currency);
    }

    fun hasAccount(currency: Currency): Boolean {
        return (getAccount(currency) != null);
    }

    fun getID(): UUID? {
        return this.id;
    }
}
