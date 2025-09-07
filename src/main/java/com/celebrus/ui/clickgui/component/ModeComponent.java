package com.celebrus.ui.clickgui.component;

import com.celebrus.setting.ModeSetting;
import net.minecraft.client.gui.Gui;
import java.awt.Color;

public class ModeComponent extends Component {
    private final ModeSetting setting;

    public ModeComponent(ModeSetting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height, new Color(30, 30, 30, 200).getRGB());
        String text = setting.getName() + ": " + setting.getMode();
        fr.drawStringWithShadow(text, x + 4, y + height / 2f - fr.FONT_HEIGHT / 2f, -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isMouseOver(mouseX, mouseY)) {
            int currentIndex = setting.getModes().indexOf(setting.getMode());
            int nextIndex = (currentIndex + 1) % setting.getModes().size();
            setting.setMode(setting.getModes().get(nextIndex));
        }
    }
}