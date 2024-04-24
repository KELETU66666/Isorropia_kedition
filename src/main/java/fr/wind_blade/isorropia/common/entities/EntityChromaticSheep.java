package fr.wind_blade.isorropia.common.entities;

import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.world.World;

public class EntityChromaticSheep extends EntitySheep {
    public EntityChromaticSheep(World world) {
        super(world);
    }


    public void onLivingUpdate() {
        if (!this.world.isRemote && this.ticksExisted % 30 == 0) {
            EnumDyeColor color = getFleeceColor();
            if (color.getMetadata() >= 15) {
                color = EnumDyeColor.byMetadata(0);
            } else {
                color = EnumDyeColor.byMetadata(color.getMetadata() + 1);
            }
            setFleeceColor(color);
        }
        super.onLivingUpdate();
    }
}