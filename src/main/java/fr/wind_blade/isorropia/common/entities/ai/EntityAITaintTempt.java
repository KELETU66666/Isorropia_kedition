 package fr.wind_blade.isorropia.common.entities.ai;
 
 import java.util.Collections;
 import java.util.Set;
 import net.minecraft.block.Block;
 import net.minecraft.entity.EntityCreature;
 import net.minecraft.entity.ai.EntityAITempt;
 import net.minecraft.item.Item;
 import net.minecraft.item.ItemStack;
 
 
 
 public class EntityAITaintTempt
   extends EntityAITempt
 {
   public EntityAITaintTempt(EntityCreature temptedEntityIn, double speedIn, boolean scaredByPlayerMovementIn) {
      this(temptedEntityIn, speedIn, scaredByPlayerMovementIn, Collections.emptySet());
   }
 
   
   public EntityAITaintTempt(EntityCreature temptedEntityIn, double speedIn, boolean scaredByPlayerMovementIn, Set<Item> aditionalTemptItems) {
/* 22 */     super(temptedEntityIn, speedIn, scaredByPlayerMovementIn, aditionalTemptItems);
   }
 
   
   protected boolean isTempting(ItemStack stack) {
      Item item = stack.getItem();
      if (item instanceof net.minecraft.item.ItemBlock) {
        Block block = Block.getBlockFromItem(item);
       
/* 31 */       if (block instanceof thaumcraft.common.blocks.world.taint.ITaintBlock) {
/* 32 */         return true;
       }
     } 
     
/* 36 */     return super.isTempting(stack);
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\entities\ai\EntityAITaintTempt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */