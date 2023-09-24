package me.xlucash.xlmobcoins.models;

public class CoinsRange {
    private final double min;
    private final double max;
    private final int chance;

    public CoinsRange(double min, double max, int chance) {
        this.min = min;
        this.max = max;
        this.chance = chance;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public int getChance() {
        return chance;
    }
}
