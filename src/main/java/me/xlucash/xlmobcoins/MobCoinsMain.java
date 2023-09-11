package me.xlucash.xlmobcoins;

import me.xlucash.xlmobcoins.config.ConfigManager;
import me.xlucash.xlmobcoins.database.DatabaseManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class MobCoinsMain extends JavaPlugin {
    private ConfigManager configManager;
    private DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        configManager.loadConfig();
        configManager.reloadConfig();

        databaseManager = new DatabaseManager(this);
        databaseManager.getDatabaseConnection();
    }

    @Override
    public void onDisable() {
        if(databaseManager != null) {
            databaseManager.closeDatabaseConnection();
        }
    }
}
