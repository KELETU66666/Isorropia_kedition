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
 
 public class BlockLiquidVat
   extends BlockLiquid implements IBlockRegistry {
   public BlockLiquidVat() {
/*  24 */     super(MaterialIR.LIQUID_VAT);
/*  25 */     setHardness(3.0F);
/*  26 */     setResistance(15.0F);
/*  27 */     this.lightValue = 8;
   }
 
   
   public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
/*  32 */     return false;
   }
 
   
   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
/*  37 */     TileVat tile = getMaster(worldIn, state, pos);
     
/*  39 */     if (tile != null) {
/*  40 */       tile.destroyMultiBlock();
     }
     
/*  43 */     super.breakBlock(worldIn, pos, state);
   }
 
 
   
   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
/*  49 */     TileVat vat = BlockCurativeVat.getMaster(worldIn, pos);
     
/*  51 */     if (vat != null && vat.getEntityContained() == playerIn) {
/*  52 */       vat.onBlockRigthClick(playerIn, facing, false);
     }
     
/*  55 */     return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
   }
 
 
   
   public Boolean isEntityInsideMaterial(IBlockAccess world, BlockPos blockpos, IBlockState iblockstate, Entity entity, double yToTest, Material materialIn, boolean testingHead) {
/*  61 */     if (materialIn == MaterialIR.LIQUID_VAT || materialIn == Material.WATER)
/*  62 */       return Boolean.valueOf(true); 
/*  63 */     return Boolean.valueOf(false);
   }
   
   public TileVat getMaster(World worldIn, IBlockState state, BlockPos pos) {
/*  67 */     for (int i = 1; i <= 2; i++) {
/*  68 */       TileEntity te = worldIn.getTileEntity(pos.up(i));
       
/*  70 */       if (te instanceof TileVat) {
/*  71 */         return (TileVat)te;
       }
     } 
     
/*  75 */     return null;
   }
   
   @SideOnly(Side.CLIENT)
   public BlockRenderLayer getBlockLayer() {
/*  80 */     return BlockRenderLayer.TRANSLUCENT;
   }
 
   
   public EnumBlockRenderType getRenderType(IBlockState state) {
/*  85 */     return EnumBlockRenderType.INVISIBLE;
   }
   
   public boolean isFullyOpaque(IBlockState state) {
/*  89 */     return false;
   }
 
   
   public boolean isNormalCube(IBlockState state) {
/*  94 */     return false;
   }
 
   
   public boolean isOpaqueCube(IBlockState state) {
/*  99 */     return false;
   }
 
   
   public boolean isBlockNormalCube(IBlockState state) {
/* 104 */     return false;
   }
 
   
   public boolean isFullBlock(IBlockState state) {
/* 109 */     return false;
   }
 
   
   public boolean isTranslucent(IBlockState state) {
/* 114 */     return true;
   }
   
   public boolean isVisuallyOpaque() {
/* 118 */     return false;
   }
 
   
   public boolean isFullCube(IBlockState state) {
/* 123 */     return false;
   }
 
   
   public boolean isInCreativeTabs() {
/* 128 */     return false;
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\blocks\BlockLiquidVat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */