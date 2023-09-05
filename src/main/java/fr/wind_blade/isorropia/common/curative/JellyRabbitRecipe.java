// Decompiled with: CFR 0.152
// Class Version: 8
package fr.wind_blade.isorropia.common.curative;

import fr.wind_blade.isorropia.common.config.Config;
import fr.wind_blade.isorropia.common.entities.EntityJellyRabbit;
import fr.wind_blade.isorropia.common.items.misc.ItemCat;
import fr.wind_blade.isorropia.common.research.recipes.CurativeInfusionRecipe;
import fr.wind_blade.isorropia.common.research.recipes.SpecieCurativeInfusionRecipe;
import fr.wind_blade.isorropia.common.tiles.TileVat;
import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.items.resources.ItemCrystalEssence;

public class JellyRabbitRecipe
        extends SpecieCurativeInfusionRecipe {
    public JellyRabbitRecipe() {
        super((SpecieCurativeInfusionRecipe.Builder) ((Builder) new Builder().withAspects(new AspectList().add(Aspect.AURA, 16).add(Aspect.EXCHANGE, 16)).withComponents(Ingredient.fromItem(Item.getItemFromBlock(Blocks.SLIME_BLOCK)), Ingredient.fromItem(ItemsTC.salisMundus), Ingredient.fromItem(ItemsTC.salisMundus), Ingredient.fromStacks(Config.CRYSTALS.toArray(new ItemStack[0]))).withInstability(2).withKnowledgeRequirement("JELLYRABBIT").withPredicate(entity -> entity.getClass() == EntityRabbit.class)).withResult(EntityJellyRabbit.class).withFakeIngredients(Ingredient.fromStacks(new ItemStack[]{ItemCat.createCat(ItemCat.EnumCat.RABBIT, "Rabbit")}), ItemCat.createCat(ItemCat.EnumCat.RABBIT, "Jelly Rabbit")).withVis(50.0f));
    }

    @Override
    public void onInfusionFinish(TileVat curative) {
        super.onInfusionFinish(curative);
        NonNullList<ItemStack> stacks = curative.getStacksUsed();
        for (ItemStack stack : stacks) {
            if (stack.getItem() != ItemsTC.crystalEssence) continue;
            ((EntityJellyRabbit)curative.getEntityContained()).setAspect(((ItemCrystalEssence) stack.getItem()).getAspects(stack).getAspects()[0]);
            break;
        }
    }
}
