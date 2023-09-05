 package fr.wind_blade.isorropia.common.capabilities;
 
 import fr.wind_blade.isorropia.common.Common;
 import java.util.UUID;
 import java.util.concurrent.Callable;
 import net.minecraft.entity.EntityLiving;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.nbt.NBTBase;
 import net.minecraft.nbt.NBTTagCompound;
 import net.minecraft.util.EnumFacing;
 import net.minecraftforge.common.capabilities.Capability;
 
 
 
 
 
 public class LivingCapability
   extends LivingBaseCapability
 {
/* 20 */   public int envy = 0;
   
   public boolean hasLooted;
   
   public UUID uuidOwner;
 
   
   public LivingCapability(EntityLiving living) {
      super((EntityLivingBase)living);
   }
 
   
   public NBTTagCompound serializeNBT() {
/* 33 */     NBTTagCompound nbt = super.serializeNBT();
/* 34 */     if (this.envy != 0)
/* 35 */       nbt.setInteger("envy", this.envy); 
/* 36 */     if (this.hasLooted)
/* 37 */       nbt.setBoolean("hasLooted", this.hasLooted); 
/* 38 */     if (this.uuidOwner != null)
/* 39 */       nbt.setUniqueId("uuidOwner", this.uuidOwner); 
/* 40 */     return nbt;
   }
 
   
   public void deserializeNBT(NBTTagCompound nbt) {
/* 45 */     super.deserializeNBT(nbt);
/* 46 */     this.envy = nbt.getInteger("envy");
/* 47 */     this.hasLooted = nbt.getBoolean("hasLooted");
/* 48 */     this.uuidOwner = nbt.getUniqueId("uuidOwner");
   }
 
   
   public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
/* 53 */     return (capability == Common.LIVING_CAPABILITY);
   }
 
   
   public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
/* 58 */     return hasCapability(capability, facing) ? (T)this : null;
   }
   
   public static class Storage
     implements Capability.IStorage<LivingCapability> {
     public NBTBase writeNBT(Capability<LivingCapability> capability, LivingCapability instance, EnumFacing side) {
/* 64 */       return null;
     }
 
 
     
     public void readNBT(Capability<LivingCapability> capability, LivingCapability instance, EnumFacing side, NBTBase nbt) {}
   }
 
 
   
   public static class Factory
     implements Callable<LivingCapability>
   {
     public LivingCapability call() throws Exception {
/* 78 */       return null;
     }
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\capabilities\LivingCapability.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */