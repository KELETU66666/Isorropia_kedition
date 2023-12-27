package fr.wind_blade.isorropia.client.renderer.entities;

import fr.wind_blade.isorropia.common.entities.projectile.EntityEmber;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;

@SideOnly(Side.CLIENT)
public class RenderEmber
        extends Render<EntityEmber>
{
    public static final ResourceLocation particleTexture = new ResourceLocation("isorropia", "textures/misc/particles.png");


    public RenderEmber(RenderManager rm) {
        super(rm);
        this.shadowSize = 0.0F;
    }

    public void renderEntityAt(EntityEmber entity, double x, double y, double z, float fq, float pticks) {
        GL11.glPushMatrix();
        GL11.glAlphaFunc(516, 0.003921569F);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 1);
        GL11.glPushMatrix();
        GL11.glDepthMask(false);
        GL11.glDisable(2884);
        int i = entity.ticksExisted % 7;
        (Minecraft.getMinecraft()).renderEngine.bindTexture(particleTexture);
        UtilsFX.renderFacingQuad(entity.posX, entity.posY + entity.height / 1.75D, entity.posZ, 16, 16, 144 + i + 7, 0.08F, 11197951, 1.0F, 1, pticks);

        GL11.glEnable(2884);
        GL11.glDepthMask(true);
        GL11.glPopMatrix();
        GL11.glDisable(3042);
        GL11.glAlphaFunc(516, 0.1F);
        GL11.glPopMatrix();
    }


    public void doRender(EntityEmber entity, double d, double d1, double d2, float f, float f1) {
        renderEntityAt(entity, d, d1, d2, f, f1);
    }


    protected ResourceLocation getEntityTexture(EntityEmber entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}