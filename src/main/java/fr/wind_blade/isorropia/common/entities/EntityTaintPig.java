 package fr.wind_blade.isorropia.common.entities;
 
 import com.google.common.collect.Sets;
 import fr.wind_blade.isorropia.common.entities.ai.EntityAIEatTaint;
 import fr.wind_blade.isorropia.common.entities.ai.EntityAITaintTempt;
 import java.util.Set;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.EntityCreature;
 import net.minecraft.entity.EntityLiving;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.entity.SharedMonsterAttributes;
 import net.minecraft.entity.ai.EntityAIAttackMelee;
 import net.minecraft.entity.ai.EntityAIBase;
 import net.minecraft.entity.ai.EntityAILookIdle;
 import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
 import net.minecraft.entity.ai.EntityAIPanic;
 import net.minecraft.entity.ai.EntityAISwimming;
 import net.minecraft.entity.ai.EntityAIWander;
 import net.minecraft.entity.ai.EntityAIWatchClosest;
 import net.minecraft.entity.passive.EntityPig;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.item.Item;
 import net.minecraft.item.ItemStack;
 import net.minecraft.util.DamageSource;
 import net.minecraft.util.EntityDamageSource;
 import net.minecraft.world.World;
 import thaumcraft.api.damagesource.DamageSourceThaumcraft;
 import thaumcraft.api.items.ItemsTC;
 import thaumcraft.api.potions.PotionFluxTaint;
 import thaumcraft.api.potions.PotionVisExhaust;
 import thaumcraft.common.entities.monster.tainted.EntityTaintCrawler;
 import thaumcraft.common.lib.potions.PotionInfectiousVisExhaust;
 
 
 
 public class EntityTaintPig
   extends EntityPig
 {
     private static final Set<Item> TEMPTATION_ITEMS = Sets.newHashSet(ItemsTC.bottleTaint);
   
   public EntityTaintPig(World worldIn) {
/*  42 */     super(worldIn);
   }
 
   
   protected void initEntityAI() {
/*  47 */     this.tasks.addTask(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
/*  48 */     this.tasks.addTask(1, (EntityAIBase)new EntityAIPanic((EntityCreature)this, 1.25D));
/*  49 */     this.tasks.addTask(2, (EntityAIBase)new EntityAIAttackMelee((EntityCreature)this, 1.0D, false));
/*  50 */     this.tasks.addTask(3, (EntityAIBase)new EntityAIEatTaint((EntityLiving)this));
/*  51 */     this.tasks.addTask(4, (EntityAIBase)new EntityAITaintTempt((EntityCreature)this, 1.2D, false, TEMPTATION_ITEMS));
/*  52 */     this.tasks.addTask(5, (EntityAIBase)new EntityAIWander((EntityCreature)this, 1.0D));
/*  53 */     this.tasks.addTask(6, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 6.0F));
/*  54 */     this.tasks.addTask(7, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
/*  55 */     this.targetTasks.addTask(1, (EntityAIBase)new EntityAINearestAttackableTarget((EntityCreature)this, EntityTaintCrawler.class, true));
   }
 
 
   
   protected void applyEntityAttributes() {
/*  61 */     super.applyEntityAttributes();
/*  62 */     getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
   }
 
   
   public void updateAITasks() {
/*  67 */     super.updateAITasks();
     
/*  69 */     if (isPotionActive(PotionFluxTaint.instance))
/*  70 */       removePotionEffect(PotionFluxTaint.instance); 
/*  71 */     if (isPotionActive(PotionVisExhaust.instance))
/*  72 */       removePotionEffect(PotionVisExhaust.instance); 
/*  73 */     if (isPotionActive(PotionInfectiousVisExhaust.instance)) {
/*  74 */       removePotionEffect(PotionInfectiousVisExhaust.instance);
     }
   }
   
   public boolean attackEntityAsMob(Entity entityIn) {
/*  79 */     setLastAttackedEntity(entityIn);
     
/*  81 */     boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this), 
/*  82 */         (float)getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
     
/*  84 */     if (flag) {
/*  85 */       applyEnchantments((EntityLivingBase)this, entityIn);
     }
     
/*  88 */     return flag;
   }
 
 
   
   public boolean isEntityInvulnerable(DamageSource source) {
/*  94 */     if (source instanceof EntityDamageSource) {
/*  95 */       Entity entity = ((EntityDamageSource)source).getTrueSource();
       
       if (entity instanceof thaumcraft.api.entities.ITaintedMob) {
         return true;
       }
/* 100 */     } else if (source instanceof DamageSourceThaumcraft && ((DamageSourceThaumcraft)source).damageType
/* 101 */       .equals("taint")) {
/* 102 */       return true;
     } 
     
/* 105 */     return super.isEntityInvulnerable(source);
   }
 
   
   public boolean isInLove() {
/* 110 */     return true;
   }
 
   
   public boolean isBreedingItem(ItemStack stack) {
/* 115 */     return false;
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\entities\EntityTaintPig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */