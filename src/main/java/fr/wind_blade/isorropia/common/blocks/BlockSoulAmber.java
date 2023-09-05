 package fr.wind_blade.isorropia.common.blocks;
 
 import net.minecraft.block.Block;
 import net.minecraft.block.SoundType;
 import net.minecraft.block.material.EnumPushReaction;
 import net.minecraft.block.material.Material;
 import net.minecraft.block.state.IBlockState;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.util.BlockRenderLayer;
 import net.minecraft.util.EnumFacing;
 import net.minecraft.util.math.BlockPos;
 import net.minecraft.world.IBlockAccess;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 
 public class BlockSoulAmber
   extends Block {
   public BlockSoulAmber() {
     super(Material.GLASS);
/* 20 */     setHardness(0.5F);
/* 21 */     setSoundType(SoundType.STONE);
   }
 
   
   public boolean isBeaconBase(IBlockAccess world, BlockPos pos, BlockPos beacon) {
      return true;
   }
 
   
   public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
/* 31 */     return true;
   }
   
   public EnumPushReaction getMobilityFlag(IBlockState state) {
/* 35 */     return EnumPushReaction.NORMAL;
   }
 
 
   
   @SideOnly(Side.CLIENT)
   public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
/* 42 */     IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
/* 43 */     Block block = iblockstate.getBlock();
/* 44 */     return (block == this) ? false : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
   }
   
   @SideOnly(Side.CLIENT)
   public BlockRenderLayer getBlockLayer() {
/* 49 */     return BlockRenderLayer.TRANSLUCENT;
   }
 
   
   public boolean isOpaqueCube(IBlockState iblockstate) {
/* 54 */     return false;
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\blocks\BlockSoulAmber.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */