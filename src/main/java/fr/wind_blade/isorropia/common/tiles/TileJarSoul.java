 package fr.wind_blade.isorropia.common.tiles;
 
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.EntityList;
 import net.minecraft.nbt.NBTBase;
 import net.minecraft.nbt.NBTTagCompound;
 import net.minecraft.util.ITickable;
 import thaumcraft.common.tiles.TileThaumcraft;
 
 
 
 public class TileJarSoul
   extends TileThaumcraft
   implements ITickable
 {
/* 16 */   public NBTTagCompound entityData = null;
/* 17 */   public Entity entity = null;
 
   
   public void update() {
/* 21 */     if (this.entity == null && this.entityData != null && !this.entityData.hasKey("ENTITY_DATA")) {
/* 22 */       this.entity = EntityList.createEntityFromNBT(this.entityData.getCompoundTag("ENTITY_DATA"), this.world);
       
/* 24 */       if (!this.world.isRemote) {
         syncTile(false);
       }
     } 
   }
   
   public void readSyncNBT(NBTTagCompound nbt) {
/* 31 */     this.entityData = nbt.getCompoundTag("entityData");
   }
 
   
   public NBTTagCompound writeSyncNBT(NBTTagCompound nbt) {
/* 36 */     if (this.entityData != null)
/* 37 */       nbt.setTag("entityData", (NBTBase)this.entityData); 
/* 38 */     return nbt;
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\tiles\TileJarSoul.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */