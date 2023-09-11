package me.xlucash.xlmobcoins.hooks;

import com.bgsoftware.wildstacker.api.WildStackerAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class WildStackerHook {
    public static boolean isWildStackerLoaded() {
        if (Bukkit.getPluginManager().getPlugin("WildStacker") != null) {
            Bukkit.getLogger().info("[XL-MobCoins] Znaleziono WildStacker! Aktywowanie hooka...");
            return true;
        } else {
            Bukkit.getLogger().warning("[XL-MobCoins] Nie znaleziono WildStacker. Funkcje zwiazane ze stackowaniem nie beda dzialac!");
            return false;
        }
    }

    public static int getEntityStackAmount(Entity entity) {
        if (WildStackerAPI.getStackedEntity((LivingEntity) entity) != null) {
            return WildStackerAPI.getStackedEntity((LivingEntity) entity).getStackAmount();
        }
        return 1;
    }
}
