package fr.wind_blade.isorropia.common.lenses;

import fr.wind_blade.isorropia.common.items.misc.ItemLens;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class LustLens
        extends Lens
{
    public LustLens(ItemLens lensIn) {
        super(lensIn);
    }

    public void handleTicks(World worldIn, EntityPlayer playerIn, boolean doubleLens) {}

    public void handleRenderGameOverlay(World worldIn, EntityPlayer playerIn, ScaledResolution resolution, boolean doubleLens, float partialTicks) {}

    public void handleRenderWorldLast(World worldIn, EntityPlayer playerIn, boolean doubleLens, float partialTicks) {}

    public void handleRemoval(World worldIn, EntityPlayer playerIn) {}
}