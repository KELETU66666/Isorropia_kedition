 package fr.wind_blade.isorropia.common;
 import fr.wind_blade.isorropia.Isorropia;
 import fr.wind_blade.isorropia.common.blocks.BlockCurativeVat;
 import fr.wind_blade.isorropia.common.blocks.BlocksIS;
 import fr.wind_blade.isorropia.common.capabilities.LivingBaseCapability;
 import fr.wind_blade.isorropia.common.capabilities.LivingCapability;
 import fr.wind_blade.isorropia.common.casters.foci.FocusEffectContainment;
 import fr.wind_blade.isorropia.common.config.Config;
 import fr.wind_blade.isorropia.common.curative.CurativeEffects;
 import fr.wind_blade.isorropia.common.curative.ICurativeEffectProvider;
 import fr.wind_blade.isorropia.common.entities.*;
 import fr.wind_blade.isorropia.common.entities.projectile.EntityIncubatedEgg;
 import fr.wind_blade.isorropia.common.events.EntityEventHandler;
 import fr.wind_blade.isorropia.common.events.IRGuiHandler;
 import fr.wind_blade.isorropia.common.events.RegistryEventHandler;
 import fr.wind_blade.isorropia.common.items.IJellyAspectEffectProvider;
 import fr.wind_blade.isorropia.common.items.ItemsIS;
 import fr.wind_blade.isorropia.common.items.JellyAspectEffects;
 import fr.wind_blade.isorropia.common.lenses.AirLens;
 import fr.wind_blade.isorropia.common.lenses.EnvyLens;
 import fr.wind_blade.isorropia.common.lenses.FireLens;
 import fr.wind_blade.isorropia.common.lenses.Lens;
 import fr.wind_blade.isorropia.common.lenses.OrdoLens;
 import fr.wind_blade.isorropia.common.network.CapabilityMessage;
 import fr.wind_blade.isorropia.common.network.ISPacketFXInfusionSource;
 import fr.wind_blade.isorropia.common.network.LensChangeMessage;
 import fr.wind_blade.isorropia.common.network.LensRemoveMessage;
 import fr.wind_blade.isorropia.common.network.LensRemoveMessageSP;
 import fr.wind_blade.isorropia.common.network.MagnetMessage;
 import fr.wind_blade.isorropia.common.network.MirrorMessage;
 import fr.wind_blade.isorropia.common.network.ParticuleDestroyMessage;
 import fr.wind_blade.isorropia.common.network.TrackingCapabilityMessage;
 import fr.wind_blade.isorropia.common.research.ResearchsIS;
 import fr.wind_blade.isorropia.common.tiles.TileJarSoul;
 import fr.wind_blade.isorropia.common.tiles.TileModifiedMatrix;
 import fr.wind_blade.isorropia.common.tiles.TileVat;
 import fr.wind_blade.isorropia.common.tiles.TileVatConnector;
 import java.util.concurrent.Callable;
 import net.minecraft.block.Block;
 import net.minecraft.block.material.Material;
 import net.minecraft.creativetab.CreativeTabs;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.EntityLiving;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.init.Blocks;
 import net.minecraft.init.Items;
 import net.minecraft.item.Item;
 import net.minecraft.item.ItemStack;
 import net.minecraft.util.ResourceLocation;
 import net.minecraftforge.common.MinecraftForge;
 import net.minecraftforge.common.capabilities.Capability;
 import net.minecraftforge.common.capabilities.CapabilityInject;
 import net.minecraftforge.common.capabilities.CapabilityManager;
 import net.minecraftforge.fml.common.event.FMLInitializationEvent;
 import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
 import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
 import net.minecraftforge.fml.common.network.IGuiHandler;
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
   @CapabilityInject(LivingBaseCapability.class)
/*  76 */   public static final Capability<LivingBaseCapability> LIVING_BASE_CAPABILITY = null;
   
   @CapabilityInject(LivingCapability.class)
/*  79 */   public static final Capability<LivingCapability> LIVING_CAPABILITY = null;
   
