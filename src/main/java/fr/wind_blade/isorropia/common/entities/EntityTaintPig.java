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
   super(worldIn);
   }
 
   
   protected void initEntityAI() {
      this.tasks.addTask(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
      this.tasks.addTask(1, (EntityAIBase)new EntityAIPanic((EntityCreature)this, 1.25D));
      this.tasks.addTask(2, (EntityAIBase)new EntityAIAttackMelee((EntityCreature)this, 1.0D, false));
      this.tasks.addTask(3, (EntityAIBase)new EntityAIEatTaint((EntityLiving)this));
      this.tasks.addTask(4, (EntityAIBase)new EntityAITaintTempt((EntityCreature)this, 1.2D, false, TEMPTATION_ITEMS));
      this.tasks.addTask(5, (EntityAIBase)new EntityAIWander((EntityCreature)this, 1.0D));
      this.tasks.addTask(6, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 6.0F));
     this.tasks.addTask(7, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
     this.targetTasks.addTask(1, (EntityAIBase)new EntityAINearestAttackableTarget((EntityCreature)this, EntityTaintCrawler.class, true));
   }
 
 
   
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
   }
 
   
   public void updateAITasks() {
      super.updateAITasks();
     
/*  69 */     if (isPotionActive(PotionFluxTaint.instance))
/*  70 */       removePotionEffect(PotionFluxTaint.instance); 
      if (isPotionActive(PotionVisExhaust.instance))
/*  72 */       removePotionEffect(PotionVisExhaust.instance); 
      if (isPotionActive(PotionInfectiousVisExhaust.instance)) {
        removePotionEffect(PotionInfectiousVisExhaust.instance);
     }
   }
   
   public boolean attackEntityAsMob(Entity entityIn) {
      setLastAttackedEntity(entityIn);
     
      boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this), 
          (float)getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
     
      if (flag) {
        applyEnchantments((EntityLivingBase)this, entityIn);
     }
     
      return flag;
   }
 
 
   
   public boolean isEntityInvulnerable(DamageSource source) {
      if (source instanceof EntityDamageSource) {
        Entity entity = ((EntityDamageSource)source).getTrueSource();
       
       if (entity instanceof thaumcraft.api.entities.ITaintedMob) {
         return true;
       }
      } else if (source instanceof DamageSourceThaumcraft && ((DamageSourceThaumcraft)source).damageType
        .equals("taint")) {
/* 102 */       return true;
     } 
     
    return super.isEntityInvulnerable(source);
   }
 
   
   public boolean isInLove() {
      return true;
   }
 
   
   public boolean isBreedingItem(ItemStack stack) {
/* 115 */     return false;
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\entities\EntityTaintPig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */