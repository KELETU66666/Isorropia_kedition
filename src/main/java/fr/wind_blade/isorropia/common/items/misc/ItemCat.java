package fr.wind_blade.isorropia.common.items.misc;

import fr.wind_blade.isorropia.common.Common;
import fr.wind_blade.isorropia.common.IsorropiaAPI;
import fr.wind_blade.isorropia.common.items.ItemsIS;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import thaumcraft.common.lib.utils.EntityUtils;

public class ItemCat extends Item {
    public ItemCat() {
        this.setHasSubtypes(true);
    }

    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (tab != Common.isorropiaCreativeTabs) {
            return;
        }
        for (EnumCat cat : EnumCat.values()) {
            if (cat.recipe == null) continue;
            items.add(new ItemStack(this, 1, cat.getID()));
        }
    }

    public static ItemStack createCat(EnumCat type, String name) {
        ItemStack stack = new ItemStack(ItemsIS.itemCat, 1, type.getID());
        return stack.setStackDisplayName(name);
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        EnumCat cat = EnumCat.values()[stack.getMetadata()];
        Entity entity = EntityUtils.getPointedEntity(worldIn, playerIn, 1.0, 5.0, 5.0f, true);
        if (cat.getRecipe() != null) {
            IsorropiaAPI.creatureInfusionRecipes.get(new ResourceLocation(cat.getRecipe())).applyWithCheat(playerIn, (EntityLivingBase)(entity instanceof EntityLivingBase ? entity : playerIn), stack);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    public enum EnumCat implements IStringSerializable
    {
        PIG(0, "pig"),
        SHEEP(1, "sheep"),
        COW(2, "cow"),
        OCELOT(3, "ocelot"),
        CHICKEN(4, "chicken"),
        RABBIT(5, "rabbit"),
        WOLF(6, "wolf"),
        SPIDER(7, "spider"),
        SQUID(8, "squid"),
        LOVE(9, "love", "isorropia:instilledfidelity"),
        SELFSHEARING(10, "selfshearing", "isorropia:selfshearing"),
        ENDERHEART(11, "enderheart", "isorropia:enderheart"),
        SHOCK(12, "shockskin", "isorropia:shockskin"),
        AWAKENED_BLOOD(13, "awakened_blood", "isorropia:awakened_blood"),
        DIAMOND_SKIN(14, "diamond_skin", "isorropia:diamond_skin"),
        QUICKSILVER_LIMBS(15, "quicksilver_limbs", "isorropia:quicksilver_limbs"),
        PORTABILITY(16, "portability", "isorropia:portability"),
        STEVE(17, "steve"),;

        private final int id;
        private final String name;
        private String recipe = null;

        EnumCat(int id, String name) {
            this.id = id;
            this.name = name;
        }

        EnumCat(int id, String name, String recipe) {
            this(id, name);
            this.recipe = recipe;
        }

        public int getID() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public String getRecipe() {
            return this.recipe;
        }
    }
}
