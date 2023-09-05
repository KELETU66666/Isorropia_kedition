 package fr.wind_blade.isorropia.client.renderer.entities.layers;
 
 import fr.wind_blade.isorropia.client.renderer.entities.RenderSheeder;
 import fr.wind_blade.isorropia.common.entities.EntitySheeder;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.entity.layers.LayerRenderer;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.util.ResourceLocation;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 
 @SideOnly(Side.CLIENT)
 public class LayerSheederWool implements LayerRenderer<EntitySheeder> {
   public static final ResourceLocation SPIDER_EYES = new ResourceLocation("isorropia", "textures/entity/sheederoverlay.png");
   
   private final RenderSheeder sheederRenderer;
   
   public LayerSheederWool(RenderSheeder sheederRendererIn) {
/* 20 */     this.sheederRenderer = sheederRendererIn;
   }
 
 
   
   public void doRenderLayer(EntitySheeder sheeder, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
      if (!sheeder.getSheared()) {
        if (sheeder.isInvisible()) {
          GlStateManager.depthMask(false);
       } else {
/* 30 */         GlStateManager.depthMask(true);
       } 
       
/* 33 */       GlStateManager.translate(0.0D, -0.01D, -0.01D);
/* 34 */       GlStateManager.scale(1.01D, 1.01D, 1.02D);
/* 35 */       this.sheederRenderer.bindTexture(SPIDER_EYES);
/* 36 */       this.sheederRenderer.getMainModel().render((Entity)sheeder, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
     } 
   }
 
 
   
   public boolean shouldCombineTextures() {
/* 43 */     return true;
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\client\renderer\entities\layers\LayerSheederWool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */