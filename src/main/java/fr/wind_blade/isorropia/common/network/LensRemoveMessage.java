package fr.wind_blade.isorropia.common.network;

import fr.wind_blade.isorropia.common.IsorropiaAPI;
import fr.wind_blade.isorropia.common.lenses.Lens;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LensRemoveMessage implements IMessage {
    private Lens rightLens = null;
    private Lens leftLens = null;





    public LensRemoveMessage(Lens rightLens, Lens leftLens) {
        this.rightLens = rightLens;
        this.leftLens = leftLens;
    }


    public void fromBytes(ByteBuf buf) {
        if (buf.readBoolean())
            this.rightLens = IsorropiaAPI.lensRegistry.getValue(buf.readInt());
        if (buf.readBoolean()) {
            this.leftLens = IsorropiaAPI.lensRegistry.getValue(buf.readInt());
        }
    }


    public void toBytes(ByteBuf buf) {
        buf.writeBoolean((this.rightLens != null));
        if (this.rightLens != null)
            buf.writeInt(IsorropiaAPI.lensRegistry.getID(this.rightLens));
        buf.writeBoolean((this.leftLens != null));
        if (this.leftLens != null)
            buf.writeInt(IsorropiaAPI.lensRegistry.getID(this.leftLens));
    }

    public LensRemoveMessage() {}

    public static class Handler
            implements IMessageHandler<LensRemoveMessage, IMessage>
    {
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(LensRemoveMessage message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                EntityPlayerSP player = (Minecraft.getMinecraft()).player;
                if (message.rightLens != null)
                    message.rightLens.handleRemoval(player.world, player);
                if (message.leftLens != null)
                    message.leftLens.handleRemoval(player.world, player);
            });
            return null;
        }
    }
}
