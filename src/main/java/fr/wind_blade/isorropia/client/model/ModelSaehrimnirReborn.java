 package fr.wind_blade.isorropia.client.model;

 import net.minecraft.client.model.ModelBase;
 import net.minecraft.client.model.ModelRenderer;
 import net.minecraft.entity.Entity;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;

 @SideOnly(Side.CLIENT)
 public class ModelSaehrimnirReborn
   extends ModelBase {
   private ModelRenderer body;

   public ModelSaehrimnirReborn() {
     this.body = new ModelRenderer(this, "body");
/* 16 */     this.body.textureHeight = 16.0F;
/* 17 */     this.body.textureWidth = 16.0F;
/* 18 */     this.body.addBox(-8.0F, 0.0F, -8.0F, 16, 16, 16, true);
   }



   public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
/* 24 */     this.body.render(scale);
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\client\model\ModelSaehrimnirReborn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */