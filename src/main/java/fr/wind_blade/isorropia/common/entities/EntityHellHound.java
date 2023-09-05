 package fr.wind_blade.isorropia.common.entities;
 
 import fr.wind_blade.isorropia.common.entities.projectile.EntityEmber;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.entity.passive.EntityWolf;
 import net.minecraft.world.World;
 
 
 
 public class EntityHellHound
   extends EntityWolf
 {
   long soundDelay;
   
   public EntityHellHound(World worldIn) {
/* 17 */     super(worldIn);
/* 18 */     this.soundDelay = 0L;
     this.isImmuneToFire = true;
   }
 
   
   public void onLivingUpdate() {
/* 24 */     super.onLivingUpdate();
     EntityLivingBase target = null;
      if (getAttackTarget() != null) {
        target = getAttackTarget();
     }
      if (getAttackTarget() != null) {
/* 30 */       target = getAttackTarget();
     }
 
     
/* 34 */     float scatter = 8.0F;
/* 35 */     EntityEmber orb = new EntityEmber(this.world, (EntityLivingBase)this, scatter);
/* 36 */     orb.damage = 1.0F;
/* 37 */     orb.firey = 1;
/* 38 */     this.world.spawnEntity((Entity)orb);
/* 39 */     orb.posX += orb.motionX;
/* 40 */     orb.posY += orb.motionY;
      orb.posZ += orb.motionZ;
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\entities\EntityHellHound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */