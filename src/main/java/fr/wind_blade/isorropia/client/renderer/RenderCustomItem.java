 package fr.wind_blade.isorropia.client.renderer;
 
 import fr.wind_blade.isorropia.client.libs.RenderEventHandler;
 import fr.wind_blade.isorropia.common.blocks.BlocksIS;
 import net.minecraft.block.Block;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
 import net.minecraft.client.renderer.texture.TextureMap;
 import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
 import net.minecraft.item.Item;
 import net.minecraft.item.ItemStack;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 
 @SideOnly(Side.CLIENT)
 public class RenderCustomItem extends TileEntityItemStackRenderer {
/* 18 */   public ItemCameraTransforms.TransformType transform = ItemCameraTransforms.TransformType.GUI;
 
   
   public void renderByItem(ItemStack stack) {
/* 22 */     super.renderByItem(stack);
/* 23 */     if (stack.getItem() == Item.getItemFromBlock((Block)BlocksIS.blockJarSoul)) {
/* 24 */       GlStateManager.pushMatrix();
       GlStateManager.translate(0.5D, 0.5D, 0.4D);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getRenderItem().renderItem(stack, RenderEventHandler.jar_soul);
        GlStateManager.popMatrix();
     } 
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\client\renderer\RenderCustomItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */