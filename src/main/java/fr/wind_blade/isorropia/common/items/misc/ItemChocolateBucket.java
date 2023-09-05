package fr.wind_blade.isorropia.common.items.misc;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucketMilk;

public class ItemChocolateBucket extends ItemBucketMilk {
    public ItemChocolateBucket()
    {
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.MISC);
        this.setContainerItem(Items.BUCKET);
    }
}