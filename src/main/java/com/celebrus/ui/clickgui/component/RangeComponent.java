package com.celebrus.ui.clickgui.component;

import com.celebrus.setting.RangeSetting;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class RangeComponent extends Component {
    private final RangeSetting setting;
    private boolean draggingMin = false;
    private boolean draggingMax = false;

    public RangeComponent(RangeSetting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        double minPercent = (setting.getMinValue() - setting.getAbsoluteMin()) / (setting.getAbsoluteMax() - setting.getAbsoluteMin());
        double maxPercent = (setting.getMaxValue() - setting.getAbsoluteMin()) / (setting.getAbsoluteMax() - setting.getAbsoluteMin());
        
        int minSliderX = (int) (x + width * minPercent);
        int maxSliderX = (int) (x + width * maxPercent);

        // Фон
        Gui.drawRect(x, y, x + width, y + height, new Color(30, 30, 30, 200).getRGB());
        
        // Область между минимумом и максимумом
        Gui.drawRect(minSliderX, y, maxSliderX, y + height, new Color(50, 100, 200, 220).getRGB());
        
        // Ползунки
        Gui.drawRect(minSliderX - 2, y, minSliderX + 2, y + height, new Color(255, 255, 255, 255).getRGB());
        Gui.drawRect(maxSliderX - 2, y, maxSliderX + 2, y + height, new Color(255, 255, 255, 255).getRGB());

        String text = setting.getName() + ": " + round(setting.getMinValue()) + " - " + round(setting.getMaxValue());
        fr.drawStringWithShadow(text, x + 4, y + height / 2f - fr.FONT_HEIGHT / 2f, -1);

        if (draggingMin) {
            double value = setting.getAbsoluteMin() + (mouseX - x) / (double) width * (setting.getAbsoluteMax() - setting.getAbsoluteMin());
            setting.setMinValue(value);
        }
        
        if (draggingMax) {
            double value = setting.getAbsoluteMin() + (mouseX - x) / (double) width * (setting.getAbsoluteMax() - setting.getAbsoluteMin());
            setting.setMaxValue(value);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isMouseOver(mouseX, mouseY) && mouseButton == 0) {
            double minPercent = (setting.getMinValue() - setting.getAbsoluteMin()) / (setting.getAbsoluteMax() - setting.getAbsoluteMin());
            double maxPercent = (setting.getMaxValue() - setting.getAbsoluteMin()) / (setting.getAbsoluteMax() - setting.getAbsoluteMin());
            
            int minSliderX = (int) (x + width * minPercent);
            int maxSliderX = (int) (x + width * maxPercent);
            
            if (Math.abs(mouseX - minSliderX) < Math.abs(mouseX - maxSliderX)) {
                draggingMin = true;
            } else {
                draggingMax = true;
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        draggingMin = false;
        draggingMax = false;
    }

    private double round(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
} 