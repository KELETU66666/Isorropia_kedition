package fr.wind_blade.isorropia.client.renderer.entities;

import fr.wind_blade.isorropia.Isorropia;
import fr.wind_blade.isorropia.common.entities.EntityGravekeeper;
import net.minecraft.client.model.ModelOcelot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderGraveKeeper extends RenderLiving<EntityGravekeeper> {
    private static final ResourceLocation OCELOT_TEXTURES = new ResourceLocation(Isorropia.MODID, "textures/entity/gravekeeper.png");

    public RenderGraveKeeper(RenderManager p_i47199_1_) {
        super(p_i47199_1_, new ModelOcelot(), 0.4F);
    }

    protected ResourceLocation getEntityTexture(EntityGravekeeper entity) {
        return OCELOT_TEXTURES;
    }

    protected void preRenderCallback(EntityGravekeeper entitylivingbaseIn, float partialTickTime) {
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);
        if (entitylivingbaseIn.isTamed()) {
            GlStateManager.scale(0.8F, 0.8F, 0.8F);
        }

    }
}
