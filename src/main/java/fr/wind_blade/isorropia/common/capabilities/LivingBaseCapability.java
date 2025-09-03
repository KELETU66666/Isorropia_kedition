package fr.wind_blade.isorropia.common.capabilities;

import fr.wind_blade.isorropia.common.Common;
import fr.wind_blade.isorropia.common.network.CapabilityMessage;
import fr.wind_blade.isorropia.common.network.PacketSyncCapability;
import fr.wind_blade.isorropia.common.network.TrackingCapabilityMessage;
import fr.wind_blade.isorropia.common.research.recipes.OrganCurativeInfusionRecipe;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class LivingBaseCapability implements ICapabilityProvider, INBTSerializable<NBTTagCompound> {
    public EntityLivingBase entity = null;
    public HashMap<ResourceLocation, NBTTagCompound> infusions = new HashMap();
    public int[] playerInfusions = new int[12];
    public int petrification = 0;
    public int tumorWarp;
    public int tumorWarpTemp;
    public int tumorWarpPermanent;
    public boolean toggleClimb;
    public boolean toggleInvisible;

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
        NBTTagCompound playerInfusions = new NBTTagCompound();
        int i = 0;
        nbt.setInteger("petrification", this.petrification);
        for (Map.Entry<ResourceLocation, NBTTagCompound> set : this.infusions.entrySet()) {
            infusions.setString(Integer.toString(i++), set.getKey().toString());
            infusions.setTag(set.getKey().toString(), set.getValue());
        }
        nbt.setTag("infusions", infusions);
        nbt.setInteger("infusions_count", i);

        if (entity instanceof EntityPlayer) {
            nbt.setIntArray("PlayerInfusions", this.playerInfusions);
            nbt.setInteger("tumorWarp", this.tumorWarp);
            nbt.setInteger("tumorWarpTemp", this.tumorWarpTemp);
            nbt.setInteger("tumorWarpPermanent", this.tumorWarpPermanent);
            nbt.setBoolean("toggleClimb", this.toggleClimb);
            nbt.setBoolean("toggleInvisible", this.toggleInvisible);
        }
        return nbt;
    }

    public void deserializeNBT(NBTTagCompound nbt) {
        NBTTagCompound infusions = nbt.getCompoundTag("infusions");
        int count = nbt.getInteger("infusions_count");
        this.petrification = nbt.getInteger("petrification");
        for (int i = 0; i < count; ++i) {
            String location = infusions.getString(Integer.toString(i));
            this.infusions.put(new ResourceLocation(location), (NBTTagCompound) infusions.getTag(location));
        }
        if (entity instanceof EntityPlayer) {
            this.playerInfusions = nbt.getIntArray("PlayerInfusions");
            if (this.playerInfusions.length == 0) {
                this.playerInfusions = new int[12];
            } else if (this.playerInfusions.length < 12) {
                this.convertPlayerInfusions();
            }
            this.tumorWarp = nbt.getInteger("tumorWarp");
            this.tumorWarpTemp = nbt.getInteger("tumorWarpTemp");
            this.tumorWarpPermanent = nbt.getInteger("tumorWarpPermanent");
            this.toggleClimb = nbt.getBoolean("toggleClimb");
            this.toggleInvisible = nbt.getBoolean("toggleInvisible");
        }
    }

    void convertPlayerInfusions() {
        final int[] newArray = new int[12];
        System.arraycopy(this.playerInfusions, 0, newArray, 0, this.playerInfusions.length);
        this.playerInfusions = newArray;
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
        return (T) (this.hasCapability(capability, facing) ? this : null);
    }

    public LivingBaseCapability petrificate(int petrification) {
        this.petrification = Math.min(this.petrification + petrification, 100);
        return this;
    }

    public void addPlayerInfusion(final int id) {
        if (id != 0) {
            for (int i = 0; i < this.playerInfusions.length; ++i) {
                if (this.playerInfusions[i] == 0) {
                    this.playerInfusions[i] = id;
                    break;
                }
            }
        }
    }

    public int[] getPlayerInfusions() {
        return this.playerInfusions;
    }

    public boolean hasPlayerInfusion(final int id) {
        for (int playerInfusion : this.playerInfusions) {
            if (playerInfusion == id) {
                return true;
            }
        }
        return false;
    }

    public void resetPlayerInfusions() {
        this.playerInfusions = new int[12];
        this.tumorWarpPermanent = 0;
        this.tumorWarp = 0;
        this.tumorWarpTemp = 0;
    }

    public void syncLivingBaseCapability(EntityPlayer player) {
        if (player instanceof EntityPlayerMP) {
            LivingBaseCapability capability = player.getCapability(Common.LIVING_BASE_CAPABILITY, null);
            if (capability != null) {
                NBTTagCompound data = capability.serializeNBT();
                Common.INSTANCE.sendTo(new PacketSyncCapability(data, 0), (EntityPlayerMP) player);
            }
        }
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
