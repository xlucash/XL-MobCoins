package me.xlucash.xlmobcoins.listeners;

import me.xlucash.xlmobcoins.database.PlayerDataManager;
import me.xlucash.xlmobcoins.guis.ShopGUI;
import me.xlucash.xlmobcoins.models.ShopItem;
import me.xlucash.xlmobcoins.utils.ShopManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {
    private PlayerDataManager playerDataManager;
    private ShopManager shopManager;
    public InventoryListener(PlayerDataManager playerDataManager, ShopManager shopManager) {
        this.playerDataManager = playerDataManager;
        this.shopManager = shopManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        if(!event.getView().getTitle().equals("§aSklep LC")) {
            return;
        }

        event.setCancelled(true);

        if (event.getClickedInventory() != event.getView().getTopInventory()) {
            return;
        }

        ItemStack clickedItem = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        String displayName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
        ShopItem shopItem = shopManager.getShopItemByDisplayName(displayName);
        if (shopItem != null) {
            if (shopManager.attemptPurchase(player, shopItem)) {
                String commandToRun = shopItem.getCommand().replace("%player%", player.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandToRun);
            }
        }
    }

    @EventHandler
    public void onInventoryExit(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals("§aSklep LC")) {
            ShopGUI.close();
        }
    }
}
