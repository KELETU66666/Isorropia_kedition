 package fr.wind_blade.isorropia.common.network;
 
 import fr.wind_blade.isorropia.common.Common;
 import fr.wind_blade.isorropia.common.capabilities.LivingBaseCapability;
 import io.netty.buffer.ByteBuf;
 import net.minecraft.client.Minecraft;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.nbt.NBTTagCompound;
 import net.minecraftforge.fml.common.network.ByteBufUtils;
 import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
 import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
 import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
 
 
 
 
 
 
 public class TrackingCapabilityMessage
   implements IMessage
 {
   private int id;
   private NBTTagCompound compound;
   
   public TrackingCapabilityMessage() {}
   
   public TrackingCapabilityMessage(EntityLivingBase base) {
      this(base, Common.getCap(base).startTrackSerializeNBT());
   }
   
   public TrackingCapabilityMessage(EntityLivingBase base, NBTTagCompound compound) {
/* 32 */     this.id = base.getEntityId();
/* 33 */     this.compound = compound;
   }
 
   
   public void fromBytes(ByteBuf buf) {
/* 38 */     this.id = buf.readInt();
/* 39 */     this.compound = ByteBufUtils.readTag(buf);
   }
 
   
   public void toBytes(ByteBuf buf) {
/* 44 */     buf.writeInt(this.id);
/* 45 */     ByteBufUtils.writeTag(buf, this.compound);
   }
 
   
   public static class ClientLivingBaseCapabilityHandler
     implements IMessageHandler<TrackingCapabilityMessage, IMessage>
   {
     public IMessage onMessage(TrackingCapabilityMessage message, MessageContext ctx) {
/* 53 */       Minecraft.getMinecraft().addScheduledTask(() -> {
             EntityLivingBase ent = (EntityLivingBase)(Minecraft.getMinecraft()).world.getEntityByID(message.id);
             LivingBaseCapability cap = Common.getCap(ent);
             if (cap != null)
               cap.startTrackDeserializeNBT(message.compound); 
           });
/* 59 */       return null;
     }
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\network\TrackingCapabilityMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */