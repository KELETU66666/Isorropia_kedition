 package fr.wind_blade.isorropia.common.network;
 import fr.wind_blade.isorropia.common.IsorropiaAPI;
 import fr.wind_blade.isorropia.common.lenses.Lens;
 import io.netty.buffer.ByteBuf;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.entity.EntityPlayerSP;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
 import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
 import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 import net.minecraftforge.registries.IForgeRegistryEntry;
 
 public class LensRemoveMessage implements IMessage {
/* 16 */   private Lens rightLens = null;
/* 17 */   private Lens leftLens = null;
 
 
 
 
   
   public LensRemoveMessage(Lens rightLens, Lens leftLens) {
/* 24 */     this.rightLens = rightLens;
     this.leftLens = leftLens;
   }
 
   
   public void fromBytes(ByteBuf buf) {
/* 30 */     if (buf.readBoolean())
/* 31 */       this.rightLens = (Lens)IsorropiaAPI.lensRegistry.getValue(buf.readInt()); 
/* 32 */     if (buf.readBoolean()) {
/* 33 */       this.leftLens = (Lens)IsorropiaAPI.lensRegistry.getValue(buf.readInt());
     }
   }
 
   
   public void toBytes(ByteBuf buf) {
/* 39 */     buf.writeBoolean((this.rightLens != null));
/* 40 */     if (this.rightLens != null)
        buf.writeInt(IsorropiaAPI.lensRegistry.getID(this.rightLens));
/* 42 */     buf.writeBoolean((this.leftLens != null));
/* 43 */     if (this.leftLens != null)
/* 44 */       buf.writeInt(IsorropiaAPI.lensRegistry.getID(this.leftLens));
   }
   
   public LensRemoveMessage() {}
   
   public static class Handler
     implements IMessageHandler<LensRemoveMessage, IMessage>
   {
     @SideOnly(Side.CLIENT)
     public IMessage onMessage(LensRemoveMessage message, MessageContext ctx) {
/* 54 */       Minecraft.getMinecraft().addScheduledTask(() -> {
             EntityPlayerSP player = (Minecraft.getMinecraft()).player;
             if (message.rightLens != null)
               message.rightLens.handleRemoval(player.world, (EntityPlayer)player); 
             if (message.leftLens != null)
               message.leftLens.handleRemoval(player.world, (EntityPlayer)player); 
           });
/* 61 */       return null;
     }
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\network\LensRemoveMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */