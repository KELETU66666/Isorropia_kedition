//
// Decompiled by Procyon v0.5.30
//

package fr.wind_blade.isorropia.common.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.tiles.TileThaumcraft;

public class TileSoulBeacon extends TileThaumcraft {

    @SideOnly(Side.CLIENT)
    private float field_146014_j;

    @SideOnly(Side.CLIENT)
    private long field_146016_i;

    public boolean activate(final EntityPlayer p) {
        p.getEntityData().setBoolean("soulBeacon", true);
        p.getEntityData().setIntArray("soulBeaconCoords", new int[]{this.pos.getX(), this.pos.getY(), this.pos.getZ()});
        p.getEntityData().setInteger("soulBeaconDim", this.world.provider.getDimension());
        if (world.isRemote) {
            p.sendMessage(new TextComponentTranslation(TextFormatting.ITALIC + "" + TextFormatting.GRAY + I18n.translateToLocal("isorropia.setBeacon")));
            FXDispatcher.INSTANCE.arcLightning(p.posX, p.posY + p.getEyeHeight(), p.posZ, this.pos.getX() + 0.5, this.pos.getY() + 0.75, this.pos.getZ() + 0.5, 0.05f, 1.0f, 0.05f, 0.5f);
        }
        this.world.playSound(
                this.pos.getX() + 0.5,
                this.pos.getY() + 0.75,
                this.pos.getZ() + 0.5,
                SoundsTC.zap,
                SoundCategory.BLOCKS,
                1.0f,
                1.0f,
                true);
        return true;
    }

    @SideOnly(Side.CLIENT)
    public float shouldBeamRender() {
        final int i = (int) (this.world.getTotalWorldTime() - this.field_146016_i);
        this.field_146016_i = this.world.getTotalWorldTime();
        if (i > 1) {
            this.field_146014_j -= i / 40.0f;
            if (this.field_146014_j < 0.0f) {
                this.field_146014_j = 0.0f;
            }
        }
        this.field_146014_j += 0.025f;
        if (this.field_146014_j > 1.0f) {
            this.field_146014_j = 1.0f;
        }
        return this.field_146014_j;
    }

    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return 65536.0;
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return TileSoulBeacon.INFINITE_EXTENT_AABB;
    }
}