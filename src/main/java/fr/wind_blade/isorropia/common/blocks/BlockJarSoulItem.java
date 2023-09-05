 package fr.wind_blade.isorropia.common.blocks;
 
 import net.minecraft.block.Block;
 import net.minecraft.entity.EntityList;
 import net.minecraft.item.ItemBlock;
 import net.minecraft.item.ItemStack;
 import net.minecraft.util.ResourceLocation;
 import net.minecraft.util.text.TextComponentTranslation;
 
 public class BlockJarSoulItem
   extends ItemBlock implements IItemStackRenderProvider {
   public BlockJarSoulItem() {
/* 13 */     super((Block)BlocksIS.blockJarSoul);
   }
 
   
   public String getItemStackDisplayName(ItemStack stack) {
     String stringy;
     if (stack.hasTagCompound() && stack.getTagCompound().hasKey("ENTITY_DATA")) {
 
 
       
/* 23 */       stringy = (new TextComponentTranslation("item.jarredSoul.jarred", new Object[] { EntityList.getTranslationName(new ResourceLocation(stack.getTagCompound().getCompoundTag("ENTITY_DATA").getString("id"))) })).getFormattedText();
     } else {
       stringy = (new TextComponentTranslation("item.jarredSoul.0.name", new Object[0])).getFormattedText();
     } 
      return stringy;
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\blocks\BlockJarSoulItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */