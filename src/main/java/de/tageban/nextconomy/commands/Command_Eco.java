package de.tageban.nextconomy.commands;

import de.tageban.nextconomy.NextConomy;
import de.tageban.nextconomy.database.Messages;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Command_Eco implements CommandExecutor, TabCompleter {

    private final NextConomy plugin;

    public Command_Eco(NextConomy plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("Add")) {
                sender.sendMessage(Messages.CommandFail.getMessage() + "Eco Add <Player> <Amount>");
                return true;
            }
            if (args[0].equalsIgnoreCase("Remove")) {
                sender.sendMessage(Messages.CommandFail.getMessage() + "Eco Remove <Player> <Amount>");
                return true;
            }
            if (args[0].equalsIgnoreCase("Reset")) {
                sender.sendMessage(Messages.CommandFail.getMessage() + "Eco Reset <Player/*> ");
                return true;
            }
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("Add")) {
                OfflinePlayer traget = Bukkit.getOfflinePlayer(args[1]);
                if (traget == null) {
                    sender.sendMessage(Messages.PlayerNotExist.toString());
                    return true;
                }
                sender.sendMessage(Messages.CommandFail.getMessage() + "Eco Add " + traget.getName() + " <Amount>");
                return true;
            }
            if (args[0].equalsIgnoreCase("Remove")) {
                OfflinePlayer traget = Bukkit.getOfflinePlayer(args[1]);
                if (traget == null) {
                    sender.sendMessage(Messages.PlayerNotExist.toString());
                    return true;
                }
                sender.sendMessage(Messages.CommandFail.getMessage() + "Eco Remove " + traget.getName() + " <Amount>");
                return true;
            }
            if (args[0].equalsIgnoreCase("Reset")) {
                if (args[1].equalsIgnoreCase("*") || args[1].equalsIgnoreCase("All") || args[1].equalsIgnoreCase("Alle")) {
                    plugin.getDatabase().Clear();
                    sender.sendMessage(Messages.Reset.getMessage());
                    return true;
                }
                OfflinePlayer traget = Bukkit.getOfflinePlayer(args[1]);
                if (traget == null) {
                    sender.sendMessage(Messages.PlayerNotExist.toString());
                    return true;
                }
                plugin.getDatabase().setBalance(traget.getUniqueId().toString(), plugin.getStartBalance());
                sender.sendMessage(Messages.ResetPlayer.getMessage().replace("{player}", traget.getName()));
                return true;
            }
        }

        if (args.length == 3) {
            OfflinePlayer traget = Bukkit.getOfflinePlayer(args[1]);
            if (traget == null) {
                sender.sendMessage(Messages.PlayerNotExist.toString());
                return true;
            }
            if (!isDouble(args[2])) {
                sender.sendMessage(Messages.KeinZahl.toString());
                return true;
            }
            double amount = Double.valueOf(args[2]);
            if (args[0].equalsIgnoreCase("Add")) {
                plugin.getEconomy().depositPlayer(traget, amount);
                sender.sendMessage(Messages.AddMoney.getMessage().replace("{player}", traget.getName()).replace("{amount}", String.valueOf(amount)));
                return true;
            }
            if (args[0].equalsIgnoreCase("Remove")) {
                plugin.getEconomy().withdrawPlayer(traget, amount);
                sender.sendMessage(Messages.RemoveMoney.getMessage().replace("{player}", traget.getName()).replace("{amount}", String.valueOf(amount)));
                return true;
            }
        }


        return false;
    }


    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> tab = new ArrayList<String>();

        List<String> ags1 = new ArrayList<String>();
        ags1.add("Add");
        ags1.add("Remove");
        ags1.add("Reset");

        if (args.length == 1) {
            if (args[0].isEmpty()) {
                for (String all : ags1) {
                    tab.add(all);
                }
            } else {
                for (String all : ags1) {
                    if (all.startsWith(args[0])) {
                        tab.add(all);
                    }
                }
            }
        }

        if (args.length == 2) {
            if (args[1].isEmpty()) {
                for (Player all : plugin.getServer().getOnlinePlayers()) {
                    tab.add(all.getName());
                }
            } else {
                for (Player all : plugin.getServer().getOnlinePlayers()) {
                    if (all.getName().startsWith(args[1])) {
                        tab.add(all.getName());
                    }
                }
            }
        }

        Collections.sort(tab);
        return tab;
    }

    private boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }

    }
}
