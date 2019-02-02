package de.tageban.nextconomy.commands;

import de.tageban.nextconomy.NextConomy;
import de.tageban.nextconomy.database.Messages;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_Balance implements CommandExecutor {

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


}