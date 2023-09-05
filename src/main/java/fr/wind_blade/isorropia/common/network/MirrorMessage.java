 package fr.wind_blade.isorropia.common.network;
 
 import io.netty.buffer.ByteBuf;
 import net.minecraft.block.Block;
 import net.minecraft.entity.player.EntityPlayerMP;
 import net.minecraft.item.ItemStack;
 import net.minecraft.nbt.NBTTagCompound;
 import net.minecraft.util.math.BlockPos;
 import net.minecraft.world.World;
 import net.minecraftforge.common.DimensionManager;
 import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
 import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
 import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
 import thaumcraft.api.blocks.BlocksTC;
 
 
 
 
 
 
 
 public class MirrorMessage
   implements IMessage
 {
   private BlockPos pos;
   
   public MirrorMessage() {}
   
   public MirrorMessage(BlockPos pos) {
/* 30 */     this.pos = pos;
   }
 
   
   public void fromBytes(ByteBuf buf) {
/* 35 */     this.pos = BlockPos.fromLong(buf.readLong());
   }
 
   
   public void toBytes(ByteBuf buf) {
/* 40 */     buf.writeLong(this.pos.toLong());
   }
 
   
   public static class MirrorMessageHandler
     implements IMessageHandler<MirrorMessage, IMessage>
   {
     public IMessage onMessage(final MirrorMessage message, final MessageContext ctx) {
/* 48 */       final EntityPlayerMP player = (ctx.getServerHandler()).player;
/* 49 */       player.getServerWorld().addScheduledTask(() -> {
/* 52 */               if (!(ctx.getServerHandler()).player.world.isAreaLoaded(message.pos, 1))
/* 53 */                 return;  World worldIn = player.world;
/* 54 */               ItemStack stack = player.inventory.getCurrentItem();
/* 55 */               if (!(stack.getItem() instanceof fr.wind_blade.isorropia.common.items.baubles.ItemBaubleMirror))
    return;
/* 57 */               Block block = worldIn.getBlockState(message.pos).getBlock();
/* 58 */               if (block == BlocksTC.mirror) {
/* 59 */                 NBTTagCompound compound = new NBTTagCompound();
/* 60 */                 compound.setInteger("linkX", message.pos.getX());
/* 61 */                 compound.setInteger("linkY", message.pos.getY());
/* 62 */                 compound.setInteger("linkZ", message.pos.getZ());
/* 63 */                 compound.setInteger("linkDim", worldIn.provider.getDimension());
/* 64 */                 compound.setString("dimname", DimensionManager.getProvider(worldIn.provider.getDimension())
/* 65 */                     .getDimensionType().getName());
/* 66 */                 stack.setTagCompound(compound);
/* 67 */                 player.inventoryContainer.detectAndSendChanges();
  }
});
/* 71 */       return null;
     }
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\network\MirrorMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */