package com.celebrus.util;

import java.util.Random;

public class MathUtil {
    private static final Random random = new Random();

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public static double random(double min, double max) {
        return min + random.nextDouble() * (max - min);
    }

    public static float random(float min, float max) {
        return min + random.nextFloat() * (max - min);
    }

    public static int random(int min, int max) {
        return min + random.nextInt(max - min + 1);
    }

    public static double lerp(double start, double end, double factor) {
        return start + (end - start) * factor;
    }

    public static float lerp(float start, float end, float factor) {
        return start + (end - start) * factor;
    }

    public static double easeInOut(double t) {
        return t < 0.5 ? 2 * t * t : -1 + (4 - 2 * t) * t;
    }

    public static double easeIn(double t) {
        return t * t;
    }

    public static double easeOut(double t) {
        return t * (2 - t);
    }

    public static double roundToDecimal(double value, int decimals) {
        double multiplier = Math.pow(10, decimals);
        return Math.round(value * multiplier) / multiplier;
    }
} 