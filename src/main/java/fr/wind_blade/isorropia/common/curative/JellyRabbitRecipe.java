 package fr.wind_blade.isorropia.common.curative;
 
 import fr.wind_blade.isorropia.common.config.Config;
 import fr.wind_blade.isorropia.common.entities.EntityJellyRabbit;
 import fr.wind_blade.isorropia.common.items.misc.ItemCat;
 import fr.wind_blade.isorropia.common.research.recipes.SpecieCurativeInfusionRecipe;
 import fr.wind_blade.isorropia.common.tiles.TileVat;
 import net.minecraft.entity.EntityLivingBase;
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
 
 public class JellyRabbitRecipe extends SpecieCurativeInfusionRecipe {
   public JellyRabbitRecipe() {
/* 22 */     super((SpecieCurativeInfusionRecipe.Builder) ((Builder) (new Builder())
/* 23 */         .withAspects((new AspectList()).add(Aspect.AURA, 16).add(Aspect.EXCHANGE, 16))
/* 24 */         .withComponents(new Ingredient[] { Ingredient.fromItem(Item.getItemFromBlock(Blocks.SLIME_BLOCK)),
             Ingredient.fromItem(ItemsTC.salisMundus), Ingredient.fromItem(ItemsTC.salisMundus),
              Ingredient.fromStacks((ItemStack[])Config.CRYSTALS.toArray((Object[])new ItemStack[0]))
            }).withInstability(2).withKnowledgeRequirement("JELLYRABBIT")
          .withPredicate(entity -> (entity.getClass() == EntityRabbit.class))).withResult(EntityJellyRabbit.class)
          .withFakeIngredients(Ingredient.fromStacks(ItemCat.createCat(ItemCat.EnumCat.RABBIT, "Rabbit")
                  /* 30 */), ItemCat.createCat(ItemCat.EnumCat.RABBIT, "Jelly Rabbit"))
/* 31 */         .withVis(50.0F));
   }
 
   
   public void onInfusionFinish(TileVat curative) {
/* 36 */     super.onInfusionFinish(curative);
/* 37 */     NonNullList<ItemStack> stacks = curative.getStacksUsed();
     
/* 39 */     for (ItemStack stack : stacks) {
/* 40 */       if (stack.getItem() == ItemsTC.crystalEssence) {
          ((EntityJellyRabbit)curative.getEntityContained())
/* 42 */           .setAspect(((ItemCrystalEssence)stack.getItem()).getAspects(stack).getAspects()[0]);
         break;
       } 
     } 
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\curative\JellyRabbitRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */