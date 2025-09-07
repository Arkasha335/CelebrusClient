package com.celebrus.ui.clickgui;

import com.celebrus.Celebrus; // <--- ДОБАВЛЕН НУЖНЫЙ IMPORT
import com.celebrus.module.Module;
import com.celebrus.ui.clickgui.component.CategoryPanel; // <--- ДОБАВЛЕН НУЖНЫЙ IMPORT
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClickGui extends GuiScreen {

    private final List<CategoryPanel> panels; // Список панелей для каждой категории

    public ClickGui() {
        panels = new ArrayList<>();

        int panelX = 10;
        // Проходим по всем категориям и создаем для каждой свою панель
        for (Module.Category category : Module.Category.values()) {
            // Мы создаем панель только если в этой категории есть хотя бы один модуль
            if (!Celebrus.instance.moduleManager.getModulesInCategory(category).isEmpty()) {
                CategoryPanel panel = new CategoryPanel(category, panelX, 10, 100, 15);
                panels.add(panel);
                panelX += 110; // Смещаем следующую панель вправо
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        // Отрисовываем все панели
        for (CategoryPanel panel : panels) {
            panel.drawScreen(mouseX, mouseY, partialTicks);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        // Передаем клики всем панелям
        for (CategoryPanel panel : panels) {
            panel.mouseClicked(mouseX, mouseY, mouseButton);
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        // Этот метод нам пока не нужен, но оставим его для будущего
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}