package com.celebrus.ui.clickgui.component;

import com.celebrus.module.Module;
import com.celebrus.setting.BooleanSetting;
import com.celebrus.setting.ModeSetting;
import com.celebrus.setting.NumberSetting;
import com.celebrus.setting.RangeSetting;
import com.celebrus.setting.Setting;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ModuleButton extends Component {
    private final Module module;
    public final List<Component> settingComponents = new ArrayList<>();
    public boolean settingsOpen = false;

    public ModuleButton(Module module, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.module = module;
        createSettingComponents();
    }

    private void createSettingComponents() {
        settingComponents.clear();
        int settingY = y + height;
        for (Setting setting : module.settings) {
            if (setting instanceof BooleanSetting) {
                settingComponents.add(new BooleanComponent((BooleanSetting) setting, x, settingY, width, height));
            } else if (setting instanceof NumberSetting) {
                settingComponents.add(new NumberComponent((NumberSetting) setting, x, settingY, width, height));
            } else if (setting instanceof ModeSetting) {
                settingComponents.add(new ModeComponent((ModeSetting) setting, x, settingY, width, height));
            } else if (setting instanceof RangeSetting) {
                settingComponents.add(new RangeComponent((RangeSetting) setting, x, settingY, width, height));
            }
            settingY += height;
        }
    }

    private void updateSettingPositions() {
        int settingY = y + height;
        for (Component component : settingComponents) {
            component.y = settingY;
            settingY += height;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int color = module.isEnabled() ? new Color(50, 100, 200, 220).getRGB() : new Color(40, 40, 40, 200).getRGB();
        Gui.drawRect(x, y, x + width, y + height, color);
        fr.drawStringWithShadow(module.getName(), x + 4, y + height / 2f - fr.FONT_HEIGHT / 2f, -1);

        if (settingsOpen) {
            updateSettingPositions();
            for (Component component : settingComponents) {
                component.drawScreen(mouseX, mouseY, partialTicks);
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isMouseOver(mouseX, mouseY)) {
            if (mouseButton == 0) {
                module.toggle();
            } else if (mouseButton == 1) {
                settingsOpen = !settingsOpen;
                if (settingsOpen) {
                    createSettingComponents();
                }
            }
        }

        if (settingsOpen) {
            for (Component component : settingComponents) {
                component.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (settingsOpen) {
            for (Component component : settingComponents) {
                component.mouseReleased(mouseX, mouseY, state);
            }
        }
    }
}