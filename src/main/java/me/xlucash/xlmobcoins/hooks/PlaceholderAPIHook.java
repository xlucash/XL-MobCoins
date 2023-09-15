package me.xlucash.xlmobcoins.hooks;

import me.xlucash.xlmobcoins.MobCoinsMain;
import me.xlucash.xlmobcoins.database.PlayerDataManager;
import me.xlucash.xlmobcoins.placeholders.MobCoinsPlaceholders;
import org.bukkit.Bukkit;

public class PlaceholderAPIHook {
    private static MobCoinsMain plugin;
    private static PlayerDataManager playerDataManager;

    public PlaceholderAPIHook(MobCoinsMain plugin, PlayerDataManager playerDataManager) {
        PlaceholderAPIHook.plugin = plugin;
        PlaceholderAPIHook.playerDataManager = playerDataManager;
    }

    public static void register() {
        if (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new MobCoinsPlaceholders(plugin, playerDataManager).register();
        }
    }
}
