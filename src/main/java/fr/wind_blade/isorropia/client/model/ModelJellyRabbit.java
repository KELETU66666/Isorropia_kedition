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
        setTextureOffset("head.main", 0, 0);
        setTextureOffset("head.nose", 0, 24);
        setTextureOffset("head.ear1", 0, 10);
        setTextureOffset("head.ear2", 6, 10);
        this.rabbitBody = new ModelRenderer(this, 0, 0);
        this.rabbitBody.addBox(-3.0F, -2.0F, -10.0F, 6, 5, 10);
        this.rabbitBody.setRotationPoint(0.0F, 19.0F, 8.0F);
        this.rabbitBody.mirror = true;
        setRotationOffset(this.rabbitBody, -0.34906584F, 0.0F, 0.0F);
        this.rabbitHead = new ModelRenderer(this, 32, 0);
        this.rabbitHead.addBox(-2.5F, -4.0F, -5.0F, 5, 4, 5);
        this.rabbitHead.setRotationPoint(0.0F, 16.0F, -1.0F);
        this.rabbitHead.mirror = true;
        setRotationOffset(this.rabbitHead, 0.0F, 0.0F, 0.0F);
        this.rabbitRightEar = new ModelRenderer(this, 52, 0);
        this.rabbitRightEar.addBox(-2.5F, -9.0F, -1.0F, 2, 5, 1);
        this.rabbitRightEar.setRotationPoint(0.0F, 16.0F, -1.0F);
        this.rabbitRightEar.mirror = true;
        setRotationOffset(this.rabbitRightEar, 0.0F, -0.2617994F, 0.0F);
        this.rabbitLeftEar = new ModelRenderer(this, 58, 0);
        this.rabbitLeftEar.addBox(0.5F, -9.0F, -1.0F, 2, 5, 1);
        this.rabbitLeftEar.setRotationPoint(0.0F, 16.0F, -1.0F);
        this.rabbitLeftEar.mirror = true;
        setRotationOffset(this.rabbitLeftEar, 0.0F, 0.2617994F, 0.0F);
        this.slimeRightEye = new ModelRenderer(this, 58, 26);
        this.slimeRightEye.addBox(-1.9F, -3.1F, -5.5F, 1, 1, 1);
        this.slimeRightEye.setRotationPoint(0.0F, 16.0F, -1.0F);
        setRotationOffset(this.slimeRightEye, 0.0F, 0.0F, 0.0F);
        this.slimeLeftEye = new ModelRenderer(this, 58, 26);
        this.slimeLeftEye.addBox(0.9F, -3.1F, -5.5F, 1, 1, 1);
        this.slimeLeftEye.setRotationPoint(0.0F, 16.0F, -1.0F);
        setRotationOffset(this.slimeLeftEye, 0.0F, 0.0F, 0.0F);
    }

    private void setRotationOffset(ModelRenderer renderer, float x, float y, float z) {
        renderer.rotateAngleX = x;
        renderer.rotateAngleY = y;
        renderer.rotateAngleZ = z;
    }



    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        this.rabbitBody.render(scale);
        this.rabbitHead.render(scale);
        this.slimeRightEye.render(scale);
        this.slimeLeftEye.render(scale);
    }



    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        this.rabbitHead.rotateAngleX = headPitch * 0.017453292F;
        this.rabbitRightEar.rotateAngleX = headPitch * 0.017453292F;
        this.rabbitLeftEar.rotateAngleX = headPitch * 0.017453292F;
        this.rabbitHead.rotateAngleY = netHeadYaw * 0.017453292F;

        this.slimeRightEye.rotateAngleX = headPitch * 0.017453292F;
        this.slimeRightEye.rotateAngleY = netHeadYaw * 0.017453292F;
        this.slimeLeftEye.rotateAngleX = headPitch * 0.017453292F;
        this.slimeLeftEye.rotateAngleY = netHeadYaw * 0.017453292F;
    }
}