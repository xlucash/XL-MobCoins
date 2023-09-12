package me.xlucash.xlmobcoins.config;

import me.xlucash.xlmobcoins.MobCoinsMain;
import me.xlucash.xlmobcoins.models.ShopItem;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopConfigManager {
    private final FileConfiguration config;
    private final Map<Integer, ShopItem> shopItems = new HashMap<>();
    private final Map<String, List<Integer>> categories = new HashMap<>();
    private final List<String> normalRotationTimes = new ArrayList<>();
    private final List<String> premiumRotationTimes = new ArrayList<>();

    public ShopConfigManager(MobCoinsMain plugin) {
        this.config = plugin.getConfig();
        loadShopConfig();
        loadCategories();
    }

    private void loadShopConfig() {
        for (String itemId : config.getConfigurationSection("shop.items").getKeys(false)) {
            int id = Integer.parseInt(itemId);
            String path = "shop.items." + itemId + ".";
            String displayName = config.getString(path + "displayname");
            Material material = Material.valueOf(config.getString(path + "material"));
            int amount = config.getInt(path + "amount");
            int stock = config.getInt(path + "stock");
            double price = config.getDouble(path + "price");
            List<String> lore = config.getStringList(path + "lore");
            String command = config.getString(path + "command");
            ShopItem item = new ShopItem(id, displayName, material, amount, stock, price, lore, command);
            shopItems.put(id, item);
        }

        normalRotationTimes.clear();
        premiumRotationTimes.clear();

        if (config.contains("shop.rotation-times.normal")) {
            normalRotationTimes.addAll(config.getStringList("shop.rotation-times.normal"));
        }

        if (config.contains("shop.rotation-times.premium")) {
            premiumRotationTimes.addAll(config.getStringList("shop.rotation-times.premium"));
        }
    }

    private void loadCategories() {
        for (String categoryName : config.getConfigurationSection("shop.categories").getKeys(false)) {
            categories.put(categoryName, config.getIntegerList("shop.categories." + categoryName));
        }
    }

    public Map<Integer, ShopItem> getShopItems() {
        return shopItems;
    }

    public Map<String, List<Integer>> getCategories() {
        return categories;
    }

    public List<String> getNormalRotationTimes() {
        return normalRotationTimes;
    }

    public List<String> getPremiumRotationTimes() {
        return premiumRotationTimes;
    }
}
