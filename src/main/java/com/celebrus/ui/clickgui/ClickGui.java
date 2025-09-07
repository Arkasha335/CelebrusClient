package com.celebrus.ui.clickgui;

import com.celebrus.Celebrus;
import com.celebrus.module.Module;
import com.celebrus.ui.clickgui.component.CategoryPanel;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClickGui extends GuiScreen {

    private final List<CategoryPanel> panels;

    public ClickGui() {
        panels = new ArrayList<>();

        int panelX = 10;
        for (Module.Category category : Module.Category.values()) {
            if (!Celebrus.instance.moduleManager.getModulesInCategory(category).isEmpty()) {
                CategoryPanel panel = new CategoryPanel(category, panelX, 10, 100, 15);
                panels.add(panel);
                panelX += 110;
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        for (CategoryPanel panel : panels) {
            panel.drawScreen(mouseX, mouseY, partialTicks);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (CategoryPanel panel : panels) {
            panel.mouseClicked(mouseX, mouseY, mouseButton);
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        // Передаем mouseReleased всем панелям
        for (CategoryPanel panel : panels) {
            panel.mouseReleased(mouseX, mouseY, state);
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}