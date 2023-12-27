package fr.wind_blade.isorropia.common;

import fr.wind_blade.isorropia.Isorropia;
import fr.wind_blade.isorropia.common.blocks.BlockCurativeVat;
import fr.wind_blade.isorropia.common.blocks.BlocksIS;
import fr.wind_blade.isorropia.common.capabilities.LivingBaseCapability;
import fr.wind_blade.isorropia.common.capabilities.LivingCapability;
import fr.wind_blade.isorropia.common.casters.foci.FocusEffectContainment;
import fr.wind_blade.isorropia.common.config.Config;
import fr.wind_blade.isorropia.common.curative.CurativeEffects;
import fr.wind_blade.isorropia.common.entities.*;
import fr.wind_blade.isorropia.common.entities.projectile.EntityIncubatedEgg;
import fr.wind_blade.isorropia.common.events.EntityEventHandler;
import fr.wind_blade.isorropia.common.events.RegistryEventHandler;
import fr.wind_blade.isorropia.common.items.ItemsIS;
import fr.wind_blade.isorropia.common.items.JellyAspectEffects;
import fr.wind_blade.isorropia.common.lenses.AirLens;
import fr.wind_blade.isorropia.common.lenses.EnvyLens;
import fr.wind_blade.isorropia.common.lenses.FireLens;
import fr.wind_blade.isorropia.common.lenses.OrdoLens;
import fr.wind_blade.isorropia.common.network.*;
import fr.wind_blade.isorropia.common.research.ResearchsIS;
import fr.wind_blade.isorropia.common.tiles.TileJarSoul;
import fr.wind_blade.isorropia.common.tiles.TileModifiedMatrix;
import fr.wind_blade.isorropia.common.tiles.TileVat;
import fr.wind_blade.isorropia.common.tiles.TileVatConnector;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.casters.FocusEngine;
import thaumcraft.api.crafting.IDustTrigger;
import thaumcraft.api.crafting.Part;
import thaumcraft.common.lib.crafting.DustTriggerMultiblock;

public class Common {
    @CapabilityInject(value=LivingBaseCapability.class)
    public static final Capability<LivingBaseCapability> LIVING_BASE_CAPABILITY = null;
    @CapabilityInject(value=LivingCapability.class)
    public static final Capability<LivingCapability> LIVING_CAPABILITY = null;
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("isorropia");
    public static final CreativeTabs isorropiaCreativeTabs = new IsorropiaCreativeTabs();

    public void preInit(FMLPreInitializationEvent event) {
        CapabilityManager.INSTANCE.register(LivingBaseCapability.class, new LivingBaseCapability.Storage(), new LivingBaseCapability.Factory());
        CapabilityManager.INSTANCE.register(LivingCapability.class, new LivingCapability.Storage(), new LivingCapability.Factory());
        Common.initTileEntity();
        Common.initHandlers();
        Common.initNetwork();
        Common.initEntities();
        Config.init(event);
    }

    public void init(FMLInitializationEvent event) {
        Config.initOreDictionary();
        ResearchsIS.init();
        GameRegistry.addSmelting(new ItemStack(ItemsIS.itemGoldEgg) ,new ItemStack(net.minecraft.init.Items.GOLD_NUGGET),0.15f);
    }

    public void postInit(FMLPostInitializationEvent event) {
        Common.initMultiBlocks();
        ResearchsIS.initAspects(event);
    }

    private static void initTileEntity() {
        GameRegistry.registerTileEntity(TileVat.class, new ResourceLocation("isorropia", "curative_vat"));
        GameRegistry.registerTileEntity(TileVatConnector.class, new ResourceLocation("isorropia", "curative_connector"));
        GameRegistry.registerTileEntity(TileJarSoul.class, new ResourceLocation("isorropia", "soul_jar"));
        GameRegistry.registerTileEntity(TileModifiedMatrix.class, new ResourceLocation("isorropia", "modified_matrix"));
    }

