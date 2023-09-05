package fr.wind_blade.isorropia.common.curative;

import fr.wind_blade.isorropia.common.tiles.TileVat;
import net.minecraft.entity.EntityLivingBase;
import thaumcraft.api.aspects.Aspect;

public interface ICurativeEffectProvider {
  Aspect getAspect();
  
  boolean effectCanApply(EntityLivingBase paramEntityLivingBase, TileVat paramTileVat);
  
  void onApply(EntityLivingBase paramEntityLivingBase, TileVat paramTileVat);
  
  int getCooldown(EntityLivingBase paramEntityLivingBase, TileVat paramTileVat);
}


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\curative\ICurativeEffectProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */