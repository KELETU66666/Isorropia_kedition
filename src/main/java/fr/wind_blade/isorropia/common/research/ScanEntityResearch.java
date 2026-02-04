package fr.wind_blade.isorropia.common.research;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.research.ScanEntity;

public class ScanEntityResearch
        extends ScanEntity {
    private final String researchRequired;
    private final String message;

    public ScanEntityResearch(String research, Class entityClass, boolean inheritedClasses, String researchRequired, String message) {
        super(research, entityClass, inheritedClasses);
        this.researchRequired = researchRequired;
        this.message = message;
    }


    public ScanEntityResearch(String research, Class entityClass, boolean inheritedClasses, String researchRequired, String message, ThaumcraftApi.EntityTagsNBT... nbt) {
        super(research, entityClass, inheritedClasses, nbt);
        this.researchRequired = researchRequired;
        this.message = message;
    }

    @Override
    public boolean checkThing(EntityPlayer player, Object obj) {
        return player != null && ThaumcraftCapabilities.getKnowledge(player).isResearchKnown(this.researchRequired) && super.checkThing(player, obj);
    }


    public void onSuccess(EntityPlayer player, Object object) {
        player.sendMessage(new TextComponentString(TextFormatting.DARK_PURPLE + (new TextComponentTranslation(this.message))
                .getFormattedText()));
    }
}