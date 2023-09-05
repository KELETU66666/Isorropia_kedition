 package fr.wind_blade.isorropia.common.curative;

 import fr.wind_blade.isorropia.common.IsorropiaAPI;
 import fr.wind_blade.isorropia.common.config.Config;
 import fr.wind_blade.isorropia.common.entities.EntityOrePig;
 import fr.wind_blade.isorropia.common.items.misc.ItemCat;
 import fr.wind_blade.isorropia.common.research.recipes.SpecieCurativeInfusionRecipe;
 import fr.wind_blade.isorropia.common.tiles.TileVat;
 import net.minecraft.entity.EntityLivingBase;
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
/* 24 */     super((SpecieCurativeInfusionRecipe.Builder)((SpecieCurativeInfusionRecipe.Builder)((SpecieCurativeInfusionRecipe.Builder)((SpecieCurativeInfusionRecipe.Builder)((SpecieCurativeInfusionRecipe.Builder)((SpecieCurativeInfusionRecipe.Builder)((SpecieCurativeInfusionRecipe.Builder)(new SpecieCurativeInfusionRecipe.Builder())
         .withAspects((new AspectList()).add(IsorropiaAPI.HUNGER, 32).add(Aspect.FIRE, 54).add(Aspect.METAL, 64)
            .add(Aspect.EARTH, 32)))
          .withComponents(new Ingredient[] { Ingredient.fromItem(Item.getItemFromBlock(Blocks.FURNACE)), Config.orePig, 
              Ingredient.fromStacks(new ItemStack[] { ThaumcraftApiHelper.makeCrystal(Aspect.FIRE)
                }), Ingredient.fromStacks(new ItemStack[] { ThaumcraftApiHelper.makeCrystal(Aspect.EARTH)
/* 30 */               }) })).withInstability(4)).withKnowledgeRequirement("OREBOAR"))
/* 31 */         .withPredicate(entity -> (entity.getClass() == EntityPig.class))).withResult(EntityOrePig.class)
/* 32 */         .withVis(50.0F)).withFakeIngredients(Ingredient.fromStacks(new ItemStack[] { ItemCat.createCat(ItemCat.EnumCat.PIG, "Pig")
/* 33 */             }), ItemCat.createCat(ItemCat.EnumCat.PIG, "Ore Boar")));
   }


   public void onInfusionFinish(TileVat curative) {
/* 38 */     super.onInfusionFinish(curative);
/* 39 */     NonNullList<ItemStack> stacks = curative.getStacksUsed();
/* 40 */     ItemStack stack = stacks.stream().filter(item -> Config.orePig.apply(item)).findFirst().get();

/* 42 */     if (stack != null && !stack.isEmpty())
/* 43 */       ((EntityOrePig)curative.getEntityContained())
/* 44 */         .setOre(OreDictionary.getOreName(OreDictionary.getOreIDs(stack)[0])); 
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\curative\OreBoarRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */