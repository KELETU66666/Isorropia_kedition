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
    public static final Map<String, Class<? extends Entity>> ENTITIES = new HashMap<>();
    public static int entities_id;

    @SubscribeEvent
    public static void onItemRegistry(RegistryEvent.Register<Item> event) {
        ItemsIS.initItems();
        ItemsIS.items.forEach(event.getRegistry()::register);
        BlocksIS.blocks.stream().filter(block -> !(block instanceof IBlockRegistry) || ((IBlockRegistry) block).haveItemBlock()).forEach(block -> event.getRegistry().register(getItemBlock(block)));
        Common.initProviders();
        Common.initOreDictionary();
    }

    @SubscribeEvent
    public static void onRegistryBlock(RegistryEvent.Register<Block> event) {
        BlocksIS.initBlocks();
        BlocksIS.blocks.forEach(event.getRegistry()::register);
    }

    @SubscribeEvent
    public static void onRegistryCreation(RegistryEvent.NewRegistry event) {
        RegistryBuilder<Lens> lens_builder = new RegistryBuilder();
        lens_builder.setName(new ResourceLocation("isorropia", "lens"));
        lens_builder.setType(Lens.class);
        lens_builder.setIDRange(0, 255);
        IsorropiaAPI.lensRegistry = (ForgeRegistry)lens_builder.create();
    }

    @SubscribeEvent
    public static void lensRegistryEvent(RegistryEvent.Register<Lens> event) {
        IsorropiaAPI.registerLens(IsorropiaAPI.air_lens = new AirLens(ItemsIS.itemAirLens), new ResourceLocation("isorropia", "air_lens"));

        IsorropiaAPI.registerLens(IsorropiaAPI.fire_lens = new FireLens(ItemsIS.itemFireLens), new ResourceLocation("isorropia", "fire_lens"));

        IsorropiaAPI.registerLens(IsorropiaAPI.ordo_lens = new OrdoLens(ItemsIS.itemOrdoLens), new ResourceLocation("isorropia", "ordo_lens"));

        IsorropiaAPI.registerLens(IsorropiaAPI.envy_lens = new EnvyLens(ItemsIS.itemEnvyLens), new ResourceLocation("isorropia", "envy_lens"));


        for (ResourceLocation res : IsorropiaAPI.lens.keySet()) {
            Lens lens = IsorropiaAPI.lens.get(res);
            lens.setRegistryName(res);
            event.getRegistry().register(lens);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {
        ItemsIS.items.forEach(item -> registerItemModel(item));

        for (int i = 0; i < (ItemCat.EnumCat.values()).length; i++) {
            ModelLoader.setCustomModelResourceLocation(ItemsIS.itemCat, i, new ModelResourceLocation(ItemsIS.itemCat.getRegistryName().toString() + "_" + ItemCat.EnumCat.values()[i].getName(), "inventory"));
        }


        ModelLoader.setCustomStateMapper(BlocksIS.blockJarSoul, (new StateMap.Builder())
                .ignore(BlockJarSoul.FACING).build());
        registerItemRenders((Block[])BlocksIS.blocks.toArray((Object[])new Block[0]));
    }

    public static ItemBlock getItemBlock(Block block) {
        return getItemBlock((block instanceof IItemBlockProvider) ? ((IItemBlockProvider)block).getItemBlock() : new ItemBlock(block));
    }


    public static <T extends ItemBlock> T getItemBlock(T itemBlock) {
        Block block = itemBlock.getBlock();
        itemBlock.setRegistryName(block.getRegistryName()).setTranslationKey(block.getTranslationKey());
        if (!(block instanceof IBlockRegistry) || ((IBlockRegistry)block).isInCreativeTabs())
            itemBlock.setCreativeTab(Common.isorropiaCreativeTabs);
        return itemBlock;
    }

    @SideOnly(Side.CLIENT)
    public static void registerItemModel(Item item) {
        registerItemModel(item, 0);
    }

    @SideOnly(Side.CLIENT)
    public static void registerItemModel(Item item, int metadata) {
        if (metadata < 0)
            metadata = 0;
        String resourceName = item.getRegistryName().toString();
        if (metadata > 0)
            resourceName = resourceName + "_m" + metadata;
        ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(resourceName, "inventory"));
    }


    @SideOnly(Side.CLIENT)
    protected static void registerItemRenders(Block... blocks) {
        for (Block block : blocks) {
            Item item = Item.getItemFromBlock(block);
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
        }
    }
}