package de.tageban.nextconomy.vault;

import de.tageban.nextconomy.NextConomy;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

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


}
