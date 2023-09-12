package me.xlucash.xlmobcoins;

import me.xlucash.xlmobcoins.commands.MobCoinsCommand;
import me.xlucash.xlmobcoins.config.ConfigManager;
import me.xlucash.xlmobcoins.config.ShopConfigManager;
import me.xlucash.xlmobcoins.database.DatabaseManager;
import me.xlucash.xlmobcoins.database.PlayerDataManager;
import me.xlucash.xlmobcoins.hooks.WildStackerHook;
import me.xlucash.xlmobcoins.listeners.InventoryListener;
import me.xlucash.xlmobcoins.listeners.MobKillListener;
import me.xlucash.xlmobcoins.utils.ShopManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class MobCoinsMain extends JavaPlugin {
    private ConfigManager configManager;
    private ShopConfigManager shopConfigManager;
    private DatabaseManager databaseManager;
    private PlayerDataManager playerDataManager;

    private ShopManager shopManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        configManager.loadConfig();
        configManager.reloadConfig();

        shopConfigManager = new ShopConfigManager(this);

        databaseManager = new DatabaseManager(this);
        databaseManager.getDatabaseConnection();

        playerDataManager = new PlayerDataManager(databaseManager);

        WildStackerHook.isWildStackerLoaded();

        shopManager = new ShopManager(shopConfigManager, this, databaseManager);

        this.getCommand("lizardcoins").setExecutor(new MobCoinsCommand(this, configManager, playerDataManager, shopManager));
        this.getCommand("lizardcoins").setTabCompleter(new MobCoinsCommand(this, configManager, playerDataManager, shopManager));


        getServer().getPluginManager().registerEvents(new MobKillListener(playerDataManager, configManager), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(playerDataManager, shopManager), this);
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
