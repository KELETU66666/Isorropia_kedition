 package fr.wind_blade.isorropia.common.entities;
 
 import fr.wind_blade.isorropia.common.entities.ai.EntityAINearestUndeadAttackableTarget;
 import java.util.List;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.EntityCreature;
 import net.minecraft.entity.EntityLiving;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.entity.ai.EntityAIAttackMelee;
 import net.minecraft.entity.ai.EntityAIBase;
 import net.minecraft.entity.ai.EntityAIFollowOwner;
 import net.minecraft.entity.ai.EntityAILeapAtTarget;
 import net.minecraft.entity.ai.EntityAIOcelotAttack;
 import net.minecraft.entity.ai.EntityAIOcelotSit;
 import net.minecraft.entity.ai.EntityAISit;
 import net.minecraft.entity.ai.EntityAISwimming;
 import net.minecraft.entity.ai.EntityAITargetNonTamed;
 import net.minecraft.entity.ai.EntityAIWander;
 import net.minecraft.entity.ai.EntityAIWatchClosest;
 import net.minecraft.entity.passive.EntityChicken;
 import net.minecraft.entity.passive.EntityOcelot;
 import net.minecraft.entity.passive.EntityTameable;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.util.DamageSource;
 import net.minecraft.util.math.AxisAlignedBB;
 import net.minecraft.world.World;
 import thaumcraft.client.fx.FXDispatcher;
 
 public class EntityGravekeeper
   extends EntityOcelot
 {
   public EntityGravekeeper(World world) {
/* 33 */     super(world);
   }
 
   
   protected void initEntityAI() {
/* 38 */     this.aiSit = new EntityAISit(this);
      this.tasks.addTask(1, new EntityAISwimming(this));
/* 40 */     this.tasks.addTask(2, this.aiSit);
      this.tasks.addTask(5, new EntityAIFollowOwner(this, 1.0D, 10.0F, 5.0F));
/* 42 */     this.tasks.addTask(6, new EntityAIOcelotSit(this, 0.8D));
/* 43 */     this.tasks.addTask(7, new EntityAILeapAtTarget(this, 0.3F));
/* 44 */     this.tasks.addTask(8, new EntityAIOcelotAttack(this));
/* 45 */     this.tasks.addTask(9, new EntityAIWander(this, 0.8D));
/* 46 */     this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
/* 47 */     this.targetTasks.addTask(1, new EntityAITargetNonTamed(this, EntityChicken.class, false, null));
/* 48 */     this.targetTasks.addTask(3, new EntityAINearestUndeadAttackableTarget(this, 1, false, false));
/* 49 */     this.targetTasks.addTask(4, new EntityAIAttackMelee(this, 1.4D, false));
   }
 
   
   public boolean attackEntityAsMob(Entity target) {
/* 54 */     return ((target instanceof EntityLivingBase && ((EntityLivingBase)target).isEntityUndead()) || target
/* 55 */       .attackEntityFrom(DamageSource.causeMobDamage(this), 2.0F));
   }
 
   
   public void onUpdate() {
/* 60 */     super.onUpdate();
/* 61 */     List<EntityLivingBase> critters = this.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(this.posX - 5.0D, this.posY - 5.0D, this.posZ - 5.0D, this.posX + 5.0D, this.posY + 5.0D, this.posZ + 5.0D));
     
/* 63 */     for (EntityLivingBase ent : critters) {
/* 64 */       if (ent.isEntityUndead()) {
/* 65 */         ent.setFire(1);
         
/* 67 */         if (this.world.isRemote)
            FXDispatcher.INSTANCE.beamBore(this.posX, this.posY + (this.height / 2.0F), this.posZ, ent.posX, ent.posY + (ent.height / 2.0F), ent.posZ, 0, 16773444, false, 2.5F, 
                Integer.valueOf(1), 1); 
       } 
     } 
   }
 }