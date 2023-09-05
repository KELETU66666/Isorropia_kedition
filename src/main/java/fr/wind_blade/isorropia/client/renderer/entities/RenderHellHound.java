 package fr.wind_blade.isorropia.client.renderer.entities;
 
 import fr.wind_blade.isorropia.common.entities.EntityHellHound;
 import net.minecraft.client.model.ModelBase;
 import net.minecraft.client.model.ModelWolf;
 import net.minecraft.client.renderer.entity.RenderLiving;
 import net.minecraft.client.renderer.entity.RenderManager;
 import net.minecraft.entity.Entity;
 import net.minecraft.util.ResourceLocation;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 
 @SideOnly(Side.CLIENT)
 public class RenderHellHound extends RenderLiving<EntityHellHound> {
   public static ResourceLocation TEXTURE = new ResourceLocation("isorropia", "textures/entity/hellhound.png");
   
   public RenderHellHound(RenderManager rendermanagerIn) {
/* 18 */     super(rendermanagerIn, (ModelBase)new ModelWolf(), 0.5F);
   }
 
   
   protected ResourceLocation getEntityTexture(EntityHellHound entity) {
/* 23 */     return TEXTURE;
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\client\renderer\entities\RenderHellHound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */