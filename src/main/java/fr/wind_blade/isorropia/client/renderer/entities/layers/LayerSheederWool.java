package fr.wind_blade.isorropia.client.renderer.entities.layers;

import fr.wind_blade.isorropia.client.renderer.entities.RenderSheeder;
import fr.wind_blade.isorropia.common.entities.EntitySheeder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerSheederWool implements LayerRenderer<EntitySheeder> {
    public static final ResourceLocation SPIDER_EYES = new ResourceLocation("isorropia", "textures/entity/sheederoverlay.png");

    private final RenderSheeder sheederRenderer;

    public LayerSheederWool(RenderSheeder sheederRendererIn) {
        this.sheederRenderer = sheederRendererIn;
    }


    public void doRenderLayer(EntitySheeder sheeder, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!sheeder.getSheared()) {
            GlStateManager.depthMask(!sheeder.isInvisible());

            GlStateManager.translate(0.0D, -0.01D, -0.01D);
            GlStateManager.scale(1.01D, 1.01D, 1.02D);
            this.sheederRenderer.bindTexture(SPIDER_EYES);
            this.sheederRenderer.getMainModel().render(sheeder, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }


    public boolean shouldCombineTextures() {
        return true;
    }
}