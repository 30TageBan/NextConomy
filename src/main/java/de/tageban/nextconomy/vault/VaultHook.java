package de.tageban.nextconomy.vault;

import com.google.common.base.Strings;
import de.tageban.nextconomy.NextConomy;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;

import java.util.List;

public class VaultHook {

    private final NextConomy plugin;
    private final Economy provider;

    public VaultHook(NextConomy plugin) {
        this.plugin = plugin;
        provider = new EconomyProvider(plugin);
    }

    public void hook() {
        Bukkit.getServicesManager().register(Economy.class, this.provider, this.plugin, ServicePriority.Normal);
    }

    public void unhook() {
        Bukkit.getServicesManager().unregister(Economy.class, this.provider);
    }

    public boolean hasPermission(OfflinePlayer offlinePlayer, String permNode) {
        RegisteredServiceProvider<Permission> rsp = this.plugin.getServer().getServicesManager().getRegistration(Permission.class);
        if ((offlinePlayer == null) || (offlinePlayer.getUniqueId() == null) || (Strings.isNullOrEmpty(offlinePlayer.getName()))) {
            return false;
        }
        return (rsp != null) && (((Permission)rsp.getProvider()).playerHas(null, offlinePlayer, permNode));
    }

}
