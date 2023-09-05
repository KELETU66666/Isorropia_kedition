 package fr.wind_blade.isorropia.common.events;
 
 import fr.wind_blade.isorropia.common.Common;
 import fr.wind_blade.isorropia.common.IsorropiaAPI;
 import fr.wind_blade.isorropia.common.blocks.BlockJarSoul;
 import fr.wind_blade.isorropia.common.blocks.BlocksIS;
 import fr.wind_blade.isorropia.common.blocks.IBlockRegistry;
 import fr.wind_blade.isorropia.common.blocks.IItemBlockProvider;
 import fr.wind_blade.isorropia.common.items.ItemsIS;
 import fr.wind_blade.isorropia.common.items.misc.ItemCat;
 import fr.wind_blade.isorropia.common.lenses.AirLens;
 import fr.wind_blade.isorropia.common.lenses.EnvyLens;
 import fr.wind_blade.isorropia.common.lenses.FireLens;
 import fr.wind_blade.isorropia.common.lenses.Lens;
 import fr.wind_blade.isorropia.common.lenses.OrdoLens;
 import java.util.HashMap;
 import java.util.Map;
 import net.minecraft.block.Block;
 import net.minecraft.block.properties.IProperty;
 import net.minecraft.client.renderer.block.model.ModelResourceLocation;
 import net.minecraft.client.renderer.block.statemap.IStateMapper;
 import net.minecraft.client.renderer.block.statemap.StateMap;
 import net.minecraft.entity.Entity;
 import net.minecraft.item.Item;
 import net.minecraft.item.ItemBlock;
 import net.minecraft.util.ResourceLocation;
 import net.minecraftforge.client.event.ModelRegistryEvent;
 import net.minecraftforge.client.model.ModelLoader;
 import net.minecraftforge.event.RegistryEvent;
 import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
 import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 import net.minecraftforge.registries.ForgeRegistry;
 import net.minecraftforge.registries.IForgeRegistryEntry;
 import net.minecraftforge.registries.RegistryBuilder;
 
 @EventBusSubscriber(modid = "isorropia")
 public class RegistryEventHandler {
/*  40 */   public static final Map<String, Class<? extends Entity>> ENTITIES = new HashMap<>();
   public static int entities_id;
   
   @SubscribeEvent
   public static void onItemRegistry(RegistryEvent.Register<Item> event) {
/*  45 */     ItemsIS.initItems();
/*  46 */     ItemsIS.items.forEach(event.getRegistry()::register);
/*  47 */     BlocksIS.blocks.stream()
/*  48 */       .filter(block -> (block instanceof IBlockRegistry) ? ((IBlockRegistry)block).haveItemBlock() : true)
/*  49 */       .forEach(block -> event.getRegistry().register(getItemBlock(block)));
/*  50 */     Common.initProviders();
/*  51 */     Common.initOreDictionary();
   }
   
   @SubscribeEvent
   public static void onRegistryBlock(RegistryEvent.Register<Block> event) {
/*  56 */     BlocksIS.initBlocks();
/*  57 */     BlocksIS.blocks.forEach(event.getRegistry()::register);
   }
   
   @SubscribeEvent
   public static void onRegistryCreation(RegistryEvent.NewRegistry event) {
/*  62 */     RegistryBuilder<Lens> lens_builder = new RegistryBuilder();
/*  63 */     lens_builder.setName(new ResourceLocation("isorropia", "lens"));
/*  64 */     lens_builder.setType(Lens.class);
/*  65 */     lens_builder.setIDRange(0, 255);
/*  66 */     IsorropiaAPI.lensRegistry = (ForgeRegistry)lens_builder.create();
   }
   
   @SubscribeEvent
   public static void lensRegistryEvent(RegistryEvent.Register<Lens> event) {
/*  71 */     IsorropiaAPI.registerLens(IsorropiaAPI.air_lens = (Lens)new AirLens(ItemsIS.itemAirLens), new ResourceLocation("isorropia", "air_lens"));
     
/*  73 */     IsorropiaAPI.registerLens(IsorropiaAPI.fire_lens = (Lens)new FireLens(ItemsIS.itemFireLens), new ResourceLocation("isorropia", "fire_lens"));
     
/*  75 */     IsorropiaAPI.registerLens(IsorropiaAPI.ordo_lens = (Lens)new OrdoLens(ItemsIS.itemOrdoLens), new ResourceLocation("isorropia", "ordo_lens"));
     
/*  77 */     IsorropiaAPI.registerLens(IsorropiaAPI.envy_lens = (Lens)new EnvyLens(ItemsIS.itemEnvyLens), new ResourceLocation("isorropia", "envy_lens"));
 
     
/*  80 */     for (ResourceLocation res : IsorropiaAPI.lens.keySet()) {
/*  81 */       Lens lens = (Lens)IsorropiaAPI.lens.get(res);
/*  82 */       lens.setRegistryName(res);
/*  83 */       event.getRegistry().register((Lens) lens);
     } 
   }
   
   @SideOnly(Side.CLIENT)
   @SubscribeEvent
   public static void onModelRegistry(ModelRegistryEvent event) {
/*  90 */     ItemsIS.items.forEach(item -> registerItemModel(item));
     
/*  92 */     for (int i = 0; i < (ItemCat.EnumCat.values()).length; i++) {
/*  93 */       ModelLoader.setCustomModelResourceLocation(ItemsIS.itemCat, i, new ModelResourceLocation(ItemsIS.itemCat
             
/*  95 */             .getRegistryName().toString() + "_" + ItemCat.EnumCat.values()[i].getName(), "inventory"));
     }
 
     
/*  99 */     ModelLoader.setCustomStateMapper((Block)BlocksIS.blockJarSoul, (IStateMapper)(new StateMap.Builder())
/* 100 */         .ignore(new IProperty[] { (IProperty)BlockJarSoul.FACING }).build());
/* 101 */     registerItemRenders((Block[])BlocksIS.blocks.toArray((Object[])new Block[0]));
   }
   
   public static ItemBlock getItemBlock(Block block) {
/* 105 */     return getItemBlock((block instanceof IItemBlockProvider) ? ((IItemBlockProvider)block).getItemBlock() : new ItemBlock(block));
   }
 
   
   public static <T extends ItemBlock> T getItemBlock(T itemBlock) {
/* 110 */     Block block = itemBlock.getBlock();
/* 111 */     ((Item)itemBlock.setRegistryName(block.getRegistryName())).setTranslationKey(block.getTranslationKey());
/* 112 */     if (!(block instanceof IBlockRegistry) || ((IBlockRegistry)block).isInCreativeTabs())
/* 113 */       itemBlock.setCreativeTab(Common.isorropiaCreativeTabs);
/* 114 */     return itemBlock;
   }
   
   @SideOnly(Side.CLIENT)
   public static void registerItemModel(Item item) {
/* 119 */     registerItemModel(item, 0);
   }
   
   @SideOnly(Side.CLIENT)
   public static void registerItemModel(Item item, int metadata) {
/* 124 */     if (metadata < 0)
/* 125 */       metadata = 0; 
/* 126 */     String resourceName = item.getRegistryName().toString();
/* 127 */     if (metadata > 0)
/* 128 */       resourceName = resourceName + "_m" + String.valueOf(metadata); 
/* 129 */     ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(resourceName, "inventory"));
   }
 
   
   @SideOnly(Side.CLIENT)
   protected static void registerItemRenders(Block... blocks) {
/* 135 */     for (Block block : blocks) {
/* 136 */       Item item = Item.getItemFromBlock(block);
/* 137 */       ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item
/* 138 */             .getRegistryName(), "inventory"));
     } 
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\events\RegistryEventHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */