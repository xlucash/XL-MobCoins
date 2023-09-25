package me.xlucash.xlmobcoins.guis;

import me.xlucash.xlmobcoins.MobCoinsMain;
import me.xlucash.xlmobcoins.database.PlayerDataManager;
import me.xlucash.xlmobcoins.models.ShopItem;
import me.xlucash.xlmobcoins.utils.ShopManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ShopGUI {
    private final PlayerDataManager playerDataManager;
    private final ShopManager shopManager;
    private final Inventory inv;
    private static BukkitTask refreshTask;
    private Player currentPlayer;

    private final DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public ShopGUI(PlayerDataManager playerDataManager, ShopManager shopManager) {
        this.playerDataManager = playerDataManager;
        this.shopManager = shopManager;
        this.inv = Bukkit.createInventory(null, 45, "§aSklep LC");
    }
    public void openFor(Player player) {
        currentPlayer = player;
        updateGUI();

        refreshTask = Bukkit.getScheduler().runTaskTimer(MobCoinsMain.getPlugin(MobCoinsMain.class), this::updateGUI, 0, 20);

        player.openInventory(inv);
    }

    public static void close() {
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
        coinsMeta.setDisplayName("§7Stan konta: §a" + decimalFormat.format(playerDataManager.getCoins(currentPlayer.getUniqueId())) + " LC");
        coinsItem.setItemMeta(coinsMeta);
        inv.setItem(44, coinsItem);

        ItemStack normalResetTimeItem = new ItemStack(Material.CLOCK);
        ItemMeta normalResetTimeMeta = normalResetTimeItem.getItemMeta();
        normalResetTimeMeta.setDisplayName("§6§lZWYKLA OFERTA");
        normalResetTimeMeta.setLore(Arrays.asList("§7Nowy sklep za: §e" + shopManager.getTimeUntilNextNormalShopReset(), "§7§o(( Przedmioty ze zwyklej oferty zmieniaja sie codziennie o 14:00 i 22:00 ))"));
        normalResetTimeItem.setItemMeta(normalResetTimeMeta);
        inv.setItem(11, normalResetTimeItem); // Slot 12

        ItemStack premiumResetTimeItem = new ItemStack(Material.CLOCK);
        ItemMeta premiumResetTimeMeta = premiumResetTimeItem.getItemMeta();
        premiumResetTimeMeta.setDisplayName("§6§lOFERTA SPECJALNA");
        premiumResetTimeMeta.setLore(Arrays.asList("§7Nowy sklep za: §e" + shopManager.getTimeUntilNextPremiumShopReset(), "§7§o(( Przedmioty z oferty specjalnej zmieniaja sie codziennie o 18:00 ))"));
        premiumResetTimeItem.setItemMeta(premiumResetTimeMeta);
        inv.setItem(15, premiumResetTimeItem);

        int[] normalSlots = {19, 20, 21, 28, 29, 30};
        List<ShopItem> normalShopItems = shopManager.getCurrentNormalShopItems();
        List<ItemStack> normalItems = normalShopItems.stream()
                .map(this::convertShopItemToItemStack)
                .collect(Collectors.toList());
        for (int i = 0; i < normalItems.size(); i++) {
            inv.setItem(normalSlots[i], normalItems.get(i));
        }

        int[] premiumSlots = {24, 33};
        List<ShopItem> premiumShopItems = shopManager.getCurrentPremiumShopItems();
        List<ItemStack> premiumItems = premiumShopItems.stream()
                .map(this::convertShopItemToItemStack)
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

    private ItemStack convertShopItemToItemStack(ShopItem shopItem) {
        ItemStack item = new ItemStack(shopItem.getMaterial(), shopItem.getAmount());
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', shopItem.getDisplayName()));

            List<String> lore = new ArrayList<>();

            lore.add("§8Sklep LC");
            lore.add(" ");

            if (shopItem.getLore() != null) {
                List<String> translatedLore = shopItem.getLore().stream()
                        .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                        .collect(Collectors.toList());
                lore.addAll(translatedLore);
            }

            lore.add(" ");

            lore.add("§7Cena: §6" + shopItem.getPrice() + " LC");
            lore.add("§7Na stanie: §a" + shopManager.getStockFromDatabase(shopItem.getId()));


            meta.setLore(lore);
            item.setItemMeta(meta);
        }

        return item;
    }
}
