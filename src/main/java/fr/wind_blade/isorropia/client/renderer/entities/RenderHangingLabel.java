package fr.wind_blade.isorropia.client.renderer.entities;

import fr.wind_blade.isorropia.common.entities.EntityHangingLabel;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;

@SideOnly(Side.CLIENT)
public class RenderHangingLabel<T extends EntityHangingLabel>
        extends Render<T> {
    public static ResourceLocation LABEL_TEXTURE = new ResourceLocation("thaumcraft", "textures/models/label.png");

    public RenderHangingLabel(RenderManager renderManager) {
        super(renderManager);
    }


    protected ResourceLocation getEntityTexture(T entity) {
        return LABEL_TEXTURE;
    }


    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks, 0.0F, 1.0F, 0.0F);

        GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);


        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0.0D, 0.0D, 0.03D);
        GlStateManager.enableRescaleNormal();

        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(getTeamColor(entity));
        }

        renderEntity(entity);

        GlStateManager.translate(-7.0F, -8.0F, -0.01F);
        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.disableRescaleNormal();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    private void renderEntity(T entity) {
        GL11.glPushMatrix();
        UtilsFX.renderQuadCentered(LABEL_TEXTURE, 0.5F, 1.0F, 1.0F, 1.0F, -99, 771, 1.0F);
        GL11.glRotated(180.0D, 0.0D, 1.0D, 0.0D);
        UtilsFX.renderQuadCentered(LABEL_TEXTURE, 0.5F, 1.0F, 1.0F, 1.0F, -99, 771, 1.0F);
        GL11.glPopMatrix();
        if (entity.getAspect() != null) {
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.0F, -0.001F);
            GL11.glScaled(0.021D, 0.021D, 0.021D);
            UtilsFX.drawTag(-8, -8, entity.getAspect());
            GL11.glPopMatrix();
        }
    }
}