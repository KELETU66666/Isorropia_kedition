 package fr.wind_blade.isorropia.common.curative;
 
 import fr.wind_blade.isorropia.common.IsorropiaAPI;
 import fr.wind_blade.isorropia.common.tiles.TileVat;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.util.DamageSource;
 import thaumcraft.api.ThaumcraftApiHelper;
 import thaumcraft.api.aspects.Aspect;
 import thaumcraft.common.entities.monster.mods.ChampionModifier;
 
 
 public class CurativeEffects
 {
   public static class IGNIS_CURE
     implements ICurativeEffectProvider
   {
     public Aspect getAspect() {
/*  19 */       return Aspect.FIRE;
     }
 
     
     public boolean effectCanApply(EntityLivingBase contained, TileVat vat) {
/*  24 */       return (contained instanceof thaumcraft.api.entities.ITaintedMob || (contained
/*  25 */         .getEntityAttribute(ThaumcraftApiHelper.CHAMPION_MOD) != null && contained
/*  26 */         .getEntityAttribute(ThaumcraftApiHelper.CHAMPION_MOD).getAttributeValue() == 13.0D));
     }
 
     
     public void onApply(EntityLivingBase contained, TileVat vat) {
/*  31 */       if (contained instanceof thaumcraft.api.entities.ITaintedMob) {
/*  32 */         contained.attackEntityFrom(DamageSource.MAGIC, 1.0F);
/*  33 */       } else if ((vat.getWorld()).rand.nextInt(100) < 3) {
/*  34 */         contained.getEntityAttribute(ThaumcraftApiHelper.CHAMPION_MOD)
/*  35 */           .removeModifier((ChampionModifier.mods[13]).attributeMod);
       } 
     }
     
     public int getCooldown(EntityLivingBase contained, TileVat vat) {
/*  40 */       return 4;
     }
   }
   
   public static class LIFE_HEAL
     implements ICurativeEffectProvider
   {
     public Aspect getAspect() {
/*  48 */       return Aspect.LIFE;
     }
 
     
     public boolean effectCanApply(EntityLivingBase contained, TileVat vat) {
/*  53 */       return (!contained.isEntityUndead() && contained.getHealth() < contained.getMaxHealth());
     }
 
     
     public void onApply(EntityLivingBase contained, TileVat vat) {
/*  58 */       contained.heal(1.0F);
     }
 
     
     public int getCooldown(EntityLivingBase contained, TileVat vat) {
/*  63 */       return 50;
     }
   }
   
   public static class HUNGER_FOOD
     implements ICurativeEffectProvider
   {
     public Aspect getAspect() {
/*  71 */       return IsorropiaAPI.HUNGER;
     }
 
     
     public boolean effectCanApply(EntityLivingBase contained, TileVat vat) {
/*  76 */       return (contained instanceof EntityPlayer && ((EntityPlayer)contained).getFoodStats().needFood());
     }
 
     
     public void onApply(EntityLivingBase contained, TileVat vat) {
/*  81 */       ((EntityPlayer)contained).getFoodStats().addStats(1, 1.0F);
     }
 
     
     public int getCooldown(EntityLivingBase contained, TileVat vat) {
/*  86 */       return 25;
     }
   }
   
   public static class DEATH_HEAL
     implements ICurativeEffectProvider
   {
     public Aspect getAspect() {
/*  94 */       return Aspect.DEATH;
     }
 
     
     public boolean effectCanApply(EntityLivingBase contained, TileVat vat) {
/*  99 */       return (contained.isEntityUndead() && contained.getHealth() < contained.getMaxHealth());
     }
 
     
     public void onApply(EntityLivingBase contained, TileVat vat) {
/* 104 */       contained.heal(4.0F);
     }
 
     
     public int getCooldown(EntityLivingBase contained, TileVat vat) {
/* 109 */       return 50;
     }
   }
 
   
   public static class UNDEAD_HEAL
     implements ICurativeEffectProvider
   {
     public Aspect getAspect() {
/* 118 */       return Aspect.UNDEAD;
     }
 
     
     public boolean effectCanApply(EntityLivingBase contained, TileVat vat) {
/* 123 */       return (contained.isEntityUndead() && contained.getHealth() < contained.getMaxHealth());
     }
 
     
     public void onApply(EntityLivingBase contained, TileVat vat) {
/* 128 */       contained.heal(8.0F);
     }
 
     
     public int getCooldown(EntityLivingBase contained, TileVat vat) {
       return 40;
     }
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\curative\CurativeEffects.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */