 package fr.wind_blade.isorropia.client.model;
 
 import net.minecraft.client.model.ModelBase;
 import net.minecraft.client.model.ModelRenderer;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.entity.passive.EntityRabbit;
 import net.minecraft.util.math.MathHelper;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 
 @SideOnly(Side.CLIENT)
 public class ModelJelly
   extends ModelBase
 {
   private final ModelRenderer rabbitLeftFoot;
   private final ModelRenderer rabbitRightFoot;
   private final ModelRenderer rabbitLeftThigh;
   private final ModelRenderer rabbitRightThigh;
   private final ModelRenderer rabbitBody;
   private final ModelRenderer rabbitLeftArm;
   private final ModelRenderer rabbitRightArm;
   private final ModelRenderer rabbitHead;
   private final ModelRenderer rabbitRightEar;
   private final ModelRenderer rabbitLeftEar;
   private final ModelRenderer rabbitTail;
   private final ModelRenderer rabbitNose;
   private float jumpRotation;
   
   public ModelJelly() {
/*  31 */     setTextureOffset("head.main", 0, 0);
/*  32 */     setTextureOffset("head.nose", 0, 24);
/*  33 */     setTextureOffset("head.ear1", 0, 10);
/*  34 */     setTextureOffset("head.ear2", 6, 10);
/*  35 */     this.rabbitLeftFoot = new ModelRenderer(this, 26, 24);
/*  36 */     this.rabbitLeftFoot.addBox(-1.0F, 5.5F, -3.7F, 2, 1, 7);
/*  37 */     this.rabbitLeftFoot.setRotationPoint(3.0F, 33.5F, 3.7F);
/*  38 */     this.rabbitLeftFoot.mirror = true;
/*  39 */     setRotationOffset(this.rabbitLeftFoot, 0.0F, 0.0F, 0.0F);
/*  40 */     this.rabbitRightFoot = new ModelRenderer(this, 8, 24);
/*  41 */     this.rabbitRightFoot.addBox(-1.0F, 5.5F, -3.7F, 2, 1, 7);
/*  42 */     this.rabbitRightFoot.setRotationPoint(-3.0F, 33.5F, 3.7F);
/*  43 */     this.rabbitRightFoot.mirror = true;
/*  44 */     setRotationOffset(this.rabbitRightFoot, 0.0F, 0.0F, 0.0F);
/*  45 */     this.rabbitLeftThigh = new ModelRenderer(this, 30, 15);
/*  46 */     this.rabbitLeftThigh.addBox(-1.0F, 0.0F, 0.0F, 2, 4, 5);
/*  47 */     this.rabbitLeftThigh.setRotationPoint(3.0F, 33.5F, 3.7F);
/*  48 */     this.rabbitLeftThigh.mirror = true;
/*  49 */     setRotationOffset(this.rabbitLeftThigh, -0.34906584F, 0.0F, 0.0F);
/*  50 */     this.rabbitRightThigh = new ModelRenderer(this, 16, 15);
/*  51 */     this.rabbitRightThigh.addBox(-1.0F, 0.0F, 0.0F, 2, 4, 5);
/*  52 */     this.rabbitRightThigh.setRotationPoint(-3.0F, 33.5F, 3.7F);
/*  53 */     this.rabbitRightThigh.mirror = true;
/*  54 */     setRotationOffset(this.rabbitRightThigh, -0.34906584F, 0.0F, 0.0F);
/*  55 */     this.rabbitBody = new ModelRenderer(this, 0, 0);
/*  56 */     this.rabbitBody.addBox(-3.0F, -2.0F, -10.0F, 6, 5, 10);
/*  57 */     this.rabbitBody.setRotationPoint(0.0F, 35.0F, 8.0F);
/*  58 */     this.rabbitBody.mirror = true;
/*  59 */     setRotationOffset(this.rabbitBody, -0.34906584F, 0.0F, 0.0F);
/*  60 */     this.rabbitLeftArm = new ModelRenderer(this, 8, 15);
/*  61 */     this.rabbitLeftArm.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2);
/*  62 */     this.rabbitLeftArm.setRotationPoint(3.0F, 33.0F, -1.0F);
/*  63 */     this.rabbitLeftArm.mirror = true;
/*  64 */     setRotationOffset(this.rabbitLeftArm, -0.17453292F, 0.0F, 0.0F);
/*  65 */     this.rabbitRightArm = new ModelRenderer(this, 0, 15);
/*  66 */     this.rabbitRightArm.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2);
/*  67 */     this.rabbitRightArm.setRotationPoint(-3.0F, 33.0F, -1.0F);
/*  68 */     this.rabbitRightArm.mirror = true;
/*  69 */     setRotationOffset(this.rabbitRightArm, -0.17453292F, 0.0F, 0.0F);
/*  70 */     this.rabbitHead = new ModelRenderer(this, 32, 0);
/*  71 */     this.rabbitHead.addBox(-2.5F, -4.0F, -5.0F, 5, 4, 5);
/*  72 */     this.rabbitHead.setRotationPoint(0.0F, 32.0F, -1.0F);
/*  73 */     this.rabbitHead.mirror = true;
/*  74 */     setRotationOffset(this.rabbitHead, 0.0F, 0.0F, 0.0F);
/*  75 */     this.rabbitRightEar = new ModelRenderer(this, 52, 0);
/*  76 */     this.rabbitRightEar.addBox(-2.5F, -9.0F, -1.0F, 2, 5, 1);
/*  77 */     this.rabbitRightEar.setRotationPoint(0.0F, 32.0F, -1.0F);
/*  78 */     this.rabbitRightEar.mirror = true;
/*  79 */     setRotationOffset(this.rabbitRightEar, 0.0F, -0.2617994F, 0.0F);
/*  80 */     this.rabbitLeftEar = new ModelRenderer(this, 58, 0);
/*  81 */     this.rabbitLeftEar.addBox(0.5F, -9.0F, -1.0F, 2, 5, 1);
/*  82 */     this.rabbitLeftEar.setRotationPoint(0.0F, 32.0F, -1.0F);
/*  83 */     this.rabbitLeftEar.mirror = true;
/*  84 */     setRotationOffset(this.rabbitLeftEar, 0.0F, 0.2617994F, 0.0F);
/*  85 */     this.rabbitTail = new ModelRenderer(this, 52, 6);
/*  86 */     this.rabbitTail.addBox(-1.5F, -1.5F, 0.0F, 3, 3, 2);
/*  87 */     this.rabbitTail.setRotationPoint(0.0F, 36.0F, 7.0F);
/*  88 */     this.rabbitTail.mirror = true;
/*  89 */     setRotationOffset(this.rabbitTail, -0.3490659F, 0.0F, 0.0F);
/*  90 */     this.rabbitNose = new ModelRenderer(this, 32, 9);
/*  91 */     this.rabbitNose.addBox(-0.5F, -2.5F, -5.5F, 1, 1, 1);
/*  92 */     this.rabbitNose.setRotationPoint(0.0F, 32.0F, -1.0F);
/*  93 */     this.rabbitNose.mirror = true;
/*  94 */     setRotationOffset(this.rabbitNose, 0.0F, 0.0F, 0.0F);
   }
 
   
   private void setRotationOffset(ModelRenderer renderer, float x, float y, float z) {
/*  99 */     renderer.rotateAngleX = x;
/* 100 */     renderer.rotateAngleY = y;
/* 101 */     renderer.rotateAngleZ = z;
   }
 
 
   
   public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
/* 107 */     setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
/* 108 */     this.rabbitLeftFoot.render(scale);
/* 109 */     this.rabbitRightFoot.render(scale);
/* 110 */     this.rabbitLeftThigh.render(scale);
/* 111 */     this.rabbitRightThigh.render(scale);
/* 112 */     this.rabbitBody.render(scale);
/* 113 */     this.rabbitLeftArm.render(scale);
/* 114 */     this.rabbitRightArm.render(scale);
/* 115 */     this.rabbitHead.render(scale);
/* 116 */     this.rabbitRightEar.render(scale);
/* 117 */     this.rabbitLeftEar.render(scale);
/* 118 */     this.rabbitTail.render(scale);
/* 119 */     this.rabbitNose.render(scale);
   }
 
 
 
 
 
 
 
 
   
   public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
/* 131 */     float f = ageInTicks - entityIn.ticksExisted;
/* 132 */     EntityRabbit entityrabbit = (EntityRabbit)entityIn;
     this.rabbitNose.rotateAngleX = headPitch * 0.017453292F;
/* 134 */     this.rabbitHead.rotateAngleX = headPitch * 0.017453292F;
/* 135 */     this.rabbitRightEar.rotateAngleX = headPitch * 0.017453292F;
/* 136 */     this.rabbitLeftEar.rotateAngleX = headPitch * 0.017453292F;
/* 137 */     this.rabbitNose.rotateAngleY = netHeadYaw * 0.017453292F;
/* 138 */     this.rabbitHead.rotateAngleY = netHeadYaw * 0.017453292F;
/* 139 */     this.rabbitNose.rotateAngleY -= 0.2617994F;
/* 140 */     this.rabbitNose.rotateAngleY += 0.2617994F;
/* 141 */     this.jumpRotation = MathHelper.sin(entityrabbit.getJumpCompletion(f) * 3.1415927F);
/* 142 */     this.rabbitLeftThigh.rotateAngleX = (this.jumpRotation * 50.0F - 21.0F) * 0.017453292F;
/* 143 */     this.rabbitRightThigh.rotateAngleX = (this.jumpRotation * 50.0F - 21.0F) * 0.017453292F;
/* 144 */     this.rabbitLeftFoot.rotateAngleX = this.jumpRotation * 50.0F * 0.017453292F;
/* 145 */     this.rabbitRightFoot.rotateAngleX = this.jumpRotation * 50.0F * 0.017453292F;
/* 146 */     this.rabbitLeftArm.rotateAngleX = (this.jumpRotation * -40.0F - 11.0F) * 0.017453292F;
/* 147 */     this.rabbitRightArm.rotateAngleX = (this.jumpRotation * -40.0F - 11.0F) * 0.017453292F;
   }
 
 
 
 
 
 
 
   
   public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
/* 158 */     super.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTickTime);
/* 159 */     this
/* 160 */       .jumpRotation = MathHelper.sin(((EntityRabbit)entitylivingbaseIn).getJumpCompletion(partialTickTime) * 3.1415927F);
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\client\model\ModelJelly.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */