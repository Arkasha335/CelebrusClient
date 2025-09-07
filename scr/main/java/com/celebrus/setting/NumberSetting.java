package com.celebrus.setting;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberSetting extends Setting {
    private double value;
    private final double min, max, increment;

    public NumberSetting(String name, double defaultValue, double min, double max, double increment) {
        super(name);
        this.value = defaultValue;
        this.min = min;
        this.max = max;
        this.increment = increment;
    }

    public double getValue() {
        return round(this.value);
    }

    public void setValue(double value) {
        double precision = 1.0D / this.increment;
        this.value = Math.round(Math.max(this.min, Math.min(this.max, value)) * precision) / precision;
    }

    private double round(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public double getMin() { return min; }
    public double getMax() { return max; }
    public double getIncrement() { return increment; }
}