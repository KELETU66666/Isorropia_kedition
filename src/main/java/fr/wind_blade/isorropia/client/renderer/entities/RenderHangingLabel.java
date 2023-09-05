 package fr.wind_blade.isorropia.client.renderer.entities;
 
 import fr.wind_blade.isorropia.common.entities.EntityHangingLabel;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.entity.Render;
 import net.minecraft.client.renderer.entity.RenderManager;
 import net.minecraft.entity.Entity;
 import net.minecraft.util.ResourceLocation;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 import org.lwjgl.opengl.GL11;
 import thaumcraft.client.lib.UtilsFX;
 
 @SideOnly(Side.CLIENT)
 public class RenderHangingLabel<T extends EntityHangingLabel>
   extends Render<T> {
/* 17 */   public static ResourceLocation LABEL_TEXTURE = new ResourceLocation("thaumcraft", "textures/models/label.png");
   
   public RenderHangingLabel(RenderManager renderManager) {
/* 20 */     super(renderManager);
   }
 
   
   protected ResourceLocation getEntityTexture(T entity) {
     return LABEL_TEXTURE;
   }
 
   
   public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
/* 30 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 31 */     GlStateManager.pushMatrix();
/* 32 */     GlStateManager.disableLighting();
/* 33 */     GlStateManager.translate((float)x, (float)y, (float)z);
/* 34 */     GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
/* 35 */     GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks, 0.0F, 1.0F, 0.0F);
     
/* 37 */     GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);
 
     
/* 40 */     GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.translate(0.0D, 0.0D, 0.03D);
/* 42 */     GlStateManager.enableRescaleNormal();
     
/* 44 */     if (this.renderOutlines) {
/* 45 */       GlStateManager.enableColorMaterial();
/* 46 */       GlStateManager.enableOutlineMode(getTeamColor(entity));
     } 
     
/* 49 */     renderEntity(entity);
     
/* 51 */     GlStateManager.translate(-7.0F, -8.0F, -0.01F);
/* 52 */     if (this.renderOutlines) {
/* 53 */       GlStateManager.disableOutlineMode();
/* 54 */       GlStateManager.disableColorMaterial();
     } 
     
/* 57 */     GlStateManager.disableRescaleNormal();
/* 58 */     GlStateManager.enableLighting();
/* 59 */     GlStateManager.popMatrix();
/* 60 */     super.doRender(entity, x, y, z, entityYaw, partialTicks);
   }
   
   private void renderEntity(T entity) {
/* 64 */     GL11.glPushMatrix();
/* 65 */     UtilsFX.renderQuadCentered(LABEL_TEXTURE, 0.5F, 1.0F, 1.0F, 1.0F, -99, 771, 1.0F);
/* 66 */     GL11.glRotated(180.0D, 0.0D, 1.0D, 0.0D);
/* 67 */     UtilsFX.renderQuadCentered(LABEL_TEXTURE, 0.5F, 1.0F, 1.0F, 1.0F, -99, 771, 1.0F);
/* 68 */     GL11.glPopMatrix();
/* 69 */     if (entity.getAspect() != null) {
/* 70 */       GL11.glPushMatrix();
/* 71 */       GL11.glTranslatef(0.0F, 0.0F, -0.001F);
/* 72 */       GL11.glScaled(0.021D, 0.021D, 0.021D);
/* 73 */       UtilsFX.drawTag(-8, -8, entity.getAspect());
/* 74 */       GL11.glPopMatrix();
     } 
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\client\renderer\entities\RenderHangingLabel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */