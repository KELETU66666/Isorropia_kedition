package fr.wind_blade.isorropia.client.renderer.entities;

import fr.wind_blade.isorropia.Isorropia;
import fr.wind_blade.isorropia.common.entities.EntityDopeSquid;
import net.minecraft.client.model.ModelSquid;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class RenderDopeSquid extends RenderLiving<EntityDopeSquid> {
    private static final ResourceLocation SQUID_TEXTURES = new ResourceLocation(Isorropia.MODID, "textures/entity/dope_squid.png");

    public RenderDopeSquid(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelSquid(), 0.7F);
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityDopeSquid entity) {
        return SQUID_TEXTURES;
    }

    @Override
    protected void applyRotations(EntityDopeSquid entityLiving, float ageInTicks, float rotationYaw, float partialTicks) {
        float f3 = entityLiving.prevSquidPitch + (entityLiving.squidPitch - entityLiving.prevSquidPitch) * partialTicks;
        float f4 = entityLiving.prevSquidYaw + (entityLiving.squidYaw - entityLiving.prevSquidYaw) * partialTicks;

        GlStateManager.translate(0.0F, 0.5F, 0.0F);
        GlStateManager.rotate(180.0F - rotationYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f3, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(f4, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0.0F, -1.2F, 0.0F);
    }

    @Override
    protected float handleRotationFloat(EntityDopeSquid livingBase, float partialTicks) {
        return livingBase.lastTentacleAngle + (livingBase.tentacleAngle - livingBase.lastTentacleAngle) * partialTicks;
    }
}