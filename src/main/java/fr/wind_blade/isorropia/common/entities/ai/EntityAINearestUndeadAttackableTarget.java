 package fr.wind_blade.isorropia.common.entities.ai;
 
 import java.util.ArrayList;
 import java.util.Collections;
 import java.util.Comparator;
 import java.util.List;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.EntityCreature;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
 import net.minecraft.entity.ai.EntityAITarget;
 import net.minecraft.util.math.AxisAlignedBB;
 
 
 
 
 
 public class EntityAINearestUndeadAttackableTarget<T extends EntityLivingBase>
   extends EntityAITarget
 {
   private final int targetChance;
   protected final EntityAINearestAttackableTarget.Sorter theNearestAttackableTargetSorter;
   protected EntityLivingBase targetEntity;
   
   public EntityAINearestUndeadAttackableTarget(EntityCreature creature, boolean checkSight) {
      this(creature, checkSight, false);
   }
   
   public EntityAINearestUndeadAttackableTarget(EntityCreature creature, boolean checkSight, boolean onlyNearby) {
/* 30 */     this(creature, 10, checkSight, onlyNearby);
   }
 
   
   public EntityAINearestUndeadAttackableTarget(EntityCreature creature, int chance, boolean checkSight, boolean onlyNearby) {
/* 35 */     super(creature, checkSight, onlyNearby);
/* 36 */     this.targetChance = chance;
/* 37 */     this.theNearestAttackableTargetSorter = new EntityAINearestAttackableTarget.Sorter((Entity)creature);
/* 38 */     setMutexBits(1);
   }
 
   
   public boolean shouldExecute() {
/* 43 */     if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0) {
/* 44 */       return false;
     }
/* 46 */     List<EntityLivingBase> lt = this.taskOwner.world.getEntitiesWithinAABB(EntityLivingBase.class, 
/* 47 */         getTargetableArea(getTargetDistance()));
/* 48 */     ArrayList<EntityLivingBase> list = new ArrayList<>();
/* 49 */     for (EntityLivingBase living : lt) {
/* 50 */       if (living.isEntityUndead())
/* 51 */         list.add(living); 
     } 
/* 53 */     if (list.isEmpty()) {
/* 54 */       return false;
     }
     
/* 57 */     Collections.sort(list, (Comparator<? super EntityLivingBase>)this.theNearestAttackableTargetSorter);
/* 58 */     this.targetEntity = list.get(0);
/* 59 */     return true;
   }
 
 
   
   protected AxisAlignedBB getTargetableArea(double targetDistance) {
/* 65 */     return this.taskOwner.getEntityBoundingBox().expand(targetDistance, 4.0D, targetDistance);
   }
 
   
   public void startExecuting() {
/* 70 */     this.taskOwner.setAttackTarget(this.targetEntity);
/* 71 */     super.startExecuting();
   }
   
   public static class Sorter implements Comparator<Entity> {
     private final Entity theEntity;
     
     public Sorter(Entity theEntityIn) {
/* 78 */       this.theEntity = theEntityIn;
     }
 
     
     public int compare(Entity p_compare_1_, Entity p_compare_2_) {
/* 83 */       double d0 = this.theEntity.getDistanceSq(p_compare_1_);
/* 84 */       double d1 = this.theEntity.getDistanceSq(p_compare_2_);
/* 85 */       return (d0 < d1) ? -1 : ((d0 > d1) ? 1 : 0);
     }
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\entities\ai\EntityAINearestUndeadAttackableTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */