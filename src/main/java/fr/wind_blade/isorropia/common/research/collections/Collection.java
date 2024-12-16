package fr.wind_blade.isorropia.common.research.collections;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;

import java.util.ArrayList;
import java.util.List;


public class Collection
{
    private String registryName;
    private final String research;
    private final ItemStack[] ingredients;

    public Collection(String research, ItemStack... stacks) {
        this.research = research;
        this.ingredients = stacks;
    }

    public Collection(String research, Item... items) {
        this(research, to_ingredients(items).<ItemStack>toArray(new ItemStack[0]));
    }

    public Collection(String research, Block... blocks) {
        this(research, to_ingredients(blocks).<ItemStack>toArray(new ItemStack[0]));
    }

    public static void checkThing(EntityPlayer player, Collection collection, ItemStack stack) {
        for (ItemStack object : collection.getIngredients()) {
            if (object.getItem() == stack.getItem() && object.getMetadata() == stack.getMetadata())
                progress(player, collection, stack);
        }
    }
    public static void progress(EntityPlayer player, Collection collection, ItemStack stack) {
        IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(player);

        knowledge.addResearch(collection
                .getRegistryName() + ":" + stack.getItem().getRegistryName() + "_" + stack.getMetadata());
        if (isFinish(player, collection))
            finish(player, collection);
    }

    public static void finish(EntityPlayer player, Collection collection) {
        IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(player);

        knowledge.addResearch("collection:" + collection.getRegistryName());
        for (ItemStack stack : collection.getIngredients())
            knowledge.removeResearch(collection
                    .getRegistryName() + ":" + stack.getItem().getRegistryName() + "_" + stack.getMetadata());
    }

    public static boolean isFinish(EntityPlayer player, Collection collection) {
        IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(player);

        if (knowledge.isResearchKnown("collection:" + collection.getRegistryName()))
            return true;
        for (ItemStack stack : collection.getIngredients()) {
            if (!knowledge.isResearchKnown("collection:" + collection
                    .getRegistryName() + ":" + stack.getItem().getRegistryName()))
                return false;
        }  return true;
    }

    private static List<ItemStack> to_ingredients(Item... items) {
        List<ItemStack> ingredients = new ArrayList<>();

        for (Item item : items)
            ingredients.add(new ItemStack(item));
        return ingredients;
    }

    private static List<ItemStack> to_ingredients(Block... blocks) {
        List<ItemStack> ingredients = new ArrayList<>();

        for (Block block : blocks)
            ingredients.add(new ItemStack(block));
        return ingredients;
    }

    public void setRegistryName(String name) {
        this.registryName = name;
    }

    public String getRegistryName() {
        return this.registryName;
    }

    public String getResearch() {
        return this.research;
    }

    public ItemStack[] getIngredients() {
        return this.ingredients;
    }
}
