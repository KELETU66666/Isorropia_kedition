package fr.wind_blade.isorropia.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;


public class ParticuleDestroyMessage
        implements IMessage
{
    private BlockPos pos;

    public ParticuleDestroyMessage() {}

    public ParticuleDestroyMessage(BlockPos pos) {
        this.pos = pos;
    }


    public void fromBytes(ByteBuf buf) {
        this.pos = BlockPos.fromLong(buf.readLong());
    }


    public void toBytes(ByteBuf buf) {
        buf.writeLong(this.pos.toLong());
    }

    public static class Handler
            implements IMessageHandler<ParticuleDestroyMessage, IMessage>
    {
        public IMessage onMessage(ParticuleDestroyMessage message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> (Minecraft.getMinecraft()).effectRenderer.addBlockDestroyEffects(message.pos, (Minecraft.getMinecraft()).world.getBlockState(message.pos)));

            return null;
        }
    }
}