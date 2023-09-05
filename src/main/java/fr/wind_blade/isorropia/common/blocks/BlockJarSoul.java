// Decompiled with: CFR 0.152
// Class Version: 8
package fr.wind_blade.isorropia.common.blocks;

import fr.wind_blade.isorropia.common.blocks.BlockJarSoulItem;
import fr.wind_blade.isorropia.common.blocks.IItemBlockProvider;
import fr.wind_blade.isorropia.common.libs.helpers.IsorropiaHelper;
import fr.wind_blade.isorropia.common.tiles.TileJarSoul;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.casters.ICaster;

public class BlockJarSoul
        extends BlockContainer
        implements IItemBlockProvider {
    public static PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockJarSoul() {
        super(Material.GLASS);
        this.setHardness(0.3f);
        this.setResistance(1.0f);
        this.setLightLevel(0.66f);
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote || !(playerIn.getHeldItem(hand).getItem() instanceof ICaster)) {
            return false;
        }
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileJarSoul) {
            IsorropiaHelper.nbtToLiving(((TileJarSoul)te).entityData, worldIn, pos).setNoAI(false);
            worldIn.destroyBlock(pos, false);
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        TileEntity te;
        if (!player.isCreative() && (te = worldIn.getTileEntity(pos)) instanceof TileJarSoul) {
            ItemStack drop = new ItemStack(this);
            drop.setTagCompound(((TileJarSoul)te).entityData);
            BlockJarSoul.spawnAsEntity((World)worldIn, (BlockPos)pos, (ItemStack)drop);
        }
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        EntityPlayer player;
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileJarSoul && stack.hasTagCompound()) {
            ((TileJarSoul)te).entityData = stack.getTagCompound();
        }
        if (placer instanceof EntityPlayer && (player = (EntityPlayer)placer).isCreative()) {
            player.inventory.deleteStack(stack);
        }
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileJarSoul) {
            ItemStack drop = new ItemStack(this);
            drop.setTagCompound(((TileJarSoul)te).entityData);
            drops.add(drop);
        }
        return drops;
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex((int)meta));
    }

    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{FACING});
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
    }

    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return this.createTileEntity(worldIn, this.getStateFromMeta(meta));
    }

    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileJarSoul();
    }

    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0.1875, 0.0, 0.1875, 0.8125, 0.75, 0.8125);
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @SideOnly(value=Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public ItemBlock getItemBlock() {
        return new BlockJarSoulItem();
    }
}
