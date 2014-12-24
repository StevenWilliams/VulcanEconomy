package net.vulcanmc.vulcaneconomy;
import net.vulcanmc.vulcaneconomy.commands.*;
import net.vulcanmc.vulcaneconomy.listeners.PlayerListener;
import net.vulcanmc.vulcaneconomy.vault.Economy_VulcanEco;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class VulcanEconomy extends JavaPlugin{
    public static Plugin plugin;
    public static String apiURL;
    public static Integer serverid;
    @Override
    public void onEnable() {
        plugin = this;
        //remember to disable commands in essentials config.
        this.getCommand("balance").setExecutor(new Balance(this));
        this.getCommand("balancetop").setExecutor(new BalanceTop(this));
        this.getCommand("pay").setExecutor(new Pay(this));
        this.getCommand("economy").setExecutor(new Economy(this));
        this.getCommand("transactions").setExecutor(new Transactions(this));
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        this.serverid = this.getConfig().getInt("server-id");
        this.apiURL = this.getConfig().getString("api-url");
        setupVault();
    }

    @Override
    public void onDisable() {
        plugin = null;
    }

    private void setupVault() {
        Plugin vault = getServer().getPluginManager().getPlugin("Vault");

        if (vault == null) {
            return;
        }

        getServer().getServicesManager().register(net.milkbowl.vault.economy.Economy.class, new Economy_VulcanEco(this), this, ServicePriority.Highest);
    }
}
