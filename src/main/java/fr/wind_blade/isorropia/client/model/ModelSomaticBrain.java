 package fr.wind_blade.isorropia.client.model;
 
 import net.minecraft.client.model.ModelBase;
 import net.minecraft.client.model.ModelBox;
 import net.minecraft.client.model.ModelRenderer;
 import net.minecraft.entity.Entity;
 import net.minecraft.util.ResourceLocation;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 import thaumcraft.client.renderers.models.gear.ModelCustomArmor;
 
 @SideOnly(Side.CLIENT)
 public class ModelSomaticBrain
   extends ModelCustomArmor {
   public static ModelSomaticBrain INSTANCE = new ModelSomaticBrain();
/* 16 */   public static ResourceLocation TEX = new ResourceLocation("isorropia", "textures/models/somatic_brain.png");
   
   public ModelSomaticBrain() {
     super(1.0F, 0, 32, 32);
/* 20 */     this.textureWidth = 32;
/* 21 */     this.textureHeight = 32;
/* 22 */     this.bipedHead = new ModelRenderer((ModelBase)this);
/* 23 */     this.bipedHead.setRotationPoint(0.0F, 24.0F, 0.0F);
/* 24 */     this.bipedHead.cubeList.add(new ModelBox(this.bipedHead, 0, 21, -2.5F, -2.0F, -2.0F, 5, 2, 4, 0.0F, false));
     this.bipedHead.cubeList.add(new ModelBox(this.bipedHead, 0, 12, -3.0F, -3.0F, -3.0F, 6, 3, 6, 0.0F, false));
      this.bipedHead.cubeList.add(new ModelBox(this.bipedHead, 0, 0, -4.0F, -4.0F, -4.0F, 8, 4, 8, 0.0F, false));
   }
 
   
   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
/* 31 */     this.bipedHead.render(0.0625F);
   }
   
   public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
/* 35 */     modelRenderer.rotateAngleX = x;
/* 36 */     modelRenderer.rotateAngleY = y;
/* 37 */     modelRenderer.rotateAngleZ = z;
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\client\model\ModelSomaticBrain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */