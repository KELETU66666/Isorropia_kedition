 package fr.wind_blade.isorropia.client.renderer.entities.layers;
 
 import fr.wind_blade.isorropia.client.renderer.entities.RenderTaintPig;
 import fr.wind_blade.isorropia.common.entities.EntityTaintPig;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.model.ModelPig;
 import net.minecraft.client.renderer.entity.layers.LayerRenderer;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.util.ResourceLocation;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 import org.lwjgl.opengl.GL11;
 
 @SideOnly(Side.CLIENT)
 public class LayerTaintPigBorder
   implements LayerRenderer<EntityTaintPig> {
/* 18 */   private static final ResourceLocation pigEyesTextures = new ResourceLocation("isorropia", "textures/entity/taintfeeder_eyes.png");
   
/* 20 */   private static final ModelPig border = new ModelPig(0.5F);
   private final RenderTaintPig taintRender;
   
   public LayerTaintPigBorder(RenderTaintPig renderTaintPig) {
/* 24 */     this.taintRender = renderTaintPig;
   }
 
 
   
   public void doRenderLayer(EntityTaintPig entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
/* 30 */     float f = (float)Math.sin(Math.toRadians((entitylivingbaseIn.ticksExisted % 45)) * 8.0D);
/* 31 */     (Minecraft.getMinecraft()).renderEngine.bindTexture(pigEyesTextures);
/* 32 */     GL11.glEnable(3042);
/* 33 */     GL11.glDisable(3008);
/* 34 */     GL11.glBlendFunc(770, 771);
     
/* 36 */     if (entitylivingbaseIn.isInvisible()) {
/* 37 */       GL11.glDepthMask(false);
     } else {
/* 39 */       GL11.glDepthMask(true);
     } 
      char c0 = '1';
/* 42 */     int j = 61680;
/* 43 */     int k = 0;
/* 44 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.32F + 0.32F * f);
/* 45 */     border.setModelAttributes(this.taintRender.getMainModel());
/* 46 */     border.render((Entity)entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
/* 47 */     GL11.glDisable(3042);
/* 48 */     GL11.glEnable(3008);
   }
 
   
   public boolean shouldCombineTextures() {
/* 53 */     return true;
   }
 }