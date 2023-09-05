 package fr.wind_blade.isorropia.common.entities.ai;
 
 import fr.wind_blade.isorropia.common.libs.helpers.IRMathHelper;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.EntityLiving;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.entity.SharedMonsterAttributes;
 import net.minecraft.entity.ai.EntityAIBase;
 import net.minecraft.entity.passive.EntityTameable;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.pathfinding.Path;
 import net.minecraft.util.DamageSource;
 import net.minecraftforge.fml.common.network.NetworkRegistry;
 import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
 import thaumcraft.common.lib.SoundsTC;
 import thaumcraft.common.lib.network.PacketHandler;
 import thaumcraft.common.lib.network.fx.PacketFXWispZap;
 
 
 
 
 
 
 
 
 public class EntityAIZapAttack
   extends EntityAIBase
 {
   protected final int attackCooldown;
   protected EntityLiving attacker;
   private final float range;
   boolean longMemory;
   Path path;
   
   public EntityAIZapAttack(EntityLiving attacker, float range, int cooldownTicks, boolean useLongMemory) {
/*  36 */     this.attacker = attacker;
/*  37 */     this.range = range;
/*  38 */     this.attackCooldown = cooldownTicks;
/*  39 */     this.longMemory = useLongMemory;
   }
 
 
   
   public void startExecuting() {}
 
 
   
   public boolean shouldExecute() {
/*  49 */     EntityLivingBase target = this.attacker.getAttackTarget();
     
/*  51 */     if (target == null && 
/*  52 */       this.attacker instanceof EntityTameable) {
/*  53 */       EntityTameable tameable = (EntityTameable)this.attacker;
/*  54 */       if (!tameable.isTamed() || tameable.getOwner() == null) {
/*  55 */         return false;
       }
       
/*  58 */       tameable.setRevengeTarget(tameable.getOwner().getAttackingEntity());
/*  59 */       target = this.attacker.getAttackTarget();
       
/*  61 */       if (target == null) {
/*  62 */         return false;
       }
     } 
 
     
/*  67 */     if (!target.isEntityAlive()) {
/*  68 */       return false;
     }
     
/*  71 */     return true;
   }
 
   
   public boolean shouldContinueExecuting() {
/*  76 */     EntityLivingBase target = this.attacker.getAttackTarget();
     
/*  78 */     if (target == null || !target.isEntityAlive()) {
/*  79 */       return false;
     }
     
/*  82 */     if (!(target instanceof EntityPlayer) || (
/*  83 */       !((EntityPlayer)target).isSpectator() && !((EntityPlayer)target).isCreative())) {
/*  84 */       return (IRMathHelper.getTchebychevDistance((Entity)this.attacker, (Entity)target) <= getAttackRange(target));
     }
     
/*  87 */     return false;
   }
 
   
   public void updateTask() {
/*  92 */     EntityLivingBase target = this.attacker.getAttackTarget();
     
/*  94 */     if (this.attacker.ticksExisted % this.attackCooldown != 0) {
       return;
     }
     
     this.attacker.playSound(SoundsTC.zap, 1.0F, 1.1F);
/*  99 */     PacketHandler.INSTANCE.sendToAllAround((IMessage)new PacketFXWispZap(this.attacker.getEntityId(), target.getEntityId()), new NetworkRegistry.TargetPoint(this.attacker.world.provider
/* 100 */           .getDimension(), this.attacker.posX, this.attacker.posY, this.attacker.posZ, 32.0D));
 
     
/* 103 */     float damage = (float)this.attacker.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
/* 104 */     if (Math.abs(target.motionX) > 0.10000000149011612D || Math.abs(target.motionY) > 0.10000000149011612D || 
/* 105 */       Math.abs(target.motionZ) > 0.10000000149011612D) {
/* 106 */       target.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this.attacker), damage);
     } else {
/* 108 */       target.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this.attacker), damage + 1.0F);
     } 
   }
   
   private float getAttackRange(EntityLivingBase target) {
/* 113 */     return this.range;
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\entities\ai\EntityAIZapAttack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */