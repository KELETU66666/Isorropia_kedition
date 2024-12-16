 package fr.wind_blade.isorropia.client.renderer.entities;

 import fr.wind_blade.isorropia.client.model.ModelSaehrimnirReborn;
 import fr.wind_blade.isorropia.client.renderer.RenderEntity;
 import fr.wind_blade.isorropia.common.entities.EntitySaehrimnirReborn;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.model.ModelBase;
 import net.minecraft.client.renderer.BufferBuilder;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.Tessellator;
 import net.minecraft.client.renderer.entity.RenderManager;
 import net.minecraft.client.renderer.texture.TextureAtlasSprite;
 import net.minecraft.client.renderer.texture.TextureMap;
 import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.util.ResourceLocation;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 
 @SideOnly(Side.CLIENT)
 public class RenderSaehrimnirReborn extends RenderEntity<EntitySaehrimnirReborn> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/blocks/cobblestone.png");
   
   public RenderSaehrimnirReborn(RenderManager renderManagerIn) {
     super(renderManagerIn, new ModelSaehrimnirReborn(), 0.5F);
   }
 
   
   protected ResourceLocation getEntityTexture(EntitySaehrimnirReborn entity) {
      return TEXTURE;
   }
 
   
   public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
      if (this.renderManager.options != null && 
        entityIn.canRenderOnFire() && (!(entityIn instanceof EntityPlayer) || 
        !((EntityPlayer)entityIn).isSpectator())) {
        renderEntityOnFire(entityIn, x, y, z, partialTicks);
     }
   }
 
   
   private void renderEntityOnFire(Entity entity, double x, double y, double z, float partialTicks) {
      GlStateManager.disableLighting();
     TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();
      TextureAtlasSprite textureatlassprite = texturemap.getAtlasSprite("minecraft:blocks/fire_layer_0");
      TextureAtlasSprite textureatlassprite1 = texturemap.getAtlasSprite("minecraft:blocks/fire_layer_1");
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)x, (float)y, (float)z);
      float f = entity.width * 1.4F;
      GlStateManager.scale(f, f, f);
      Tessellator tessellator = Tessellator.getInstance();
     BufferBuilder vertexbuffer = tessellator.getBuffer();
     float f1 = 0.5F;
      float f3 = entity.height / f;
/*  57 */     float f4 = (float)(entity.posY - (entity.getEntityBoundingBox()).minY);
      GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GlStateManager.translate(0.0F, 0.0F, -0.3F + (int)f3 * 0.02F);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      float f5 = 0.0F;
      int i = 0;
      vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
     
/*  65 */     while (f3 > 0.0F) {
/*  66 */       TextureAtlasSprite textureatlassprite2 = (i % 2 == 0) ? textureatlassprite : textureatlassprite1;
        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        float f6 = textureatlassprite2.getMinU();
/*  69 */       float f7 = textureatlassprite2.getMinV();
/*  70 */       float f8 = textureatlassprite2.getMaxU();
        float f9 = textureatlassprite2.getMaxV();
       
        if (i / 2 % 2 == 0) {
          float f10 = f8;
          f8 = f6;
          f6 = f10;
       } 
       
        vertexbuffer.pos((f1 - 0.0F), (0.0F - f4), f5).tex(f8, f9).endVertex();
/*  80 */       vertexbuffer.pos((-f1 - 0.0F), (0.0F - f4), f5).tex(f6, f9).endVertex();
        vertexbuffer.pos((-f1 - 0.0F), (1.4F - f4), f5).tex(f6, f7).endVertex();
        vertexbuffer.pos((f1 - 0.0F), (1.4F - f4), f5).tex(f8, f7).endVertex();
        f3 -= 0.45F;
        f4 -= 0.45F;
        f1 *= 0.9F;
        f5 += 0.03F;
        i++;
     } 
     
      tessellator.draw();
      GlStateManager.popMatrix();
     GlStateManager.enableLighting();
   }
 
 
   
   public void doRender(EntitySaehrimnirReborn entity, double x, double y, double z, float entityYaw, float partialTicks) {
     GlStateManager.pushMatrix();
      float scale = (entity.ticksExisted + partialTicks) / 24000.0F;
      GlStateManager.translate((float)x, (float)y + (
          (entity.getEntityBoundingBox()).maxY - (entity.getEntityBoundingBox()).minY) / 2.0D, (float)z);
/* 102 */     GlStateManager.scale(scale, scale, scale);
    GlStateManager.translate(0.0D, -0.5D, 0.0D);
      if (this.renderOutlines) {
      GlStateManager.enableColorMaterial();
        GlStateManager.enableOutlineMode(getTeamColor(entity));
     } 
     
     bindEntityTexture(entity);
      ModelBase model = this.mainModel;
 
     
   float pitch = -((float)Math.toRadians((entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks)));
 
     
/* 116 */     model.render(entity, 0.0F, 0.0F, entity.ticksExisted + partialTicks, 0.0F, pitch, 0.0625F);
     
/* 118 */     if (this.renderOutlines) {
/* 119 */       GlStateManager.disableOutlineMode();
      GlStateManager.disableColorMaterial();
     } 
     
/* 123 */     GlStateManager.popMatrix();
     
/* 125 */     if (!this.renderOutlines)
/* 126 */       renderName(entity, x, y, z);
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\client\renderer\entities\RenderSaehrimnirReborn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */