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

public class Command_Pay implements CommandExecutor, TabCompleter {
    private NextConomy plugin;

    public Command_Pay(NextConomy plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.onlyPlayers.getMessage());
            return true;
        }
        if (args.length == 1) {
            OfflinePlayer traget = Bukkit.getOfflinePlayer(args[0]);
            if (traget == null) {
                sender.sendMessage(Messages.PlayerNotExist.toString());
                return true;
            }
            sender.sendMessage(Messages.CommandFail.getMessage() + "Pay " + traget.getName() + " <Anzahl>");
            return true;
        }
        if (args.length == 2) {
            Player player = (Player) sender;
            OfflinePlayer traget = Bukkit.getOfflinePlayer(args[0]);
            if (traget == null) {
                player.sendMessage(Messages.PlayerNotExist.toString());
                return true;
            }
            if (traget == player) {
                player.sendMessage(Messages.NotPaySelf.getMessage());
                return true;
            }
            if (!isDouble(args[1])) {
                player.sendMessage(Messages.KeinZahl.getMessage());
                return true;
            }
            if (Double.valueOf(args[1]) < 0.001) {
                player.sendMessage(Messages.MehrGeld.getMessage());
                return true;
            }
            if (plugin.getDatabase().getTogglePay(traget.getUniqueId().toString())) {
                player.sendMessage(Messages.PayToggled.getMessage().replace("{player}", traget.getName()));
                return true;
            }
            if (!plugin.getEconomy().has(player, Double.valueOf(args[1].replaceAll(",", ".")))) {
                player.sendMessage(Messages.NichtGenugGeld.getMessage());
                return true;
            }
            plugin.getEconomy().withdrawPlayer(player, Double.valueOf(args[1].replaceAll(",", ".")));
            player.sendMessage(Messages.PayedMoney.getMessage().replace("{player}", traget.getName()).replace("{balance}", args[1]));
            plugin.getEconomy().depositPlayer(traget, Double.valueOf(args[1].replaceAll(",", ".")));
            if (traget.isOnline()) {
                traget.getPlayer().sendMessage(Messages.GetPayedMoney.getMessage().replace("{player}", player.getName()).replace("{balance}", args[1]));
            }
            return true;
        }
        return false;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> tab = new ArrayList<String>();
        if (args.length == 1) {
            if (args[0].isEmpty()) {
                for (OfflinePlayer all : plugin.getServer().getOfflinePlayers()) {
                    tab.add(all.getName());
                }
            } else {
                for (OfflinePlayer all : plugin.getServer().getOfflinePlayers()) {
                    if (all.getName().startsWith(args[0])) {
                        if (!plugin.getDatabase().getTogglePay(all.getUniqueId().toString())) {
                            tab.add(all.getName());
                        }
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
