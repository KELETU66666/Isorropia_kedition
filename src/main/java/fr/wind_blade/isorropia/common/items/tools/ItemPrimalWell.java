 package fr.wind_blade.isorropia.common.items.tools;
 
 import net.minecraft.item.EnumRarity;
 import net.minecraft.item.Item;
 import net.minecraft.item.ItemStack;
 import thaumcraft.api.items.IScribeTools;
 
 public class ItemPrimalWell
   extends Item implements IScribeTools {
   public ItemPrimalWell() {
     setMaxStackSize(1);
/* 12 */     setMaxDamage(200);
   }
 
   
   public EnumRarity getRarity(ItemStack stack) {
/* 17 */     return EnumRarity.EPIC;
   }
 
   
   public void setDamage(ItemStack stack, int damage) {
/* 22 */     super.setDamage(stack, 0);
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\items\tools\ItemPrimalWell.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */