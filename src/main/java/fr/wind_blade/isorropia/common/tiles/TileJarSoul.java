// Decompiled with: CFR 0.152
// Class Version: 8
package fr.wind_blade.isorropia.common.tiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import thaumcraft.common.tiles.TileThaumcraft;

public class TileJarSoul
        extends TileThaumcraft
        implements ITickable {
    public NBTTagCompound entityData = null;
    public Entity entity = null;

    public void update() {
        if (this.entity == null && this.entityData != null && !this.entityData.hasKey("ENTITY_DATA")) {
            this.entity = EntityList.createEntityFromNBT((NBTTagCompound)this.entityData.getCompoundTag("ENTITY_DATA"), (World)this.world);
            if (!this.world.isRemote) {
                this.syncTile(false);
            }
        }
    }

    public void readSyncNBT(NBTTagCompound nbt) {
        this.entityData = nbt.getCompoundTag("entityData");
    }

    public NBTTagCompound writeSyncNBT(NBTTagCompound nbt) {
        if (this.entityData != null) {
            nbt.setTag("entityData", this.entityData);
        }
        return nbt;
    }
}
