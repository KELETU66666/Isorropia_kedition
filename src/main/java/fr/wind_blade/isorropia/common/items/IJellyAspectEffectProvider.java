 package fr.wind_blade.isorropia.common.items;
 
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.item.ItemStack;
 
 
 public interface IJellyAspectEffectProvider
 {
   default void onFoodEeaten(EntityPlayer player, ItemStack stack) {}
   
   default boolean canBeEaten(EntityPlayer player, ItemStack stack) {
/* 12 */     return true;
   }
   
   default int getEatDuration(ItemStack stack) {
/* 16 */     return 32;
   }
   
   default int getHungerReplinish(ItemStack stack) {
/* 20 */     return 2;
   }
   
   default int getSaturationReplinish(ItemStack stack) {
/* 24 */     return getHungerReplinish(stack);
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\items\IJellyAspectEffectProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */