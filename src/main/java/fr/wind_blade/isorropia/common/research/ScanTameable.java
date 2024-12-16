package fr.wind_blade.isorropia.common.research;

import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.research.IScanThing;

import java.util.UUID;

public class ScanTameable implements IScanThing {
    public boolean checkThing(EntityPlayer var1, Object obj) {
        if (!(obj instanceof EntityTameable) ||
                !ThaumcraftCapabilities.getKnowledge(var1).isResearchKnown("INSTILLEDFIDELITY")) {
            return false;
        }
        EntityTameable tame = (EntityTameable)obj;

        if (tame.isTamed() && ScanFidelity.memory.containsKey(var1.getUniqueID()) && ((UUID)ScanFidelity.memory
                .get(var1.getUniqueID())).equals(tame.getUniqueID()))
            return true;
        return false;
    }


    public void onSuccess(EntityPlayer player, Object object) {
        player.sendMessage(new TextComponentString(TextFormatting.DARK_PURPLE + (new TextComponentTranslation("scan.pretame", new Object[0]))
                .getFormattedText()));
        ScanFidelity.memory.remove(player.getUniqueID());
    }


    public String getResearchKey(EntityPlayer var1, Object var2) {
        return "!scan.fidelity";
    }
}