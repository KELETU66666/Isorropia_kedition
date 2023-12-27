package fr.wind_blade.isorropia.common.research.recipes;

import fr.wind_blade.isorropia.common.research.recipes.CurativeInfusionRecipe;
import java.util.List;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class CurativePetrificationInfusionRecipe
        extends CurativeInfusionRecipe {
    public CurativePetrificationInfusionRecipe(CurativeInfusionRecipe.Builder builder) {
        super(builder);
    }

    @Override
    public AspectList getCurrentAspect(EntityPlayer infuser, World worldIn, EntityLivingBase base, float stability, float totalInstability, List<Ingredient> optionalsIngredientsInfused) {
        return new AspectList().add(Aspect.EARTH, Math.round(base.getMaxHealth() * 5.0f));
    }
}
