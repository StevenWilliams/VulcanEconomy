package net.vulcanmc.vulcaneconomy.listeners

import net.vulcanmc.vulcaneconomy.VulcanEconomy
import net.vulcanmc.vulcaneconomy.rest.*
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class PlayerListener(private val plugin: VulcanEconomy) : Listener {

    @EventHandler
    fun onPlayerLogin(event: AsyncPlayerPreLoginEvent) {
        println("playerLogin VulcanEco")

        val user = User(event.uniqueId)
        val acc = user.getAccount(plugin.currencies.defaultCurrency)
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        println("playerJoin VulcanEco")
        val player = event.player
        val user = User(player.uniqueId)
        val acc = user.getAccount(plugin.currencies.defaultCurrency)
        acc!!.updateBalance()
        //send balance on join?
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        println("playerquit VulcanEco")

        plugin.accounts.removeAccountsFromCache(event.player.uniqueId)
    }

}
