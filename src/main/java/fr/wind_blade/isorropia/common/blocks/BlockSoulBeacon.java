//
// Decompiled by Procyon v0.5.30
//

package fr.wind_blade.isorropia.common.blocks;

import fr.wind_blade.isorropia.common.tiles.TileSoulBeacon;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSoulBeacon extends BlockContainer {

    public BlockSoulBeacon() {
        super(Material.IRON);
        this.setHardness(7.0f);
        this.setResistance(7.0f);
        this.setLightLevel(1.0f);
    }

    public TileEntity createNewTileEntity(final World world, final int md) {
        return this.createTileEntity(world, md);
    }

    public TileEntity createTileEntity(final World world, final int metadata) {
        return new TileSoulBeacon();
    }

    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        final TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileSoulBeacon && hand == EnumHand.MAIN_HAND){
            return ((TileSoulBeacon) te).activate(player);
        }
        return false;
    }

    //@SideOnly(Side.CLIENT)
    //public int getRenderBlockPass(IBlockState state) {
    //    return 1;
    //}

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
}