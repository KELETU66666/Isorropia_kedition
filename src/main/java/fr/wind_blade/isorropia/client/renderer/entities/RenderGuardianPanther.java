package fr.wind_blade.isorropia.client.renderer.entities;

import fr.wind_blade.isorropia.Isorropia;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderOcelot;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderGuardianPanther extends RenderOcelot {

    public RenderGuardianPanther(RenderManager rendermanagerIn) {
        super(rendermanagerIn);
    }

    protected ResourceLocation getEntityTexture(EntityOcelot entity) {
        switch (entity.getTameSkin()) {
            case 0:
            default:
                return new ResourceLocation(Isorropia.MODID, "textures/entity/guardian1.png");
            case 1:
                return new ResourceLocation(Isorropia.MODID, "textures/entity/guardian1.png");
            case 2:
                return new ResourceLocation(Isorropia.MODID, "textures/entity/guardian2.png");
            case 3:
                return new ResourceLocation(Isorropia.MODID, "textures/entity/guardian3.png");
        }
    }

    protected void preRenderCallback(final EntityOcelot p_77041_1_, final float p_77041_2_) {
        super.preRenderCallback(p_77041_1_, p_77041_2_);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
    }
}