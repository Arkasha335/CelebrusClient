package com.celebrus.module.impl.combat;

import com.celebrus.manager.EventManager;
import com.celebrus.module.Module;
import com.celebrus.setting.BooleanSetting;
import com.celebrus.setting.ModeSetting;
import com.celebrus.setting.NumberSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class KillAura extends Module {

    // --- Настройки ---
    private final NumberSetting range = new NumberSetting("Range", 4.2, 3.0, 6.0, 0.1);
    private final NumberSetting cps = new NumberSetting("CPS", 12, 1, 20, 1);
    private final BooleanSetting keepSprint = new BooleanSetting("KeepSprint", true);
    private final BooleanSetting players = new BooleanSetting("Players", true);
    private final BooleanSetting invisibles = new BooleanSetting("Invisibles", false);
    private final BooleanSetting fakeAutoBlock = new BooleanSetting("FakeAutoBlock", true);

    // --- Переменные для логики ---
    private EntityLivingBase target;
    private long lastAttackTime;

    public KillAura() {
        super("KillAura", Category.COMBAT);
        addSettings(range, cps, keepSprint, players, invisibles, fakeAutoBlock);
    }

    @EventManager.Subscribe
    public void onUpdate(EventManager.EventUpdate event) {
        if (!this.isEnabled()) return;

        // 1. Поиск новой цели
        findTarget();

        // 2. Проверка, валидна ли текущая цель
        if (target == null || mc.thePlayer.getDistanceToEntity(target) > range.getValue() || target.isDead) {
            target = null;
            return;
        }

        // 3. Расчет времени для атаки
        long time = System.currentTimeMillis();
        long delay = (long) (1000 / cps.getValue());

        if (time - lastAttackTime >= delay) {
            // 4. Атака
            attack(target);
            lastAttackTime = time;
        }
    }

    private void findTarget() {
        List<EntityLivingBase> targets = mc.theWorld.loadedEntityList.stream()
                .filter(entity -> entity instanceof EntityLivingBase)
                .map(entity -> (EntityLivingBase) entity)
                .filter(this::isValid)
                .sorted(Comparator.comparingDouble(e -> mc.thePlayer.getDistanceSqToEntity(e)))
                .collect(Collectors.toList());

        target = targets.isEmpty() ? null : targets.get(0);
    }

    private void attack(Entity entity) {
        mc.thePlayer.swingItem();
        mc.playerController.attackEntity(mc.thePlayer, entity);

        if (!keepSprint.isEnabled()) {
            mc.thePlayer.setSprinting(false);
        }
    }

    private boolean isValid(EntityLivingBase entity) {
        if (entity == mc.thePlayer || entity.isDead) return false;
        if (mc.thePlayer.getDistanceToEntity(entity) > range.getValue()) return false;
        if (entity.isInvisible() && !invisibles.isEnabled()) return false;
        return entity instanceof EntityPlayer && players.isEnabled();
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        target = null; // Сбрасываем цель при выключении
    }
}