/*  81 */   public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("isorropia");
/*  82 */   public static final CreativeTabs isorropiaCreativeTabs = new IsorropiaCreativeTabs();
   
   public void preInit(FMLPreInitializationEvent event) {
/*  85 */     CapabilityManager.INSTANCE.register(LivingBaseCapability.class, new LivingBaseCapability.Storage(), new LivingBaseCapability.Factory());
     
/*  87 */     CapabilityManager.INSTANCE.register(LivingCapability.class, new LivingCapability.Storage(), new LivingCapability.Factory());
     
/*  89 */     initTileEntity();
/*  90 */     initHandlers();
/*  91 */     initNetwork();
/*  92 */     initEntities();
/*  93 */     Config.init(event);
   }
   
   public void init(FMLInitializationEvent event) {
     Config.initOreDictionary();
     ResearchsIS.init();
       GameRegistry.addSmelting(new ItemStack(ItemsIS.itemGoldEgg) ,new ItemStack(net.minecraft.init.Items.GOLD_NUGGET),0.15f);
   }
   
   public void postInit(FMLPostInitializationEvent event) {
/* 102 */     initMultiBlocks();
/* 103 */     ResearchsIS.initAspects(event);
   }
   
   private static void initTileEntity() {
/* 107 */     GameRegistry.registerTileEntity(TileVat.class, new ResourceLocation("isorropia", "curative_vat"));
/* 108 */     GameRegistry.registerTileEntity(TileVatConnector.class, new ResourceLocation("isorropia", "curative_connector"));
     
/* 110 */     GameRegistry.registerTileEntity(TileJarSoul.class, new ResourceLocation("isorropia", "soul_jar"));
/* 111 */     GameRegistry.registerTileEntity(TileModifiedMatrix.class, new ResourceLocation("isorropia", "modified_matrix"));
   }
 
   
   private static void initEntities() {
       registerEntity("incubated_egg", EntityIncubatedEgg.class, 64, 1, true);
       registerEntity("taint_pig", EntityTaintPig.class, 64, 3, true);
       registerEntity("gravekeeper", EntityGravekeeper.class, 64, 3, true);
       registerEntity("sheeder", EntitySheeder.class, 64, 3, true);
       registerEntity("chromatic_sheep", EntityChromaticSheep.class, 64, 3, true);
       registerEntity("scholar_chicken", EntityScholarChicken.class, 64, 3, true);
       registerEntity("ore_pig", EntityOrePig.class, 64, 3, true);
       registerEntity("jelly_rabbit", EntityJellyRabbit.class, 64, 3, true);
       registerEntity("hanging_label", EntityHangingLabel.class, 160, 2147483647, false);
       registerEntity("chocolate_cow", EntityChocow.class, 64, 3, true);
       registerEntity("golden_chicken", EntityGoldenChicken.class, 64, 3, true);
   }
   
   public static void initProviders() {
/* 128 */     FocusEngine.registerElement(FocusEffectContainment.class, new ResourceLocation("isorropia", "textures/misc/containment.png"), -13411841);
     
/* 130 */     IsorropiaAPI.registerLens(new AirLens(ItemsIS.itemAirLens), new ResourceLocation("isorropia", "air_lens"));
/* 131 */     IsorropiaAPI.registerLens(new FireLens(ItemsIS.itemFireLens), new ResourceLocation("isorropia", "fire_lens"));
     
     IsorropiaAPI.registerLens(new OrdoLens(ItemsIS.itemOrdoLens), new ResourceLocation("isorropia", "ordo_lens"));
     
/* 135 */     IsorropiaAPI.registerLens(new EnvyLens(ItemsIS.itemEnvyLens), new ResourceLocation("isorropia", "envy_lens"));
 
     
/* 138 */     IsorropiaAPI.registerCurativeEffect(new CurativeEffects.IGNIS_CURE());
/* 139 */     IsorropiaAPI.registerCurativeEffect(new CurativeEffects.LIFE_HEAL());
/* 140 */     IsorropiaAPI.registerCurativeEffect(new CurativeEffects.HUNGER_FOOD());
/* 141 */     IsorropiaAPI.registerCurativeEffect(new CurativeEffects.DEATH_HEAL());
/* 142 */     IsorropiaAPI.registerCurativeEffect(new CurativeEffects.UNDEAD_HEAL());
     
/* 144 */     IsorropiaAPI.bindJellyAspectEffect(null, new JellyAspectEffects.DEFAULT_EFFECT());
/* 145 */     IsorropiaAPI.bindJellyAspectEffect(Aspect.MOTION, new JellyAspectEffects.MOTION_EFFECT());
/* 146 */     IsorropiaAPI.bindJellyAspectEffect(Aspect.LIFE, new JellyAspectEffects.LIFE_EFFECT());
/* 147 */     IsorropiaAPI.bindJellyAspectEffect(Aspect.EXCHANGE, new JellyAspectEffects.EXCHANGE_EFFECT());
/* 148 */     IsorropiaAPI.bindJellyAspectEffect(Aspect.PROTECT, new JellyAspectEffects.PROTECT_EFFECT());
   }
   
   private static void initHandlers() {
/* 152 */     MinecraftForge.EVENT_BUS.register(new EntityEventHandler());
/* 153 */     NetworkRegistry.INSTANCE.registerGuiHandler(Isorropia.instance, new IRGuiHandler());
   }
   
   private static void initNetwork() {
/* 157 */     INSTANCE.registerMessage(MirrorMessage.MirrorMessageHandler.class, MirrorMessage.class, 0, Side.SERVER);
/* 158 */     INSTANCE.registerMessage(LensChangeMessage.LensChangeMessageHandler.class, LensChangeMessage.class, 1, Side.SERVER);
     
/* 160 */     INSTANCE.registerMessage(CapabilityMessage.ClientLivingBaseCapabilityHandler.class, CapabilityMessage.class, 2, Side.CLIENT);
     
/* 162 */     INSTANCE.registerMessage(MagnetMessage.MessageHandler.class, MagnetMessage.class, 3, Side.SERVER);
/* 163 */     INSTANCE.registerMessage(ISPacketFXInfusionSource.class, ISPacketFXInfusionSource.class, 4, Side.CLIENT);
/* 164 */     INSTANCE.registerMessage(TrackingCapabilityMessage.ClientLivingBaseCapabilityHandler.class, TrackingCapabilityMessage.class, 5, Side.CLIENT);
     
/* 166 */     INSTANCE.registerMessage(ParticuleDestroyMessage.Handler.class, ParticuleDestroyMessage.class, 6, Side.CLIENT);
/* 167 */     INSTANCE.registerMessage(LensRemoveMessage.Handler.class, LensRemoveMessage.class, 7, Side.CLIENT);
/* 168 */     INSTANCE.registerMessage(LensRemoveMessageSP.Handler.class, LensRemoveMessageSP.class, 8, Side.SERVER);
   }
   
   private static void initMultiBlocks() {
/* 172 */     Part pl = new Part(BlocksTC.plankGreatwood, BlocksIS.blockCurativeVat);
     
/* 174 */     Part co = new Part(BlocksTC.plankGreatwood, new ItemStack(BlocksIS.blockCurativeVat, 1, BlockCurativeVat.Type.CONNECTOR.getMetadata()));
     
/* 176 */     Part top = new Part(BlocksTC.metalAlchemical, new ItemStack(BlocksIS.blockCurativeVat, 1, BlockCurativeVat.Type.TOP.getMetadata()));
     
/* 178 */     Part bottom = new Part(BlocksTC.metalAlchemical, new ItemStack(BlocksIS.blockCurativeVat, 1, BlockCurativeVat.Type.BOTTOM.getMetadata()));
/* 179 */     Part gl = new Part(Blocks.GLASS, BlocksIS.blockCurativeVat);
/* 180 */     Part in = new Part(Material.WATER, BlocksIS.blockVatInterior);
/* 181 */     Part[][][] curative_vat = { { { pl, pl, pl }, { pl, top, pl }, { pl, pl, pl } }, { { gl, gl, gl }, { gl, in, gl }, { gl, gl, gl } }, { { gl, gl, gl }, { gl, in, gl }, { gl, gl, gl } }, { { pl, co, pl }, { co, bottom, co }, { pl, co, pl } } };
 
     
/* 184 */     IDustTrigger.registerDustTrigger(new DustTriggerMultiblock("CURATIVEVAT", curative_vat));
/* 185 */     curative_vat = curative_vat.clone();
/* 186 */     Part[][][] crafting_recipe = { { { pl, pl, pl }, { pl, top, pl }, { pl, pl, pl } }, { { gl, gl, gl }, { gl, null, gl }, { gl, gl, gl } }, { { gl, gl, gl }, { gl, null, gl }, { gl, gl, gl } }, { { pl, co, pl }, { co, bottom, co }, { pl, co, pl } } };
 
 
     
/* 190 */     ThaumcraftApi.addFakeCraftingRecipe(new ResourceLocation("isorropia", "curative_vat"), new ThaumcraftApi.BluePrint("CURATIVEVAT", crafting_recipe, new ItemStack(BlocksTC.plankGreatwood, 16), new ItemStack(Blocks.GLASS, 16), new ItemStack(BlocksTC.metalAlchemical, 2, 0), new ItemStack(Items.WATER_BUCKET, 2)));
   }
 
 
 
   
   public static void initOreDictionary() {
/* 197 */     OreDictionary.registerOre("slimeball", new ItemStack(ItemsIS.itemJelly));
/* 198 */     OreDictionary.registerOre("dyeBlack", new ItemStack(ItemsIS.itemInkEgg));
   }
 
   
   private static void registerEntity(String name, Class<? extends Entity> entityClass, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
/* 203 */     ResourceLocation loc = new ResourceLocation("isorropia", name);
/* 204 */     EntityRegistry.registerModEntity(loc, entityClass, name, RegistryEventHandler.entities_id++, Isorropia.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
     
/* 206 */     if (EntityLiving.class.isAssignableFrom(entityClass))
/* 207 */       EntityRegistry.registerEgg(loc, 0, 1); 
/* 208 */     RegistryEventHandler.ENTITIES.put(name, entityClass);
   }
   
   public static LivingBaseCapability getCap(EntityLivingBase base) {
/* 212 */     if (base instanceof EntityLiving) {
/* 213 */       return base.getCapability(LIVING_CAPABILITY, null);
     }
     
/* 216 */     return base.getCapability(LIVING_BASE_CAPABILITY, null);
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\Common.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */