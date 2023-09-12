package me.xlucash.xlmobcoins.guis;

import me.xlucash.xlmobcoins.MobCoinsMain;
import me.xlucash.xlmobcoins.database.PlayerDataManager;
import me.xlucash.xlmobcoins.models.ShopItem;
import me.xlucash.xlmobcoins.utils.ShopManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.stream.Collectors;

public class ShopGUI {
    private final PlayerDataManager playerDataManager;
    private final ShopManager shopManager;
    private final Inventory inv;
    private BukkitTask refreshTask;
    private Player currentPlayer;

    public ShopGUI(PlayerDataManager playerDataManager, ShopManager shopManager) {
        this.playerDataManager = playerDataManager;
        this.shopManager = shopManager;
        this.inv = Bukkit.createInventory(null, 45, "Sklep");
    }
    public void openFor(Player player) {
        currentPlayer = player;
        updateGUI();

        refreshTask = Bukkit.getScheduler().runTaskTimer(MobCoinsMain.getPlugin(MobCoinsMain.class), this::updateGUI, 0, 20);

        player.openInventory(inv);
    }

    public void close() {
        if (refreshTask != null) {
            refreshTask.cancel();
        }
    }

    private void updateGUI() {
        if (currentPlayer == null) {
            return;
        }

        ItemStack coinsItem = new ItemStack(Material.SUNFLOWER);
        ItemMeta coinsMeta = coinsItem.getItemMeta();
        coinsMeta.setDisplayName("Twoje MobCoins: " + playerDataManager.getCoins(currentPlayer.getUniqueId()));
        coinsItem.setItemMeta(coinsMeta);
        inv.setItem(44, coinsItem);

        ItemStack normalResetTimeItem = new ItemStack(Material.CLOCK);
        ItemMeta normalResetTimeMeta = normalResetTimeItem.getItemMeta();
        normalResetTimeMeta.setDisplayName("Czas do resetu zwykłego sklepu: " + shopManager.getTimeUntilNextNormalShopReset());
        normalResetTimeItem.setItemMeta(normalResetTimeMeta);
        inv.setItem(11, normalResetTimeItem); // Slot 12

        ItemStack premiumResetTimeItem = new ItemStack(Material.CLOCK);
        ItemMeta premiumResetTimeMeta = premiumResetTimeItem.getItemMeta();
        premiumResetTimeMeta.setDisplayName("Czas do resetu sklepu premium: " + shopManager.getTimeUntilNextPremiumShopReset());
        premiumResetTimeItem.setItemMeta(premiumResetTimeMeta);
        inv.setItem(15, premiumResetTimeItem); // Slot 16

        int[] normalSlots = {19, 20, 21, 28, 29, 30};
        List<ShopItem> normalShopItems = shopManager.getCurrentNormalShopItems();
        List<ItemStack> normalItems = normalShopItems.stream()
                .map(shopItem -> {
                    ItemStack itemStack = new ItemStack(shopItem.getMaterial(), shopItem.getAmount());
                    ItemMeta meta = itemStack.getItemMeta();
                    meta.setDisplayName(shopItem.getDisplayName());
                    meta.setLore(shopItem.getLore());
                    itemStack.setItemMeta(meta);
                    return itemStack;
                })
                .collect(Collectors.toList());
        for (int i = 0; i < normalItems.size(); i++) {
            inv.setItem(normalSlots[i], normalItems.get(i));
        }

        int[] premiumSlots = {24, 33};
        List<ShopItem> premiumShopItems = shopManager.getCurrentPremiumShopItems();
        List<ItemStack> premiumItems = premiumShopItems.stream()
                .map(shopItem -> {
                    ItemStack itemStack = new ItemStack(shopItem.getMaterial(), shopItem.getAmount());
                    ItemMeta meta = itemStack.getItemMeta();
                    meta.setDisplayName(shopItem.getDisplayName());
                    meta.setLore(shopItem.getLore());
                    itemStack.setItemMeta(meta);
                    return itemStack;
                })
                .collect(Collectors.toList());
        for (int i = 0; i < premiumItems.size(); i++) {
            inv.setItem(premiumSlots[i], premiumItems.get(i));
        }


        ItemStack blackGlassPane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta glassMeta = blackGlassPane.getItemMeta();
        glassMeta.setDisplayName(" ");
        blackGlassPane.setItemMeta(glassMeta);

        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, blackGlassPane);
            }
        }
    }
}
