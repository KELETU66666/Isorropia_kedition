package fr.wind_blade.isorropia.common.capabilities;

import fr.wind_blade.isorropia.common.Common;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import java.util.UUID;
import java.util.concurrent.Callable;

public class LivingCapability extends LivingBaseCapability {
    public int envy = 0;
    public boolean hasLooted;
    public UUID uuidOwner;

    public LivingCapability(EntityLiving living) {
        super(living);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = super.serializeNBT();
        if (this.envy != 0) {
            nbt.setInteger("envy", this.envy);
        }
        if (this.hasLooted) {
            nbt.setBoolean("hasLooted", this.hasLooted);
        }
        if (this.uuidOwner != null) {
            nbt.setUniqueId("uuidOwner", this.uuidOwner);
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        super.deserializeNBT(nbt);
        this.envy = nbt.getInteger("envy");
        this.hasLooted = nbt.getBoolean("hasLooted");
        this.uuidOwner = nbt.getUniqueId("uuidOwner");
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == Common.LIVING_CAPABILITY;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return (T)(this.hasCapability(capability, facing) ? this : null);
    }

    public static class Factory
            implements Callable<LivingCapability> {
        @Override
        public LivingCapability call() throws Exception {
            return null;
        }
    }

    public static class Storage
            implements Capability.IStorage<LivingCapability> {
        public NBTBase writeNBT(Capability<LivingCapability> capability, LivingCapability instance, EnumFacing side) {
            return null;
        }

        public void readNBT(Capability<LivingCapability> capability, LivingCapability instance, EnumFacing side, NBTBase nbt) {
        }
    }
}
