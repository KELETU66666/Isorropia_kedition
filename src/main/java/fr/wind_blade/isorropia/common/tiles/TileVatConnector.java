 package fr.wind_blade.isorropia.common.tiles;
 
 import fr.wind_blade.isorropia.common.blocks.BlockCurativeVat;
 import net.minecraft.tileentity.TileEntity;
 import net.minecraft.util.EnumFacing;
 import net.minecraft.util.ITickable;
 import thaumcraft.api.aspects.Aspect;
 import thaumcraft.api.aspects.IEssentiaTransport;
 
 
 
 public class TileVatConnector
   extends TileEntity
   implements IEssentiaTransport, ITickable
 {
   public TileVat getMaster() {
/* 17 */     return BlockCurativeVat.getMaster(this.world, this.pos);
   }
 
   
   public int addEssentia(Aspect arg0, int arg1, EnumFacing arg2) {
/* 22 */     TileVat vat = getMaster();
/* 23 */     if (vat != null)
/* 24 */       return vat.addEssentia(arg0, arg1, arg2); 
     return 0;
   }
 
   
   public boolean canInputFrom(EnumFacing arg0) {
/* 30 */     return true;
   }
 
   
   public boolean canOutputTo(EnumFacing arg0) {
/* 35 */     return false;
   }
 
   
   public int getEssentiaAmount(EnumFacing arg0) {
/* 40 */     return 0;
   }
 
   
   public Aspect getEssentiaType(EnumFacing arg0) {
/* 45 */     return null;
   }
 
   
   public int getSuctionAmount(EnumFacing arg0) {
/* 50 */     TileVat vat = getMaster();
/* 51 */     if (vat != null)
/* 52 */       return (vat.getEssentiaNeeded().size() > 0) ? 128 : 0; 
/* 53 */     return 0;
   }
 
   
   public Aspect getSuctionType(EnumFacing arg0) {
/* 58 */     TileVat vat = getMaster();
/* 59 */     if (vat != null)
/* 60 */       return vat.getSuctionType(); 
/* 61 */     return null;
   }
 
   
   public boolean isConnectable(EnumFacing arg0) {
/* 66 */     return true;
   }
 
   
   public int takeEssentia(Aspect arg0, int arg1, EnumFacing arg2) {
/* 71 */     return 0;
   }
 
   
   public int getMinimumSuction() {
/* 76 */     return 0;
   }
   
   public void setSuction(Aspect arg0, int arg1) {}
   
   public void update() {}
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\tiles\TileVatConnector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */