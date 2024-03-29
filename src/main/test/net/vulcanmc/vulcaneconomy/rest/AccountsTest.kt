package net.vulcanmc.vulcaneconomy.rest

import org.junit.After
import org.junit.Before
import org.junit.Test

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import be.seeseemelk.mockbukkit.command.ConsoleCommandSenderMock
import be.seeseemelk.mockbukkit.entity.PlayerMock
import net.vulcanmc.vulcaneconomy.VulcanEconomy
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class AccountsTest {

    var plugin: VulcanEconomy? = null
    var server: ServerMock? = null
    var currency = Currency();
    var player: PlayerMock? = null
    var acc:Account? =null;
    private val lock = CountDownLatch(1)
    @Before
    fun setUp() {
        server = MockBukkit.mock()
        plugin = MockBukkit.load(VulcanEconomy::class.java) as VulcanEconomy
       player = server!!.addPlayer();
        acc =  plugin!!.accounts!!.getAccount(player!!, currency);
    }

    @After
    fun tearDown() {
        MockBukkit.unload();
    }

    @Test
    fun getAccount() {
        acc!!.deposit(10, "test");
    }
    @Test
    fun getBalanceAsync() {
        val updateBalanceAsync = acc!!.updateBalanceAsync();

        lock.await(4000, TimeUnit.MILLISECONDS);
        println(updateBalanceAsync.get().body());
    }

    @Test
    fun getTransactions() {
      //  val user : User = User(UUID.fromString("5980eae2-ad6a-489b-a398-66322f1cca8e"));
        val acc = plugin!!.accounts!!.getAccount(PlayerMock(server, "puppy3276",UUID.fromString("5980eae2-ad6a-489b-a398-66322f1cca8e")), plugin!!.currencies.defaultCurrency)
        val transactions: ArrayList<TransactionModel?> = acc!!.getTransactions(10);
        println(transactions.size)
        for(transaction in transactions) {
            println(transaction!!.amount)
        }
    }
    @Test
    fun getTop() {
        val top = plugin!!.accounts!!.getTop(plugin!!.currencies.defaultCurrency);

    }
    @Test
    fun ecoSetCommand() {

        ConsoleCommandSenderMock()
    }
}