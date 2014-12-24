package net.vulcanmc.vulcaneconomy.listeners;

import net.vulcanmc.vulcaneconomy.VulcanEconomy;
import net.vulcanmc.vulcaneconomy.rest.Accounts;
import net.vulcanmc.vulcaneconomy.rest.Currency;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    private final VulcanEconomy plugin;

    public PlayerListener(VulcanEconomy plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        //create account
        Accounts.createAccount(player, new Currency());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        //send balance on join?
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        /*
        Database database = plugin.getFeDatabase();

        Player player = event.getPlayer();

        Account account = database.getCachedAccount(player.getName(), player.getUniqueId().toString());

        if (account != null) {
            account.save(account.getMoney());

            database.removeCachedAccount(account);
        }
        */
    }
}
