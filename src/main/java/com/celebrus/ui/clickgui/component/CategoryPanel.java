package com.celebrus.ui.clickgui.component;

import com.celebrus.Celebrus;
import com.celebrus.module.Module;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class CategoryPanel extends Component {
    private final Module.Category category;
    private final List<ModuleButton> moduleButtons = new ArrayList<>();
    private boolean open = true;

    public CategoryPanel(Module.Category category, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.category = category;
        createModuleButtons();
    }

    private void createModuleButtons() {
        moduleButtons.clear();
        int buttonY = y + height;
        for (Module module : Celebrus.instance.moduleManager.getModulesInCategory(category)) {
            moduleButtons.add(new ModuleButton(module, x, buttonY, width, height));
            buttonY += height;
        }
    }

    private void updateModulePositions() {
        int buttonY = y + height;
        for (ModuleButton button : moduleButtons) {
            button.y = buttonY;
            buttonY += height;
            // Если у модуля открыты настройки, добавляем дополнительную высоту
            if (button.settingsOpen) {
                buttonY += button.settingComponents.size() * height;
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height, new Color(20, 20, 20, 200).getRGB());
        fr.drawStringWithShadow(category.name(), x + 4, y + height / 2f - fr.FONT_HEIGHT / 2f, -1);

        if (open) {
            updateModulePositions(); // Обновляем позиции перед отрисовкой
            for (ModuleButton button : moduleButtons) {
                button.drawScreen(mouseX, mouseY, partialTicks);
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isMouseOver(mouseX, mouseY)) {
            if (mouseButton == 1) {
                open = !open;
            }
        }

        if (open) {
            for (ModuleButton button : moduleButtons) {
                button.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (open) {
            for (ModuleButton button : moduleButtons) {
                button.mouseReleased(mouseX, mouseY, state);
            }
        }
    }
}