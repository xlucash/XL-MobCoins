package me.xlucash.xlmobcoins.models;

import org.bukkit.Material;

import java.util.List;

public class ShopItem {
    private int id;
    private String displayName;
    private Material material;
    private int amount;
    private int stock;
    private double price;
    private List<String> lore;
    private String command;

    public ShopItem(int id, String displayName, Material material, int amount, int stock, double price, List<String> lore, String command) {
        this.id = id;
        this.displayName = displayName;
        this.material = material;
        this.amount = amount;
        this.stock = stock;
        this.price = price;
        this.lore = lore;
        this.command = command;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
