package fr.wind_blade.isorropia.common.celestial;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.function.BiPredicate;

public class CelestialBodyMoon
        extends CelestialBody
{
    public final int moonPhase;

    public CelestialBodyMoon(ResourceLocation registryName, ResourceLocation tex, int moonPhase) {
        this(registryName, tex, (player, worldIn) -> (!worldIn.isRaining() && CelestialBody.isNight(worldIn) && (moonPhase == -1 || worldIn.getMoonPhase() == moonPhase)), moonPhase);
    }



    public CelestialBodyMoon(ResourceLocation registryName, ResourceLocation tex, BiPredicate<EntityPlayer, World> biPredicate, int moonPhase) {
        super(registryName, tex, biPredicate);
        this.moonPhase = moonPhase;
    }


    public boolean isAuraEquals(EntityPlayer player, World worldIn, ICelestialBody newCelestialBody) {
        return (newCelestialBody instanceof CelestialBodyMoon && this != CelestialBody.MOON && newCelestialBody == CelestialBody.MOON);
    }
}