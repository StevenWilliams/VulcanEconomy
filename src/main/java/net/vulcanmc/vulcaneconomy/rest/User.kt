package net.vulcanmc.vulcaneconomy.rest

import kong.unirest.Unirest
import net.vulcanmc.vulcaneconomy.VulcanEconomy
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import java.math.BigDecimal
import java.util.UUID

class User(private val uuid: UUID) {
    private val player: OfflinePlayer = Bukkit.getOfflinePlayer(uuid);
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
        if(account != null) return account;

        //JsonNode response = Unirest.post(VulcanEconomy.getApiURL() + "player/" + uuid+ "/" +).basicAuth(VulcanEconomy.getPlugin().getApiUser(), VulcanEconomy.getPlugin().getApiPass()).queryString("accountOwner", playerid).queryString("server", VulcanEconomy.getServerid()).queryString("currency", new Currency().getNameSingle()).asJson().getBody();
        val response = Unirest.get(VulcanEconomy.getApiURL() + "player/$uuid/currency/$currency/").asJson().body;
        val accountID = UUID.fromString(response.`object`.getString("id"));
        val valueStr = response.`object`.getString("value");
        val value = BigDecimal(valueStr);
        val account = Account(accountID, this, currency, value);
        return account;
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
