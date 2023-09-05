 package fr.wind_blade.isorropia.client.renderer;
 
 import net.minecraft.client.model.ModelBase;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.entity.Render;
 import net.minecraft.client.renderer.entity.RenderManager;
 import net.minecraft.entity.Entity;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 
 @SideOnly(Side.CLIENT)
 public abstract class RenderEntity<T extends Entity>
   extends Render<T> {
   protected ModelBase mainModel;
   
   public RenderEntity(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
/* 17 */     super(renderManagerIn);
/* 18 */     this.mainModel = modelBaseIn;
     this.shadowSize = shadowSizeIn;
   }
 
   
   public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
/* 24 */     GlStateManager.pushMatrix();
     GlStateManager.translate((float)x, (float)y + 1.5F, (float)z);
      GlStateManager.rotate(180.0F - entityYaw, 0.0F, 1.0F, 0.0F);
      GlStateManager.scale(-1.0F, -1.0F, 1.0F);
     
      if (this.renderOutlines) {
/* 30 */       GlStateManager.enableColorMaterial();
/* 31 */       GlStateManager.enableOutlineMode(getTeamColor(entity));
     } 
     
/* 34 */     bindEntityTexture(entity);
/* 35 */     ModelBase model = this.mainModel;
 
     
/* 38 */     float pitch = -((float)Math.toRadians((entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks)));
     
/* 40 */     model.render(entity, 0.0F, 0.0F, entity.ticksExisted + partialTicks, 0.0F, pitch, 0.0625F);
     
/* 42 */     if (this.renderOutlines) {
/* 43 */       GlStateManager.disableOutlineMode();
/* 44 */       GlStateManager.disableColorMaterial();
     } 
     
/* 47 */     GlStateManager.popMatrix();
     
/* 49 */     super.doRender(entity, x, y, z, entityYaw, partialTicks);
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\client\renderer\RenderEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */