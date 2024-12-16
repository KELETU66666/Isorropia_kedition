 package fr.wind_blade.isorropia.client.renderer.entities;

 import fr.wind_blade.isorropia.common.entities.EntitySaehrimnir;
 import net.minecraft.client.model.ModelBase;
 import net.minecraft.client.model.ModelPig;
 import net.minecraft.client.renderer.entity.RenderLiving;
 import net.minecraft.client.renderer.entity.RenderManager;
 import net.minecraft.util.ResourceLocation;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;

 @SideOnly(Side.CLIENT)
 public class RenderSaehrimnir extends RenderLiving<EntitySaehrimnir> {
/* 14 */   private static final ResourceLocation PIG_TEXTURES = new ResourceLocation("textures/entity/pig/pig.png");

   public RenderSaehrimnir(RenderManager renderManagerIn) {
      super(renderManagerIn, (ModelBase)new ModelPig(), 0.5F);
   }


   protected ResourceLocation getEntityTexture(EntitySaehrimnir entity) {
/* 22 */     return PIG_TEXTURES;
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\client\renderer\entities\RenderSaehrimnir.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */