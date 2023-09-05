 package fr.wind_blade.isorropia.common.research.collections;
 
 import java.util.ArrayList;
 import java.util.List;
 import net.minecraft.block.Block;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.item.Item;
 import net.minecraft.item.ItemStack;
 import thaumcraft.api.capabilities.IPlayerKnowledge;
 import thaumcraft.api.capabilities.ThaumcraftCapabilities;
 
 
 public class Collection
 {
   private String registryName;
   private final String research;
   private final ItemStack[] ingredients;
   
   public Collection(String research, ItemStack... stacks) {
/* 20 */     this.research = research;
/* 21 */     this.ingredients = stacks;
   }
   
   public Collection(String research, Item... items) {
     this(research, to_ingredients(items).<ItemStack>toArray(new ItemStack[0]));
   }
   
   public Collection(String research, Block... blocks) {
      this(research, to_ingredients(blocks).<ItemStack>toArray(new ItemStack[0]));
   }
   
   public static void checkThing(EntityPlayer player, Collection collection, ItemStack stack) {
/* 33 */     for (ItemStack object : collection.getIngredients()) {
/* 34 */       if (object.getItem() == stack.getItem() && object.getMetadata() == stack.getMetadata())
/* 35 */         progress(player, collection, stack); 
     } 
   }
   public static void progress(EntityPlayer player, Collection collection, ItemStack stack) {
/* 39 */     IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(player);
     
      knowledge.addResearch(collection
/* 42 */         .getRegistryName() + ":" + stack.getItem().getRegistryName() + "_" + stack.getMetadata());
/* 43 */     if (isFinish(player, collection))
/* 44 */       finish(player, collection); 
   }
   
   public static void finish(EntityPlayer player, Collection collection) {
/* 48 */     IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(player);
     
/* 50 */     knowledge.addResearch("collection:" + collection.getRegistryName());
/* 51 */     for (ItemStack stack : collection.getIngredients())
/* 52 */       knowledge.removeResearch(collection
/* 53 */           .getRegistryName() + ":" + stack.getItem().getRegistryName() + "_" + stack.getMetadata()); 
   }
   
   public static boolean isFinish(EntityPlayer player, Collection collection) {
/* 57 */     IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(player);
     
/* 59 */     if (knowledge.isResearchKnown("collection:" + collection.getRegistryName()))
/* 60 */       return true; 
/* 61 */     for (ItemStack stack : collection.getIngredients()) {
/* 62 */       if (!knowledge.isResearchKnown("collection:" + collection
/* 63 */           .getRegistryName() + ":" + stack.getItem().getRegistryName()))
/* 64 */         return false; 
/* 65 */     }  return true;
   }
   
   private static List<ItemStack> to_ingredients(Item... items) {
/* 69 */     List<ItemStack> ingredients = new ArrayList<>();
     
/* 71 */     for (Item item : items)
/* 72 */       ingredients.add(new ItemStack(item)); 
/* 73 */     return ingredients;
   }
   
   private static List<ItemStack> to_ingredients(Block... blocks) {
/* 77 */     List<ItemStack> ingredients = new ArrayList<>();
     
/* 79 */     for (Block block : blocks)
/* 80 */       ingredients.add(new ItemStack(block)); 
/* 81 */     return ingredients;
   }
   
   public void setRegistryName(String name) {
/* 85 */     this.registryName = name;
   }
   
   public String getRegistryName() {
/* 89 */     return this.registryName;
   }
   
   public String getResearch() {
/* 93 */     return this.research;
   }
   
   public ItemStack[] getIngredients() {
/* 97 */     return this.ingredients;
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\research\collections\Collection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */