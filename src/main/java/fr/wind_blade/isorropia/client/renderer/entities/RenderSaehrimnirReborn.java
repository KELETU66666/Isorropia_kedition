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
/*  23 */   private static final ResourceLocation TEXTURE = new ResourceLocation("textures/blocks/cobblestone.png");
   
   public RenderSaehrimnirReborn(RenderManager renderManagerIn) {
/*  26 */     super(renderManagerIn, new ModelSaehrimnirReborn(), 0.5F);
   }
 
   
   protected ResourceLocation getEntityTexture(EntitySaehrimnirReborn entity) {
/*  31 */     return TEXTURE;
   }
 
   
   public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
/*  36 */     if (this.renderManager.options != null && 
/*  37 */       entityIn.canRenderOnFire() && (!(entityIn instanceof EntityPlayer) || 
/*  38 */       !((EntityPlayer)entityIn).isSpectator())) {
/*  39 */       renderEntityOnFire(entityIn, x, y, z, partialTicks);
     }
   }
 
   
   private void renderEntityOnFire(Entity entity, double x, double y, double z, float partialTicks) {
/*  45 */     GlStateManager.disableLighting();
/*  46 */     TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();
/*  47 */     TextureAtlasSprite textureatlassprite = texturemap.getAtlasSprite("minecraft:blocks/fire_layer_0");
/*  48 */     TextureAtlasSprite textureatlassprite1 = texturemap.getAtlasSprite("minecraft:blocks/fire_layer_1");
/*  49 */     GlStateManager.pushMatrix();
/*  50 */     GlStateManager.translate((float)x, (float)y, (float)z);
/*  51 */     float f = entity.width * 1.4F;
/*  52 */     GlStateManager.scale(f, f, f);
/*  53 */     Tessellator tessellator = Tessellator.getInstance();
/*  54 */     BufferBuilder vertexbuffer = tessellator.getBuffer();
/*  55 */     float f1 = 0.5F;
/*  56 */     float f3 = entity.height / f;
/*  57 */     float f4 = (float)(entity.posY - (entity.getEntityBoundingBox()).minY);
/*  58 */     GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
/*  59 */     GlStateManager.translate(0.0F, 0.0F, -0.3F + (int)f3 * 0.02F);
/*  60 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  61 */     float f5 = 0.0F;
/*  62 */     int i = 0;
/*  63 */     vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
     
/*  65 */     while (f3 > 0.0F) {
/*  66 */       TextureAtlasSprite textureatlassprite2 = (i % 2 == 0) ? textureatlassprite : textureatlassprite1;
/*  67 */       bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
/*  68 */       float f6 = textureatlassprite2.getMinU();
/*  69 */       float f7 = textureatlassprite2.getMinV();
/*  70 */       float f8 = textureatlassprite2.getMaxU();
/*  71 */       float f9 = textureatlassprite2.getMaxV();
       
/*  73 */       if (i / 2 % 2 == 0) {
/*  74 */         float f10 = f8;
/*  75 */         f8 = f6;
/*  76 */         f6 = f10;
       } 
       
/*  79 */       vertexbuffer.pos((f1 - 0.0F), (0.0F - f4), f5).tex(f8, f9).endVertex();
/*  80 */       vertexbuffer.pos((-f1 - 0.0F), (0.0F - f4), f5).tex(f6, f9).endVertex();
/*  81 */       vertexbuffer.pos((-f1 - 0.0F), (1.4F - f4), f5).tex(f6, f7).endVertex();
/*  82 */       vertexbuffer.pos((f1 - 0.0F), (1.4F - f4), f5).tex(f8, f7).endVertex();
/*  83 */       f3 -= 0.45F;
/*  84 */       f4 -= 0.45F;
/*  85 */       f1 *= 0.9F;
/*  86 */       f5 += 0.03F;
/*  87 */       i++;
     } 
     
/*  90 */     tessellator.draw();
/*  91 */     GlStateManager.popMatrix();
/*  92 */     GlStateManager.enableLighting();
   }
 
 
   
   public void doRender(EntitySaehrimnirReborn entity, double x, double y, double z, float entityYaw, float partialTicks) {
     GlStateManager.pushMatrix();
/*  99 */     float scale = (entity.ticksExisted + partialTicks) / 24000.0F;
/* 100 */     GlStateManager.translate((float)x, (float)y + (
/* 101 */         (entity.getEntityBoundingBox()).maxY - (entity.getEntityBoundingBox()).minY) / 2.0D, (float)z);
/* 102 */     GlStateManager.scale(scale, scale, scale);
/* 103 */     GlStateManager.translate(0.0D, -0.5D, 0.0D);
/* 104 */     if (this.renderOutlines) {
/* 105 */       GlStateManager.enableColorMaterial();
/* 106 */       GlStateManager.enableOutlineMode(getTeamColor(entity));
     } 
     
/* 109 */     bindEntityTexture(entity);
/* 110 */     ModelBase model = this.mainModel;
 
     
/* 113 */     float pitch = -((float)Math.toRadians((entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks)));
 
     
/* 116 */     model.render(entity, 0.0F, 0.0F, entity.ticksExisted + partialTicks, 0.0F, pitch, 0.0625F);
     
/* 118 */     if (this.renderOutlines) {
/* 119 */       GlStateManager.disableOutlineMode();
/* 120 */       GlStateManager.disableColorMaterial();
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