package fr.wind_blade.isorropia.common.items;

import fr.wind_blade.isorropia.common.Common;
import fr.wind_blade.isorropia.common.items.baubles.ItemSomaticBrain;
import fr.wind_blade.isorropia.common.items.misc.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class ItemsIS {

    public static final List<Item> items = new ArrayList<>();
    public static Item itemBaseLens;
    public static Item itemChocolateItem;
    public static Item itemGoldEgg;
    public static ItemLens itemAirLens;
    public static ItemLens itemFireLens;
    public static ItemLens itemOrdoLens;
    public static ItemLens itemEnvyLens;
    public static ItemSomaticBrain itemSomaticBrain;
    public static Item itemIncubatedEgg;
    public static Item itemInkEgg;
    public static Item itemFlesh;
    public static Item itemCat;
    public static ItemBucketMilk itemChocolateBucket;
    public static ItemChocolateCream ItemChocolateCream;
    public static ItemJelly itemJelly;
    public static ItemCorpseEffigy corpseEffigy;

    public static void initItems() {
        itemBaseLens = getItem(new Item(), "lens");
        itemChocolateItem = getItem(new Item(), "chocolate_bar");
        itemGoldEgg = getItem(new Item(), "gold_egg");
        itemAirLens = getItem(new ItemLens(), "lens_air");
        itemOrdoLens = getItem(new ItemLens(), "lens_ordo");
        itemFireLens = getItem(new ItemLens(), "lens_fire");
        itemEnvyLens = getItem(new ItemLens(), "lens_envy");
        itemSomaticBrain = getItem(new ItemSomaticBrain(), "somatic_brain");
        itemIncubatedEgg = getItem(new ItemIncubatedEgg(), "incubated_egg");
        itemInkEgg = getItem("ink_egg");
        itemFlesh = getItem("flesh");
        itemCat = getItem(new ItemCat(), "cat");
        itemJelly = getItem(new ItemJelly(), "jelly");
        itemChocolateBucket = getItem(new ItemChocolateBucket(), "chocolate_milk_bucket");
        ItemChocolateCream = getItem(new ItemChocolateCream(), "chocolate_cream");
        corpseEffigy = getItem(new ItemCorpseEffigy(), "corpse_effigy");
    }

    public static Item getItem(String name) {
        return getItem(new Item(), name);
    }

    public static <T extends Item> T getItem(T item, String name) {
        item.setRegistryName(new ResourceLocation("isorropia", name)).setTranslationKey("isorropia." + name);
        item.setCreativeTab(Common.isorropiaCreativeTabs);
        items.add(item);
        return item;
    }
}