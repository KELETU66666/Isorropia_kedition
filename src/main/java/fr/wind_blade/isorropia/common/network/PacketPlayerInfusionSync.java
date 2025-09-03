//
// Decompiled by Procyon v0.5.30
//

package fr.wind_blade.isorropia.common.network;

import fr.wind_blade.isorropia.common.Common;
import fr.wind_blade.isorropia.common.capabilities.LivingBaseCapability;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;

public class PacketPlayerInfusionSync implements IMessage, IMessageHandler<PacketPlayerInfusionSync, IMessage> {

    int[] infusions;
    String name;
    boolean toggleClimb;
    boolean toggleInvisible;

    public PacketPlayerInfusionSync() {
        this.infusions = new int[12];
        this.name = "";
        this.toggleClimb = false;
        this.toggleInvisible = false;
    }

    public PacketPlayerInfusionSync(final String name, final int[] infusions, final boolean toggleClimb, final boolean toggleInvisible) {
        this.infusions = new int[12];
        this.name = "";
        this.name = name;
        this.infusions = infusions;
        this.toggleClimb = toggleClimb;
        this.toggleInvisible = toggleInvisible;
    }

    public IMessage onMessage(final PacketPlayerInfusionSync message, final MessageContext ctx) {
        EntityPlayer player = Minecraft.getMinecraft().world.getPlayerEntityByName(message.name);
        if (Minecraft.getMinecraft().world != null && player != null) {
            LivingBaseCapability prop = Common.getCap(player);
            if (prop.toggleClimb != message.toggleClimb) {
                prop.toggleClimb = message.toggleClimb;
            }
            if (prop.toggleInvisible != message.toggleInvisible) {
                prop.toggleInvisible = message.toggleInvisible;
                if (prop.toggleInvisible) {
                    player.removePotionEffect(MobEffects.INVISIBILITY);
                    player.setInvisible(false);
                } else {
                    final PotionEffect effect = new PotionEffect(MobEffects.INVISIBILITY, Integer.MAX_VALUE, 0, true, false);
                    effect.setCurativeItems(new ArrayList<>());
                    player.addPotionEffect(effect);
                    player.setInvisible(true);
                }
            }
        }
        return null;
    }

    public void fromBytes(final ByteBuf buf) {
        final int length = buf.readInt();
        final byte[] bites = new byte[length];
        final char[] chars = new char[length];
        buf.readBytes(bites);
        for (int i = 0; i < length; ++i) {
            chars[i] = (char) bites[i];
        }
        this.name = String.copyValueOf(chars);
        for (int i = 0; i < 12; ++i) {
            this.infusions[i] = buf.readInt();
        }
        this.toggleClimb = buf.readBoolean();
        this.toggleInvisible = buf.readBoolean();
    }

    public void toBytes(final ByteBuf buf) {
        buf.writeInt(this.name.length());
        final byte[] bites = new byte[this.name.length()];
        final char[] chars = this.name.toCharArray();
        for (int i = 0; i < this.name.length(); ++i) {
            bites[i] = (byte) chars[i];
        }
        buf.writeBytes(bites);
        for (int i = 0; i < 12; ++i) {
            buf.writeInt(this.infusions[i]);
        }
        buf.writeBoolean(this.toggleClimb);
        buf.writeBoolean(this.toggleInvisible);
    }
}