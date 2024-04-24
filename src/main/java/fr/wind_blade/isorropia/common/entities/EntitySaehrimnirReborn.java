package fr.wind_blade.isorropia.common.entities;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;


public class EntitySaehrimnirReborn
        extends Entity {
    public EntitySaehrimnirReborn(World worldIn) {
        super(worldIn);
    }


    public boolean attackEntityFrom(DamageSource source, float amount) {
        return super.attackEntityFrom(source, amount);
    }


    public void onEntityUpdate() {
        super.onEntityUpdate();
        if (this.ticksExisted >= 24000 && !this.isDead && !this.world.isRemote) {
            EntitySaehrimnir entity = new EntitySaehrimnir(this.world);
            entity.setPositionAndUpdate(this.posX, this.posY, this.posZ);
            this.world.spawnEntity((Entity) entity);
            setDead();
        }
    }


    public void readEntityFromNBT(NBTTagCompound compound) {
        /* 36 */
        this.ticksExisted = compound.getShort("ticksExisted");
    }


    public void writeEntityToNBT(NBTTagCompound compound) {
        compound.setShort("ticksExisted", (short) this.ticksExisted);
    }

    protected void entityInit() {
    }
}