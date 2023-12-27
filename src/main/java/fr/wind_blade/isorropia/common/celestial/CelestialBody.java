package fr.wind_blade.isorropia.common.celestial;

import fr.wind_blade.isorropia.common.IsorropiaAPI;
import java.util.function.BiPredicate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;



public class CelestialBody implements ICelestialBody

{

    public static final ICelestialBody NONE = new CelestialBody(new ResourceLocation("isorropia", "none"), null); public static final ICelestialBody SUN; static {

        SUN = new CelestialBody(new ResourceLocation("isorropia", "sun"), new ResourceLocation("thaumcraft", "textures/items/celestial/sun.png"), (player, world) -> isDay(world));


        STARS = new CelestialBody(new ResourceLocation("isorropia", "stars"), new ResourceLocation("thaumcraft", "textures/items/celestial/stars1.png"), (player, world) -> isNight(world));

    }

    public static final ICelestialBody STARS;

    public static final ICelestialBody MOON = new CelestialBodyMoon(new ResourceLocation("isorropia", "moon"), new ResourceLocation("thaumcraft", "textures/items/celestial/moon1.png"), -1);



    public static final ICelestialBody FULL = new CelestialBodyMoon(new ResourceLocation("isorropia", "moon_full"), new ResourceLocation("thaumcraft", "textures/items/celestial/moon2.png"), 0);



    public static final ICelestialBody WANING_GIBBOUS = new CelestialBodyMoon(new ResourceLocation("isorropia", "moon_waning_gibbous"), new ResourceLocation("thaumcraft", "textures/items/celestial/moon3.png"), 1);





    public static final ICelestialBody THIRD_QARTER = new CelestialBodyMoon(new ResourceLocation("isorropia", "moon_third_qarter"), new ResourceLocation("thaumcraft", "textures/items/celestial/moon4.png"), 2);





    public static final ICelestialBody WANING_CRESCENT = new CelestialBodyMoon(new ResourceLocation("isorropia", "moon_waning_crescent"), new ResourceLocation("thaumcraft", "textures/items/celestial/moon5.png"), 3);





    public static final ICelestialBody NEW = new CelestialBodyMoon(new ResourceLocation("isorropia", "moon_new"), new ResourceLocation("thaumcraft", "textures/items/celestial/moon6.png"), 4);



    public static final ICelestialBody WAXING_CRESCENT = new CelestialBodyMoon(new ResourceLocation("isorropia", "moon_waxing_crescent"), new ResourceLocation("thaumcraft", "textures/items/celestial/moon7.png"), 5);





    public static final ICelestialBody FIRST_QUARTER = new CelestialBodyMoon(new ResourceLocation("isorropia", "moon_first_quarter"), new ResourceLocation("thaumcraft", "textures/items/celestial/moon8.png"), 6);



    protected final BiPredicate<EntityPlayer, World> seenPredicate;



    protected final ResourceLocation registryName;



    protected final ResourceLocation tex;



    public CelestialBody(ResourceLocation registryName, ResourceLocation tex) {

        this(registryName, tex, (player, world) -> true);

    }





    public CelestialBody(ResourceLocation registryName, ResourceLocation tex, BiPredicate<EntityPlayer, World> seenPredicate) {

        IsorropiaAPI.registerCelestialBody(registryName, this);

        this.seenPredicate = seenPredicate;

        this.registryName = registryName;

        this.tex = tex;

    }





    public boolean canBeSeen(EntityPlayer player, World worldIn) {

        return this.seenPredicate.test(player, worldIn);

    }





    public boolean canBeDrained(EntityPlayer player, World worldIn) {

        return canBeSeen(player, worldIn);

    }





    public boolean isAuraEquals(EntityPlayer player, World worldIn, ICelestialBody newCelestialBody) {

        return false;

    }





    public float auraDrainedFactor(EntityPlayer player, World worldIn) {

        return 1.0F;

    }





    public ResourceLocation getRegistryName() {

        return this.registryName;

    }



    public BiPredicate<EntityPlayer, World> getSeenPredicate() {

        return this.seenPredicate;

    }



    public static boolean isNight(World world) {

        return ((world.getCelestialAngle(0.0F) + 0.25D) * 360.0D % 360.0D > 180.0D);

    }



    public static boolean isDay(World world) {

        return !isNight(world);

    }





    public ResourceLocation getTex() {

        return this.tex;

    }

}
