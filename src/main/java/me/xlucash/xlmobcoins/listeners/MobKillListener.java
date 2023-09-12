package me.xlucash.xlmobcoins.listeners;

import me.xlucash.xlmobcoins.config.ConfigManager;
import me.xlucash.xlmobcoins.database.PlayerDataManager;
import me.xlucash.xlmobcoins.hooks.WildStackerHook;
import me.xlucash.xlmobcoins.models.CoinsRange;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class MobKillListener implements Listener {
    private final PlayerDataManager playerDataManager;
    private final ConfigManager configManager;

    public MobKillListener(PlayerDataManager playerDataManager, ConfigManager configManager) {
        this.playerDataManager = playerDataManager;
        this.configManager = configManager;
    }

    @EventHandler
    public void onMobKill(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Player player = event.getEntity().getKiller();

        if (player != null && configManager.getMobCoinsRanges().containsKey(entity.getType().name())) {
            CoinsRange range = configManager.getMobCoinsRanges().get(entity.getType().name());

            int stackAmount = WildStackerHook.getEntityStackAmount(entity);
            double coins = 0;

            for (int i = 0; i < stackAmount; i++) {
                double randomValue = new Random().nextDouble(range.getMin(), range.getMax());
                coins += randomValue;
            }

            BigDecimal coinsRounded = new BigDecimal(coins).setScale(2, RoundingMode.HALF_DOWN);

            playerDataManager.addCoins(player.getUniqueId(), coinsRounded.doubleValue());
            player.sendMessage("§fZdobyłeś §a+" + coinsRounded + " §fcoinsów za zabicie moba.");
        }
    }
}
