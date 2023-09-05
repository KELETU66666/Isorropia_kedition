package fr.wind_blade.isorropia.client.renderer.entities;


import fr.wind_blade.isorropia.common.entities.EntityGoldenChicken;
import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class RenderGoldChicken extends RenderLiving<EntityGoldenChicken> {
    public static ResourceLocation TEXTURE = new ResourceLocation("isorropia", "textures/entity/golden_chicken.png");
    public RenderGoldChicken(RenderManager rendermanagerIn) {
        super(rendermanagerIn, new ModelChicken(), 0.5F);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityGoldenChicken entity) {
        return TEXTURE;
    }

    protected float handleRotationFloat(EntityGoldenChicken livingBase, float partialTicks)
    {
        float f = livingBase.oFlap + (livingBase.wingRotation - livingBase.oFlap) * partialTicks;
        float f1 = livingBase.oFlapSpeed + (livingBase.destPos - livingBase.oFlapSpeed) * partialTicks;
        return (MathHelper.sin(f) + 1.0F) * f1;
    }
}