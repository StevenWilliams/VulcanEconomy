package net.vulcanmc.vulcaneconomy.commands;

import net.vulcanmc.vulcaneconomy.VulcanEconomy;
import net.vulcanmc.vulcaneconomy.rest.Currency;
import net.vulcanmc.vulcaneconomy.rest.User;
import net.vulcanmc.vulcaneconomy.rest.Users;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

//CODES A MESS. FIX
public class Economy implements CommandExecutor {
    private final VulcanEconomy plugin;

    public Economy(VulcanEconomy plugin) {
        this.plugin = plugin; // Store the plugin in situations where you need it.
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)  {
        /*
        if(args.length >= 3) {
            String subcommand = args[0];
            //target uuid
            UUID uuid = VulcanEconomy.getPlugin().getUUID(args[1]);
            if(uuid == null ) {
                sender.sendMessage(ChatColor.RED + "Null profile lookup for " + args[1]);
                return false;
            }
            if (subcommand.toLowerCase().equals("give")) {
                try {
                    Long amount = Long.valueOf(args[2]);
                    if (amount < 0) {
                        sender.sendMessage(ChatColor.RED + "Amount must be greater than 0!");
                        return false;
                    }
                    User user = Users.getUser(uuid);
                    sender.sendMessage("Giving user " + amount.toString());
                    if (user.getAccount(new Currency()).deposit(amount, "Economy Manage Command: " + sender.getName()) != null) {
                        sender.sendMessage(ChatColor.AQUA + "Successfully set balance of " + user.getOfflinePlayer().getName() + " to " + user.getAccount(new Currency()).getBalance());
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.RED + "Error occurred when trying to set balance.");
                        sender.sendMessage(ChatColor.RED + user.getOfflinePlayer().getName() + "'s balance: " + user.getAccount(new Currency()).getBalance());
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;
            } else if (subcommand.toLowerCase().equals("take")) {
                try {
                    Long amount = Long.valueOf(args[2]);
                    if (amount < 0) {
                        sender.sendMessage(ChatColor.RED + "Amount must be greater than 0!");
                        return false;
                    }
                    User user = Users.getUser(uuid);
                    if (user.getAccount(new Currency()).withdraw(amount, "Economy Manage Command: " + sender.getName()) != null) {
                        sender.sendMessage(ChatColor.AQUA + "Successfully set balance of " + user.getOfflinePlayer().getName() + " to " + user.getAccount(new Currency()).getBalance());
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.RED + "Error occurred when trying to set balance.");
                        sender.sendMessage(ChatColor.RED + user.getOfflinePlayer().getName() + "'s balance: " + user.getAccount(new Currency()).getBalance());
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            } else if (subcommand.toLowerCase().equals("set")) {
                Long targetamount = Long.valueOf(args[2]);
                Long amount = 0L;
                    if (targetamount < 0) {
                        sender.sendMessage(ChatColor.RED + "Amount must be greater than 0!");
                        return false;
                    }

                    User user = Users.getUser(uuid);
                    amount = user.getAccount(new Currency()).getBalance() - targetamount;
                    Long balance = user.getAccount(new Currency()).getBalance();
                    if(targetamount == balance) {
                        sender.sendMessage(ChatColor.AQUA + user.getOfflinePlayer().getName() + "'s balance is already " + balance);
                    }
                    //todo cleanup code!!!
                    if(targetamount < user.getAccount(new Currency()).getBalance()) {
                        //withdraw
                        if(user.getAccount(new Currency()).withdraw(amount, "Economy Manage Command: " + sender.getName()) != null) {
                            sender.sendMessage(ChatColor.AQUA + "Successfully set balance of " + user.getOfflinePlayer().getName() + " to " + user.getAccount(new Currency()).getBalance());
                            return true;
                        } else {
                            sender.sendMessage(ChatColor.RED + "Error occurred when trying to set balance.");
                            sender.sendMessage(ChatColor.RED + user.getOfflinePlayer().getName() + "'s balance: " + user.getAccount(new Currency()).getBalance());
                            return false;
                        }
                    }
                    if (targetamount > user.getAccount(new Currency()).getBalance()) {
                        if(user.getAccount(new Currency()).deposit(-1L * amount, "Economy Manage Command: " + sender.getName()) != null) {
                            sender.sendMessage(ChatColor.AQUA + "Successfully set balance of " + user.getOfflinePlayer().getName() + " to " + user.getAccount(new Currency()).getBalance());
                            return true;
                        } else {
                            sender.sendMessage(ChatColor.RED + "Error occurred when trying to set balance.");
                            sender.sendMessage(ChatColor.RED + user.getOfflinePlayer().getName() + "'s balance: " + user.getAccount(new Currency()).getBalance());
                            return false;
                        }
                    }
            }
        }*/
        return false;
    }

}
