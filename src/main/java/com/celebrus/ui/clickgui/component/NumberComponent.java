package com.celebrus.ui.clickgui.component;

import com.celebrus.setting.NumberSetting;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberComponent extends Component {
    private final NumberSetting setting;
    private boolean dragging = false;

    public NumberComponent(NumberSetting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        double percent = (setting.getValue() - setting.getMin()) / (setting.getMax() - setting.getMin());
        int sliderWidth = (int) (width * percent);

        Gui.drawRect(x, y, x + width, y + height, new Color(30, 30, 30, 200).getRGB());
        Gui.drawRect(x, y, x + sliderWidth, y + height, new Color(50, 100, 200, 220).getRGB());

        String text = setting.getName() + ": " + round(setting.getValue());
        fr.drawStringWithShadow(text, x + 4, y + height / 2f - fr.FONT_HEIGHT / 2f, -1);

        if (dragging) {
            double value = setting.getMin() + (mouseX - x) / (double) width * (setting.getMax() - setting.getMin());
            setting.setValue(value);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isMouseOver(mouseX, mouseY) && mouseButton == 0) {
            dragging = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        dragging = false;
    }

    private double round(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}