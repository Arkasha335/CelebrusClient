package com.celebrus.module.impl.combat;

import com.celebrus.manager.EventManager;
import com.celebrus.module.Module;
import com.celebrus.setting.BooleanSetting;
import com.celebrus.setting.ModeSetting;
import com.celebrus.setting.NumberSetting;
import com.celebrus.setting.RangeSetting;
import com.celebrus.util.EntityUtil;
import com.celebrus.util.MathUtil;
import com.celebrus.util.RotationUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class KillAura extends Module {

    // === GENERAL SETTINGS ===
    private final RangeSetting cps = new RangeSetting("CPS", 10, 14, 1, 20, 1);
    private final NumberSetting range = new NumberSetting("Range", 4.2, 3.0, 6.0, 0.1);
    private final BooleanSetting keepSprint = new BooleanSetting("KeepSprint", true);
    private final BooleanSetting requireMouseDown = new BooleanSetting("RequireMouseDown", false);
    private final ModeSetting swing = new ModeSetting("Swing", "Client", "Client", "Server", "None");

    // === TARGETING SETTINGS ===
    private final ModeSetting priority = new ModeSetting("Priority", "Distance", "Distance", "Health", "Angle", "Random");
    private final ModeSetting targetMode = new ModeSetting("TargetMode", "Single", "Single", "Switch");
    private final NumberSetting switchDelay = new NumberSetting("SwitchDelay", 400, 0, 1000, 50);
    private final NumberSetting fov = new NumberSetting("FOV", 180.0, 30.0, 360.0, 5.0);
    private final BooleanSetting players = new BooleanSetting("Players", true);
    private final BooleanSetting mobs = new BooleanSetting("Mobs", false);
    private final BooleanSetting invisibles = new BooleanSetting("Invisibles", false);
    private final BooleanSetting teams = new BooleanSetting("Teams", false);

    // === ROTATION SETTINGS ===
    private final ModeSetting rotationMode = new ModeSetting("RotationMode", "Silent", "Silent", "Legit", "None");
    private final BooleanSetting smoothing = new BooleanSetting("Smoothing", true);
    private final ModeSetting smoothingAlgorithm = new ModeSetting("SmoothingAlgorithm", "EaseInOut", "EaseInOut", "Linear", "Exponential");
    private final RangeSetting horizontalSpeed = new RangeSetting("HorizontalSpeed", 15.0, 20.0, 1.0, 50.0, 0.5);
    private final RangeSetting verticalSpeed = new RangeSetting("VerticalSpeed", 10.0, 15.0, 1.0, 50.0, 0.5);
    private final NumberSetting randomization = new NumberSetting("Randomization", 0.1, 0.0, 1.0, 0.05);
    private final BooleanSetting gcd = new BooleanSetting("GCD", true);
    private final ModeSetting gcdMethod = new ModeSetting("GCDMethod", "Dynamic", "Dynamic", "Static");
    private final BooleanSetting raycast = new BooleanSetting("RayCast", true);
    private final ModeSetting raycastMethod = new ModeSetting("RayCastMethod", "Standard", "Standard", "Advanced");
    private final BooleanSetting dynamicResolution = new BooleanSetting("DynamicResolution", true);
    private final ModeSetting aimPoint = new ModeSetting("AimPoint", "Dynamic", "Dynamic", "Head", "Body", "Legs");
    private final RangeSetting verticalOffset = new RangeSetting("VerticalOffset", 0.1, 0.4, -1.0, 1.0, 0.1);
    private final RangeSetting horizontalOffset = new RangeSetting("HorizontalOffset", -0.2, 0.2, -1.0, 1.0, 0.1);
    private final BooleanSetting prediction = new BooleanSetting("Prediction", true);
    private final NumberSetting predictionFactor = new NumberSetting("PredictionFactor", 1.2, 0.5, 2.0, 0.1);

    // === BYPASS SETTINGS ===
    private final NumberSetting hurtTime = new NumberSetting("HurtTime", 10, 0, 20, 1);
    private final ModeSetting strafeFix = new ModeSetting("StrafeFix", "Silent", "Silent", "Legit", "None");
    private final BooleanSetting packetJitter = new BooleanSetting("PacketJitter", false);
    private final BooleanSetting timerAbuse = new BooleanSetting("TimerAbuse", false);

    // === VISUAL SETTINGS ===
    private final ModeSetting targetESP = new ModeSetting("TargetESP", "Box", "Box", "2D", "Tracers", "None");
    private final BooleanSetting fakeAutoBlock = new BooleanSetting("FakeAutoBlock", true);
    private final BooleanSetting drawRange = new BooleanSetting("DrawRange", true);

    // === INTERNAL VARIABLES ===
    private EntityLivingBase target;
    private EntityLivingBase lastTarget;
    private long lastAttackTime;
    private long lastSwitchTime;
    private float[] lastRotations = new float[2];
    private final Random random = new Random();
    private int attackCount = 0;

    public KillAura() {
        super("KillAura", Category.COMBAT);
        
        // Добавляем все настройки
        addSettings(
            // General
            cps, range, keepSprint, requireMouseDown, swing,
            // Targeting
            priority, targetMode, switchDelay, fov, players, mobs, invisibles, teams,
            // Rotation
            rotationMode, smoothing, smoothingAlgorithm, horizontalSpeed, verticalSpeed, randomization,
            gcd, gcdMethod, raycast, raycastMethod, dynamicResolution, aimPoint,
            verticalOffset, horizontalOffset, prediction, predictionFactor,
            // Bypass
            hurtTime, strafeFix, packetJitter, timerAbuse,
            // Visual
            targetESP, fakeAutoBlock, drawRange
        );
    }

    @EventManager.Subscribe
    public void onUpdate(EventManager.EventUpdate event) {
        if (!this.isEnabled()) return;

        // Проверяем требование нажатия мыши
        if (requireMouseDown.isEnabled() && !mc.gameSettings.keyBindAttack.isKeyDown()) {
            return;
        }

        // Поиск цели
        findTarget();

        // Проверка валидности цели
        if (target == null || !isValidTarget(target)) {
            target = null;
            return;
        }

        // Ротация к цели
        if (!rotationMode.getMode().equals("None")) {
            rotateToTarget();
        }

        // Атака
        if (shouldAttack()) {
            attackTarget();
        }
    }

    private void findTarget() {
        List<EntityLivingBase> targets = mc.theWorld.loadedEntityList.stream()
                .filter(entity -> entity instanceof EntityLivingBase)
                .map(entity -> (EntityLivingBase) entity)
                .filter(this::isValidTarget)
                .collect(Collectors.toList());

        if (targets.isEmpty()) {
            target = null;
            return;
        }

        // Сортировка по приоритету
        switch (priority.getMode()) {
            case "Distance":
                targets.sort(Comparator.comparingDouble(e -> mc.thePlayer.getDistanceSqToEntity(e)));
                break;
            case "Health":
                targets.sort(Comparator.comparingDouble(EntityLivingBase::getHealth));
                break;
            case "Angle":
                targets.sort(Comparator.comparingDouble(this::getAngleToEntity));
                break;
            case "Random":
                targets.sort((a, b) -> random.nextInt(3) - 1);
                break;
        }

        // Выбор цели
        if (targetMode.getMode().equals("Switch")) {
            // Логика переключения целей
            if (target == null || System.currentTimeMillis() - lastSwitchTime > switchDelay.getValue()) {
                target = targets.get(0);
                lastSwitchTime = System.currentTimeMillis();
            }
        } else {
            target = targets.get(0);
        }
    }

    private boolean isValidTarget(EntityLivingBase entity) {
        if (entity == mc.thePlayer || entity.isDead) return false;
        if (mc.thePlayer.getDistanceToEntity(entity) > range.getValue()) return false;
        if (entity.isInvisible() && !invisibles.isEnabled()) return false;
        if (entity.hurtTime > hurtTime.getValue()) return false;

        // Проверка FOV
        if (!RotationUtil.isInFOV(entity, fov.getValue())) return false;

        // Проверка типов целей
        if (entity instanceof EntityPlayer) {
            if (!players.isEnabled()) return false;
            // Проверка команд (если включено)
            if (teams.isEnabled() && EntityUtil.isTeammate((EntityPlayer) entity)) return false;
        } else if (entity instanceof EntityMob) {
            if (!mobs.isEnabled()) return false;
        }

        return true;
    }

    private double getAngleToEntity(Entity entity) {
        return RotationUtil.getAngleToEntity(entity);
    }

    private void rotateToTarget() {
        if (target == null) return;

        float[] targetRotations = calculateTargetRotations();
        
        if (rotationMode.getMode().equals("Silent")) {
            // Silent rotation - не изменяем реальные повороты игрока
            lastRotations = targetRotations;
        } else if (rotationMode.getMode().equals("Legit")) {
            // Legit rotation - плавно поворачиваем к цели
            float[] smoothedRotations = smoothRotations(targetRotations);
            mc.thePlayer.rotationYaw = smoothedRotations[0];
            mc.thePlayer.rotationPitch = smoothedRotations[1];
            lastRotations = smoothedRotations;
        }
    }

    private float[] calculateTargetRotations() {
        Vec3 targetPos = getTargetPosition();
        
        // Применяем предсказание движения
        if (prediction.isEnabled()) {
            targetPos = predictTargetPosition(targetPos);
        }

        // Применяем смещения
        targetPos = applyOffsets(targetPos);

        return RotationUtil.getRotations(targetPos);
    }

    private Vec3 getTargetPosition() {
        switch (aimPoint.getMode()) {
            case "Head":
                return getEntityHeadPosition(target);
            case "Body":
                return getEntityBodyPosition(target);
            case "Legs":
                return getEntityLegsPosition(target);
            default: // Dynamic
                return getEntityDynamicPosition(target);
        }
    }

    private Vec3 getEntityHeadPosition(Entity entity) {
        AxisAlignedBB bb = entity.getEntityBoundingBox();
        return new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.5, bb.maxY, bb.minZ + (bb.maxZ - bb.minZ) * 0.5);
    }

    private Vec3 getEntityBodyPosition(Entity entity) {
        AxisAlignedBB bb = entity.getEntityBoundingBox();
        return new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.5, bb.minY + (bb.maxY - bb.minY) * 0.5, bb.minZ + (bb.maxZ - bb.minZ) * 0.5);
    }

    private Vec3 getEntityLegsPosition(Entity entity) {
        AxisAlignedBB bb = entity.getEntityBoundingBox();
        return new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.5, bb.minY, bb.minZ + (bb.maxZ - bb.minZ) * 0.5);
    }

    private Vec3 getEntityDynamicPosition(Entity entity) {
        AxisAlignedBB bb = entity.getEntityBoundingBox();
        double randomY = 0.3 + Math.random() * 0.4; // От 30% до 70% высоты
        return new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.5, bb.minY + (bb.maxY - bb.minY) * randomY, bb.minZ + (bb.maxZ - bb.minZ) * 0.5);
    }

    private Vec3 predictTargetPosition(Vec3 currentPos) {
        if (target.motionX == 0 && target.motionY == 0 && target.motionZ == 0) {
            return currentPos;
        }

        double ticks = mc.thePlayer.getDistanceToEntity(target) / 20.0; // Примерное время полета
        return new Vec3(
            currentPos.xCoord + target.motionX * ticks * predictionFactor.getValue(),
            currentPos.yCoord + target.motionY * ticks * predictionFactor.getValue(),
            currentPos.zCoord + target.motionZ * ticks * predictionFactor.getValue()
        );
    }

    private Vec3 applyOffsets(Vec3 pos) {
        double verticalOffsetValue = verticalOffset.getRandomValue();
        double horizontalOffsetValue = horizontalOffset.getRandomValue();
        
        // Применяем рандомизацию
        if (randomization.getValue() > 0) {
            verticalOffsetValue += (Math.random() - 0.5) * randomization.getValue();
            horizontalOffsetValue += (Math.random() - 0.5) * randomization.getValue();
        }

        return new Vec3(
            pos.xCoord + horizontalOffsetValue,
            pos.yCoord + verticalOffsetValue,
            pos.zCoord
        );
    }

    private float[] smoothRotations(float[] targetRotations) {
        if (!smoothing.isEnabled()) {
            return targetRotations;
        }

        float currentYaw = mc.thePlayer.rotationYaw;
        float currentPitch = mc.thePlayer.rotationPitch;

        float horizontalSpeedValue = (float) horizontalSpeed.getRandomValue();
        float verticalSpeedValue = (float) verticalSpeed.getRandomValue();

        float newYaw = smoothRotation(currentYaw, targetRotations[0], horizontalSpeedValue);
        float newPitch = smoothRotation(currentPitch, targetRotations[1], verticalSpeedValue);

        return new float[]{newYaw, newPitch};
    }

    private float smoothRotation(float current, float target, float speed) {
        float angleDifference = MathHelper.wrapAngleTo180_float(target - current);

        switch (smoothingAlgorithm.getMode()) {
            case "Linear":
                if (angleDifference > speed) angleDifference = speed;
                if (angleDifference < -speed) angleDifference = -speed;
                break;
            case "Exponential":
                angleDifference *= 0.1f;
                break;
            default: // EaseInOut
                if (Math.abs(angleDifference) > speed) {
                    angleDifference = angleDifference > 0 ? speed : -speed;
                }
                break;
        }

        return current + angleDifference;
    }

    private boolean shouldAttack() {
        long time = System.currentTimeMillis();
        double cpsValue = cps.getRandomValue();
        long delay = (long) (1000 / cpsValue);

        return time - lastAttackTime >= delay;
    }

    private void attackTarget() {
        if (target == null) return;

        // Отправляем пакеты атаки
        if (!swing.getMode().equals("None")) {
            if (swing.getMode().equals("Client")) {
                mc.thePlayer.swingItem();
            } else if (swing.getMode().equals("Server")) {
                mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
            }
        }

        // Атака
        mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));

        // Обходы
        if (packetJitter.isEnabled()) {
            // Добавляем джиттер в пакеты
            try {
                Thread.sleep(random.nextInt(5));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (!keepSprint.isEnabled()) {
            mc.thePlayer.setSprinting(false);
        }

        lastAttackTime = System.currentTimeMillis();
        attackCount++;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        target = null;
        lastTarget = null;
        attackCount = 0;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        target = null;
        lastTarget = null;
    }
}