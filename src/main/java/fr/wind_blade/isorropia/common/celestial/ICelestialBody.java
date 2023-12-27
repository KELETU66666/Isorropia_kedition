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