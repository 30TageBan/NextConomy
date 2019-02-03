package de.tageban.nextconomy.vault;

import de.tageban.nextconomy.NextConomy;
import de.tageban.nextconomy.database.Config;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class EconomyProvider implements Economy {

    private final NextConomy plugin;

    public EconomyProvider(NextConomy plugin) {
        this.plugin = plugin;
    }

    public String getName() {
        return "NextConomy";
    }

    public String format(double amount) {
        DecimalFormat format = new DecimalFormat("0.00");
        format.setGroupingSize(3);
        format.setGroupingUsed(true);
        DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        format.setDecimalFormatSymbols(symbols);
        return format.format(amount);
    }

    public String currencyNamePlural() {
        Config config = new Config(plugin.getDataFolder(), "Config");
        return config.getConfig().getString("Name.Plural");
    }

    public String currencyNameSingular() {
        Config config = new Config(plugin.getDataFolder(), "Config");
        return config.getConfig().getString("Name.Singular");
    }

    public boolean isEnabled() {
        return plugin.isEnabled();
    }

    public int fractionalDigits() {
        return 2;
    }

    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return plugin.getDatabase().isUserExist(offlinePlayer.getUniqueId().toString());
    }

    public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
        return hasAccount(offlinePlayer);
    }

    public boolean hasAccount(String s) {
        OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(s);
        if (offlinePlayer == null) {
            return false;
        }
        return hasAccount(offlinePlayer);
    }

    public boolean hasAccount(String s, String s1) {
        return hasAccount(s);
    }

    public boolean has(OfflinePlayer offlinePlayer, double amount) {
        if (getBalance(offlinePlayer) >= amount) {
            return true;
        }
        return false;
    }

    public boolean has(OfflinePlayer offlinePlayer, String s, double amount) {
        return has(offlinePlayer, amount);
    }

    public boolean has(String s, double amount) {
        OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(s);
        if (offlinePlayer == null) {
            return false;
        }
        return has(offlinePlayer, amount);
    }

    public boolean has(String s, String s1, double amount) {
        return has(s, amount);
    }

    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        if (plugin.getDatabase().isUserExist(offlinePlayer.getUniqueId().toString())) {
            return true;
        }
        plugin.getDatabase().createUser(offlinePlayer.getUniqueId().toString(), plugin.getStartBalance());
        if (plugin.getDatabase().isUserExist(offlinePlayer.getUniqueId().toString())) {
            return true;
        }
        return false;
    }

    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
        return createPlayerAccount(offlinePlayer);
    }

    public boolean createPlayerAccount(String s) {
        OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(s);
        if (offlinePlayer == null) {
            return false;
        }
        return createPlayerAccount(offlinePlayer);
    }

    public boolean createPlayerAccount(String s, String s1) {
        return createPlayerAccount(s);
    }

    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double amount) {
        plugin.getDatabase().setBalance(offlinePlayer.getUniqueId().toString(), (getBalance(offlinePlayer) + amount));
       /* String name = new SimpleDateFormat("yyy_MM_DD").format(new Date());
        String message = "["+new SimpleDateFormat("HH:mm:ss DD.MM.yyyy").format(new Date())+"] Name:"+ offlinePlayer.getName()+"; UUID:"+offlinePlayer.getUniqueId()+";\r\n";
        try {
            File log = new File(system.getDataFolder()+"/logs/Join/",name+".txt");
            if (!log.getParentFile().exists()) {
                log.getParentFile().mkdirs();
            }
            FileWriter fileWriter = new FileWriter(log, true);
            fileWriter.write(message);
            fileWriter.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }*/
        return new EconomyResponse(amount, getBalance(offlinePlayer), EconomyResponse.ResponseType.SUCCESS, null);
    }

    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double amount) {
        return depositPlayer(offlinePlayer, amount);
    }

    public EconomyResponse depositPlayer(String s, double amount) {
        OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(s);
        if (offlinePlayer == null) {
            return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.FAILURE, null);
        }
        return depositPlayer(offlinePlayer, amount);
    }

    public EconomyResponse depositPlayer(String s, String s1, double amount) {
        return depositPlayer(s, amount);
    }

    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount) {
        plugin.getDatabase().setBalance(offlinePlayer.getUniqueId().toString(), (getBalance(offlinePlayer) - amount));
        return new EconomyResponse(amount, getBalance(offlinePlayer), EconomyResponse.ResponseType.SUCCESS, null);
    }

    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double amount) {
        return withdrawPlayer(offlinePlayer, amount);
    }

    public EconomyResponse withdrawPlayer(String s, double amount) {
        OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(s);
        if (offlinePlayer == null) {
            return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.FAILURE, null);
        }
        return withdrawPlayer(offlinePlayer, amount);
    }

    public EconomyResponse withdrawPlayer(String s, String s1, double amount) {
        return withdrawPlayer(s, amount);
    }

    public double getBalance(OfflinePlayer offlinePlayer) {
        return plugin.getDatabase().getBalance(offlinePlayer.getUniqueId().toString());
    }

    public double getBalance(OfflinePlayer offlinePlayer, String s) {
        return getBalance(offlinePlayer);
    }

    public double getBalance(String s) {
        OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(s);
        if (offlinePlayer == null) {
            return 0;
        }
        return getBalance(offlinePlayer);
    }

    public double getBalance(String s, String s1) {
        return getBalance(s);
    }

    public boolean hasBankSupport() {
        return false;
    }

    public List<String> getBanks() {
        return Collections.EMPTY_LIST;
    }

    public EconomyResponse deleteBank(String s) {
        return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    public EconomyResponse isBankMember(String s, String s1) {
        return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    public EconomyResponse isBankOwner(String s, String s1) {
        return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);

    }

    public EconomyResponse bankBalance(String s) {
        return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    public EconomyResponse bankDeposit(String s, double amount) {
        return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    public EconomyResponse bankHas(String s, double amount) {
        return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    public EconomyResponse bankWithdraw(String s, double amount) {
        return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    public EconomyResponse createBank(String s, String s1) {
        return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }
}

