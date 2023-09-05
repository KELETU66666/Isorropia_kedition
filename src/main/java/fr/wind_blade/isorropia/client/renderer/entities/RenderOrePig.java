 package fr.wind_blade.isorropia.client.renderer.entities;
 
 import net.minecraft.client.renderer.entity.RenderManager;
 import net.minecraft.client.renderer.entity.RenderPig;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.passive.EntityPig;
 import net.minecraft.util.ResourceLocation;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 
 @SideOnly(Side.CLIENT)
 public class RenderOrePig
   extends RenderPig {
/* 14 */   public static final ResourceLocation TEXTURE = new ResourceLocation("isorropia", "textures/entity/oreboar.png");
   
   public RenderOrePig(RenderManager renderManagerIn) {
/* 17 */     super(renderManagerIn);
   }
 
   
   protected ResourceLocation getEntityTexture(EntityPig entity) {
/* 22 */     return TEXTURE;
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\client\renderer\entities\RenderOrePig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */