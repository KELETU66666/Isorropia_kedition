package fr.wind_blade.isorropia.client.renderer.tiles;

import fr.wind_blade.isorropia.Isorropia;
import fr.wind_blade.isorropia.common.tiles.TileVatBottom;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import thaumcraft.client.lib.obj.AdvancedModelLoader;
import thaumcraft.client.lib.obj.IModelCustom;

public class RenderTileVat extends TileEntitySpecialRenderer<TileVatBottom> {
    private final IModelCustom model = AdvancedModelLoader.loadModel(VAT);
    private static final ResourceLocation VAT = new ResourceLocation(Isorropia.MODID, "models/block/vat_redo.obj");

    private final String tx1;

    public RenderTileVat() {
        this.tx1 = "textures/models/vat.png";
    }

    @Override
    public boolean isGlobalRenderer(TileVatBottom te) {
        return true;
    }

    @Override
    public void render(TileVatBottom te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();

        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        GlStateManager.translate((float) x + 0.5f, (float) y, (float) z + 0.5f);

        GlStateManager.depthMask(false);

        this.bindTexture(new ResourceLocation(Isorropia.MODID, "models/block/water_still.png"));
        model.renderPart("Water");
        GlStateManager.depthMask(true);

        this.bindTexture(new ResourceLocation(Isorropia.MODID, "models/block/curative_vat.png"));
        model.renderAllExcept("Water");

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}