package com.celebrus.ui.clickgui.component;

import com.celebrus.setting.BooleanSetting;
import net.minecraft.client.gui.Gui;
import java.awt.Color;

public class BooleanComponent extends Component {
    private final BooleanSetting setting;

    public BooleanComponent(BooleanSetting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height, new Color(30, 30, 30, 200).getRGB());
        String text = setting.getName() + ": " + (setting.isEnabled() ? "On" : "Off");
        fr.drawStringWithShadow(text, x + 4, y + height / 2f - fr.FONT_HEIGHT / 2f, -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isMouseOver(mouseX, mouseY) && mouseButton == 0) {
            setting.toggle();
        }
    }
}