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
        this.body.textureHeight = 16.0F;
        this.body.textureWidth = 16.0F;
        this.body.addBox(-8.0F, 0.0F, -8.0F, 16, 16, 16, true);
    }



    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.body.render(scale);
    }
}