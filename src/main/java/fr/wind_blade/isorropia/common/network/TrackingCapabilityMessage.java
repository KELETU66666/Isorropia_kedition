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
        implements IMessage {
    private int id;
    private NBTTagCompound compound;

    public TrackingCapabilityMessage() {
    }

    public TrackingCapabilityMessage(EntityLivingBase base) {
        this(base, Common.getCap(base).startTrackSerializeNBT());
    }

    public TrackingCapabilityMessage(EntityLivingBase base, NBTTagCompound compound) {
        this.id = base.getEntityId();
        this.compound = compound;
    }

    public void fromBytes(ByteBuf buf) {
        this.id = buf.readInt();
        this.compound = ByteBufUtils.readTag(buf);
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.id);
        ByteBufUtils.writeTag(buf, this.compound);
    }

    public static class ClientLivingBaseCapabilityHandler
            implements IMessageHandler<TrackingCapabilityMessage, IMessage> {
        public IMessage onMessage(TrackingCapabilityMessage message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                EntityLivingBase ent = (EntityLivingBase) Minecraft.getMinecraft().world.getEntityByID(message.id);
                if (ent != null) {
                    LivingBaseCapability cap = Common.getCap(ent);
                    if (cap != null) {
                        cap.startTrackDeserializeNBT(message.compound);
                    }
                }
            });
            return null;
        }
    }
}
