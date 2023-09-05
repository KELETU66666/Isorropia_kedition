 package fr.wind_blade.isorropia.common.network;
 
 import fr.wind_blade.isorropia.common.tiles.TileVat;
 import io.netty.buffer.ByteBuf;
 import net.minecraft.client.Minecraft;
 import net.minecraft.tileentity.TileEntity;
 import net.minecraft.util.math.BlockPos;
 import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
 import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
 import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
 import thaumcraft.Thaumcraft;
 import thaumcraft.common.tiles.crafting.TilePedestal;

 import java.util.Map;


 public class ISPacketFXInfusionSource
   implements IMessage, IMessageHandler<ISPacketFXInfusionSource, IMessage>
 {
   private long p1;
   private long p2;
   private int color;
   private int index;
   
   public ISPacketFXInfusionSource() {}
   
   public ISPacketFXInfusionSource(BlockPos pos, BlockPos pos2, int color) {
     this(pos, pos2, color, 0);
   }
   
   public ISPacketFXInfusionSource(BlockPos pos, BlockPos pos2, int color, int index) {
      this.p1 = pos.toLong();
/* 30 */     this.p2 = pos2.toLong();
/* 31 */     this.color = color;
/* 32 */     this.index = index;
   }
 
   
   public void toBytes(ByteBuf buffer) {
/* 37 */     buffer.writeLong(this.p1);
/* 38 */     buffer.writeLong(this.p2);
/* 39 */     buffer.writeInt(this.color);
/* 40 */     buffer.writeInt(this.index);
   }
 
   
   public void fromBytes(ByteBuf buffer) {
/* 45 */     this.p1 = buffer.readLong();
/* 46 */     this.p2 = buffer.readLong();
/* 47 */     this.color = buffer.readInt();
/* 48 */     this.index = buffer.readInt();
   }


     public IMessage onMessage(ISPacketFXInfusionSource message, MessageContext ctx) {
         BlockPos p1 = BlockPos.fromLong(message.p1);
         BlockPos p2 = BlockPos.fromLong(message.p2);
         String key = p2.getX() + ":" + p2.getY() + ":" + p2.getZ() + ":" + message.color;
         TileEntity tile = Thaumcraft.proxy.getClientWorld().getTileEntity(p1);
         if (tile instanceof TileVat) {
             TileVat is;
             int count = 15;
             TileEntity te = Minecraft.getMinecraft().world.getTileEntity(p2);
             if (te instanceof TilePedestal) {
                 count = 60;
             }
             if ((is = (TileVat)tile).getSourceFX().containsKey(key)) {
                 TileVat.SourceFX sf = is.getSourceFX().get(key);
                 sf.ticks = count;
                 is.getSourceFX().put(key, sf);
             } else {
                 TileVat matrix = is;
                 matrix.getClass();
                 Map<String, TileVat.SourceFX> map = is.getSourceFX();
                 TileVat tileVat = matrix;
                 tileVat.getClass();
                 map.put(key, new TileVat.SourceFX(p2, count, message.color));
             }
         }
         return null;
     }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\network\ISPacketFXInfusionSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */