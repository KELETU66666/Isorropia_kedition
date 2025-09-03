//
// Decompiled by Procyon v0.5.30
//

package fr.wind_blade.isorropia.common.network;

import fr.wind_blade.isorropia.common.Common;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketToggleClimbToServer implements IMessage, IMessageHandler<PacketToggleClimbToServer, IMessage> {

    private int playerid;
    private int dim;

    public PacketToggleClimbToServer() {}

    public PacketToggleClimbToServer(final EntityPlayer player, final int dim) {
        this.playerid = player.getEntityId();
        this.dim = dim;
    }

    public void toBytes(final ByteBuf buffer) {
        buffer.writeInt(this.playerid);
        buffer.writeInt(this.dim);
    }

    public void fromBytes(final ByteBuf buffer) {
        this.playerid = buffer.readInt();
        this.dim = buffer.readInt();
    }

    public IMessage onMessage(final PacketToggleClimbToServer message, final MessageContext ctx) {
        if (!Common.selfInfusionSecurityCheck(ctx, "toggle spider climb", message.playerid, 9)) {
            return null;
        }
        final World world = DimensionManager.getWorld(message.dim);
        final EntityPlayer player = (EntityPlayer) world.getEntityByID(message.playerid);
        Common.getCap(player).toggleClimb = !Common.getCap(player).toggleClimb;
        return null;
    }
}