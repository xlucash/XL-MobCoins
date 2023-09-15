package me.xlucash.xlmobcoins.config;

import me.xlucash.xlmobcoins.MobCoinsMain;
import me.xlucash.xlmobcoins.models.CoinsRange;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private final MobCoinsMain plugin;
    private FileConfiguration config;

    public ConfigManager(MobCoinsMain plugin) {
        this.plugin = plugin;
        config = plugin.getConfig();
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
    }

    public void reloadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    public String getDatabaseType() {
        return config.getString("database.type");
    }

    public String getMySQLHost() {
        return config.getString("database.mysql.host");
    }

    public int getMySQLPort() {
        return config.getInt("database.mysql.port");
    }

    public String getMySQLDatabase() {
        return config.getString("database.mysql.database");
    }

    public String getMySQLUsername() {
        return config.getString("database.mysql.username");
    }

    public String getMySQLPassword() {
        return config.getString("database.mysql.password");
    }

    public Map<String, CoinsRange> getMobCoinsRanges() {
        Map<String, CoinsRange> mobs = new HashMap<>();
        ConfigurationSection section = config.getConfigurationSection("mobs");

        for (String mob : section.getKeys(false)) {
            double min = section.getInt(mob + ".min-coins");
            double max = section.getInt(mob + ".max-coins");
            mobs.put(mob, new CoinsRange(min, max));
        }

        return mobs;
    }
}
