package me.xlucash.xlmobcoins.commands;

import me.xlucash.xlmobcoins.MobCoinsMain;
import me.xlucash.xlmobcoins.config.ConfigManager;
import me.xlucash.xlmobcoins.database.PlayerDataManager;
import me.xlucash.xlmobcoins.guis.ShopGUI;
import me.xlucash.xlmobcoins.utils.ShopManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MobCoinsCommand implements CommandExecutor, TabCompleter {
    private final MobCoinsMain plugin;
    private final ConfigManager configManager;
    private final PlayerDataManager dataManager;
    private final ShopManager shopManager;

    public MobCoinsCommand(MobCoinsMain plugin, ConfigManager configManager, PlayerDataManager dataManager, ShopManager shopManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.dataManager = dataManager;
        this.shopManager = shopManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Ta komenda jest dostępna tylko dla graczy!");
            return true;
        }
        Player player = (Player) sender;

        if (args.length == 0) {
            double coins = dataManager.getCoins(player.getUniqueId());
            player.sendMessage("§7Posiadasz §a" + coins + " coinsów.");
            return true;
        }

        if (args[0].equalsIgnoreCase("sklep")) {
            ShopGUI shopGUI = new ShopGUI(dataManager, shopManager);
            shopGUI.openFor(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!player.hasPermission("lizardcoins.reload")) {
                player.sendMessage("§cNie masz uprawnień do tej komendy.");
                return true;
            }
            configManager.reloadConfig();
            player.sendMessage("§aKonfiguracja została przeładowana.");
            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            if ("sklep".startsWith(args[0].toLowerCase())) {
                completions.add("sklep");
            }
            if ("reload".startsWith(args[0].toLowerCase()) && sender.hasPermission("lizardcoins.reload")) {
                completions.add("reload");
            }
            return completions;
        }

        return null;
    }
}
