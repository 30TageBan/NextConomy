package de.tageban.nextconomy.commands;

import de.tageban.nextconomy.NextConomy;
import de.tageban.nextconomy.database.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class Command_TogglePay implements CommandExecutor, TabCompleter {
    private NextConomy plugin;
    public Command_TogglePay(NextConomy plugin) {
        this.plugin = plugin;
    }
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return Collections.emptyList();
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(Messages.onlyPlayers.getMessage());
            return true;
        }
        Player player = (Player)commandSender;
        if (plugin.getDatabase().getTogglePay(player.getUniqueId().toString())) {
            player.sendMessage(Messages.PayOn.getMessage());
            plugin.getDatabase().updateTogglePay(player.getUniqueId().toString(), false);
            return true;
        }
        player.sendMessage(Messages.PayOff.getMessage());
        plugin.getDatabase().updateTogglePay(player.getUniqueId().toString(), true);
        return true;
    }
}
