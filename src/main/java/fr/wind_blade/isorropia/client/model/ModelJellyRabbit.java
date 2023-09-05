 package fr.wind_blade.isorropia.client.model;
 
 import net.minecraft.client.model.ModelBase;
 import net.minecraft.client.model.ModelRenderer;
 import net.minecraft.entity.Entity;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 
 @SideOnly(Side.CLIENT)
 public class ModelJellyRabbit
   extends ModelBase {
   private final ModelRenderer rabbitBody;
   private final ModelRenderer rabbitHead;
   private final ModelRenderer rabbitRightEar;
   private final ModelRenderer rabbitLeftEar;
   ModelRenderer slimeRightEye;
   ModelRenderer slimeLeftEye;
   
   public ModelJellyRabbit() {
/* 20 */     setTextureOffset("head.main", 0, 0);
/* 21 */     setTextureOffset("head.nose", 0, 24);
/* 22 */     setTextureOffset("head.ear1", 0, 10);
/* 23 */     setTextureOffset("head.ear2", 6, 10);
/* 24 */     this.rabbitBody = new ModelRenderer(this, 0, 0);
     this.rabbitBody.addBox(-3.0F, -2.0F, -10.0F, 6, 5, 10);
      this.rabbitBody.setRotationPoint(0.0F, 19.0F, 8.0F);
      this.rabbitBody.mirror = true;
      setRotationOffset(this.rabbitBody, -0.34906584F, 0.0F, 0.0F);
      this.rabbitHead = new ModelRenderer(this, 32, 0);
/* 30 */     this.rabbitHead.addBox(-2.5F, -4.0F, -5.0F, 5, 4, 5);
/* 31 */     this.rabbitHead.setRotationPoint(0.0F, 16.0F, -1.0F);
/* 32 */     this.rabbitHead.mirror = true;
/* 33 */     setRotationOffset(this.rabbitHead, 0.0F, 0.0F, 0.0F);
/* 34 */     this.rabbitRightEar = new ModelRenderer(this, 52, 0);
/* 35 */     this.rabbitRightEar.addBox(-2.5F, -9.0F, -1.0F, 2, 5, 1);
/* 36 */     this.rabbitRightEar.setRotationPoint(0.0F, 16.0F, -1.0F);
/* 37 */     this.rabbitRightEar.mirror = true;
/* 38 */     setRotationOffset(this.rabbitRightEar, 0.0F, -0.2617994F, 0.0F);
/* 39 */     this.rabbitLeftEar = new ModelRenderer(this, 58, 0);
/* 40 */     this.rabbitLeftEar.addBox(0.5F, -9.0F, -1.0F, 2, 5, 1);
      this.rabbitLeftEar.setRotationPoint(0.0F, 16.0F, -1.0F);
/* 42 */     this.rabbitLeftEar.mirror = true;
/* 43 */     setRotationOffset(this.rabbitLeftEar, 0.0F, 0.2617994F, 0.0F);
/* 44 */     this.slimeRightEye = new ModelRenderer(this, 58, 26);
/* 45 */     this.slimeRightEye.addBox(-1.9F, -3.1F, -5.5F, 1, 1, 1);
/* 46 */     this.slimeRightEye.setRotationPoint(0.0F, 16.0F, -1.0F);
/* 47 */     setRotationOffset(this.slimeRightEye, 0.0F, 0.0F, 0.0F);
/* 48 */     this.slimeLeftEye = new ModelRenderer(this, 58, 26);
/* 49 */     this.slimeLeftEye.addBox(0.9F, -3.1F, -5.5F, 1, 1, 1);
/* 50 */     this.slimeLeftEye.setRotationPoint(0.0F, 16.0F, -1.0F);
/* 51 */     setRotationOffset(this.slimeLeftEye, 0.0F, 0.0F, 0.0F);
   }
   
   private void setRotationOffset(ModelRenderer renderer, float x, float y, float z) {
/* 55 */     renderer.rotateAngleX = x;
/* 56 */     renderer.rotateAngleY = y;
/* 57 */     renderer.rotateAngleZ = z;
   }
 
 
   
   public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
/* 63 */     setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
/* 64 */     this.rabbitBody.render(scale);
/* 65 */     this.rabbitHead.render(scale);
/* 66 */     this.slimeRightEye.render(scale);
/* 67 */     this.slimeLeftEye.render(scale);
   }
 
 
   
   public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
/* 73 */     this.rabbitHead.rotateAngleX = headPitch * 0.017453292F;
/* 74 */     this.rabbitRightEar.rotateAngleX = headPitch * 0.017453292F;
/* 75 */     this.rabbitLeftEar.rotateAngleX = headPitch * 0.017453292F;
/* 76 */     this.rabbitHead.rotateAngleY = netHeadYaw * 0.017453292F;
     
/* 78 */     this.slimeRightEye.rotateAngleX = headPitch * 0.017453292F;
/* 79 */     this.slimeRightEye.rotateAngleY = netHeadYaw * 0.017453292F;
/* 80 */     this.slimeLeftEye.rotateAngleX = headPitch * 0.017453292F;
/* 81 */     this.slimeLeftEye.rotateAngleY = netHeadYaw * 0.017453292F;
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\client\model\ModelJellyRabbit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */