package fr.wind_blade.isorropia.common.entities;

import fr.wind_blade.isorropia.common.items.ItemsIS;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;



public class EntityScholarChicken extends EntityChicken
{
    public int timeUntilNextFeather;

    public EntityScholarChicken(World world) {
        super(world);
        this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        this.timeUntilNextFeather = this.rand.nextInt(4000) + 4000;
    }


    public void onLivingUpdate() {
        int notTime = this.timeUntilNextEgg;
        this.timeUntilNextEgg = Integer.MAX_VALUE;
        super.onLivingUpdate();
        this.timeUntilNextEgg = notTime;

        if (this.world.isRemote || isChild() || isChickenJockey()) {
            return;
        }
        if (--this.timeUntilNextEgg <= 0) {
            playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (this.rand
                    .nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            dropItem(ItemsIS.itemInkEgg, 1);
            this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        }

        if (--this.timeUntilNextFeather <= 0) {
            playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (this.rand
                    .nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            dropItem(Items.FEATHER, 1);
            this.timeUntilNextFeather = this.rand.nextInt(4000) + 4000;
        }
    }


    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("egg", this.timeUntilNextEgg);
        compound.setInteger("feather", this.timeUntilNextFeather);
    }


    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.timeUntilNextEgg = (compound.getInteger("egg") != 0) ? compound.getInteger("egg") : this.timeUntilNextEgg;
        this.timeUntilNextFeather = (compound.getInteger("feather") != 0) ? compound.getInteger("feather") : this.timeUntilNextFeather;
    }
}