package fr.wind_blade.isorropia.common.tiles;

import fr.wind_blade.isorropia.common.blocks.BlockCurativeVat;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaTransport;



public class TileVatConnector
        extends TileEntity
        implements IEssentiaTransport, ITickable
{
    public TileVat getMaster() {
        return BlockCurativeVat.getMaster(this.world, this.pos);
    }


    public int addEssentia(Aspect arg0, int arg1, EnumFacing arg2) {
        TileVat vat = getMaster();
        if (vat != null)
            return vat.addEssentia(arg0, arg1, arg2);
        return 0;
    }


    public boolean canInputFrom(EnumFacing arg0) {
        return true;
    }


    public boolean canOutputTo(EnumFacing arg0) {
        return false;
    }


    public int getEssentiaAmount(EnumFacing arg0) {
        return 0;
    }


    public Aspect getEssentiaType(EnumFacing arg0) {
        return null;
    }


    public int getSuctionAmount(EnumFacing arg0) {
        TileVat vat = getMaster();
        if (vat != null)
            return (vat.getEssentiaNeeded().size() > 0) ? 128 : 0;
        return 0;
    }


    public Aspect getSuctionType(EnumFacing arg0) {
        TileVat vat = getMaster();
        if (vat != null)
            return vat.getSuctionType();
        return null;
    }


    public boolean isConnectable(EnumFacing arg0) {
        return true;
    }


    public int takeEssentia(Aspect arg0, int arg1, EnumFacing arg2) {
        return 0;
    }


    public int getMinimumSuction() {
        return 0;
    }

    public void setSuction(Aspect arg0, int arg1) {}

    public void update() {}
}