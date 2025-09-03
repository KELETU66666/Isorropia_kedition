package fr.wind_blade.isorropia.client.renderer.tiles;

import fr.wind_blade.isorropia.common.tiles.TileSoulBeacon;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSoulBeacon extends TileEntitySpecialRenderer<TileSoulBeacon> {
    public static final ResourceLocation TEXTURE_BEACON_BEAM = new ResourceLocation("textures/entity/beacon_beam.png");

    public RenderSoulBeacon() {
    }

    @Override
    public void render(TileSoulBeacon te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.disableFog();
        if (te.getWorld() == null || !te.getWorld().canBlockSeeSky(te.getPos())) {
            return;
        }
        GlStateManager.alphaFunc(516, 0.1F);
        this.bindTexture(TEXTURE_BEACON_BEAM);
        TileEntityBeaconRenderer.renderBeamSegment(x, y, z, partialTicks, te.shouldBeamRender(), (double) te.getWorld().getTotalWorldTime(), 0, (int) (256 - y), EnumDyeColor.WHITE.getColorComponentValues(), 0.2, 0.25);
        //TileEntityBeaconRenderer.renderBeamSegment(x, y, z, partialTicks, f, (double) te.getWorld().getTotalWorldTime(), 0, -i, afloat, 0.15, 0.175);

        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
        GlStateManager.enableFog();
    }

    @Override
    public boolean isGlobalRenderer(TileSoulBeacon te) {
        return true;
    }

}
