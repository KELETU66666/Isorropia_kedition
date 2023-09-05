 package fr.wind_blade.isorropia.common.blocks;
 
 import fr.wind_blade.isorropia.Isorropia;
 import fr.wind_blade.isorropia.common.tiles.TileCelestialMagnet;
 import net.minecraft.block.Block;
 import net.minecraft.block.ITileEntityProvider;
 import net.minecraft.block.material.Material;
 import net.minecraft.block.properties.IProperty;
 import net.minecraft.block.properties.PropertyDirection;
 import net.minecraft.block.state.BlockStateContainer;
 import net.minecraft.block.state.IBlockState;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.item.ItemStack;
 import net.minecraft.tileentity.TileEntity;
 import net.minecraft.util.BlockRenderLayer;
 import net.minecraft.util.EnumFacing;
 import net.minecraft.util.EnumHand;
 import net.minecraft.util.math.BlockPos;
 import net.minecraft.world.World;
 import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
 import thaumcraft.api.aspects.IEssentiaContainerItem;
 import thaumcraft.api.blocks.ILabelable;
 
 public class BlockCelestialMagnet
   extends Block implements ITileEntityProvider, ILabelable {
    public static PropertyDirection FACING = PropertyDirection.create("facing");
   
   public BlockCelestialMagnet(Material materialIn) {
/* 30 */     super(materialIn);
   }
 
   
   public IBlockState getStateFromMeta(int meta) {
/* 35 */     return getDefaultState().withProperty((IProperty)FACING, (Comparable)EnumFacing.byIndex(meta));
   }
 
   
   public int getMetaFromState(IBlockState state) {
/* 40 */     return ((EnumFacing)state.getValue((IProperty)FACING)).getIndex();
   }
 
   
   public BlockStateContainer createBlockState() {
/* 45 */     return new BlockStateContainer(this, new IProperty[] { (IProperty)FACING });
   }
 
 
   
   public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
/* 51 */     return getDefaultState().withProperty((IProperty)FACING, (Comparable)facing);
   }
 
   
   public TileEntity createNewTileEntity(World worldIn, int meta) {
/* 56 */     return (TileEntity)new TileCelestialMagnet();
   }
   
   public BlockRenderLayer getBlockLayer() {
/* 60 */     return BlockRenderLayer.TRANSLUCENT;
   }
 
   
   public boolean isOpaqueCube(IBlockState state) {
/* 65 */     return false;
   }
 
   
   public boolean isFullCube(IBlockState state) {
/* 70 */     return false;
   }
 
 
   
   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
/* 76 */     FMLNetworkHandler.openGui(playerIn, Isorropia.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
/* 77 */     return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
   }
 
   
   public boolean applyLabel(EntityPlayer entityPlayer, BlockPos pos, EnumFacing enumFacing, ItemStack stack) {
/* 82 */     if (!stack.isEmpty()) {
/* 83 */       World worldIn = entityPlayer.world;
/* 84 */       TileEntity te = worldIn.getTileEntity(pos);
/* 85 */       if (!(te instanceof TileCelestialMagnet))
/* 86 */         return false; 
/* 87 */       TileCelestialMagnet temp = (TileCelestialMagnet)te;
/* 88 */       if (stack.getItem() instanceof thaumcraft.common.items.consumables.ItemLabel) {
/* 89 */         IEssentiaContainerItem label = (IEssentiaContainerItem)stack.getItem();
/* 90 */         if (label.getAspects(stack) != null) {
/* 91 */           return temp.addAspectFilter(label.getAspects(stack).getAspects()[0]);
         }
       } 
     } 
/* 95 */     return false;
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\blocks\BlockCelestialMagnet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */