 package fr.wind_blade.isorropia.common.network;
 
 import fr.wind_blade.isorropia.common.lenses.LensManager;
 import io.netty.buffer.ByteBuf;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.entity.player.EntityPlayerMP;
 import net.minecraft.item.ItemStack;
 import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
 import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
 import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
 
 
 
 
 
 
 
 
 
 public class LensRemoveMessageSP
   implements IMessage
 {
   public void fromBytes(ByteBuf buf) {}
   
   public void toBytes(ByteBuf buf) {}
   
   public static class Handler
     implements IMessageHandler<LensRemoveMessageSP, IMessage>
   {
     public IMessage onMessage(LensRemoveMessageSP message, MessageContext ctx) {
/* 31 */       (ctx.getServerHandler()).player.getServerWorld().addScheduledTask(() -> {
             EntityPlayerMP player = (ctx.getServerHandler()).player;
             ItemStack stack = LensManager.getRevealer((EntityPlayer)player);
             if (!stack.isEmpty()) {
               LensManager.removeLens((EntityPlayer)player, stack, LensManager.LENSSLOT.LEFT);
               LensManager.removeLens((EntityPlayer)player, stack, LensManager.LENSSLOT.RIGHT);
             } 
           });
/* 39 */       return null;
     }
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\network\LensRemoveMessageSP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */