package net.vulcanmc.vulcaneconomy.listeners;

import net.vulcanmc.vulcaneconomy.VulcanEconomy;
import net.vulcanmc.vulcaneconomy.rest.Currency;
import net.vulcanmc.vulcaneconomy.rest.User;
import net.vulcanmc.vulcaneconomy.rest.Users;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    private final VulcanEconomy plugin;

    public PlayerListener(VulcanEconomy plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLogin(AsyncPlayerPreLoginEvent event) {
        User user = Users.getUser(event.getUniqueId());
        //VulcanEconomy.plugin.getLogger().info("User exists");
        if(!user.hasAccount(new Currency()))
        {
            VulcanEconomy.getPlugin().getLogger().info(("Creating account: " + event.getName() + "/" + event.getUniqueId()));
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

     /*   if(VulcanEconomy.getPlugin().getAccountcache().containsKey(user.getId())) {
            VulcanEconomy.getPlugin().getAccountcache().remove(user.getId());
        }
        if(VulcanEconomy.getPlugin().getUsercache().containsKey(event.getPlayer().getUniqueId())) {
            VulcanEconomy.getPlugin().getUsercache().remove(event.getPlayer().getUniqueId());
        }*/

    }
}
