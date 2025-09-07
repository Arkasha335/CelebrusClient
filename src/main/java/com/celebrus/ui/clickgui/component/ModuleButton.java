package com.celebrus.ui.clickgui.component;

import com.celebrus.module.Module;
import net.minecraft.client.gui.Gui;
import java.awt.Color;

// Кнопка, представляющая один модуль (например, "KillAura")
public class ModuleButton extends Component {
    private final Module module;

    public ModuleButton(Module module, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.module = module;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Цвет кнопки зависит от того, включен ли модуль
        int color = module.isEnabled() ? new Color(50, 100, 200, 220).getRGB() : new Color(40, 40, 40, 200).getRGB();
        Gui.drawRect(x, y, x + width, y + height, color);
        fr.drawStringWithShadow(module.getName(), x + 4, y + height / 2f - fr.FONT_HEIGHT / 2f, -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isMouseOver(mouseX, mouseY)) {
            // Левая кнопка мыши - включить/выключить модуль
            if (mouseButton == 0) {
                module.toggle();
            }
            // Правая кнопка мыши - в будущем будет открывать настройки
        }
    }
}