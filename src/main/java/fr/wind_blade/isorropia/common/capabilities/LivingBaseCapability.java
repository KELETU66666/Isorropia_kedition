package fr.wind_blade.isorropia.common.capabilities;

import fr.wind_blade.isorropia.common.Common;
import fr.wind_blade.isorropia.common.network.CapabilityMessage;
import fr.wind_blade.isorropia.common.network.TrackingCapabilityMessage;
import fr.wind_blade.isorropia.common.research.recipes.OrganCurativeInfusionRecipe;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

public class LivingBaseCapability implements ICapabilityProvider, INBTSerializable<NBTTagCompound> {
    public EntityLivingBase entity = null;
    public HashMap<ResourceLocation, NBTTagCompound> infusions = new HashMap();
    public int petrification = 0;

    public LivingBaseCapability(EntityLivingBase base) {
        this.entity = base;
    }

    public void update() {
    }

    public boolean entityHasOrgan(OrganCurativeInfusionRecipe.Organ organ, String name) {
        NBTTagCompound nbt = this.infusions.get(new ResourceLocation("isorropia:organs"));
        return nbt != null && nbt.hasKey(organ.registryName.toString()) && nbt.getString(organ.registryName.toString()).equals(name);
    }

    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagCompound infusions = new NBTTagCompound();
        int i = 0;
        nbt.setInteger("petrification", this.petrification);
        for (Map.Entry<ResourceLocation, NBTTagCompound> set : this.infusions.entrySet()) {
            infusions.setString(Integer.toString(i++), set.getKey().toString());
            infusions.setTag(set.getKey().toString(), set.getValue());
        }
        nbt.setTag("infusions", infusions);
        nbt.setInteger("infusions_count", i);
        return nbt;
    }

    public void deserializeNBT(NBTTagCompound nbt) {
        NBTTagCompound infusions = nbt.getCompoundTag("infusions");
        int count = nbt.getInteger("infusions_count");
        this.petrification = nbt.getInteger("petrification");
        for (int i = 0; i < count; ++i) {
            String location = infusions.getString(Integer.toString(i));
            this.infusions.put(new ResourceLocation(location), (NBTTagCompound)infusions.getTag(location));
        }
    }

    public NBTTagCompound startTrackSerializeNBT() {
        return this.serializeNBT();
    }

    public void startTrackDeserializeNBT(NBTTagCompound nbt) {
        this.deserializeNBT(nbt);
    }

    public void synch(EntityPlayerMP player) {
        Common.INSTANCE.sendTo(new CapabilityMessage(this.entity, this.serializeNBT()), player);
    }

    public void synchStartTracking(EntityPlayerMP playerIn) {
        Common.INSTANCE.sendTo(new TrackingCapabilityMessage(this.entity, this.startTrackSerializeNBT()), playerIn);
    }

    public void synchToAllTracking() {
        Common.INSTANCE.sendToAllTracking(new CapabilityMessage(this.entity), this.entity);
    }

    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == Common.LIVING_BASE_CAPABILITY;
    }

    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return (T)(this.hasCapability(capability, facing) ? this : null);
    }

    public LivingBaseCapability petrificate(int petrification) {
        this.petrification = Math.min(this.petrification + petrification, 100);
        return this;
    }

    public static class Factory
            implements Callable<LivingBaseCapability> {
        @Override
        public LivingBaseCapability call() {
            return null;
        }
    }

    public static class Storage
            implements Capability.IStorage<LivingBaseCapability> {
        public NBTBase writeNBT(Capability<LivingBaseCapability> capability, LivingBaseCapability instance, EnumFacing side) {
            return null;
        }

        public void readNBT(Capability<LivingBaseCapability> capability, LivingBaseCapability instance, EnumFacing side, NBTBase nbt) {
        }
    }
}
