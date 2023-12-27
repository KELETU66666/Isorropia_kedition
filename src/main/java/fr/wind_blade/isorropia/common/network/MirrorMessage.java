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

public class MirrorMessage implements IMessage {
    private BlockPos pos;

    public MirrorMessage() {}

    public MirrorMessage(BlockPos pos) {
        this.pos = pos;
    }


    public void fromBytes(ByteBuf buf) {
        this.pos = BlockPos.fromLong(buf.readLong());
    }


    public void toBytes(ByteBuf buf) {
        buf.writeLong(this.pos.toLong());
    }


    public static class MirrorMessageHandler
            implements IMessageHandler<MirrorMessage, IMessage>
    {
        public IMessage onMessage(final MirrorMessage message, final MessageContext ctx) {
            final EntityPlayerMP player = (ctx.getServerHandler()).player;
            player.getServerWorld().addScheduledTask(() -> {
                if (!(ctx.getServerHandler()).player.world.isAreaLoaded(message.pos, 1))
                    return;  World worldIn = player.world;
                    ItemStack stack = player.inventory.getCurrentItem();
                    if (!(stack.getItem() instanceof fr.wind_blade.isorropia.common.items.baubles.ItemBaubleMirror))
                        return;
                    Block block = worldIn.getBlockState(message.pos).getBlock();
                    if (block == BlocksTC.mirror) {
                        NBTTagCompound compound = new NBTTagCompound();
                        compound.setInteger("linkX", message.pos.getX());
                        compound.setInteger("linkY", message.pos.getY());
                        compound.setInteger("linkZ", message.pos.getZ());
                        compound.setInteger("linkDim", worldIn.provider.getDimension());
                        compound.setString("dimname", DimensionManager.getProvider(worldIn.provider.getDimension())
                                .getDimensionType().getName());
                        stack.setTagCompound(compound);
                        player.inventoryContainer.detectAndSendChanges();}});
        return null;
        }
    }
}
