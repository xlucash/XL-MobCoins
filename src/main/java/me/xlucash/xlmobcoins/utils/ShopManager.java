package me.xlucash.xlmobcoins.utils;

import me.xlucash.xlmobcoins.MobCoinsMain;
import me.xlucash.xlmobcoins.config.ShopConfigManager;
import me.xlucash.xlmobcoins.database.DatabaseManager;
import me.xlucash.xlmobcoins.models.ShopItem;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShopManager {
    private final ShopConfigManager configManager;
    private final MobCoinsMain plugin;
    private final DatabaseManager databaseManager;
    private List<ShopItem> currentNormalShopItems;
    private List<ShopItem> currentPremiumShopItems;

    public ShopManager(ShopConfigManager configManager, MobCoinsMain plugin, DatabaseManager databaseManager) {
        this.configManager = configManager;
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        scheduleRotation();
        loadCurrentItems();
    }

    public void scheduleRotation() {
        long normalDelay = TimeCalculator.getSecondsToNextRotation(configManager.getNormalRotationTimes());
        long premiumDelay = TimeCalculator.getSecondsToNextRotation(configManager.getPremiumRotationTimes());

        new BukkitRunnable() {
            @Override
            public void run() {
                updateNormalShop();
                scheduleRotation();
            }
        }.runTaskLater(plugin, normalDelay * 20); // 20 tiki = 1 sekunda

        new BukkitRunnable() {
            @Override
            public void run() {
                updatePremiumShop();
                scheduleRotation();
            }
        }.runTaskLater(plugin, premiumDelay * 20);
    }

    private void updateNormalShop() {
        List<Integer> availableNormalItems = new ArrayList<>(configManager.getCategories().get("normal"));
        Collections.shuffle(availableNormalItems);

        currentNormalShopItems.clear();

        for (int i = 0; i < 6 && i < availableNormalItems.size(); i++) {
            currentNormalShopItems.add(configManager.getShopItems().get(availableNormalItems.get(i)));
        }

        saveToDatabase("normal", currentNormalShopItems);
    }

    private void updatePremiumShop() {
        List<Integer> availablePremiumItems = new ArrayList<>(configManager.getCategories().get("premium"));
        Collections.shuffle(availablePremiumItems);

        currentPremiumShopItems.clear();

        for (int i = 0; i < 2 && i < availablePremiumItems.size(); i++) {
            currentPremiumShopItems.add(configManager.getShopItems().get(availablePremiumItems.get(i)));
        }

        saveToDatabase("premium", currentPremiumShopItems);
    }

    private void loadCurrentItems() {
        List<ShopItem> normalItems = loadFromDatabase("normal");
        List<ShopItem> premiumItems = loadFromDatabase("premium");
        if (normalItems.isEmpty() && premiumItems.isEmpty()) {
            List<Integer> normalItemIds = configManager.getCategories().get("normal");
            List<Integer> premiumItemIds = configManager.getCategories().get("premium");

            currentNormalShopItems = new ArrayList<>();
            currentPremiumShopItems = new ArrayList<>();

            for (int i = 0; i < 6 && i < normalItemIds.size(); i++) {
                currentNormalShopItems.add(configManager.getShopItems().get(normalItemIds.get(i)));
            }

            for (int i = 0; i < 2 && i < premiumItemIds.size(); i++) {
                currentPremiumShopItems.add(configManager.getShopItems().get(premiumItemIds.get(i)));
            }
        } else {
            currentNormalShopItems = normalItems;
            currentPremiumShopItems = premiumItems;
        }
    }

    public List<ShopItem> getCurrentNormalShopItems() {
        return currentNormalShopItems;
    }

    public List<ShopItem> getCurrentPremiumShopItems() {
        return currentPremiumShopItems;
    }

    public String getTimeUntilNextNormalShopReset() {
        long secondsToNextNormalReset = TimeCalculator.getSecondsToNextRotation(configManager.getNormalRotationTimes());
        return formatTime(secondsToNextNormalReset);
    }

    public String getTimeUntilNextPremiumShopReset() {
        long secondsToNextPremiumReset = TimeCalculator.getSecondsToNextRotation(configManager.getPremiumRotationTimes());
        return formatTime(secondsToNextPremiumReset);
    }

    private String formatTime(long totalSeconds) {
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        return hours + "h " + minutes + "m " + seconds + "s";
    }

    private void saveToDatabase(String category, List<ShopItem> items) {
        String deleteQuery = "DELETE FROM shop_items WHERE category = ?";
        String updateQuery = "UPDATE shop_items SET stock = ?, rotation_time = datetime('now') WHERE category = ? AND item_id = ?";
        String insertQuery = "INSERT INTO shop_items (category, item_id, stock, rotation_time) VALUES (?, ?, ?, datetime('now'))";

        try (Connection connection = databaseManager.getDatabaseConnection()) {
            // Usuń wszystkie wpisy dla danej kategorii
            try (PreparedStatement deletePs = connection.prepareStatement(deleteQuery)) {
                deletePs.setString(1, category);
                deletePs.executeUpdate();
            }

            for (ShopItem item : items) {
                try (PreparedStatement ps = connection.prepareStatement(updateQuery)) {
                    ps.setInt(1, item.getStock());
                    ps.setString(2, category);
                    ps.setInt(3, item.getId());
                    int affectedRows = ps.executeUpdate();

                    if (affectedRows == 0) {
                        try (PreparedStatement insertPs = connection.prepareStatement(insertQuery)) {
                            insertPs.setString(1, category);
                            insertPs.setInt(2, item.getId());
                            insertPs.setInt(3, item.getStock());
                            insertPs.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<ShopItem> loadFromDatabase(String category) {
        List<ShopItem> items = new ArrayList<>();
        String query = "SELECT * FROM shop_items WHERE category = ?";
        try (Connection connection = databaseManager.getDatabaseConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, category);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int itemId = rs.getInt("item_id");
                int stock = rs.getInt("stock");
                ShopItem item = configManager.getShopItems().get(itemId);
                item.setStock(stock);
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

}
