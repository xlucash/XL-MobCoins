package me.xlucash.xlmobcoins.listeners;

import me.xlucash.xlmobcoins.database.PlayerDataManager;
import me.xlucash.xlmobcoins.guis.ShopGUI;
import me.xlucash.xlmobcoins.utils.ShopManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryListener implements Listener {
    private PlayerDataManager playerDataManager;
    private ShopManager shopManager;
    public InventoryListener(PlayerDataManager playerDataManager, ShopManager shopManager) {
        this.playerDataManager = playerDataManager;
        this.shopManager = shopManager;
    }
    @EventHandler
    public void onInventoryExit(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals("Sklep")) {
            ShopGUI shopGUI = new ShopGUI(playerDataManager, shopManager);
            shopGUI.close();
        }
    }
}
