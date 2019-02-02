package de.tageban.nextconomy.commands;

import de.tageban.nextconomy.NextConomy;
import de.tageban.nextconomy.database.Config;
import de.tageban.nextconomy.database.Messages;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_Pay implements CommandExecutor {
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
            }/*
            Config config = new Config(plugin.getDataFolder() + "/playerdata", player.getUniqueId().toString());
            if (ssc.getConfig().getBoolean("Toggle.Pay")) {
                player.sendMessage(Messages.PayToggled.getMessage());
                return;
            }*/
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

    private boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }

    }
}
