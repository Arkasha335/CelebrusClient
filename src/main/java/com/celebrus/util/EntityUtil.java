package com.celebrus.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

public class EntityUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static boolean isValidTarget(EntityLivingBase entity, double range, boolean players, boolean mobs, boolean invisibles) {
        if (entity == mc.thePlayer || entity.isDead) return false;
        if (mc.thePlayer.getDistanceToEntity(entity) > range) return false;
        if (entity.isInvisible() && !invisibles) return false;

        if (entity instanceof EntityPlayer) {
            return players;
        } else if (entity instanceof EntityMob) {
            return mobs;
        } else if (entity instanceof EntityAnimal) {
            return false; // Животных не атакуем
        }

        return false;
    }

    public static boolean isTeammate(EntityPlayer player) {
        String playerName = player.getName();
        String myName = mc.thePlayer.getName();
        
        if (playerName.length() > 0 && myName.length() > 0) {
            return playerName.charAt(0) == myName.charAt(0);
        }
        return false;
    }

    public static boolean canAttack(EntityLivingBase entity, int hurtTime) {
        return entity.hurtTime <= hurtTime;
    }

    public static double getHealthPercentage(EntityLivingBase entity) {
        return entity.getHealth() / entity.getMaxHealth();
    }

    public static boolean isMoving(Entity entity) {
        return entity.motionX != 0 || entity.motionY != 0 || entity.motionZ != 0;
    }

    public static double getSpeed(Entity entity) {
        return Math.sqrt(entity.motionX * entity.motionX + entity.motionZ * entity.motionZ);
    }
} 