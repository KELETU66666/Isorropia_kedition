 package fr.wind_blade.isorropia.common;
 
 import fr.wind_blade.isorropia.common.events.RegistryEventHandler;
 import fr.wind_blade.isorropia.common.items.misc.ItemCat;
 import net.minecraft.creativetab.CreativeTabs;
 import net.minecraft.entity.EntityLiving;
 import net.minecraft.init.Items;
 import net.minecraft.item.ItemMonsterPlacer;
 import net.minecraft.item.ItemStack;
 import net.minecraft.util.NonNullList;
 import net.minecraft.util.ResourceLocation;
 
 
 public class IsorropiaCreativeTabs
   extends CreativeTabs
 {
   public IsorropiaCreativeTabs() {
/* 18 */     super("isorropia");
   }
 
   
   public void displayAllRelevantItems(NonNullList<ItemStack> items) {
/* 23 */     super.displayAllRelevantItems(items);
/* 24 */     for (String key : RegistryEventHandler.ENTITIES.keySet()) {
       if (!EntityLiving.class.isAssignableFrom((Class)RegistryEventHandler.ENTITIES.get(key)))
         continue; 
        ItemStack stack = new ItemStack(Items.SPAWN_EGG);
        ItemMonsterPlacer.applyEntityIdToItemStack(stack, new ResourceLocation("isorropia", key));
        items.add(stack);
     } 
   }
 
   
   public ItemStack createIcon() {
/* 35 */     return ItemCat.createCat(ItemCat.EnumCat.CHICKEN, "Isorropia");
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\IsorropiaCreativeTabs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */