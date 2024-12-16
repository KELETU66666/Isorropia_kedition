package fr.wind_blade.isorropia.common.items.misc;

import fr.wind_blade.isorropia.common.entities.projectile.EntityIncubatedEgg;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemIncubatedEgg
        extends ItemEgg {
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (!playerIn.capabilities.isCreativeMode) {
            stack.shrink(1);
        }
        worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_EGG_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (itemRand.nextFloat() * 0.4f + 0.8f));
        if (!worldIn.isRemote) {
            EntityIncubatedEgg entityegg = new EntityIncubatedEgg(worldIn, playerIn);
            entityegg.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0f, 1.5f, 1.0f);
            worldIn.spawnEntity(entityegg);
            playerIn.addStat(StatList.getObjectUseStats(this));
        }
        return new ActionResult(EnumActionResult.SUCCESS, stack);
    }
}
