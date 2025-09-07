package com.celebrus.module.impl.combat;

import com.celebrus.manager.EventManager;
import com.celebrus.module.Module;
import com.celebrus.setting.BooleanSetting;
import com.celebrus.setting.ModeSetting;
import com.celebrus.setting.NumberSetting;

public class KillAura extends Module {

    // General Settings
    private final NumberSetting cps = new NumberSetting("CPS", 12, 1, 20, 1);
    private final NumberSetting range = new NumberSetting("Range", 4.2, 3.0, 6.0, 0.1);
    private final BooleanSetting keepSprint = new BooleanSetting("KeepSprint", true);
    private final ModeSetting swing = new ModeSetting("Swing", "Client", "Client", "Server", "None");

    // Targeting Settings
    private final ModeSetting priority = new ModeSetting("Priority", "Distance", "Distance", "Health", "FOV");
    private final NumberSetting fov = new NumberSetting("FOV", 180, 1, 360, 1);
    private final BooleanSetting players = new BooleanSetting("Players", true);
    private final BooleanSetting invisibles = new BooleanSetting("Invisibles", false);

    // Rotations Settings
    private final ModeSetting rotations = new ModeSetting("Rotations", "Silent", "Silent", "Normal");
    private final NumberSetting horizontalSpeed = new NumberSetting("Horizontal Speed", 15, 1, 40, 1);
    private final NumberSetting verticalSpeed = new NumberSetting("Vertical Speed", 10, 1, 40, 1);
    private final BooleanSetting rayCast = new BooleanSetting("RayCast", true);
    private final BooleanSetting prediction = new BooleanSetting("Prediction", true);

    // Bypass Settings
    private final NumberSetting hurtTime = new NumberSetting("HurtTime", 10, 0, 10, 1);
    private final ModeSetting strafeFix = new ModeSetting("StrafeFix", "Silent", "Silent", "Strict", "None");

    // Visuals Settings
    private final BooleanSetting targetESP = new BooleanSetting("TargetESP", true);
    private final BooleanSetting fakeAutoBlock = new BooleanSetting("FakeAutoBlock", true);


    public KillAura() {
        super("KillAura", Category.COMBAT);
        // Добавляем все наши настройки в модуль, чтобы они были видны в GUI
        addSettings(
                cps, range, keepSprint, swing,
                priority, fov, players, invisibles,
                rotations, horizontalSpeed, verticalSpeed, rayCast, prediction,
                hurtTime, strafeFix,
                targetESP, fakeAutoBlock
        );
    }

    @EventManager.Subscribe
    public void onUpdate(EventManager.EventUpdate event) {
        if (!this.isEnabled()) return;

        // Здесь будет основная логика KillAura
        // 1. Поиск цели
        // 2. Расчет ротаций
        // 3. Атака
    }

    @Override
    public void onEnable() {
        super.onEnable(); // Это важно, чтобы зарегистрировать событие
        // Логика при включении модуля
    }

    @Override
    public void onDisable() {
        super.onDisable(); // Это важно, чтобы отписаться от события
        // Логика при выключении модуля
    }
}