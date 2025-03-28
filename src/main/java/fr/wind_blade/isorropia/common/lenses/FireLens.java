package fr.wind_blade.isorropia.common.lenses;

import fr.wind_blade.isorropia.common.items.misc.ItemLens;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class FireLens extends Lens {
    public FireLens(ItemLens lensIn) {
        super(lensIn);
    }

    public void handleTicks(World worldIn, EntityPlayer playerIn, boolean doubleLens) {
    }

    public void handleRenderGameOverlay(World worldIn, EntityPlayer player, ScaledResolution resolution, boolean doubleLens, float partialTicks) {
        if (worldIn.isRemote) {
            Minecraft mc = Minecraft.getMinecraft();
            if (!mc.player.isPotionActive(Potion.getPotionById(16)) || mc.player
                    .getActivePotionEffect(Potion.getPotionById(16)).getDuration() < 242) {
                mc.player.addPotionEffect(new PotionEffect(Potion.getPotionById(16), 255, 0, false, false));
            }
        }
    }

    public void handleRenderWorldLast(World worldIn, EntityPlayer playerIn, boolean doubleLens, float partialTicks) {
    }

    public void handleRemoval(World worldIn, EntityPlayer playerIn) {
        playerIn.removePotionEffect(Potion.getPotionById(16));
    }
}