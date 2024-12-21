package fr.wind_blade.isorropia.common.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.casters.IInteractWithCaster;
import thaumcraft.api.items.IGogglesDisplayExtended;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class TileModifiedMatrix extends TileEntity implements IInteractWithCaster, IAspectContainer, IGogglesDisplayExtended {
    static DecimalFormat myFormatter = new DecimalFormat("#######.##");
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    public AspectList getAspects() {
        TileVat vat = this.getMaster();
        return vat == null ? null : vat.getAspects();
    }

    public void setAspects(AspectList paramAspectList) {
    }

    public boolean doesContainerAccept(Aspect paramAspect) {
        return false;
    }

    public int addToContainer(Aspect paramAspect, int paramInt) {
        return 0;
    }

    public boolean takeFromContainer(Aspect paramAspect, int paramInt) {
        return false;
    }

    public boolean takeFromContainer(AspectList paramAspectList) {
        return false;
    }

    public boolean doesContainerContainAmount(Aspect paramAspect, int paramInt) {
        return false;
    }

    public boolean doesContainerContain(AspectList paramAspectList) {
        return false;
    }

    public int containerContains(Aspect paramAspect) {
        return 0;
    }

    public boolean onCasterRightClick(World paramWorld, ItemStack paramItemStack, EntityPlayer paramEntityPlayer, BlockPos paramBlockPos, EnumFacing paramEnumFacing, EnumHand paramEnumHand) {
        TileVat vat = this.getMaster();
        if (vat != null) {
            vat.onCasterRightClick(paramWorld, paramItemStack, paramEntityPlayer, paramBlockPos, paramEnumFacing, paramEnumHand);
            return true;
        }
        return false;
    }

    public TileVat getMaster() {
        TileEntity te = this.world.getTileEntity(this.getPos().down());
        return te instanceof TileVat ? (TileVat)te : null;
    }

    public String[] getIGogglesText() {
        TileVat te = this.getMaster();
        if (te != null) {
            float lpc;
            ArrayList<String> strings = new ArrayList<String>();
            strings.add(TextFormatting.BOLD + I18n.translateToLocal("stability." + te.getStability().name()));
            if (te.getFluxStocked() > 0.0f) {
                strings.add(TextFormatting.DARK_PURPLE + "Flux : " + te.getFluxStocked());
            }
            if (te.getCelestialAura() > 0.0f || te.getCelestialAuraNeeded() > 0.0f) {
                strings.add(TextFormatting.fromColorIndex(new Color(0.49019608f, 0.5568628f, 0.63529414f).getRGB()) + "Celestia Aura " + te.getCelestialAura() + "/" + te.getCelestialAuraNeeded());
            }
            if ((lpc = te.getLossPerCycle()) != 0.0f) {
                strings.add(String.valueOf(TextFormatting.GOLD) + TextFormatting.ITALIC + myFormatter.format(te.getSymmetryStability()) + " " + I18n.translateToLocal("stability.gain"));
                strings.add(TextFormatting.RED + I18n.translateToLocal("stability.range") + TextFormatting.ITALIC + myFormatter.format(lpc) + " " + I18n.translateToLocal("stability.loss"));
            } else {
                strings.add(String.valueOf(TextFormatting.GOLD) + TextFormatting.ITALIC + myFormatter.format(te.getSymmetryStability()) + " " + I18n.translateToLocal("stability.gain"));
            }
            return strings.toArray(new String[0]);
        }
        return EMPTY_STRING_ARRAY;
    }
}
