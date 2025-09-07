package com.celebrus.module;

import com.celebrus.manager.EventManager;
import com.celebrus.setting.Setting;
import net.minecraft.client.Minecraft;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Module {
    // V--- ИСПРАВЛЕНО ЗДЕСЬ ---V
    public static final Minecraft mc = Minecraft.getMinecraft();

    private final String name;
    private final Category category;
    private int keyBind;
    private boolean enabled;
    public List<Setting> settings = new ArrayList<>();

    public Module(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    public void onEnable() {
        EventManager.register(this);
    }

    public void onDisable() {
        EventManager.unregister(this);
    }

    public void toggle() {
        this.enabled = !this.enabled;
        if (this.enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public void addSettings(Setting... settings) {
        this.settings.addAll(Arrays.asList(settings));
    }

    public String getName() { return name; }
    public Category getCategory() { return category; }
    public int getKeyBind() { return keyBind; }
    public void setKeyBind(int keyBind) { this.keyBind = keyBind; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public enum Category {
        COMBAT,
        MOVEMENT,
        PLAYER,
        RENDER,
        MISC
    }
}