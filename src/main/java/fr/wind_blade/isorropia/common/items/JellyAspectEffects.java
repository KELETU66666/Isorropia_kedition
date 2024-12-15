package fr.wind_blade.isorropia.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;


public class JellyAspectEffects {
    public static class DEFAULT_EFFECT
            implements IJellyAspectEffectProvider {
    }

    public static class MOTION_EFFECT
            implements IJellyAspectEffectProvider {
        public void onFoodEeaten(EntityPlayer player, ItemStack stack) {
            player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 600, 2, false, false));
        }
    }

    public static class LIFE_EFFECT
            implements IJellyAspectEffectProvider {
        public void onFoodEeaten(EntityPlayer player, ItemStack stack) {
            player.heal(3.0F);
        }
    }

    public static class EXCHANGE_EFFECT
            implements IJellyAspectEffectProvider {
        public void onFoodEeaten(EntityPlayer player, ItemStack stack) {
            player.attackEntityFrom(DamageSource.MAGIC, 3.0F);
        }


        public int getHungerReplinish(ItemStack stack) {
            return 7;
        }
    }

    public static class PROTECT_EFFECT
            implements IJellyAspectEffectProvider {
        public void onFoodEeaten(EntityPlayer player, ItemStack stack) {
            player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 200, 2, false, false));
        }
    }
}