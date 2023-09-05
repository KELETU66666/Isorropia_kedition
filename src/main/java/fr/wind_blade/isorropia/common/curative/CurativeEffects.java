// Decompiled with: CFR 0.152
// Class Version: 8
package fr.wind_blade.isorropia.common.curative;

import fr.wind_blade.isorropia.common.IsorropiaAPI;
import fr.wind_blade.isorropia.common.curative.ICurativeEffectProvider;
import fr.wind_blade.isorropia.common.tiles.TileVat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.entities.ITaintedMob;
import thaumcraft.common.entities.monster.mods.ChampionModifier;

public class CurativeEffects {

  public static class UNDEAD_HEAL
          implements ICurativeEffectProvider {
    @Override
    public Aspect getAspect() {
      return Aspect.UNDEAD;
    }

    @Override
    public boolean effectCanApply(EntityLivingBase contained, TileVat vat) {
      return contained.isEntityUndead() && contained.getHealth() < contained.getMaxHealth();
    }

    @Override
    public void onApply(EntityLivingBase contained, TileVat vat) {
      contained.heal(8.0f);
    }

    @Override
    public int getCooldown(EntityLivingBase contained, TileVat vat) {
      return 40;
    }
  }

  public static class DEATH_HEAL
          implements ICurativeEffectProvider {
    @Override
    public Aspect getAspect() {
      return Aspect.DEATH;
    }

    @Override
    public boolean effectCanApply(EntityLivingBase contained, TileVat vat) {
      return contained.isEntityUndead() && contained.getHealth() < contained.getMaxHealth();
    }

    @Override
    public void onApply(EntityLivingBase contained, TileVat vat) {
      contained.heal(4.0f);
    }

    @Override
    public int getCooldown(EntityLivingBase contained, TileVat vat) {
      return 50;
    }
  }

  public static class HUNGER_FOOD
          implements ICurativeEffectProvider {
    @Override
    public Aspect getAspect() {
      return IsorropiaAPI.HUNGER;
    }

    @Override
    public boolean effectCanApply(EntityLivingBase contained, TileVat vat) {
      return contained instanceof EntityPlayer && ((EntityPlayer)contained).getFoodStats().needFood();
    }

    @Override
    public void onApply(EntityLivingBase contained, TileVat vat) {
      ((EntityPlayer)contained).getFoodStats().addStats(1, 1.0f);
    }

    @Override
    public int getCooldown(EntityLivingBase contained, TileVat vat) {
      return 25;
    }
  }

  public static class LIFE_HEAL
          implements ICurativeEffectProvider {
    @Override
    public Aspect getAspect() {
      return Aspect.LIFE;
    }

    @Override
    public boolean effectCanApply(EntityLivingBase contained, TileVat vat) {
      return !contained.isEntityUndead() && contained.getHealth() < contained.getMaxHealth();
    }

    @Override
    public void onApply(EntityLivingBase contained, TileVat vat) {
      contained.heal(1.0f);
    }

    @Override
    public int getCooldown(EntityLivingBase contained, TileVat vat) {
      return 50;
    }
  }

  public static class IGNIS_CURE
          implements ICurativeEffectProvider {
    @Override
    public Aspect getAspect() {
      return Aspect.FIRE;
    }

    @Override
    public boolean effectCanApply(EntityLivingBase contained, TileVat vat) {
      return contained instanceof ITaintedMob || contained.getEntityAttribute(ThaumcraftApiHelper.CHAMPION_MOD) != null && contained.getEntityAttribute(ThaumcraftApiHelper.CHAMPION_MOD).getAttributeValue() == 13.0;
    }

    @Override
    public void onApply(EntityLivingBase contained, TileVat vat) {
      if (contained instanceof ITaintedMob) {
        contained.attackEntityFrom(DamageSource.MAGIC, 1.0f);
      } else if (vat.getWorld().rand.nextInt(100) < 3) {
        contained.getEntityAttribute(ThaumcraftApiHelper.CHAMPION_MOD).removeModifier(ChampionModifier.mods[13].attributeMod);
      }
    }

    @Override
    public int getCooldown(EntityLivingBase contained, TileVat vat) {
      return 4;
    }
  }
}
