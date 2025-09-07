package com.celebrus.util;

import java.awt.Color;

public class RenderUtil {

    /**
     * Возвращает цвет, который плавно переливается в зависимости от времени.
     * @param offset Смещение для получения разных цветов в один и тот же момент времени.
     * @return Целочисленное значение цвета.
     */
    public static int getRainbow(int offset) {
        float hue = (System.currentTimeMillis() + offset) % 2000L;
        hue /= 2000.0F;
        return Color.getHSBColor(hue, 0.7f, 1.0f).getRGB();
    }
}