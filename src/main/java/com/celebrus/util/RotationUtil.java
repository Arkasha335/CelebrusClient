package com.celebrus.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class RotationUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();

    /**
     * Рассчитывает необходимые углы (yaw, pitch) для того, чтобы посмотреть на определенную точку в мире.
     * @param target Позиция, на которую нужно посмотреть.
     * @return Массив из двух float: {yaw, pitch}.
     */
    public static float[] getRotations(Vec3 target) {
        Vec3 playerPos = mc.thePlayer.getPositionEyes(1.0f);
        double diffX = target.xCoord - playerPos.xCoord;
        double diffY = target.yCoord - playerPos.yCoord;
        double diffZ = target.zCoord - playerPos.zCoord;
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);

        float yaw = (float) (Math.toDegrees(Math.atan2(diffZ, diffX))) - 90.0F;
        float pitch = (float) -Math.toDegrees(Math.atan2(diffY, dist));

        return new float[]{yaw, pitch};
    }

    /**
     * Рассчитывает углы для взгляда на центр сущности (entity).
     * @param entity Сущность, на которую нужно посмотреть.
     * @return Массив из двух float: {yaw, pitch}.
     */
    public static float[] getRotationsToEntity(Entity entity) {
        return getRotations(entity.getEntityBoundingBox().getCenter());
    }

    /**
     * Плавно изменяет текущий угол в сторону целевого угла.
     * @param current Текущий угол.
     * @param target Целевой угол.
     * @param speed Скорость изменения (максимальное изменение за один вызов).
     * @return Новый, сглаженный угол.
     */
    public static float smoothRotation(float current, float target, float speed) {
        float angleDifference = MathHelper.wrapAngleTo180_float(target - current);

        if (angleDifference > speed) {
            angleDifference = speed;
        }
        if (angleDifference < -speed) {
            angleDifference = -speed;
        }

        return current + angleDifference;
    }
}