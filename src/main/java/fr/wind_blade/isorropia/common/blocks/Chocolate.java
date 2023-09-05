package fr.wind_blade.isorropia.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class Chocolate extends Block {

    public Chocolate() {
        super(Material.ROCK);
        setHardness(2.0F);
        setResistance(10.0F);
        setCreativeTab(CreativeTabs.MISC);
        this.setHarvestLevel("pickaxe", 0);
    }
}