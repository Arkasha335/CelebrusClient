package com.celebrus.setting;

import java.util.Arrays;
import java.util.List;

public class ModeSetting extends Setting {
    private String currentMode;
    private final List<String> modes;
    private int index;

    public ModeSetting(String name, String defaultMode, String... modes) {
        super(name);
        this.modes = Arrays.asList(modes);
        this.currentMode = defaultMode;
        this.index = this.modes.indexOf(defaultMode);
    }

    public String getMode() {
        return currentMode;
    }

    public void setMode(String mode) {
        this.currentMode = mode;
        this.index = modes.indexOf(mode);
    }

    public List<String> getModes() {
        return modes;
    }
}