package fr.wind_blade.isorropia.client.renderer.entities;

import fr.wind_blade.isorropia.common.entities.EntitySaehrimnir;
import net.minecraft.client.model.ModelPig;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSaehrimnir extends RenderLiving<EntitySaehrimnir> {
    private static final ResourceLocation PIG_TEXTURES = new ResourceLocation("textures/entity/pig/pig.png");

    public RenderSaehrimnir(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelPig(), 0.5F);
    }


    protected ResourceLocation getEntityTexture(EntitySaehrimnir entity) {
        return PIG_TEXTURES;
    }
}