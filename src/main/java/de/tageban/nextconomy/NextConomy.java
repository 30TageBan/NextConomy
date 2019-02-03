package de.tageban.nextconomy;

import de.tageban.nextconomy.commands.Command_Balance;
import de.tageban.nextconomy.commands.Command_Eco;
import de.tageban.nextconomy.commands.Command_Pay;
import de.tageban.nextconomy.database.*;
import de.tageban.nextconomy.vault.VaultHook;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class NextConomy extends JavaPlugin {

    private VaultHook vaultHook;
    private double StartBalance;
    private Database database;
    private Economy economy;


    @Override
    public void onEnable() {

        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }


        Config defaultmessages = new Config(getDataFolder()+"/messages","Use_This_Messages");
        defaultmessages.copyFromDefault(this);
        Config messages_DE = new Config(getDataFolder()+"/messages","Messages_DE");
        messages_DE.copyFromDefault(this);
        Config messages_EN = new Config(getDataFolder()+"/messages","Messages_EN");
        messages_EN.copyFromDefault(this);
        Config config = new Config(getDataFolder(), "Config");
        config.copyFromDefault(this);

        /* Database */
        Config databaseConfig = new Config(getDataFolder(), "Database");
        databaseConfig.copyFromDefault(this);
        if (databaseConfig.getConfig().getString("Method").equalsIgnoreCase("MySQL")) {
            database = new MySQLConnection(this);
        } else if (databaseConfig.getConfig().getString("Method").equalsIgnoreCase("SQLite")) {
            database = new SQLiteConnection(this);
        }
        database.Connect();
        database.createTabel();


        vaultHook = new VaultHook(this);
        vaultHook.hook();

        if (getServer().getServicesManager().getRegistration(Economy.class) != null) {
            economy = getServer().getServicesManager().getRegistration(Economy.class).getProvider();
        }

        StartBalance = 100;


        getCommand("Money").setExecutor(new Command_Balance(this));
        getCommand("Money").setTabCompleter(new Command_Balance(this));
        getCommand("Money").setPermission("NextConomy.Command.Balance");
        getCommand("Money").setPermissionMessage(Messages.NoPerms.getMessage());
        getCommand("Money").setUsage(Messages.CommandFail.getMessage()+"Balance [<Player>]");
        getCommand("Money").setDescription("Get Players Balance.");
        getCommand("Bal").setExecutor(new Command_Balance(this));
        getCommand("Bal").setTabCompleter(new Command_Balance(this));
        getCommand("Bal").setPermission("NextConomy.Command.Balance");
        getCommand("Bal").setPermissionMessage(Messages.NoPerms.getMessage());
        getCommand("Bal").setUsage(Messages.CommandFail.getMessage()+"Balance [<Player>]");
        getCommand("Bal").setDescription("Get Players Balance.");
        getCommand("Balance").setExecutor(new Command_Balance(this));
        getCommand("Balance").setTabCompleter(new Command_Balance(this));
        getCommand("Balance").setPermission("NextConomy.Command.Balance");
        getCommand("Balance").setPermissionMessage(Messages.NoPerms.getMessage());
        getCommand("Balance").setUsage(Messages.CommandFail.getMessage()+"Balance [<Player>]");
        getCommand("Balance").setDescription("Get Players Balance.");

        getCommand("Eco").setExecutor(new Command_Eco(this));
        getCommand("Eco").setTabCompleter(new Command_Eco(this));
        getCommand("Eco").setPermission("NextConomy.Command.Eco");
        getCommand("Eco").setPermissionMessage(Messages.NoPerms.getMessage());
        getCommand("Eco").setUsage(Messages.CommandFail.getMessage()+"Eco [<Add,Remove,Reset>]");
        getCommand("Eco").setDescription("Manage Player/All Balance.");
        getCommand("Economy").setExecutor(new Command_Eco(this));
        getCommand("Economy").setTabCompleter(new Command_Eco(this));
        getCommand("Economy").setPermission("NextConomy.Command.Eco");
        getCommand("Economy").setPermissionMessage(Messages.NoPerms.getMessage());
        getCommand("Economy").setUsage(Messages.CommandFail.getMessage()+"Eco [<Add,Remove,Reset>]");
        getCommand("Economy").setDescription("Manage Player/All Balance.");
        getCommand("Conomy").setExecutor(new Command_Eco(this));
        getCommand("Conomy").setTabCompleter(new Command_Eco(this));
        getCommand("Conomy").setPermission("NextConomy.Command.Eco");
        getCommand("Conomy").setPermissionMessage(Messages.NoPerms.getMessage());
        getCommand("Conomy").setUsage(Messages.CommandFail.getMessage()+"Eco [<Add,Remove,Reset>]");
        getCommand("Conomy").setDescription("Manage Player/All Balance.");

        getCommand("Pay").setExecutor(new Command_Pay(this));
        getCommand("Pay").setTabCompleter(new Command_Pay(this));
        getCommand("Pay").setPermission("NextConomy.Command.Pay");
        getCommand("Pay").setPermissionMessage(Messages.NoPerms.getMessage());
        getCommand("Pay").setUsage(Messages.CommandFail.getMessage()+"Pay <Player> <Amount>");
        getCommand("Pay").setDescription("Pay a player Balance.");



        setEnabled(true);
    }

    @Override
    public void onDisable() {
        vaultHook.unhook();
        database.Disconnect();
        setEnabled(false);
    }

    public double getStartBalance() {
        return StartBalance;
    }

    public Database getDatabase() {
        return database;
    }

    public Economy getEconomy() {
        return economy;
    }
}
