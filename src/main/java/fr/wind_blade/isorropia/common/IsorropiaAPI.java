package fr.wind_blade.isorropia.common;

import fr.wind_blade.isorropia.common.celestial.CelestialBody;
import fr.wind_blade.isorropia.common.celestial.ICelestialBody;
import fr.wind_blade.isorropia.common.curative.ICurativeEffectProvider;
import fr.wind_blade.isorropia.common.items.IJellyAspectEffectProvider;
import fr.wind_blade.isorropia.common.items.ItemsIS;
import fr.wind_blade.isorropia.common.lenses.Lens;
import fr.wind_blade.isorropia.common.research.recipes.CurativeInfusionRecipe;
import fr.wind_blade.isorropia.common.tiles.TileVat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.registries.ForgeRegistry;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;

public class IsorropiaAPI {
    public static final Ingredient DEFAULT_INFUSION_FAKE = Ingredient.fromItem((Item)ItemsIS.itemCat);
    public static final Aspect HUNGER = new Aspect("fames", 10093317, new Aspect[]{Aspect.LIFE, Aspect.VOID}, new ResourceLocation("isorropia", "textures/aspects/fames.png"), 1);
    public static final Aspect FLESH = new Aspect("corpus", 15615885, new Aspect[]{Aspect.DEATH, Aspect.BEAST}, new ResourceLocation("isorropia", "textures/aspects/corpus.png"), 1);
    public static final Aspect ENVY = new Aspect("invidia", 47616, new Aspect[]{Aspect.SENSES, HUNGER}, new ResourceLocation("isorropia", "textures/aspects/invidia.png"), 1);
    public static final Aspect GLUTTONY = new Aspect("gula", 13999174, new Aspect[]{HUNGER, Aspect.VOID}, new ResourceLocation("isorropia", "textures/aspects/gula.png"), 1);
    public static final Aspect LUST = new Aspect("luxuria", 16761294, new Aspect[]{FLESH, HUNGER}, new ResourceLocation("isorropia", "textures/aspects/luxuria.png"), 1);
    public static final Aspect NETHER = new Aspect("infernus", 0xFF0000, new Aspect[]{Aspect.FIRE, Aspect.MAGIC}, new ResourceLocation("isorropia", "textures/aspects/infernus.png"), 771);
    public static final Aspect PRIDE = new Aspect("superbia", 9845247, new Aspect[]{Aspect.FLIGHT, Aspect.VOID}, new ResourceLocation("isorropia", "textures/aspects/superbia.png"), 1);
    public static final Aspect SLOTH = new Aspect("desidia", 0x6E6E6E, new Aspect[]{Aspect.TRAP, Aspect.SOUL}, new ResourceLocation("isorropia", "textures/aspects/desidia.png"), 771);
    public static final Aspect WRATH = new Aspect("ira", 8848388, new Aspect[]{Aspect.AVERSION, Aspect.FIRE}, new ResourceLocation("isorropia", "textures/aspects/ira.png"), 771);
    public static Lens air_lens;
    public static Lens fire_lens;
    public static Lens ordo_lens;
    public static Lens lust_lens;
    public static Lens envy_lens;
    public static Lens gluttony_lens;
    public static ForgeRegistry<Lens> lensRegistry;
    public static Map<ResourceLocation, Lens> lens;
    public static Map<ResourceLocation, CurativeInfusionRecipe> creatureInfusionRecipes;
    public static Map<CurativeInfusionRecipe, ResourceLocation> creatureInfusionRecipesLocal;
    private static final HashMap<ResourceLocation, ICelestialBody> registryCelestialBody;
    public static final List<ICurativeEffectProvider> curativeEffects;
    private static final Map<Aspect, IJellyAspectEffectProvider> jellyEffects;

    private IsorropiaAPI() {
    }

    @Deprecated
    public static void registerLens(Lens lens, ResourceLocation registryName) {
        IsorropiaAPI.lens.put(registryName, lens);
    }

    public static void registerCelestialBody(ResourceLocation registryName, ICelestialBody celestialBody) {
        if (registryName == null || celestialBody == null) {
            FMLLog.log.debug("Skipping automatic mod {} celestial body registration, celestial body or his registry name can't be null {} class {}", registryName.getPath(), registryName, celestialBody.getClass());
        } else {
            registryCelestialBody.put(registryName, celestialBody);
        }
    }

    public static void registerCreatureInfusionRecipe(ResourceLocation registryLocation, CurativeInfusionRecipe recipeIn) {
        if (!creatureInfusionRecipes.containsKey(registryLocation)) {
            creatureInfusionRecipes.put(registryLocation, recipeIn);
            creatureInfusionRecipesLocal.put(recipeIn, registryLocation);
            ThaumcraftApi.addFakeCraftingRecipe(registryLocation, recipeIn);
        }
    }

    public static void registerCurativeEffect(ICurativeEffectProvider effect) {
        if (!curativeEffects.contains(effect)) {
            curativeEffects.add(effect);
        }
    }

    public static void bindJellyAspectEffect(Aspect aspect, IJellyAspectEffectProvider provider) {
        jellyEffects.put(aspect, provider);
    }

    public static IJellyAspectEffectProvider getJellyAspectEffect(Aspect aspect) {
        return jellyEffects.get(aspect);
    }

    public static CurativeInfusionRecipe findMatchingCreatureInfusionRecipe(EntityLivingBase entityContained, ArrayList<ItemStack> components, EntityPlayer player, TileVat vat) {
        CurativeInfusionRecipe recipe;
        Iterator<CurativeInfusionRecipe> var3 = creatureInfusionRecipes.values().iterator();
        do {
            if (var3.hasNext()) continue;
            return null;
        } while (!(recipe = var3.next()).matches(components, entityContained, player.world, player, vat));
        return recipe;
    }

    public static ICelestialBody getCelestialBodyByRegistryName(ResourceLocation registryName) {
        return registryCelestialBody.values().stream().filter(body -> body.getRegistryName().equals(registryName)).findFirst().orElse(CelestialBody.NONE);
    }

    static {
        lens = new HashMap<>();
        creatureInfusionRecipes = new HashMap<>();
        creatureInfusionRecipesLocal = new HashMap<>();
        registryCelestialBody = new HashMap();
        curativeEffects = new ArrayList<>();
        jellyEffects = new HashMap<>();
    }
}
