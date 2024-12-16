package fr.wind_blade.isorropia.client.renderer.entities.layers;

import fr.wind_blade.isorropia.client.renderer.entities.RenderTaintPig;
import fr.wind_blade.isorropia.common.entities.EntityTaintPig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPig;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class LayerTaintPigBorder
        implements LayerRenderer<EntityTaintPig> {
    private static final ResourceLocation pigEyesTextures = new ResourceLocation("isorropia", "textures/entity/taintfeeder_eyes.png");

    private static final ModelPig border = new ModelPig(0.5F);
    private final RenderTaintPig taintRender;

    public LayerTaintPigBorder(RenderTaintPig renderTaintPig) {
        this.taintRender = renderTaintPig;
    }



    public void doRenderLayer(EntityTaintPig entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        float f = (float)Math.sin(Math.toRadians((entitylivingbaseIn.ticksExisted % 45)) * 8.0D);
        (Minecraft.getMinecraft()).renderEngine.bindTexture(pigEyesTextures);
        GL11.glEnable(3042);
        GL11.glDisable(3008);
        GL11.glBlendFunc(770, 771);

        if (entitylivingbaseIn.isInvisible()) {
            GL11.glDepthMask(false);
        } else {
            GL11.glDepthMask(true);
        }
        char c0 = '1';
        int j = 61680;
        int k = 0;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.32F + 0.32F * f);
        border.setModelAttributes(this.taintRender.getMainModel());
        border.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
    }


    public boolean shouldCombineTextures() {
        return true;
    }
}