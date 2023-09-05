package fr.wind_blade.isorropia.common.celestial;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public interface ICelestialBody {
  boolean canBeSeen(EntityPlayer paramEntityPlayer, World paramWorld);
  
  boolean canBeDrained(EntityPlayer paramEntityPlayer, World paramWorld);
  
  boolean isAuraEquals(EntityPlayer paramEntityPlayer, World paramWorld, ICelestialBody paramICelestialBody);
  
  float auraDrainedFactor(EntityPlayer paramEntityPlayer, World paramWorld);
  
  ResourceLocation getRegistryName();
  
  ResourceLocation getTex();
}


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\celestial\ICelestialBody.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */