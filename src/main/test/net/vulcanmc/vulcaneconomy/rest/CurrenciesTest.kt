package net.vulcanmc.vulcaneconomy.rest

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import be.seeseemelk.mockbukkit.entity.PlayerMock
import net.vulcanmc.vulcaneconomy.VulcanEconomy
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import java.util.concurrent.CountDownLatch

class CurrenciesTest {

    var plugin: VulcanEconomy? = null
    var server: ServerMock? = null
    var currency = Currency();
    var player: PlayerMock? = null
   // var acc:Account? =null;
    private val lock = CountDownLatch(1)
    @Before
    fun setUp() {
        server = MockBukkit.mock()
        plugin = MockBukkit.load(VulcanEconomy::class.java) as VulcanEconomy
        player = server!!.addPlayer();
      //  acc =  plugin!!.accounts.getAccount(player!!, currency);
    }

    @After
    fun tearDown() {
        MockBukkit.unload();
    }


    @Test
    fun getDefaultCurrency() {
        val defaultCurrency: Currency = plugin!!.currencies.defaultCurrency;
        assertNotNull(defaultCurrency);
        assertEquals("Dollars", defaultCurrency.name)
    }


    @Test
    fun getCurrencies() {
    }
}