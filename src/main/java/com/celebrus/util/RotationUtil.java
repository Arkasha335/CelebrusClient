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
        AxisAlignedBB bb = entity.getEntityBoundingBox();
        Vec3 center = new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.5, bb.minY + (bb.maxY - bb.minY) * 0.5, bb.minZ + (bb.maxZ - bb.minZ) * 0.5);
        return getRotations(center);
    }

    public static float[] getRotationsToEntityHead(Entity entity) {
        AxisAlignedBB bb = entity.getEntityBoundingBox();
        Vec3 head = new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.5, bb.maxY, bb.minZ + (bb.maxZ - bb.minZ) * 0.5);
        return getRotations(head);
    }

    public static float[] getRotationsToEntityBody(Entity entity) {
        AxisAlignedBB bb = entity.getEntityBoundingBox();
        Vec3 body = new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.5, bb.minY + (bb.maxY - bb.minY) * 0.5, bb.minZ + (bb.maxZ - bb.minZ) * 0.5);
        return getRotations(body);
    }

    public static float[] getRotationsToEntityLegs(Entity entity) {
        AxisAlignedBB bb = entity.getEntityBoundingBox();
        Vec3 legs = new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.5, bb.minY, bb.minZ + (bb.maxZ - bb.minZ) * 0.5);
        return getRotations(legs);
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

    public static float[] smoothRotations(float[] current, float[] target, float horizontalSpeed, float verticalSpeed) {
        return new float[]{
            smoothRotation(current[0], target[0], horizontalSpeed),
            smoothRotation(current[1], target[1], verticalSpeed)
        };
    }

    public static double getAngleToEntity(Entity entity) {
        float[] rotations = getRotationsToEntity(entity);
        float currentYaw = mc.thePlayer.rotationYaw;
        float currentPitch = mc.thePlayer.rotationPitch;
        
        float yawDiff = Math.abs(rotations[0] - currentYaw);
        float pitchDiff = Math.abs(rotations[1] - currentPitch);
        
        return Math.sqrt(yawDiff * yawDiff + pitchDiff * pitchDiff);
    }

    public static boolean isInFOV(Entity entity, double fov) {
        return getAngleToEntity(entity) <= fov / 2;
    }

    public static Vec3 predictEntityPosition(Entity entity, double ticks) {
        return new Vec3(
            entity.posX + entity.motionX * ticks,
            entity.posY + entity.motionY * ticks,
            entity.posZ + entity.motionZ * ticks
        );
    }
}