    private static void initEntities() {
        Common.registerEntity("incubated_egg", EntityIncubatedEgg.class, 64, 1, true);
        Common.registerEntity("taint_pig", EntityTaintPig.class, 64, 3, true);
        Common.registerEntity("gravekeeper", EntityGravekeeper.class, 64, 3, true);
        Common.registerEntity("sheeder", EntitySheeder.class, 64, 3, true);
        Common.registerEntity("chromatic_sheep", EntityChromaticSheep.class, 64, 3, true);
        Common.registerEntity("scholar_chicken", EntityScholarChicken.class, 64, 3, true);
        Common.registerEntity("ore_pig", EntityOrePig.class, 64, 3, true);
        Common.registerEntity("jelly_rabbit", EntityJellyRabbit.class, 64, 3, true);
        Common.registerEntity("hanging_label", EntityHangingLabel.class, 160, Integer.MAX_VALUE, false);
        registerEntity("chocolate_cow", EntityChocow.class, 64, 3, true);
        registerEntity("golden_chicken", EntityGoldenChicken.class, 64, 3, true);
    }

    public static void initProviders() {
        FocusEngine.registerElement(FocusEffectContainment.class, new ResourceLocation("isorropia", "textures/misc/containment.png"), -13411841);
        IsorropiaAPI.registerLens(new AirLens(ItemsIS.itemAirLens), new ResourceLocation("isorropia", "air_lens"));
        IsorropiaAPI.registerLens(new FireLens(ItemsIS.itemFireLens), new ResourceLocation("isorropia", "fire_lens"));
        IsorropiaAPI.registerLens(new OrdoLens(ItemsIS.itemOrdoLens), new ResourceLocation("isorropia", "ordo_lens"));
        IsorropiaAPI.registerLens(new EnvyLens(ItemsIS.itemEnvyLens), new ResourceLocation("isorropia", "envy_lens"));
        IsorropiaAPI.registerCurativeEffect(new CurativeEffects.IGNIS_CURE());
        IsorropiaAPI.registerCurativeEffect(new CurativeEffects.LIFE_HEAL());
        IsorropiaAPI.registerCurativeEffect(new CurativeEffects.HUNGER_FOOD());
        IsorropiaAPI.registerCurativeEffect(new CurativeEffects.DEATH_HEAL());
        IsorropiaAPI.registerCurativeEffect(new CurativeEffects.UNDEAD_HEAL());
        IsorropiaAPI.bindJellyAspectEffect(null, new JellyAspectEffects.DEFAULT_EFFECT());
        IsorropiaAPI.bindJellyAspectEffect(Aspect.MOTION, new JellyAspectEffects.MOTION_EFFECT());
        IsorropiaAPI.bindJellyAspectEffect(Aspect.LIFE, new JellyAspectEffects.LIFE_EFFECT());
        IsorropiaAPI.bindJellyAspectEffect(Aspect.EXCHANGE, new JellyAspectEffects.EXCHANGE_EFFECT());
        IsorropiaAPI.bindJellyAspectEffect(Aspect.PROTECT, new JellyAspectEffects.PROTECT_EFFECT());
    }

    private static void initHandlers() {
        MinecraftForge.EVENT_BUS.register(new EntityEventHandler());
    }

    private static void initNetwork() {
        INSTANCE.registerMessage(MirrorMessage.MirrorMessageHandler.class, MirrorMessage.class, 0, Side.SERVER);
        INSTANCE.registerMessage(LensChangeMessage.LensChangeMessageHandler.class, LensChangeMessage.class, 1, Side.SERVER);
        INSTANCE.registerMessage(CapabilityMessage.ClientLivingBaseCapabilityHandler.class, CapabilityMessage.class, 2, Side.CLIENT);
        INSTANCE.registerMessage(ISPacketFXInfusionSource.class, ISPacketFXInfusionSource.class, 4, Side.CLIENT);
        INSTANCE.registerMessage(TrackingCapabilityMessage.ClientLivingBaseCapabilityHandler.class, TrackingCapabilityMessage.class, 5, Side.CLIENT);
        INSTANCE.registerMessage(ParticuleDestroyMessage.Handler.class, ParticuleDestroyMessage.class, 6, Side.CLIENT);
        INSTANCE.registerMessage(LensRemoveMessage.Handler.class, LensRemoveMessage.class, 7, Side.CLIENT);
        INSTANCE.registerMessage(LensRemoveMessageSP.Handler.class, LensRemoveMessageSP.class, 8, Side.SERVER);
    }

