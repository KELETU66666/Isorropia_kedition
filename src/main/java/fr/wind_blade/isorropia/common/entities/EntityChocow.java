package fr.wind_blade.isorropia.common.entities;

import fr.wind_blade.isorropia.common.items.ItemsIS;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class EntityChocow extends EntityCow {
    public EntityChocow(World world) {
        super(world);
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        if (itemstack.getItem() == Items.BUCKET && !player.capabilities.isCreativeMode && !this.isChild())
        {
            player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
            itemstack.shrink(1);

            if (itemstack.isEmpty())
            {
                player.setHeldItem(hand, new ItemStack(ItemsIS.itemChocolateBucket));
            }
            else if (!player.inventory.addItemStackToInventory(new ItemStack(ItemsIS.itemChocolateBucket)))
            {
                player.dropItem(new ItemStack(ItemsIS.itemChocolateBucket), false);
            }

            return true;
        }
        else
        {
            return super.processInteract(player, hand);
        }
    }

}