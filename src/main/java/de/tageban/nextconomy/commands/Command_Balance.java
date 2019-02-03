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

public class Command_Balance implements CommandExecutor, TabCompleter {

    private final NextConomy plugin;

    public Command_Balance(NextConomy plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && sender.hasPermission("NextConomy.Command.BalanceOther")) {
            OfflinePlayer traget = Bukkit.getOfflinePlayer(args[0]);
            if (traget == null) {
                sender.sendMessage(Messages.PlayerNotExist.toString());
                return true;
            }
            sender.sendMessage(Messages.BalanceOther.getMessage().replace("{player}", traget.getName()).replace("{balance}", plugin.getEconomy().format(plugin.getEconomy().getBalance(traget))));
            return true;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage(Messages.Balance.getMessage().replace("{balance}", plugin.getEconomy().format(plugin.getEconomy().getBalance(player))));
            return true;
        }
        return false;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> tab = new ArrayList<String>();
        if (sender.hasPermission("NextConomy.Command.BalanceOther")) {
            if (args.length == 1) {
                if (args[0].isEmpty()) {
                    for (Player all : plugin.getServer().getOnlinePlayers()) {
                        tab.add(all.getName());
                    }
                } else {
                    for (Player all : plugin.getServer().getOnlinePlayers()) {
                        if (all.getName().startsWith(args[0])) {
                            tab.add(all.getName());
                        }
                    }
                }
            }
        }
        Collections.sort(tab);
        return tab;
    }
}