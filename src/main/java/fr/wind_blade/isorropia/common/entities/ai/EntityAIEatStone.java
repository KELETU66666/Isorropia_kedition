 package fr.wind_blade.isorropia.common.entities.ai;
 
 import java.util.List;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.EntityLiving;
 import net.minecraft.entity.ai.EntityAIBase;
 import net.minecraft.entity.item.EntityItem;
 import net.minecraft.init.Blocks;
 import net.minecraft.init.Items;
 import net.minecraft.init.SoundEvents;
 import net.minecraft.item.Item;
 import net.minecraft.item.ItemStack;
 import net.minecraft.util.math.AxisAlignedBB;
 
 
 
 
 
 public class EntityAIEatStone<T extends EntityLiving & IEatStone>
   extends EntityAIBase
 {
   private T eater;
   private Entity targetEntity;
   int count;
   
   public EntityAIEatStone(T eater) {
      this.count = 0;
      this.eater = eater;
   }
 
   
   public boolean shouldExecute() {
/* 33 */     return findItem();
   }
   
   private boolean findItem() {
/* 37 */     float dmod = 16.0F;
/* 38 */     List<Entity> targets = ((EntityLiving)this.eater).world.getEntitiesWithinAABBExcludingEntity((Entity)this.eater, new AxisAlignedBB(((EntityLiving)this.eater).posX - 16.0D, ((EntityLiving)this.eater).posY - 16.0D, ((EntityLiving)this.eater).posZ - 16.0D, ((EntityLiving)this.eater).posX + 16.0D, ((EntityLiving)this.eater).posY + 16.0D, ((EntityLiving)this.eater).posZ + 16.0D));
 
     
      if (targets.size() == 0) {
/* 42 */       return false;
     }
/* 44 */     for (Entity e : targets) {
/* 45 */       if (e instanceof EntityItem && ((EntityItem)e)
/* 46 */         .getItem().getItem() == Item.getItemFromBlock(Blocks.COBBLESTONE)) {
/* 47 */         double distance2 = e.getDistanceSq(((EntityLiving)this.eater).posX, ((EntityLiving)this.eater).posY, ((EntityLiving)this.eater).posZ);
/* 48 */         if (distance2 >= 256.0D) {
           continue;
         }
/* 51 */         this.targetEntity = e;
         break;
       } 
     } 
/* 55 */     return (this.targetEntity != null);
   }
   
   public boolean continueExecuting() {
/* 59 */     return (this.count-- > 0 && !this.eater.getNavigator().noPath() && this.targetEntity.isEntityAlive());
   }
 
   
   public void resetTask() {
/* 64 */     this.count = 0;
/* 65 */     this.targetEntity = null;
/* 66 */     this.eater.getNavigator().clearPath();
   }
 
   
   public void updateTask() {
/* 71 */     this.eater.getLookHelper().setLookPositionWithEntity(this.targetEntity, 30.0F, 30.0F);
/* 72 */     double dist = this.eater.getDistanceSq(this.targetEntity);
/* 73 */     if (dist <= 2.0D) {
/* 74 */       pickUp();
     } else {
/* 76 */       this.eater.getNavigator().tryMoveToEntityLiving(this.targetEntity, (this.eater.getAIMoveSpeed() + 1.0F));
     } 
   }
   
   private void pickUp() {
/* 81 */     if (this.targetEntity instanceof EntityItem) {
/* 82 */       ItemStack entityItem = ((EntityItem)this.targetEntity).getItem();
/* 83 */       if (!entityItem.isEmpty() && entityItem.getItem() != Items.AIR) {
/* 84 */         ((IEatStone)this.eater).eatStone();
/* 85 */         entityItem.shrink(1);
/* 86 */         if (((EntityItem)this.targetEntity).getItem().getCount() <= 0) {
/* 87 */           this.targetEntity.setDead();
         }
/* 89 */         this.eater.playSound(SoundEvents.ENTITY_PLAYER_BURP, 0.2F, ((((EntityLiving)this.eater).world.rand
/* 90 */             .nextFloat() - ((EntityLiving)this.eater).world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
       } 
     } 
   }
 
   
   public void startExecuting() {
/* 97 */     this.count = 500;
/* 98 */     this.eater.getNavigator().tryMoveToEntityLiving(this.targetEntity, (this.eater.getAIMoveSpeed() + 1.0F));
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\entities\ai\EntityAIEatStone.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */