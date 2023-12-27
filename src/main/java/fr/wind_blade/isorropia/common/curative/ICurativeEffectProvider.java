package fr.wind_blade.isorropia.common.curative;

import fr.wind_blade.isorropia.common.tiles.TileVat;
import net.minecraft.entity.EntityLivingBase;
import thaumcraft.api.aspects.Aspect;

public interface ICurativeEffectProvider {
  Aspect getAspect();

  boolean effectCanApply(EntityLivingBase var1, TileVat var2);

  void onApply(EntityLivingBase var1, TileVat var2);

  int getCooldown(EntityLivingBase var1, TileVat var2);
}
