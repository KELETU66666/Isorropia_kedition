// Decompiled with: CFR 0.152
// Class Version: 8
package fr.wind_blade.isorropia.common.libs.helpers;

import net.minecraft.entity.Entity;

public class IRMathHelper {
    private IRMathHelper() {
    }

    public static double getEuclidean(double posX, double posY, double posZ, double targetX, double targetY, double targetZ) {
        return Math.sqrt(Math.pow(posX - targetX, 2.0) + Math.pow(posY - targetY, 2.0));
    }

    public static double getTchebychevDistance(Entity entity, Entity target) {
        return IRMathHelper.getTchebychevDistance(entity.posX, entity.posY, entity.posZ, target.posX, target.posY, target.posZ);
    }

    public static double getTchebychevDistance(double posX, double posY, double posZ, double targetX, double targetY, double targetZ) {
        return Math.max(Math.abs(posX - targetX), Math.max(Math.abs(posY - targetY), Math.abs(posZ - targetZ)));
    }
}
