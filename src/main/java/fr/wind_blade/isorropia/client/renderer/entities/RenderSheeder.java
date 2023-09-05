 package fr.wind_blade.isorropia.client.renderer.entities;
 import fr.wind_blade.isorropia.client.renderer.entities.layers.LayerSheederWool;
 import fr.wind_blade.isorropia.common.entities.EntitySheeder;
 import net.minecraft.client.renderer.entity.RenderManager;
 import net.minecraft.client.renderer.entity.RenderSpider;
 import net.minecraft.client.renderer.entity.layers.LayerRenderer;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.monster.EntitySpider;
 import net.minecraft.util.ResourceLocation;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 
 @SideOnly(Side.CLIENT)
 public class RenderSheeder extends RenderSpider<EntitySheeder> {
   public static final ResourceLocation TEXTURE = new ResourceLocation("isorropia", "textures/entity/sheeder.png");
   
   public RenderSheeder(RenderManager renderManagerIn) {
/* 18 */     super(renderManagerIn);
     addLayer((LayerRenderer)new LayerSheederWool(this));
   }
 
   
   protected ResourceLocation getEntityTexture(EntitySheeder entity) {
/* 24 */     return TEXTURE;
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\client\renderer\entities\RenderSheeder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */