package fr.wind_blade.isorropia.client.renderer.entities;

import fr.wind_blade.isorropia.client.model.ModelJelly;
import fr.wind_blade.isorropia.client.model.ModelJellyRabbit;
import fr.wind_blade.isorropia.common.entities.EntityJellyRabbit;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class RenderJellyRabbit
        extends RenderLiving<EntityJellyRabbit>
{
    public static ResourceLocation TEXTURE = new ResourceLocation("isorropia", "textures/entity/white.png");
    public static final float MC_HEIGHT = 1.5F;

    public RenderJellyRabbit(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelJellyRabbit(), 0.3F);
        addLayer(new LayerRabbitJelly(this));
    }


    protected void renderModel(EntityJellyRabbit rabbit, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        Color color;
        GlStateManager.pushMatrix();
        Aspect aspect = rabbit.getAspect();

        if (aspect != null) {
            color = new Color(aspect.getColor());
        } else {
            color = new Color(Aspect.SENSES.getColor());
        }
        GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 0.7F);

        float size = 0.55F;
        float offset = rabbit.height * (size - 1.0F) / 2.0F + 0.08F;
        GlStateManager.translate(0.0F, 1.5F + offset, 0.0F);
        GlStateManager.scale(size, size, size);
        GlStateManager.translate(0.0F, -1.5F, 0.0F);
        super.renderModel(rabbit, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }


    protected ResourceLocation getEntityTexture(EntityJellyRabbit entity) {
        return TEXTURE;
    }

    public class LayerRabbitJelly
            implements LayerRenderer<EntityJellyRabbit> {
        private final RenderJellyRabbit jellyRabbitRenderer;
        private final ModelBase jellyModel = new ModelJelly();
        public final ResourceLocation TEXTURE_LAYER = new ResourceLocation("isorropia", "textures/entity/jelly_rabbit.png");


        public LayerRabbitJelly(RenderJellyRabbit jellyRabbitRendererIn) {
            this.jellyRabbitRenderer = jellyRabbitRendererIn;
        }




        public void doRenderLayer(EntityJellyRabbit rabbit, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            if (!rabbit.isInvisible()) {
                Color color; GlStateManager.pushMatrix();

                Aspect aspect = rabbit.getAspect();


                if (aspect != null) {
                    color = new Color(aspect.getColor());
                } else {
                    color = new Color(Aspect.SENSES.getColor());
                }

                GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 0.7F);

                float jellySize = rabbit.getJellySize() / 25.0F + 1.0F;
                float offset = rabbit.height * (jellySize - 1.0F) / 2.0F;
                GlStateManager.translate(0.0F, 1.5F + offset, 0.0F);
                GlStateManager.scale(jellySize, jellySize, jellySize);
                GlStateManager.translate(0.0F, -1.5F, 0.0F);
                GlStateManager.scale(0.6F, 0.6F, 0.6F);

                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

                RenderJellyRabbit.this.bindTexture(this.TEXTURE_LAYER);
                this.jellyModel.setModelAttributes(this.jellyRabbitRenderer.getMainModel());
                this.jellyModel.render(rabbit, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                GlStateManager.disableBlend();
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.popMatrix();
            }
        }


        public boolean shouldCombineTextures() {
            return true;
        }
    }
}