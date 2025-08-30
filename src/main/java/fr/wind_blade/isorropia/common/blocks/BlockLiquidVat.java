package fr.wind_blade.isorropia.common.blocks;

import fr.wind_blade.isorropia.common.blocks.material.MaterialIR;
import fr.wind_blade.isorropia.common.tiles.TileVat;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLiquidVat extends BlockLiquid implements IBlockRegistry {
    public BlockLiquidVat() {
        super(MaterialIR.LIQUID_VAT);
        this.setHardness(3.0f);
        this.setResistance(15.0f);
        this.lightValue = 8;
    }

    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
        return false;
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileVat tile = this.getMaster(worldIn, state, pos);
        if (tile != null) {
            tile.destroyMultiBlock();
        }
        super.breakBlock(worldIn, pos, state);
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileVat vat = BlockCurativeVat.getMaster(worldIn, pos);
        if (vat != null && vat.getEntityContained() == playerIn) {
            vat.onBlockRigthClick(playerIn, facing, false);
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    public Boolean isEntityInsideMaterial(IBlockAccess world, BlockPos blockpos, IBlockState iblockstate, Entity entity, double yToTest, Material materialIn, boolean testingHead) {
        if (materialIn == MaterialIR.LIQUID_VAT || materialIn == Material.WATER) {
            return true;
        }
        return false;
    }

    public TileVat getMaster(World worldIn, IBlockState state, BlockPos pos) {
        for (int i = 1; i <= 2; ++i) {
            TileEntity te = worldIn.getTileEntity(pos.up(i));
            if (!(te instanceof TileVat)) continue;
            return (TileVat) te;
        }
        return null;
    }

    @SideOnly(value = Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    public boolean isFullyOpaque(IBlockState state) {
        return false;
    }

    public boolean isNormalCube(IBlockState state) {
        return false;
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isBlockNormalCube(IBlockState state) {
        return false;
    }

    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    public boolean isTranslucent(IBlockState state) {
        return true;
    }

    public boolean isVisuallyOpaque() {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isInCreativeTabs() {
        return false;
    }
}
