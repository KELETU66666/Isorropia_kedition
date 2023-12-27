package fr.wind_blade.isorropia.common.blocks;

import fr.wind_blade.isorropia.common.tiles.TileModifiedMatrix;
import fr.wind_blade.isorropia.common.tiles.TileVat;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockModifiedMatrix
        extends Block
        implements ITileEntityProvider {
    public BlockModifiedMatrix() {
        super(Material.ROCK);
        this.setSoundType(SoundType.STONE);
        this.setHardness(2.0f);
        this.setResistance(20.0f);
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileModifiedMatrix();
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity te = worldIn.getTileEntity(pos.down());
        if (te instanceof TileVat) {
            TileVat vat = (TileVat)te;
            if (vat.isInfusing()) {
                vat.destroyMultiBlock();
            }
            vat.setActive(false);
            vat.setStartUp(0.0f);
            vat.syncTile(false);
        }
        super.breakBlock(worldIn, pos, state);
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return worldIn.getTileEntity(pos.down()) instanceof TileVat;
    }
}
