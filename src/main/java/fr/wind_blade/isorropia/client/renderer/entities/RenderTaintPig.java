 package fr.wind_blade.isorropia.client.renderer.entities;

 import fr.wind_blade.isorropia.client.renderer.entities.layers.LayerTaintPigBorder;
 import fr.wind_blade.isorropia.common.entities.EntityTaintPig;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.model.ModelPig;
 import net.minecraft.client.renderer.entity.RenderLiving;
 import net.minecraft.client.renderer.entity.RenderManager;
 import net.minecraft.client.renderer.entity.layers.LayerRenderer;
 import net.minecraft.util.ResourceLocation;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 import org.lwjgl.opengl.GL11;
 import thaumcraft.client.lib.UtilsFX;
 
 @SideOnly(Side.CLIENT)
 public class RenderTaintPig extends RenderLiving<EntityTaintPig> {
/* 20 */   private static final ResourceLocation PIG_TEXTURES = new ResourceLocation("isorropia", "textures/entity/taintfeeder.png");
   
/* 22 */   public static final ResourceLocation nodetex = new ResourceLocation("isorropia", "textures/misc/nodes.png");
   
   public RenderTaintPig(RenderManager renderManagerIn) {
     super(renderManagerIn, new ModelPig(), 0.5F);
      addLayer((LayerRenderer)new LayerTaintPigBorder(this));
   }
 
   
   protected ResourceLocation getEntityTexture(EntityTaintPig entity) {
/* 31 */     return PIG_TEXTURES;
   }
 
   
   public void doRender(EntityTaintPig entity, double x, double y, double z, float entityYaw, float partialTicks) {
/* 36 */     super.doRender(entity, x, y, z, entityYaw, partialTicks);
/* 37 */     GL11.glPushMatrix();
/* 38 */     GL11.glAlphaFunc(516, 0.003921569F);
      GL11.glEnable(3042);
/* 40 */     GL11.glBlendFunc(770, 1);
      GL11.glPushMatrix();
/* 42 */     GL11.glDepthMask(false);
/* 43 */     GL11.glDisable(2884);
/* 44 */     int i = entity.ticksExisted % 32;
/* 45 */     (Minecraft.getMinecraft()).renderEngine.bindTexture(nodetex);
/* 46 */     UtilsFX.renderFacingQuad(entity.posX, entity.posY + entity.height / 1.75D, entity.posZ, 32, 32, 192 + i, 2.0F, 11197951, 1.0F, 1, partialTicks);
     
/* 48 */     GL11.glEnable(2884);
/* 49 */     GL11.glDepthMask(true);
/* 50 */     GL11.glPopMatrix();
/* 51 */     GL11.glDisable(3042);
/* 52 */     GL11.glAlphaFunc(516, 0.1F);
      GL11.glPopMatrix();
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\client\renderer\entities\RenderTaintPig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */