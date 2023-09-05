 package fr.wind_blade.isorropia.common.network;

 import io.netty.buffer.ByteBuf;
 import net.minecraft.util.math.BlockPos;
 import net.minecraftforge.fml.common.network.ByteBufUtils;
 import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
 import thaumcraft.api.aspects.Aspect;
 
 
 
 
 public class MagnetMessage
   implements IMessage
 {
   private BlockPos pos;
   private Aspect aspect;
/* 21 */   private int meta = -1;
 
   
   private boolean aspectFilter;
   
   private int area;
 
   
   public MagnetMessage(BlockPos pos, Aspect aspectIn, boolean aspectFilter, int area) {
/* 30 */     this.pos = pos;
/* 31 */     this.aspect = aspectIn;
/* 32 */     this.aspectFilter = aspectFilter;
/* 33 */     this.area = area;
   }
 
   
   public void fromBytes(ByteBuf buf) {
/* 44 */     this.pos = BlockPos.fromLong(buf.readLong());
/* 45 */     this.meta = buf.readInt();
/* 46 */     this.aspectFilter = buf.readBoolean();
/* 47 */     this.area = buf.readInt();
/* 48 */     String tag = ByteBufUtils.readUTF8String(buf);
/* 49 */     this.aspect = (tag == "") ? null : Aspect.aspects.get(tag);
   }
 
   
   public void toBytes(ByteBuf buf) {
/* 54 */     buf.writeLong(this.pos.toLong());
/* 55 */     buf.writeInt(this.meta);
/* 56 */     buf.writeBoolean(this.aspectFilter);
/* 57 */     buf.writeInt(this.area);
/* 58 */     ByteBufUtils.writeUTF8String(buf, (this.aspect == null) ? "" : this.aspect.getTag());
   }
   
   public MagnetMessage() {}

 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\network\MagnetMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */