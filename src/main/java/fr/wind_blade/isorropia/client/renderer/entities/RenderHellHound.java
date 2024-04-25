package fr.wind_blade.isorropia.client.renderer.entities;

import fr.wind_blade.isorropia.common.entities.EntityHellHound;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderHellHound extends RenderLiving<EntityHellHound> {
    public static ResourceLocation TEXTURE = new ResourceLocation("isorropia", "textures/entity/hellhound.png");

    public RenderHellHound(RenderManager rendermanagerIn) {
        super(rendermanagerIn, new ModelWolf(), 0.5F);
    }

    protected float handleRotationFloat(EntityHellHound livingBase, float partialTicks) {
        return livingBase.getTailRotation();
    }

    protected ResourceLocation getEntityTexture(EntityHellHound entity) {
        return TEXTURE;
    }
}