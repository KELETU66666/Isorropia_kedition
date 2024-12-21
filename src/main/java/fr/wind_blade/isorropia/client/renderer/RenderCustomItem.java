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
    public ItemCameraTransforms.TransformType transform = ItemCameraTransforms.TransformType.GUI;


    public void renderByItem(ItemStack stack) {
        super.renderByItem(stack);
        if (stack.getItem() == Item.getItemFromBlock((Block) BlocksIS.blockJarSoul)) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.5D, 0.5D, 0.4D);
            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            Minecraft.getMinecraft().getRenderItem().renderItem(stack, RenderEventHandler.jar_soul);
            GlStateManager.popMatrix();
        }
    }
}