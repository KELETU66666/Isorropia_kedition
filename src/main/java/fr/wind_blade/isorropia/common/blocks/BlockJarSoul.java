 package fr.wind_blade.isorropia.common.blocks;
 
 import fr.wind_blade.isorropia.common.libs.helpers.IsorropiaHelper;
 import fr.wind_blade.isorropia.common.tiles.TileJarSoul;
 import java.util.ArrayList;
 import java.util.List;
 import net.minecraft.block.Block;
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
 
 public class BlockJarSoul
   extends BlockContainer implements IItemBlockProvider {
/*  34 */   public static PropertyDirection FACING = BlockHorizontal.FACING;
   
   public BlockJarSoul() {
/*  37 */     super(Material.GLASS);
/*  38 */     setHardness(0.3F);
/*  39 */     setResistance(1.0F);
/*  40 */     setLightLevel(0.66F);
   }
 
 
 
   
   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
/*  47 */     if (worldIn.isRemote || !(playerIn.getHeldItem(hand).getItem() instanceof thaumcraft.api.casters.ICaster)) {
/*  48 */       return false;
     }
/*  50 */     TileEntity te = worldIn.getTileEntity(pos);
     
/*  52 */     if (te instanceof TileJarSoul) {
/*  53 */       IsorropiaHelper.nbtToLiving(((TileJarSoul)te).entityData, worldIn, pos).setNoAI(false);
/*  54 */       worldIn.destroyBlock(pos, false);
     } 
     
/*  57 */     return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
   }
 
   
   public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
/*  62 */     if (!player.isCreative()) {
/*  63 */       TileEntity te = worldIn.getTileEntity(pos);
/*  64 */       if (te instanceof TileJarSoul) {
/*  65 */         ItemStack drop = new ItemStack((Block)this);
/*  66 */         drop.setTagCompound(((TileJarSoul)te).entityData);
         
/*  68 */         spawnAsEntity(worldIn, pos, drop);
       } 
     } 
   }
 
 
   
   public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
/*  76 */     TileEntity te = worldIn.getTileEntity(pos);
/*  77 */     if (te instanceof TileJarSoul && stack.hasTagCompound()) {
/*  78 */       ((TileJarSoul)te).entityData = stack.getTagCompound();
     }
/*  80 */     if (placer instanceof EntityPlayer) {
/*  81 */       EntityPlayer player = (EntityPlayer)placer;
/*  82 */       if (player.isCreative())
/*  83 */         player.inventory.deleteStack(stack); 
     } 
/*  85 */     super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
   }
 
   
   public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
/*  90 */     ArrayList<ItemStack> drops = new ArrayList<>();
/*  91 */     TileEntity te = world.getTileEntity(pos);
/*  92 */     if (te instanceof TileJarSoul) {
/*  93 */       ItemStack drop = new ItemStack((Block)this);
/*  94 */       drop.setTagCompound(((TileJarSoul)te).entityData);
/*  95 */       drops.add(drop);
     } 
     return drops;
   }
 
   
   public IBlockState getStateFromMeta(int meta) {
/* 102 */     return getDefaultState().withProperty((IProperty)FACING, (Comparable)EnumFacing.byHorizontalIndex(meta));
   }
 
   
   public int getMetaFromState(IBlockState state) {
/* 107 */     return ((EnumFacing)state.getValue((IProperty)FACING)).getIndex();
   }
 
   
   protected BlockStateContainer createBlockState() {
/* 112 */     return new BlockStateContainer((Block)this, new IProperty[] { (IProperty)FACING });
   }
 
 
   
   public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
/* 118 */     return getDefaultState().withProperty((IProperty)FACING, (Comparable)placer.getHorizontalFacing());
   }
 
   
   public TileEntity createNewTileEntity(World worldIn, int meta) {
/* 123 */     return createTileEntity(worldIn, getStateFromMeta(meta));
   }
 
   
   public TileEntity createTileEntity(World world, IBlockState state) {
/* 128 */     return (TileEntity)new TileJarSoul();
   }
 
   
   public EnumBlockRenderType getRenderType(IBlockState state) {
     return EnumBlockRenderType.MODEL;
   }
 
 
   
   public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
/* 139 */     return new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.75D, 0.8125D);
   }
 
   
   public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
/* 144 */     return BlockFaceShape.UNDEFINED;
   }
   
   @SideOnly(Side.CLIENT)
   public BlockRenderLayer getBlockLayer() {
/* 149 */     return BlockRenderLayer.TRANSLUCENT;
   }
 
   
   public boolean isOpaqueCube(IBlockState state) {
/* 154 */     return false;
   }
 
   
   public boolean isFullCube(IBlockState state) {
/* 159 */     return false;
   }
 
   
   public ItemBlock getItemBlock() {
/* 164 */     return new BlockJarSoulItem();
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\blocks\BlockJarSoul.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */