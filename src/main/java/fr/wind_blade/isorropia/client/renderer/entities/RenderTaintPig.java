package fr.wind_blade.isorropia.client.renderer.entities;

import fr.wind_blade.isorropia.client.renderer.entities.layers.LayerTaintPigBorder;
import fr.wind_blade.isorropia.common.entities.EntityTaintPig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPig;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;

@SideOnly(Side.CLIENT)
public class RenderTaintPig extends RenderLiving<EntityTaintPig> {
    private static final ResourceLocation PIG_TEXTURES = new ResourceLocation("isorropia", "textures/entity/taintfeeder.png");

    public static final ResourceLocation nodetex = new ResourceLocation("isorropia", "textures/misc/nodes.png");

    public RenderTaintPig(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelPig(), 0.5F);
        addLayer((LayerRenderer) new LayerTaintPigBorder(this));
    }


    protected ResourceLocation getEntityTexture(EntityTaintPig entity) {
        return PIG_TEXTURES;
    }


    public void doRender(EntityTaintPig entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GL11.glPushMatrix();
        GL11.glAlphaFunc(516, 0.003921569F);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 1);
        GL11.glPushMatrix();
        GL11.glDepthMask(false);
        GL11.glDisable(2884);
        int i = entity.ticksExisted % 32;
        (Minecraft.getMinecraft()).renderEngine.bindTexture(nodetex);
        UtilsFX.renderFacingQuad(entity.posX, entity.posY + entity.height / 1.75D, entity.posZ, 32, 32, 192 + i, 2.0F, 11197951, 1.0F, 1, partialTicks);

        GL11.glEnable(2884);
        GL11.glDepthMask(true);
        GL11.glPopMatrix();
        GL11.glDisable(3042);
        GL11.glAlphaFunc(516, 0.1F);
        GL11.glPopMatrix();
    }
}