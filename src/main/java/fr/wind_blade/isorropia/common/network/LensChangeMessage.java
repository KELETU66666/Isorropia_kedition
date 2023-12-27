package fr.wind_blade.isorropia.common.network;

import fr.wind_blade.isorropia.common.IsorropiaAPI;
import fr.wind_blade.isorropia.common.lenses.Lens;
import fr.wind_blade.isorropia.common.lenses.LensManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class LensChangeMessage implements IMessage
{
    private Lens lens;
    private int stackSlot;
    private LensManager.LENSSLOT type;

    public LensChangeMessage() {}

    public LensChangeMessage(Lens lens, int slot, LensManager.LENSSLOT type) {
        this.lens = lens;
        this.stackSlot = slot;
        this.type = type;
    }


    public void fromBytes(ByteBuf buf) {
        this.lens = (Lens)IsorropiaAPI.lensRegistry.getValue(buf.readInt());
        this.stackSlot = buf.readInt();
        this.type = LensManager.LENSSLOT.getByString(ByteBufUtils.readUTF8String(buf));
    }


    public void toBytes(ByteBuf buf) {
        buf.writeInt(IsorropiaAPI.lensRegistry.getID(this.lens));
        buf.writeInt(this.stackSlot);
        ByteBufUtils.writeUTF8String(buf, this.type.getName());
    }


    public static class LensChangeMessageHandler
            implements IMessageHandler<LensChangeMessage, IMessage>
    {
        public IMessage onMessage(LensChangeMessage message, MessageContext ctx) {
            (ctx.getServerHandler()).player.getServerWorld().addScheduledTask(() -> {
                EntityPlayerMP player = (ctx.getServerHandler()).player;
                player.inventory.removeStackFromSlot(message.stackSlot);
                LensManager.changeLens((EntityPlayer)player, message.lens, message.type);
            });
            return null;
        }
    }
}