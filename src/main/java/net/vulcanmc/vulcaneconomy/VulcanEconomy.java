package net.vulcanmc.vulcaneconomy;
import net.vulcanmc.vulcaneconomy.commands.Balance;
import net.vulcanmc.vulcaneconomy.commands.BalanceTop;
import net.vulcanmc.vulcaneconomy.commands.Economy;
import net.vulcanmc.vulcaneconomy.commands.Pay;
import net.vulcanmc.vulcaneconomy.listeners.PlayerListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class VulcanEconomy extends JavaPlugin{
    public static Plugin plugin;

    @Override
    public void onEnable() {
        plugin = this;
        //remember to disable commands in essentials config.
        this.getCommand("balance").setExecutor(new Balance(this));
        this.getCommand("balancetop").setExecutor(new BalanceTop(this));
        this.getCommand("pay").setExecutor(new Pay(this));
        this.getCommand("economy").setExecutor(new Economy(this));
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

    }

    @Override
    public void onDisable() {
        plugin = null;
    }
}
