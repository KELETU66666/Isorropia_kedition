 package fr.wind_blade.isorropia.common.research.recipes;
 
 import java.util.List;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.item.crafting.Ingredient;
 import net.minecraft.world.World;
 import thaumcraft.api.aspects.Aspect;
 import thaumcraft.api.aspects.AspectList;
 
 public class CurativePetrificationInfusionRecipe
   extends CurativeInfusionRecipe
 {
   public CurativePetrificationInfusionRecipe(CurativeInfusionRecipe.Builder builder) {
     super(builder);
   }
 
 
   
   public AspectList getCurrentAspect(EntityPlayer infuser, World worldIn, EntityLivingBase base, float stability, float totalInstability, List<Ingredient> optionalsIngredientsInfused) {
/* 21 */     return (new AspectList()).add(Aspect.EARTH, Math.round(base.getMaxHealth() * 5.0F));
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\research\recipes\CurativePetrificationInfusionRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */