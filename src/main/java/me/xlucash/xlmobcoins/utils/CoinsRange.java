package me.xlucash.xlmobcoins.utils;

public class CoinsRange {
    private final double min;
    private final double max;

    public CoinsRange(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }
}
