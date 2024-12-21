package fr.wind_blade.isorropia.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;


public interface IJellyAspectEffectProvider {
    default void onFoodEeaten(EntityPlayer player, ItemStack stack) {
    }

    default boolean canBeEaten(EntityPlayer player, ItemStack stack) {
        return true;
    }

    default int getEatDuration(ItemStack stack) {
        return 32;
    }

    default int getHungerReplinish(ItemStack stack) {
        return 2;
    }

    default int getSaturationReplinish(ItemStack stack) {
        return getHungerReplinish(stack);
    }
}