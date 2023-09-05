 package fr.wind_blade.isorropia.common.libs.helpers;
 
 import net.minecraft.entity.Entity;
 
 
 
 
 
 public class IRMathHelper
 {
   public static double getEuclidean(double posX, double posY, double posZ, double targetX, double targetY, double targetZ) {
/* 12 */     return Math.sqrt(Math.pow(posX - targetX, 2.0D) + Math.pow(posY - targetY, 2.0D));
   }
   
   public static double getTchebychevDistance(Entity entity, Entity target) {
/* 16 */     return getTchebychevDistance(entity.posX, entity.posY, entity.posZ, target.posX, target.posY, target.posZ);
   }
 
 
   
   public static double getTchebychevDistance(double posX, double posY, double posZ, double targetX, double targetY, double targetZ) {
/* 22 */     return Math.max(Math.abs(posX - targetX), Math.max(Math.abs(posY - targetY), Math.abs(posZ - targetZ)));
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\libs\helpers\IRMathHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */