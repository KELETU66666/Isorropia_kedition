 package fr.wind_blade.isorropia.common.capabilities;
 
 import fr.wind_blade.isorropia.common.Common;
 import fr.wind_blade.isorropia.common.network.CapabilityMessage;
 import fr.wind_blade.isorropia.common.network.TrackingCapabilityMessage;
 import fr.wind_blade.isorropia.common.research.recipes.OrganCurativeInfusionRecipe;
 import java.util.HashMap;
 import java.util.Map;
 import java.util.concurrent.Callable;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.entity.player.EntityPlayerMP;
 import net.minecraft.nbt.NBTBase;
 import net.minecraft.nbt.NBTTagCompound;
 import net.minecraft.util.EnumFacing;
 import net.minecraft.util.ResourceLocation;
 import net.minecraftforge.common.capabilities.Capability;
 import net.minecraftforge.common.capabilities.ICapabilityProvider;
 import net.minecraftforge.common.util.INBTSerializable;
 import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
 
 public class LivingBaseCapability implements ICapabilityProvider, INBTSerializable<NBTTagCompound> {
/*  23 */   public EntityLivingBase entity = null;
   
/*  25 */   public HashMap<ResourceLocation, NBTTagCompound> infusions = new HashMap<>();
 
 
 
 
   
/*  31 */   public int petrification = 0;
   
   public LivingBaseCapability(EntityLivingBase base) {
/*  34 */     this.entity = base;
   }
 
 
 
 
 
 
   
   public void update() {}
 
 
 
 
 
 
   
   public boolean entityHasOrgan(OrganCurativeInfusionRecipe.Organ organ, String name) {
/*  52 */     NBTTagCompound nbt = this.infusions.get(new ResourceLocation("isorropia:organs"));
     
/*  54 */     return (nbt != null && nbt.hasKey(organ.registryName.toString()) && nbt.getString(organ.registryName.toString()).equals(name));
   }
 
   
   public NBTTagCompound serializeNBT() {
/*  59 */     NBTTagCompound nbt = new NBTTagCompound();
/*  60 */     NBTTagCompound infusions = new NBTTagCompound();
/*  61 */     int i = 0;
     
/*  63 */     nbt.setInteger("petrification", this.petrification);
/*  64 */     for (Map.Entry<ResourceLocation, NBTTagCompound> set : this.infusions.entrySet()) {
/*  65 */       infusions.setString(Integer.toString(i++), ((ResourceLocation)set.getKey()).toString());
/*  66 */       infusions.setTag(((ResourceLocation)set.getKey()).toString(), (NBTBase)set.getValue());
     } 
/*  68 */     nbt.setTag("infusions", (NBTBase)infusions);
/*  69 */     nbt.setInteger("infusions_count", i);
/*  70 */     return nbt;
   }
 
   
   public void deserializeNBT(NBTTagCompound nbt) {
/*  75 */     NBTTagCompound infusions = nbt.getCompoundTag("infusions");
/*  76 */     int count = nbt.getInteger("infusions_count");
     
/*  78 */     this.petrification = nbt.getInteger("petrification");
/*  79 */     for (int i = 0; i < count; i++) {
/*  80 */       String location = infusions.getString(Integer.toString(i));
/*  81 */       this.infusions.put(new ResourceLocation(location), (NBTTagCompound)infusions.getTag(location));
     } 
   }
   
   public NBTTagCompound startTrackSerializeNBT() {
/*  86 */     return serializeNBT();
   }
   
   public void startTrackDeserializeNBT(NBTTagCompound nbt) {
/*  90 */     deserializeNBT(nbt);
   }
   
   public void synch(EntityPlayerMP player) {
/*  94 */     Common.INSTANCE.sendTo((IMessage)new CapabilityMessage(this.entity, serializeNBT()), player);
   }
   
   public void synchStartTracking(EntityPlayerMP playerIn) {
     Common.INSTANCE.sendTo((IMessage)new TrackingCapabilityMessage(this.entity, startTrackSerializeNBT()), playerIn);
   }
   
   public void synchToAllTracking() {
/* 102 */     Common.INSTANCE.sendToAllTracking((IMessage)new CapabilityMessage(this.entity), (Entity)this.entity);
   }
 
   
   public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
/* 107 */     return (capability == Common.LIVING_BASE_CAPABILITY);
   }
 
   
   public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
/* 112 */     return hasCapability(capability, facing) ? (T)this : null;
   }
 
   
   public static class Storage
     implements Capability.IStorage<LivingBaseCapability>
   {
     public NBTBase writeNBT(Capability<LivingBaseCapability> capability, LivingBaseCapability instance, EnumFacing side) {
/* 120 */       return null;
     }
 
 
     
     public void readNBT(Capability<LivingBaseCapability> capability, LivingBaseCapability instance, EnumFacing side, NBTBase nbt) {}
   }
 
 
   
   public static class Factory
     implements Callable<LivingBaseCapability>
   {
     public LivingBaseCapability call() throws Exception {
/* 134 */       return null;
     }
   }
   
   public LivingBaseCapability petrificate(int petrification) {
/* 139 */     this.petrification = Math.min(this.petrification + petrification, 100);
/* 140 */     return this;
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\capabilities\LivingBaseCapability.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */