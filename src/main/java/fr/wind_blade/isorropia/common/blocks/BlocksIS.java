package fr.wind_blade.isorropia.common.blocks;

import fr.wind_blade.isorropia.common.Common;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class BlocksIS {
    public static List<Block> blocks = new ArrayList<Block>();
    public static BlockCurativeVat blockCurativeVat;
    public static BlockLiquidVat blockVatInterior;
    public static BlockModifiedMatrix blockModifiedMatrix;
    public static BlockJarSoul blockJarSoul;
    public static Chocolate blockChocolate;
    public static BlockSoulBeacon blockSoulBeacon;

    public static void initBlocks() {
        blockCurativeVat = BlocksIS.getBlock(new BlockCurativeVat(), "curative_vat");
        blockVatInterior = BlocksIS.getBlock(new BlockLiquidVat(), "vat_interior");
        blockJarSoul = BlocksIS.getBlock(new BlockJarSoul(), "jar_soul");
        blockChocolate = BlocksIS.getBlock(new Chocolate(), "chocolate");
        blockModifiedMatrix = BlocksIS.getBlock(new BlockModifiedMatrix(), "modified_matrix");
        blockSoulBeacon = BlocksIS.getBlock(new BlockSoulBeacon(), "soul_beacon");
    }

    public static Block getBlock(String name, Material material) {
        return BlocksIS.getBlock(new Block(material), name);
    }

    public static <T extends Block> T getBlock(T block, String name) {
        return (T)BlocksIS.registry(block.setRegistryName(new ResourceLocation("isorropia", name)).setTranslationKey("isorropia." + name));
    }

    public static <T extends Block> T registry(T block) {
        if (!(block instanceof IBlockRegistry) || ((IBlockRegistry) block).isInCreativeTabs()) {
            block.setCreativeTab(Common.isorropiaCreativeTabs);
        }
        blocks.add(block);
        return block;
    }
}
