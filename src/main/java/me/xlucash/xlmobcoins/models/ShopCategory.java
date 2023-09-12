package me.xlucash.xlmobcoins.models;

import java.util.ArrayList;
import java.util.List;

public class ShopCategory {
    private String name;
    private List<ShopItem> items;
    private List<String> rotationTimes;

    public ShopCategory(String name, List<ShopItem> items, List<String> rotationTimes) {
        this.name = name;
        this.items = new ArrayList<>(items);
        this.rotationTimes = new ArrayList<>(rotationTimes);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ShopItem> getItems() {
        return items;
    }

    public void setItems(List<ShopItem> items) {
        this.items = items;
    }

    public List<String> getRotationTimes() {
        return rotationTimes;
    }

    public void setRotationTimes(List<String> rotationTimes) {
        this.rotationTimes = rotationTimes;
    }
}