    private static void initMultiBlocks() {
        Part pl = new Part(BlocksTC.plankGreatwood, BlocksIS.blockCurativeVat);
        Part co = new Part(BlocksTC.plankGreatwood, new ItemStack(BlocksIS.blockCurativeVat, 1, BlockCurativeVat.Type.CONNECTOR.getMetadata()));
        Part top = new Part(BlocksTC.metalAlchemical, new ItemStack(BlocksIS.blockCurativeVat, 1, BlockCurativeVat.Type.TOP.getMetadata()));
        Part bottom = new Part(BlocksTC.metalAlchemical, new ItemStack(BlocksIS.blockCurativeVat, 1, BlockCurativeVat.Type.BOTTOM.getMetadata()));
        Part gl = new Part(Blocks.GLASS, BlocksIS.blockCurativeVat);
        Part in = new Part(Material.WATER, BlocksIS.blockVatInterior);
        Part[][][] curative_vat = new Part[][][]{{{pl, pl, pl}, {pl, top, pl}, {pl, pl, pl}}, {{gl, gl, gl}, {gl, in, gl}, {gl, gl, gl}}, {{gl, gl, gl}, {gl, in, gl}, {gl, gl, gl}}, {{pl, co, pl}, {co, bottom, co}, {pl, co, pl}}};
        IDustTrigger.registerDustTrigger(new DustTriggerMultiblock("CURATIVEVAT", curative_vat));
        curative_vat = (Part[][][])curative_vat.clone();
        Part[][][] crafting_recipe = new Part[][][]{{{pl, pl, pl}, {pl, top, pl}, {pl, pl, pl}}, {{gl, gl, gl}, {gl, null, gl}, {gl, gl, gl}}, {{gl, gl, gl}, {gl, null, gl}, {gl, gl, gl}}, {{pl, co, pl}, {co, bottom, co}, {pl, co, pl}}};
        ThaumcraftApi.addFakeCraftingRecipe(new ResourceLocation("isorropia", "curative_vat"), new ThaumcraftApi.BluePrint("CURATIVEVAT", crafting_recipe, new ItemStack[]{new ItemStack(BlocksTC.plankGreatwood, 16), new ItemStack(Blocks.GLASS, 16), new ItemStack(BlocksTC.metalAlchemical, 2, 0), new ItemStack(Items.WATER_BUCKET, 2)}));
    }

    public static void initOreDictionary() {
        OreDictionary.registerOre("slimeball", new ItemStack(ItemsIS.itemJelly));
        OreDictionary.registerOre("dyeBlack", new ItemStack(ItemsIS.itemInkEgg));
    }

    private static void registerEntity(String name, Class<? extends Entity> entityClass, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
        ResourceLocation loc = new ResourceLocation("isorropia", name);
        EntityRegistry.registerModEntity(loc, entityClass, name, RegistryEventHandler.entities_id++, Isorropia.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
        if (EntityLiving.class.isAssignableFrom(entityClass)) {
            EntityRegistry.registerEgg(loc, 0, 1);
        }
        RegistryEventHandler.ENTITIES.put(name, entityClass);
    }

    public static LivingBaseCapability getCap(EntityLivingBase base) {
        if (base instanceof EntityLiving) {
            return (LivingBaseCapability)base.getCapability(LIVING_CAPABILITY, null);
        }
        return (LivingBaseCapability)base.getCapability(LIVING_BASE_CAPABILITY, null);
    }
}