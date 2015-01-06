package net.vulcanmc.vulcaneconomy.listeners;

import net.vulcanmc.vulcaneconomy.VulcanEconomy;
import net.vulcanmc.vulcaneconomy.rest.Currency;
import net.vulcanmc.vulcaneconomy.rest.User;
import net.vulcanmc.vulcaneconomy.rest.Users;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    private final VulcanEconomy plugin;

    public PlayerListener(VulcanEconomy plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if(!Users.userExists(player.getUniqueId())) {
            VulcanEconomy.plugin.getLogger().info(("Creating user: " + player.getName() + "/" + player.getUniqueId()));
            Users.createUser(player.getUniqueId(), player.getName());
        } else {
            //VulcanEconomy.plugin.getLogger().info("User exists");
        }
        User user = Users.getUser(player);

        if(!user.hasAccount(new Currency()))
        {
            VulcanEconomy.plugin.getLogger().info(("Creating account: " + player.getName() + "/" + player.getUniqueId()));
            user.createAccount(new Currency());
        } else {
            //VulcanEconomy.plugin.getLogger().info("User has account");
        }
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        //send balance on join?
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        //also remove other keys (account and balance)
        User user = Users.getUser(event.getPlayer().getUniqueId());
        if(VulcanEconomy.plugin.accountcache.containsKey(user.getId())) {
            VulcanEconomy.plugin.accountcache.remove(user.getId());
        }
        if(VulcanEconomy.plugin.usercache.containsKey(event.getPlayer().getUniqueId())) {
            VulcanEconomy.plugin.usercache.remove(event.getPlayer().getUniqueId());
        }

    }
}
