package fr.wind_blade.isorropia.common.research;

import fr.wind_blade.isorropia.common.celestial.CelestialBody;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.research.IScanThing;

public class ScanSun implements IScanThing {
    public boolean checkThing(EntityPlayer player, Object obj) {
        if (!ThaumcraftCapabilities.getKnowledge(player).isResearchKnown("FIRELENS@0") ||
                CelestialBody.isNight(player.world) || obj != null || player.rotationPitch > 0.0F ||
                !player.world.canSeeSky(player.getPosition().up()) || player.world.provider.getDimensionType() != DimensionType.OVERWORLD) {
            return false;
        }

        int yaw = (int)(player.rotationYaw + 90.0F) % 360;
        int pitch = (int)Math.abs(player.rotationPitch);
        int ca = (int)((player.world.getCelestialAngle(0.0F) + 0.25D) * 360.0D) % 360;
        boolean inRangeYaw = false;
        boolean inRangePitch = false;

        if (ca > 90) {
            inRangeYaw = (Math.abs(Math.abs(yaw) - 180) < 10);
            inRangePitch = (Math.abs(180 - ca - pitch) < 7);
        } else {
            inRangeYaw = (Math.abs(yaw) < 10);
            inRangePitch = (Math.abs(ca - pitch) < 7);
        }

        return (inRangeYaw && inRangePitch);
    }


    public void onSuccess(EntityPlayer player, Object object) {
        player.sendMessage((ITextComponent)new TextComponentString(TextFormatting.DARK_PURPLE + (new TextComponentTranslation("research.scan.sun.text", new Object[0])).getFormattedText()));
    }


    public String getResearchKey(EntityPlayer player, Object object) {
        return "!scan.sun";
    }
}