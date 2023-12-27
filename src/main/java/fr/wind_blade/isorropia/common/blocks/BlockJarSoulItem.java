package fr.wind_blade.isorropia.common.blocks;

import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

public class BlockJarSoulItem
        extends ItemBlock
        implements IItemStackRenderProvider {
    public BlockJarSoulItem() {
        super(BlocksIS.blockJarSoul);
    }

    public String getItemStackDisplayName(ItemStack stack) {
        String stringy = stack.hasTagCompound() && stack.getTagCompound().hasKey("ENTITY_DATA") ? new TextComponentTranslation("item.jarredSoul.jarred", EntityList.getTranslationName(new ResourceLocation(stack.getTagCompound().getCompoundTag("ENTITY_DATA").getString("id")))).getFormattedText() : new TextComponentTranslation("item.jarredSoul.0.name").getFormattedText();
        return stringy;
    }
}
