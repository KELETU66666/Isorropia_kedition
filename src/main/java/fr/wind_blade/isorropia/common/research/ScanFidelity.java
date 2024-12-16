package fr.wind_blade.isorropia.common.research;

import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.research.IScanThing;

import java.util.HashMap;
import java.util.UUID;

public class ScanFidelity
        implements IScanThing {
    public static HashMap<UUID, UUID> memory = new HashMap<>();


    public boolean checkThing(EntityPlayer var1, Object obj) {
        if (!(obj instanceof EntityTameable) ||
                !ThaumcraftCapabilities.getKnowledge(var1).isResearchKnown("INSTILLEDFIDELITY@0") ||
                ThaumcraftCapabilities.getKnowledge(var1).isResearchKnown("!scan.fidelity"))
            return false;
        return !((EntityTameable)obj).isTamed();
    }


    public void onSuccess(EntityPlayer player, Object obj) {
        EntityTameable tame = (EntityTameable)obj;

        memory.put(player.getUniqueID(), tame.getUniqueID());
        player.sendMessage(new TextComponentString(TextFormatting.DARK_PURPLE + (new TextComponentTranslation("scan.fidelity", new Object[0]))
                .getFormattedText()));
        ThaumcraftCapabilities.getKnowledge(player).removeResearch("!scan.pretame");
    }


    public String getResearchKey(EntityPlayer var1, Object var2) {
        return "!scan.pretame";
    }
}