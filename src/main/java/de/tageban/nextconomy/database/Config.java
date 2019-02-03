package de.tageban.nextconomy.database;

import de.tageban.nextconomy.NextConomy;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class Config {
    protected File configFile;
    protected YamlConfiguration config;

    public Config(File folder, String name) {
        configFile = new File(folder, name + ".yml");
        if (!configFile.getParentFile().exists()) {
            configFile.getParentFile().mkdirs();
        }
        config = YamlConfiguration.loadConfiguration(this.configFile);
    }

    public Config(String folder, String name) {
        configFile = new File(folder, name + ".yml");
        if (!configFile.getParentFile().exists()) {
            configFile.getParentFile().mkdirs();
        }
        config = YamlConfiguration.loadConfiguration(this.configFile);
    }

    public void copyFromDefault(NextConomy plugin) {
        if (!configFile.exists()) {
            if (configFile.getName().contains("Messages")) {
                plugin.saveResource("messages/"+configFile.getName(),false);
            }
            else {
                plugin.saveResource(configFile.getName(),false);
            }
            config = YamlConfiguration.loadConfiguration(configFile);
        }
    }

    public boolean fileExists() {
        return this.configFile.exists();
    }

    public YamlConfiguration getConfig() {
        return this.config;
    }

    public void save() {
        try {
            config.save(configFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
