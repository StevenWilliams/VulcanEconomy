package net.vulcanmc.vulcaneconomy.listeners;

import net.vulcanmc.vulcaneconomy.VulcanEconomy;
import net.vulcanmc.vulcaneconomy.rest.Accounts;
import net.vulcanmc.vulcaneconomy.rest.Currency;
import net.vulcanmc.vulcaneconomy.rest.Users;
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

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if(!Users.userExists(player.getUniqueId().toString())) {
            Users.createUser(player.getUniqueId().toString(), player.getName());
        } else {
            VulcanEconomy.plugin.getLogger().info("User exists");
        }
        if(!Users.getUser(player).hasAccount(new Currency()))
        {
            Users.getUser(player).createAccount(new Currency());
        } else {
            VulcanEconomy.plugin.getLogger().info("User has account");
        }
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
