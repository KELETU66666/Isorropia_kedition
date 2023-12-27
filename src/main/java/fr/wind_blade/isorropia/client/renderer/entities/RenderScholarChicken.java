 package fr.wind_blade.isorropia.client.renderer.entities;
 import fr.wind_blade.isorropia.common.entities.EntityScholarChicken;
 import net.minecraft.client.model.ModelBase;
 import net.minecraft.client.model.ModelChicken;
 import net.minecraft.client.renderer.entity.RenderLiving;
 import net.minecraft.client.renderer.entity.RenderManager;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.util.ResourceLocation;
 import net.minecraft.util.math.MathHelper;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 
 @SideOnly(Side.CLIENT)
 public class RenderScholarChicken extends RenderLiving<EntityScholarChicken> {
    public static ResourceLocation TEXTURE = new ResourceLocation("isorropia", "textures/entity/scholar_chicken.png");
 
   
   public RenderScholarChicken(RenderManager rendermanagerIn) {
/* 20 */     super(rendermanagerIn, (ModelBase)new ModelChicken(), 0.5F);
   }
 
   
   protected ResourceLocation getEntityTexture(EntityScholarChicken entity) {
     return TEXTURE;
   }
 
   
   protected float handleRotationFloat(EntityScholarChicken livingBase, float partialTicks) {
/* 30 */     float f = livingBase.oFlap + (livingBase.wingRotation - livingBase.oFlap) * partialTicks;
/* 31 */     float f1 = livingBase.oFlapSpeed + (livingBase.destPos - livingBase.oFlapSpeed) * partialTicks;
/* 32 */     return (MathHelper.sin(f) + 1.0F) * f1;
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\client\renderer\entities\RenderScholarChicken.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */