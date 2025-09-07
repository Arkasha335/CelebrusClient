package com.celebrus.setting;

public class RangeSetting extends Setting {
    private double minValue, maxValue;
    private final double absoluteMin, absoluteMax, increment;

    public RangeSetting(String name, double defaultMin, double defaultMax, double absoluteMin, double absoluteMax, double increment) {
        super(name);
        this.minValue = defaultMin;
        this.maxValue = defaultMax;
        this.absoluteMin = absoluteMin;
        this.absoluteMax = absoluteMax;
        this.increment = increment;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = Math.max(absoluteMin, Math.min(absoluteMax, minValue));
        if (this.minValue > maxValue) {
            this.minValue = maxValue;
        }
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = Math.max(absoluteMin, Math.min(absoluteMax, maxValue));
        if (this.maxValue < minValue) {
            this.maxValue = minValue;
        }
    }

    public double getAbsoluteMin() {
        return absoluteMin;
    }

    public double getAbsoluteMax() {
        return absoluteMax;
    }

    public double getIncrement() {
        return increment;
    }

    public double getRandomValue() {
        if (minValue == maxValue) return minValue;
        return minValue + Math.random() * (maxValue - minValue);
    }
} 