package fr.wind_blade.isorropia.client.renderer.entities;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPig;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderOrePig extends RenderPig {
    public static final ResourceLocation TEXTURE = new ResourceLocation("isorropia", "textures/entity/oreboar.png");

    public RenderOrePig(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }


    protected ResourceLocation getEntityTexture(EntityPig entity) {
        return TEXTURE;
    }
}