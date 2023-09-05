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
 
 public class ItemSomaticBrain
   extends Item implements IBauble, IRenderBauble {
   public ItemSomaticBrain() {
/* 18 */     setMaxStackSize(1);
   }
 
   
   public EnumRarity getRarity(ItemStack itemstack) {
/* 23 */     return EnumRarity.RARE;
   }
 
   
   public BaubleType getBaubleType(ItemStack arg0) {
      return BaubleType.HEAD;
   }
 
   
   public void onPlayerBaubleRender(ItemStack stack, EntityPlayer player, IRenderBauble.RenderType type, float arg3) {
/* 33 */     if (type == IRenderBauble.RenderType.HEAD) {
/* 34 */       boolean helmet = !player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty();
/* 35 */       IRenderBauble.Helper.translateToHeadLevel(player);
/* 36 */       IRenderBauble.Helper.translateToFace();
/* 37 */       IRenderBauble.Helper.defaultTransforms();
/* 38 */       GlStateManager.scale(1.81F, -1.81F, 1.81F);
/* 39 */       GlStateManager.translate(0.0D, helmet ? -1.73D : -1.72D, 0.27D);
/* 40 */       GlStateManager.enableBlend();
        (Minecraft.getMinecraft()).renderEngine.bindTexture(ModelSomaticBrain.TEX);
/* 42 */       ModelSomaticBrain.INSTANCE.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
/* 43 */       GlStateManager.disableBlend();
     } 
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\items\baubles\ItemSomaticBrain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */