package fr.wind_blade.isorropia.common.blocks;

import fr.wind_blade.isorropia.common.tiles.TileVat;
import fr.wind_blade.isorropia.common.tiles.TileVatConnector;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCurativeVat
        extends Block
        implements ITileEntityProvider,
        IBlockRegistry {
    public static final PropertyEnum<Type> VARIANT = PropertyEnum.create("variant", Type.class);

    public BlockCurativeVat() {
        super(Material.WOOD);
        this.lightValue = 8;
        this.setHardness(2.0f);
    }

    public static TileVat getMaster(World world, BlockPos pos) {
        return BlockCurativeVat.getMaster(world, world.getBlockState(pos), pos);
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tile = worldIn.getTileEntity(pos);
        switch (this.getMetaFromState(state)) {
            case 1: {
                if (!(tile instanceof TileVat)) break;
                ((TileVat)tile).destroyMultiBlock();
                break;
            }
            case 2: {
                tile = worldIn.getTileEntity(pos.up(3));
                if (!(tile instanceof TileVat)) break;
                ((TileVat)tile).destroyMultiBlock();
                break;
            }
            default: {
                tile = BlockCurativeVat.getMaster(worldIn, state, pos);
                if (!(tile instanceof TileVat) || tile == null)
                    break;
                ((TileVat)tile).destroyMultiBlock();
            }
        }
        super.breakBlock(worldIn, pos, state);
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (hand != EnumHand.MAIN_HAND) {
            return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
        }
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileVat) {
            TileVat vat = (TileVat)worldIn.getTileEntity(pos);
            return vat.onBlockRigthClick(playerIn, facing, true);
        }
        TileVat vat = BlockCurativeVat.getMaster(worldIn, pos);
        if (vat != null) {
            return vat.onBlockRigthClick(playerIn, facing, false);
        }
        return true;
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.AIR;
    }

    public TileEntity createNewTileEntity(World worldIn, int meta) {
        switch (meta) {
            case 1: {
                return new TileVat();
            }
            case 3: {
                return new TileVatConnector();
            }
        }
        return null;
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, Type.getStateFromMeta(meta));
    }

    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT).getMetadata();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }

    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return this.getMetaFromState(state) != Type.PLACEHOLDER.getMetadata() && this.getMetaFromState(state) != Type.CONNECTOR.getMetadata();
    }

    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return layer == BlockRenderLayer.TRANSLUCENT || layer == BlockRenderLayer.SOLID;
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

    public static TileVat getMaster(World world, IBlockState state, BlockPos pos) {
        for (BlockPos pos2 : BlockPos.getAllInBoxMutable(pos.getX() - 1, pos.getY(), pos.getZ() - 1, pos.getX() + 1, pos.getY(), pos.getZ() + 1)) {
            TileEntity te;
            IBlockState state2 = world.getBlockState(pos2);
            if (state2.getBlock() instanceof BlockLiquidVat) {
                state2 = world.getBlockState(pos2.up());
                te = world.getTileEntity(pos2.up());
                if (te instanceof TileVat) {
                    return (TileVat)te;
                }
                state2 = world.getBlockState(pos2.up(2));
                te = world.getTileEntity(pos2.up(2));
                if (!(te instanceof TileVat)) continue;
                return (TileVat)te;
            }
            if (!(state2.getBlock() instanceof BlockCurativeVat) || !(state2.getBlock().getMetaFromState(state2) == 1 ? (te = world.getTileEntity(pos2)) instanceof TileVat : state2.getBlock().getMetaFromState(state2) == 2 && (te = world.getTileEntity(pos2.up(3))) instanceof TileVat)) continue;
            return (TileVat)te;
        }
        return null;
    }

    @Override
    public boolean isInCreativeTabs() {
        return false;
    }

    public enum Type implements IStringSerializable
    {
        PLACEHOLDER(0, "placeholder"),
        TOP(1, "top"),
        BOTTOM(2, "bottom"),
        CONNECTOR(3, "connector");

        private static final Type[] METADATA;
        private final String name;
        private final int metadata;

        Type(int metadata, String name) {
            this.metadata = metadata;
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public int getMetadata() {
            return this.metadata;
        }

        public String toString() {
            return this.name;
        }

        public static Type getStateFromMeta(int metadata) {
            if (metadata < 0 || metadata >= METADATA.length) {
                metadata = 0;
            }
            return METADATA[metadata];
        }

        static {
            METADATA = new Type[Type.values().length];
            Type[] var0 = Type.values();
            for (Type type : var0) {
                Type.METADATA[type.getMetadata()] = type;
            }
        }
    }
}
