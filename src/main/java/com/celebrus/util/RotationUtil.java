package com.celebrus.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class RotationUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();

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

    public static float[] getRotationsToEntity(Entity entity) {
        // V--- ИСПРАВЛЕНО ЗДЕСЬ ---V
        AxisAlignedBB bb = entity.getEntityBoundingBox();
        Vec3 center = new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.5, bb.minY + (bb.maxY - bb.minY) * 0.5, bb.minZ + (bb.maxZ - bb.minZ) * 0.5);
        return getRotations(center);
    }

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