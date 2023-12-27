package fr.wind_blade.isorropia.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import thaumcraft.api.aspects.Aspect;




public class MagnetMessage
        implements IMessage
{
    private BlockPos pos;
    private Aspect aspect;
    private int meta = -1;


    private boolean aspectFilter;

    private int area;


    public MagnetMessage(BlockPos pos, Aspect aspectIn, boolean aspectFilter, int area) {
        this.pos = pos;
        this.aspect = aspectIn;
        this.aspectFilter = aspectFilter;
        this.area = area;
    }


    public void fromBytes(ByteBuf buf) {
        this.pos = BlockPos.fromLong(buf.readLong());
        this.meta = buf.readInt();
        this.aspectFilter = buf.readBoolean();
        this.area = buf.readInt();
        String tag = ByteBufUtils.readUTF8String(buf);
        this.aspect = (tag == "") ? null : Aspect.aspects.get(tag);
    }


    public void toBytes(ByteBuf buf) {
        buf.writeLong(this.pos.toLong());
        buf.writeInt(this.meta);
        buf.writeBoolean(this.aspectFilter);
        buf.writeInt(this.area);
        ByteBufUtils.writeUTF8String(buf, (this.aspect == null) ? "" : this.aspect.getTag());
    }

    public MagnetMessage() {}

}