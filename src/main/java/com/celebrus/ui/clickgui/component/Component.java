package com.celebrus.ui.clickgui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

// Базовый класс для всех компонентов GUI. Содержит общие методы и поля.
public class Component {
    protected static final Minecraft mc = Minecraft.getMinecraft();
    protected static final FontRenderer fr = mc.fontRendererObj;

    public int x, y, width, height;

    public Component(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // Методы, которые будут переопределены в дочерних классах
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {}
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {}
    public void mouseReleased(int mouseX, int mouseY, int state) {}
    public void keyTyped(char typedChar, int keyCode) {}

    // Проверка, находится ли курсор мыши над компонентом
    public boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}