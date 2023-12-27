 package fr.wind_blade.isorropia.common.items;
 
 import java.util.List;
 import java.util.stream.Collectors;
 import net.minecraft.item.ItemStack;
 import net.minecraft.util.NonNullList;
 import net.minecraftforge.items.ItemStackHandler;
 
 
 
 
 
 
 
 
 public class ISItemStackHandler
   extends ItemStackHandler
 {
   public ISItemStackHandler(int size) {
/* 20 */     super(size);
   }
   
   public int getSize() {
      return (int)this.stacks.stream().filter(stack -> !stack.isEmpty()).count();
   }
   
   public ItemStack removeLastItemStack() {
      for (int i = this.stacks.size(); i > 0; i--) {
        if (!((ItemStack)this.stacks.get(i - 1)).isEmpty()) {
/* 30 */         ItemStack stack = extractItem(i - 1, 1, false);
/* 31 */         if (!stack.isEmpty()) {
/* 32 */           return stack;
         }
       } 
     } 
/* 36 */     return ItemStack.EMPTY;
   }
   
   public NonNullList<ItemStack> getStacks() {
/* 40 */     return this.stacks;
   }
   
   public List<ItemStack> toList() {
/* 44 */     return (List<ItemStack>)this.stacks.stream().filter(stack -> !stack.isEmpty()).collect(Collectors.toList());
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\items\ISItemStackHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */