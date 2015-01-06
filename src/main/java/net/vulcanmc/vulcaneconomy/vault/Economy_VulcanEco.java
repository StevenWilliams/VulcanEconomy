package net.vulcanmc.vulcaneconomy.vault;

import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.vulcanmc.vulcaneconomy.VulcanEconomy;
import net.vulcanmc.vulcaneconomy.rest.Account;
import net.vulcanmc.vulcaneconomy.rest.Currency;
import net.vulcanmc.vulcaneconomy.rest.User;
import net.vulcanmc.vulcaneconomy.rest.Users;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Economy_VulcanEco  extends AbstractEconomy {

    private static final Logger log = Logger.getLogger("Minecraft");
    private final String name = "VulcanEconomy";
    private VulcanEconomy vulcaneco = null;
    private Plugin plugin = null;
    private Currency currency = null;

    public Economy_VulcanEco(Plugin plugin) {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(new EconomyServerListener(this), plugin);

        // Load Plugin in case it was loaded before
        if (vulcaneco == null) {
            Plugin essentials = plugin.getServer().getPluginManager().getPlugin("VulcanEconomy");
            if (essentials != null && essentials.isEnabled()) {
                vulcaneco = (VulcanEconomy) essentials;
                log.info(String.format("[%s][Economy] %s hooked.", plugin.getDescription().getName(), name));
            }
        }
        //add dynamic currencies later
        currency = new Currency();
    }

    @Override
    public boolean isEnabled() {
        if (vulcaneco == null) {
            return false;
        } else {
            return vulcaneco.isEnabled();
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

//continue here
    @Override
    public String format(double v) {
        return null;
    }

    @Override
    public String currencyNamePlural() {
        return currency.getNamePlural();
    }

    @Override
    public String currencyNameSingular() {
        return currency.getNameSingle();
    }

    @Override
    public boolean hasAccount(String playername) {
        /*
        try {

            UUID uuid = VulcanEconomy.plugin.getUUID(playername);
            if (uuid == null) {
                return true;
                //prevent essentials from handling...
            }
            if (Users.userExists(profile.getUniqueId()) && Users.getUser(profile.getUniqueId()).hasAccount(new Currency())) {
                   return true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //prevents essentials from handling
        return true;
        //return false;
    }

    @Override
    public boolean hasAccount(String playername, String worldname) {
        return hasAccount(playername);
    }

    @Override
    public double getBalance(String playername) {

            /*
            Profile profile = VulcanEconomy.resolver.findByName(playername);
            if (profile == null) {
                if(Bukkit.getPlayer(playername) != null) {
                    Player player = Bukkit.getPlayer(playername);
                    return Users.getUser(player).getAccount(currency).getBalance();
                } else {
                    return Users.getUser(Bukkit.getOfflinePlayer(playername)).getAccount(currency).getBalance();
                }
                //return -1;
            }*/
            User user = Users.getUser(VulcanEconomy.plugin.getUUID(playername));
            if (user == null) {
                return -1;
            }
            if (user.hasAccount(currency)) {
                    return user.getAccount(currency).getBalance();
            }
        return -1;
    }
    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String worldName) {
        return getBalance(offlinePlayer);
    }
    /*
    @Override
    public double getBalance(OfflinePlayer player) {
        UUID uuid = player.getUniqueId();
        if (Users.userExists(uuid) && Users.getUser(uuid).hasAccount(currency)) {
            return Users.getUser(uuid).getAccount(currency).getBalance();
        }
        return -1;
    }*/
    @Override
    public double getBalance(String playername, String worldname) {
        return getBalance(playername);
    }

    @Override
    public boolean has(String playername, double amount) {
        return getBalance(playername) >= amount;
    }

    @Override
    public boolean has(String playername, String worldname, double amount) {
        return has(playername, amount);
    }



    @Override
    public EconomyResponse withdrawPlayer(String playername, double amount) {
        return withdraw(playername, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playername, String world, double amount) {
        return withdraw(playername, amount);
    }



    @Override
    public EconomyResponse depositPlayer(String playername, double amount) {
        return deposit(playername, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playername, String world, double amount) {
        return deposit(playername, amount);
    }


    @Override
    public boolean createPlayerAccount(String playername) {
        if(!Users.getUser(VulcanEconomy.plugin.getUUID(playername)).hasAccount(new Currency())) {
            if (Users.getUser(VulcanEconomy.plugin.getUUID(playername)).createAccount(new Currency()) != null){
                return true;
              }
        }
        return false;
    }

    @Override
    public boolean createPlayerAccount(String playername, String world) {
        return createPlayerAccount(playername);
    }



    private EconomyResponse deposit(String playername, double amount) {
        if (amount < 0) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Cannot deposit negative funds");
        }

        Account account = Users.getUser(VulcanEconomy.plugin.getUUID(playername)).getAccount(currency);

        if (account == null) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Account doesn't exist");
        }
        Long roundedamount = Math.round(amount);

        account.deposit(roundedamount, "VaultAPI deposit");

        return new EconomyResponse(roundedamount, account.getBalance(), EconomyResponse.ResponseType.SUCCESS, "");
    }


    private EconomyResponse withdraw(String playername, double amount) {
        if (amount < 0) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Cannot withdraw negative funds");
        }

        Account account = Users.getUser(Bukkit.getOfflinePlayer(playername)).getAccount(currency);

        if (account == null) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Account doesn't exist");
        }

        Long roundedamount = Math.round(amount);

        if (account.has(roundedamount)) {
            account.withdraw(roundedamount, "VaultAPI withdrawal");
            return new EconomyResponse(amount, account.getBalance(), EconomyResponse.ResponseType.SUCCESS, "");
        } else {
            return new EconomyResponse(0, account.getBalance(), EconomyResponse.ResponseType.FAILURE, "Insufficient funds");
        }
    }

    //Ignore
    @Override
    public EconomyResponse createBank(String s, String s2) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "VulcanEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "VulcanEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "VulcanEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "VulcanEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "VulcanEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "VulcanEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s2) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "VulcanEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse isBankMember(String s, String s2) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "VulcanEconomy does not support bank accounts!");
    }

    @Override
    public List<String> getBanks() {
        return new ArrayList<String>();
    }
//upto here



    public class EconomyServerListener implements Listener {
        Economy_VulcanEco economy = null;

        public EconomyServerListener(Economy_VulcanEco economy) {
            this.economy = economy;
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPluginEnable(PluginEnableEvent event) {
            if (economy.vulcaneco == null) {
                Plugin vulcanecoplugin = event.getPlugin();

                if (vulcanecoplugin.getDescription().getName().equals("VulcanEconomy")) {
                    economy.vulcaneco = (VulcanEconomy) vulcanecoplugin;
                    log.info(String.format("[%s][Economy] %s hooked.", plugin.getDescription().getName(), economy.name));
                }
            }
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPluginDisable(PluginDisableEvent event) {
            if (economy.vulcaneco != null) {
                if (event.getPlugin().getDescription().getName().equals("VulcanEconomy")) {
                    economy.vulcaneco = null;
                    log.info(String.format("[%s][Economy] %s unhooked.", plugin.getDescription().getName(), economy.name));
                }
            }
        }
    }

}
