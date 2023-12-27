package fr.wind_blade.isorropia.common.items.baubles;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.api.render.IRenderBauble;
import fr.wind_blade.isorropia.client.model.ModelSomaticBrain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemSomaticBrain extends Item implements IBauble, IRenderBauble {
    public ItemSomaticBrain() {
        setMaxStackSize(1);
    }


    public EnumRarity getRarity(ItemStack itemstack) {
        return EnumRarity.RARE;
    }


    public BaubleType getBaubleType(ItemStack arg0) {
        return BaubleType.HEAD;
    }


    public void onPlayerBaubleRender(ItemStack stack, EntityPlayer player, IRenderBauble.RenderType type, float arg3) {
        if (type == IRenderBauble.RenderType.HEAD) {
            boolean helmet = !player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty();
            IRenderBauble.Helper.translateToHeadLevel(player);
            IRenderBauble.Helper.translateToFace();
            IRenderBauble.Helper.defaultTransforms();
            GlStateManager.scale(1.81F, -1.81F, 1.81F);
            GlStateManager.translate(0.0D, helmet ? -1.73D : -1.72D, 0.27D);
            GlStateManager.enableBlend();
            (Minecraft.getMinecraft()).renderEngine.bindTexture(ModelSomaticBrain.TEX);
            ModelSomaticBrain.INSTANCE.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.disableBlend();
        }
    }
}