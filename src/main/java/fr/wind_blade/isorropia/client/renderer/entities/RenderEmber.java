 package fr.wind_blade.isorropia.client.renderer.entities;
 
 import fr.wind_blade.isorropia.common.entities.projectile.EntityEmber;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.renderer.entity.Render;
 import net.minecraft.client.renderer.entity.RenderManager;
 import net.minecraft.client.renderer.texture.TextureMap;
 import net.minecraft.entity.Entity;
 import net.minecraft.util.ResourceLocation;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 import org.lwjgl.opengl.GL11;
 import thaumcraft.client.lib.UtilsFX;
 
 @SideOnly(Side.CLIENT)
 public class RenderEmber
   extends Render<EntityEmber>
 {
   public static final ResourceLocation particleTexture = new ResourceLocation("isorropia", "textures/misc/particles.png");
 
   
   public RenderEmber(RenderManager rm) {
/* 23 */     super(rm);
/* 24 */     this.shadowSize = 0.0F;
   }
   
   public void renderEntityAt(EntityEmber entity, double x, double y, double z, float fq, float pticks) {
      GL11.glPushMatrix();
      GL11.glAlphaFunc(516, 0.003921569F);
/* 30 */     GL11.glEnable(3042);
/* 31 */     GL11.glBlendFunc(770, 1);
/* 32 */     GL11.glPushMatrix();
/* 33 */     GL11.glDepthMask(false);
/* 34 */     GL11.glDisable(2884);
/* 35 */     int i = entity.ticksExisted % 7;
/* 36 */     (Minecraft.getMinecraft()).renderEngine.bindTexture(particleTexture);
/* 37 */     UtilsFX.renderFacingQuad(entity.posX, entity.posY + entity.height / 1.75D, entity.posZ, 16, 16, 144 + i + 7, 0.08F, 11197951, 1.0F, 1, pticks);
     
/* 39 */     GL11.glEnable(2884);
/* 40 */     GL11.glDepthMask(true);
      GL11.glPopMatrix();
/* 42 */     GL11.glDisable(3042);
/* 43 */     GL11.glAlphaFunc(516, 0.1F);
/* 44 */     GL11.glPopMatrix();
   }
 
   
   public void doRender(EntityEmber entity, double d, double d1, double d2, float f, float f1) {
/* 49 */     renderEntityAt(entity, d, d1, d2, f, f1);
   }
 
   
   protected ResourceLocation getEntityTexture(EntityEmber entity) {
/* 54 */     return TextureMap.LOCATION_BLOCKS_TEXTURE;
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\client\renderer\entities\RenderEmber.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */