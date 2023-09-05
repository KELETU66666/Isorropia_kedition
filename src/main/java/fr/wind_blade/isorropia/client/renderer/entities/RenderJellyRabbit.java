 package fr.wind_blade.isorropia.client.renderer.entities;

 import fr.wind_blade.isorropia.client.model.ModelJelly;
 import fr.wind_blade.isorropia.client.model.ModelJellyRabbit;
 import fr.wind_blade.isorropia.common.entities.EntityJellyRabbit;
 import net.minecraft.client.model.ModelBase;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.entity.RenderLiving;
 import net.minecraft.client.renderer.entity.RenderManager;
 import net.minecraft.client.renderer.entity.layers.LayerRenderer;
 import net.minecraft.util.ResourceLocation;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 import org.lwjgl.opengl.GL11;
 import thaumcraft.api.aspects.Aspect;

 import java.awt.*;
 
 @SideOnly(Side.CLIENT)
 public class RenderJellyRabbit
   extends RenderLiving<EntityJellyRabbit>
 {
/*  24 */   public static ResourceLocation TEXTURE = new ResourceLocation("isorropia", "textures/entity/white.png");
   public static final float MC_HEIGHT = 1.5F;
   
   public RenderJellyRabbit(RenderManager renderManagerIn) {
/*  28 */     super(renderManagerIn, new ModelJellyRabbit(), 0.3F);
/*  29 */     addLayer(new LayerRabbitJelly(this));
   }
 
   
   protected void renderModel(EntityJellyRabbit rabbit, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
     Color color;
/*  35 */     GlStateManager.pushMatrix();
/*  36 */     Aspect aspect = rabbit.getAspect();
     
/*  38 */     if (aspect != null) {
/*  39 */       color = new Color(aspect.getColor());
     } else {
/*  41 */       color = new Color(Aspect.SENSES.getColor());
     } 
/*  43 */     GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 0.7F);
     
/*  45 */     float size = 0.55F;
/*  46 */     float offset = rabbit.height * (size - 1.0F) / 2.0F + 0.08F;
/*  47 */     GlStateManager.translate(0.0F, 1.5F + offset, 0.0F);
/*  48 */     GlStateManager.scale(size, size, size);
/*  49 */     GlStateManager.translate(0.0F, -1.5F, 0.0F);
/*  50 */     super.renderModel(rabbit, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
/*  51 */     GlStateManager.color(1.0F, 1.0F, 1.0F);
/*  52 */     GlStateManager.popMatrix();
   }
 
   
   protected ResourceLocation getEntityTexture(EntityJellyRabbit entity) {
/*  57 */     return TEXTURE;
   }
   
   public class LayerRabbitJelly
     implements LayerRenderer<EntityJellyRabbit> {
     private final RenderJellyRabbit jellyRabbitRenderer;
/*  63 */     private final ModelBase jellyModel = new ModelJelly();
/*  64 */     public final ResourceLocation TEXTURE_LAYER = new ResourceLocation("isorropia", "textures/entity/jelly_rabbit.png");
 
     
     public LayerRabbitJelly(RenderJellyRabbit jellyRabbitRendererIn) {
/*  68 */       this.jellyRabbitRenderer = jellyRabbitRendererIn;
     }
 
 
 
     
     public void doRenderLayer(EntityJellyRabbit rabbit, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
/*  75 */       if (!rabbit.isInvisible()) {
/*  76 */         Color color; GlStateManager.pushMatrix();
         
/*  78 */         Aspect aspect = rabbit.getAspect();
 
         
/*  81 */         if (aspect != null) {
/*  82 */           color = new Color(aspect.getColor());
         } else {
/*  84 */           color = new Color(Aspect.SENSES.getColor());
         } 
         
/*  87 */         GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 0.7F);
         
/*  89 */         float jellySize = rabbit.getJellySize() / 25.0F + 1.0F;
/*  90 */         float offset = rabbit.height * (jellySize - 1.0F) / 2.0F;
/*  91 */         GlStateManager.translate(0.0F, 1.5F + offset, 0.0F);
/*  92 */         GlStateManager.scale(jellySize, jellySize, jellySize);
/*  93 */         GlStateManager.translate(0.0F, -1.5F, 0.0F);
/*  94 */         GlStateManager.scale(0.6F, 0.6F, 0.6F);
         
/*  96 */         GlStateManager.enableBlend();
         GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
         
/*  99 */         RenderJellyRabbit.this.bindTexture(this.TEXTURE_LAYER);
/* 100 */         this.jellyModel.setModelAttributes(this.jellyRabbitRenderer.getMainModel());
/* 101 */         this.jellyModel.render(rabbit, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
/* 102 */         GlStateManager.disableBlend();
/* 103 */         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 104 */         GlStateManager.popMatrix();
       } 
     }
 
     
     public boolean shouldCombineTextures() {
/* 110 */       return true;
     }
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\client\renderer\entities\RenderJellyRabbit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */