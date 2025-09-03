//
// Decompiled by Procyon v0.5.30
//

package fr.wind_blade.isorropia.common.network;

import fr.wind_blade.isorropia.common.Common;
import fr.wind_blade.isorropia.common.capabilities.LivingBaseCapability;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;

public class PacketToggleInvisibleToServer
        implements IMessage, IMessageHandler<PacketToggleInvisibleToServer, IMessage> {

    private int playerid;
    private int dim;

    public PacketToggleInvisibleToServer() {
    }

    public PacketToggleInvisibleToServer(final EntityPlayer player, final int dim) {
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

    public IMessage onMessage(final PacketToggleInvisibleToServer message, final MessageContext ctx) {
        if (!Common
                .selfInfusionSecurityCheck(ctx, "toggle chamelon skin (i.e. invisible)", message.playerid, 10)) {
            return null;
        }
        final EntityPlayerMP player = ctx.getServerHandler().player;
        LivingBaseCapability ieep = Common.getCap(player);
        ieep.toggleInvisible = !ieep.toggleInvisible;
        if (ieep.toggleInvisible) {
            player.removePotionEffect(MobEffects.INVISIBILITY);
            player.setInvisible(false);
        } else {
            final PotionEffect effect = new PotionEffect(MobEffects.INVISIBILITY, Integer.MAX_VALUE, 0, true, false);
            effect.setCurativeItems(new ArrayList<>());
            player.addPotionEffect(effect);
            player.setInvisible(true);
        }
        return null;
    }
}