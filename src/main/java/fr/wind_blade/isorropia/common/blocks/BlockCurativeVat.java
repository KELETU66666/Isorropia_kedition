 package fr.wind_blade.isorropia.common.blocks;
 
 import fr.wind_blade.isorropia.common.tiles.TileVat;
 import fr.wind_blade.isorropia.common.tiles.TileVatConnector;
 import java.util.Random;
 import net.minecraft.block.Block;
 import net.minecraft.block.ITileEntityProvider;
 import net.minecraft.block.material.Material;
 import net.minecraft.block.properties.IProperty;
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
   implements ITileEntityProvider, IBlockRegistry {
/*  28 */   public static final PropertyEnum<Type> VARIANT = PropertyEnum.create("variant", Type.class);
   
   public BlockCurativeVat() {
/*  31 */     super(Material.WOOD);
/*  32 */     this.lightValue = 8;
/*  33 */     setHardness(2.0F);
   }
   
   public static TileVat getMaster(World world, BlockPos pos) {
/*  37 */     return getMaster(world, world.getBlockState(pos), pos);
   }
   
   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
     TileVat tileVat;
/*  42 */     TileEntity tile = worldIn.getTileEntity(pos);
     
/*  44 */     switch (getMetaFromState(state)) {
       case 1:
/*  46 */         if (!(tile instanceof TileVat)) {
           break;
         }
/*  49 */         ((TileVat)tile).destroyMultiBlock();
         break;
       
       case 2:
/*  53 */         tile = worldIn.getTileEntity(pos.up(3));
/*  54 */         if (!(tile instanceof TileVat)) {
           break;
         }
/*  57 */         ((TileVat)tile).destroyMultiBlock();
         break;
       
       default:
/*  61 */         tileVat = getMaster(worldIn, state, pos);
/*  62 */         if (!(tileVat instanceof TileVat)) {
           break;
         }
/*  65 */         if (tileVat != null)
/*  66 */           tileVat.destroyMultiBlock(); 
         break;
     } 
/*  69 */     super.breakBlock(worldIn, pos, state);
   }
 
 
 
   
   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
/*  76 */     if (hand != EnumHand.MAIN_HAND) {
/*  77 */       return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
     }
/*  79 */     TileEntity te = worldIn.getTileEntity(pos);
/*  80 */     if (te instanceof TileVat) {
/*  81 */       TileVat tileVat = (TileVat)worldIn.getTileEntity(pos);
/*  82 */       return tileVat.onBlockRigthClick(playerIn, facing, true);
     } 
/*  84 */     TileVat vat = getMaster(worldIn, pos);
/*  85 */     if (vat != null) {
/*  86 */       return vat.onBlockRigthClick(playerIn, facing, false);
     }
     
/*  89 */     return true;
   }
 
   
   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
/*  94 */     return Items.AIR;
   }
 
   
   public TileEntity createNewTileEntity(World worldIn, int meta) {
/*  99 */     switch (meta) {
       case 1:
/* 101 */         return (TileEntity)new TileVat();
       case 3:
/* 103 */         return (TileEntity)new TileVatConnector();
     } 
/* 105 */     return null;
   }
 
 
   
   public IBlockState getStateFromMeta(int meta) {
/* 111 */     return getDefaultState().withProperty((IProperty)VARIANT, Type.getStateFromMeta(meta));
   }
 
   
   public int getMetaFromState(IBlockState state) {
/* 116 */     return ((Type)state.getValue((IProperty)VARIANT)).getMetadata();
   }
 
   
   protected BlockStateContainer createBlockState() {
/* 121 */     return new BlockStateContainer(this, new IProperty[] { (IProperty)VARIANT });
   }
 
   
   public boolean shouldSideBeRendered(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
/* 126 */     return !(getMetaFromState(state) == Type.PLACEHOLDER.getMetadata() || 
/* 127 */       getMetaFromState(state) == Type.CONNECTOR.getMetadata());
   }
 
   
   public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
/* 132 */     return (layer == BlockRenderLayer.TRANSLUCENT || layer == BlockRenderLayer.SOLID);
   }
   
   public boolean isFullyOpaque(IBlockState state) {
/* 136 */     return false;
   }
 
   
   public boolean isNormalCube(IBlockState state) {
/* 141 */     return false;
   }
 
   
   public boolean isOpaqueCube(IBlockState state) {
/* 146 */     return false;
   }
 
   
   public boolean isBlockNormalCube(IBlockState state) {
/* 151 */     return false;
   }
 
   
   public boolean isFullBlock(IBlockState state) {
/* 156 */     return false;
   }
 
   
   public boolean isTranslucent(IBlockState state) {
/* 161 */     return true;
   }
   
   public boolean isVisuallyOpaque() {
/* 165 */     return false;
   }
 
   
   public boolean isFullCube(IBlockState state) {
/* 170 */     return false;
   }
 
 
   
   public static TileVat getMaster(World world, IBlockState state, BlockPos pos) {
/* 176 */     for (BlockPos pos2 : BlockPos.getAllInBoxMutable(pos.getX() - 1, pos.getY(), pos.getZ() - 1, pos.getX() + 1, pos
/* 177 */         .getY(), pos.getZ() + 1)) {
/* 178 */       IBlockState state2 = world.getBlockState(pos2);
/* 179 */       if (state2.getBlock() instanceof BlockLiquidVat) {
/* 180 */         state2 = world.getBlockState(pos2.up());
/* 181 */         TileEntity te = world.getTileEntity(pos2.up());
/* 182 */         if (te instanceof TileVat) {
/* 183 */           return (TileVat)te;
         }
/* 185 */         state2 = world.getBlockState(pos2.up(2));
/* 186 */         te = world.getTileEntity(pos2.up(2));
/* 187 */         if (te instanceof TileVat)
/* 188 */           return (TileVat)te; 
         continue;
       } 
/* 191 */       if (state2.getBlock() instanceof BlockCurativeVat) {
/* 192 */         if (((BlockCurativeVat)state2.getBlock()).getMetaFromState(state2) == 1) {
/* 193 */           TileEntity te = world.getTileEntity(pos2);
/* 194 */           if (te instanceof TileVat)
/* 195 */             return (TileVat)te;  continue;
         } 
/* 197 */         if (((BlockCurativeVat)state2.getBlock()).getMetaFromState(state2) == 2) {
/* 198 */           TileEntity te = world.getTileEntity(pos2.up(3));
/* 199 */           if (te instanceof TileVat) {
/* 200 */             return (TileVat)te;
           }
         } 
       } 
     } 
/* 205 */     return null;
   }
 
   
   public boolean isInCreativeTabs() {
/* 210 */     return false;
   }
   
   public enum Type
     implements IStringSerializable {
/* 215 */     PLACEHOLDER(0, "placeholder"), TOP(1, "top"), BOTTOM(2, "bottom"), CONNECTOR(3, "connector"); private final int metadata;
     private final String name;
/* 217 */     private static final Type[] METADATA = new Type[(values()).length];
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
     
     static {
/* 249 */       Type[] var0 = values();
/* 250 */       int var1 = var0.length;
       
/* 252 */       for (int var2 = 0; var2 < var1; var2++) {
/* 253 */         Type var3 = var0[var2];
/* 254 */         METADATA[var3.getMetadata()] = var3;
       } 
     }
     
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
       if (metadata < 0 || metadata >= METADATA.length)
         metadata = 0; 
       return METADATA[metadata];
     }
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\blocks\BlockCurativeVat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */