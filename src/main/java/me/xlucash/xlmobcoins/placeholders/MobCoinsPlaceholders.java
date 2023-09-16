package me.xlucash.xlmobcoins.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.xlucash.xlmobcoins.MobCoinsMain;
import me.xlucash.xlmobcoins.database.PlayerDataManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;

public class MobCoinsPlaceholders extends PlaceholderExpansion {
    private final MobCoinsMain plugin;
    private final PlayerDataManager playerDataManager;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public MobCoinsPlaceholders(MobCoinsMain plugin, PlayerDataManager playerDataManager) {
        this.plugin = plugin;
        this.playerDataManager = playerDataManager;
    }
    @Override
    public @NotNull String getIdentifier() {
        return "xlmobcoins";
    }

    @Override
    public @NotNull String getAuthor() {
        return "xlucash";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return null;
        }

        // %xlmobcoins_coins%
        if (params.equalsIgnoreCase("coins")) {
            return String.valueOf(df.format(playerDataManager.getCoins(player.getUniqueId())));
        }

        // %xlmobcoins_coins_int%
        if (params.equalsIgnoreCase("coins_int")) {
            return String.valueOf((int) playerDataManager.getCoins(player.getUniqueId()));
        }

        return null;
    }
}
