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
   extends Block implements ITileEntityProvider {
   public BlockModifiedMatrix() {
/* 21 */     super(Material.ROCK);
/* 22 */     setSoundType(SoundType.STONE);
/* 23 */     setHardness(2.0F);
/* 24 */     setResistance(20.0F);
   }
 
   
   public boolean isOpaqueCube(IBlockState state) {
      return false;
   }
 
   
   public boolean isFullCube(IBlockState state) {
/* 34 */     return false;
   }
 
   
   public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
/* 39 */     return BlockFaceShape.UNDEFINED;
   }
 
   
   public EnumBlockRenderType getRenderType(IBlockState state) {
/* 44 */     return EnumBlockRenderType.INVISIBLE;
   }
 
   
   public TileEntity createNewTileEntity(World worldIn, int meta) {
/* 49 */     return (TileEntity)new TileModifiedMatrix();
   }
 
   
   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
/* 54 */     TileEntity te = worldIn.getTileEntity(pos.down());
/* 55 */     if (te instanceof TileVat) {
/* 56 */       TileVat vat = (TileVat)te;
/* 57 */       if (vat.isInfusing()) {
/* 58 */         vat.destroyMultiBlock();
       }
/* 60 */       vat.setActive(false);
/* 61 */       vat.setStartUp(0.0F);
/* 62 */       vat.syncTile(false);
     } 
/* 64 */     super.breakBlock(worldIn, pos, state);
   }
 
   
   public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
/* 69 */     return worldIn.getTileEntity(pos.down()) instanceof TileVat;
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\blocks\BlockModifiedMatrix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */