// Decompiled with: CFR 0.152
// Class Version: 8
package fr.wind_blade.isorropia.common.curative;

import fr.wind_blade.isorropia.common.IsorropiaAPI;
import fr.wind_blade.isorropia.common.config.Config;
import fr.wind_blade.isorropia.common.entities.EntityOrePig;
import fr.wind_blade.isorropia.common.items.misc.ItemCat;
import fr.wind_blade.isorropia.common.research.recipes.CurativeInfusionRecipe;
import fr.wind_blade.isorropia.common.research.recipes.SpecieCurativeInfusionRecipe;
import fr.wind_blade.isorropia.common.tiles.TileVat;
import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class OreBoarRecipe
        extends SpecieCurativeInfusionRecipe {
    public OreBoarRecipe() {
        super((SpecieCurativeInfusionRecipe.Builder) ((Builder) new Builder().withAspects(new AspectList().add(IsorropiaAPI.HUNGER, 32).add(Aspect.FIRE, 54).add(Aspect.METAL, 64).add(Aspect.EARTH, 32)).withComponents(Ingredient.fromItem(Item.getItemFromBlock(Blocks.FURNACE)), Config.orePig, Ingredient.fromStacks(new ItemStack[]{ThaumcraftApiHelper.makeCrystal(Aspect.FIRE)}), Ingredient.fromStacks(new ItemStack[]{ThaumcraftApiHelper.makeCrystal(Aspect.EARTH)})).withInstability(4).withKnowledgeRequirement("OREBOAR").withPredicate(entity -> entity.getClass() == EntityPig.class)).withResult(EntityOrePig.class).withVis(50.0f).withFakeIngredients(Ingredient.fromStacks(new ItemStack[]{ItemCat.createCat(ItemCat.EnumCat.PIG, "Pig")}), ItemCat.createCat(ItemCat.EnumCat.PIG, "Ore Boar")));
    }

    @Override
    public void onInfusionFinish(TileVat curative) {
        super.onInfusionFinish(curative);
        NonNullList<ItemStack> stacks = curative.getStacksUsed();
        ItemStack stack = stacks.stream().filter(item -> Config.orePig.apply(item)).findFirst().get();
        if (stack != null && !stack.isEmpty()) {
            ((EntityOrePig)curative.getEntityContained()).setOre(OreDictionary.getOreName(OreDictionary.getOreIDs(stack)[0]));
        }
    }
}
