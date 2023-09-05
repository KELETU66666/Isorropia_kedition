 package fr.wind_blade.isorropia.common.celestial;
 
 import java.util.function.BiPredicate;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.util.ResourceLocation;
 import net.minecraft.world.World;
 
 public class CelestialBodyMoon
   extends CelestialBody
 {
   public final int moonPhase;
   
   public CelestialBodyMoon(ResourceLocation registryName, ResourceLocation tex, int moonPhase) {
/* 14 */     this(registryName, tex, (player, worldIn) -> (!worldIn.isRaining() && CelestialBody.isNight(worldIn) && (moonPhase == -1 || worldIn.getMoonPhase() == moonPhase)), moonPhase);
   }
 
 
   
   public CelestialBodyMoon(ResourceLocation registryName, ResourceLocation tex, BiPredicate<EntityPlayer, World> biPredicate, int moonPhase) {
/* 20 */     super(registryName, tex, biPredicate);
/* 21 */     this.moonPhase = moonPhase;
   }
 
   
   public boolean isAuraEquals(EntityPlayer player, World worldIn, ICelestialBody newCelestialBody) {
      return (newCelestialBody instanceof CelestialBodyMoon && this != CelestialBody.MOON && newCelestialBody == CelestialBody.MOON);
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\celestial\CelestialBodyMoon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */