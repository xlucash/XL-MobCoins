package me.xlucash.xlmobcoins;

import me.xlucash.xlmobcoins.commands.MobCoinsCommand;
import me.xlucash.xlmobcoins.config.ConfigManager;
import me.xlucash.xlmobcoins.database.DatabaseManager;
import me.xlucash.xlmobcoins.database.PlayerDataManager;
import me.xlucash.xlmobcoins.hooks.WildStackerHook;
import me.xlucash.xlmobcoins.listeners.MobKillListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class MobCoinsMain extends JavaPlugin {
    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    private PlayerDataManager playerDataManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        configManager.loadConfig();
        configManager.reloadConfig();

        databaseManager = new DatabaseManager(this);
        databaseManager.getDatabaseConnection();

        playerDataManager = new PlayerDataManager(databaseManager);

        WildStackerHook.isWildStackerLoaded();

        this.getCommand("lizardcoins").setExecutor(new MobCoinsCommand(this, configManager, playerDataManager));
        this.getCommand("lizardcoins").setTabCompleter(new MobCoinsCommand(this, configManager, playerDataManager));


        getServer().getPluginManager().registerEvents(new MobKillListener(playerDataManager, configManager), this);
    }

    @Override
    public void onDisable() {
        if(databaseManager != null) {
            databaseManager.closeDatabaseConnection();
        }
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
