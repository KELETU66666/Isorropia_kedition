package fr.wind_blade.isorropia.common.items.tools;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import thaumcraft.api.items.IScribeTools;

public class ItemPrimalWell extends Item implements IScribeTools {
    public ItemPrimalWell() {
        setMaxStackSize(1);
        setMaxDamage(200);
    }


    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }


    public void setDamage(ItemStack stack, int damage) {
        super.setDamage(stack, 0);
    }
}