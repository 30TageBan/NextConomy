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


        Config messages = new Config(getDataFolder(),"Messages");
        messages.copyFromDefault(this);
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


        getCommand("Balance").setExecutor(new Command_Balance(this));
        getCommand("Balance").setPermission("NextConomy.Command.Balance");
        getCommand("Balance").setPermissionMessage(Messages.NoPerms.getMessage());
        getCommand("Balance").setUsage(Messages.CommandFail.getMessage()+"Balance [<Player>]");
        getCommand("Balance").setDescription("Get Players Balance.");
        getCommand("Balance").setAliases(Arrays.asList("Bal","Money"));

        getCommand("Eco").setExecutor(new Command_Eco(this));
        getCommand("Eco").setPermission("NextConomy.Command.Eco");
        getCommand("Eco").setPermissionMessage(Messages.NoPerms.getMessage());
        getCommand("Eco").setUsage(Messages.CommandFail.getMessage()+"Eco [<Add,Remove,Reset>]");
        getCommand("Eco").setDescription("Get Players Balance.");
        getCommand("Eco").setAliases(Arrays.asList("Economy","Conomy"));

        getCommand("Pay").setExecutor(new Command_Pay(this));
        getCommand("Pay").setPermission("NextConomy.Command.Pay");
        getCommand("Pay").setPermissionMessage(Messages.NoPerms.getMessage());
        getCommand("Pay").setUsage(Messages.CommandFail.getMessage()+"Pay <Player> <Anzahl>");
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
