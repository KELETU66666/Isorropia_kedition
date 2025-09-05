package fr.wind_blade.isorropia.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import thaumcraft.common.tiles.crafting.TileArcaneWorkbench;
import thaumcraft.common.world.aura.AuraHandler;


public class TileFinger extends TileArcaneWorkbench implements IInventory {

    public EntityPlayer player;

    public TileFinger(EntityPlayer player) {
        this.player = player;
    }

    @Override
    public void getAura() {
        if (!player.world.isRemote) {
            this.auraVisServer = (int) AuraHandler.getVis(player.world, player.getPosition());
        }
    }

    @Override
    public void spendAura(int vis) {
        if (!player.world.isRemote) {
            AuraHandler.drainVis(player.world, player.getPosition(), (float) vis, false);
        }
    }

    @Override
    public int getSizeInventory() {
        return this.inventoryCraft.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(final int par1) {
        return (par1 >= this.getSizeInventory()) ? ItemStack.EMPTY : this.inventoryCraft.getStackInSlot(par1);
    }

    @Override
    public ItemStack removeStackFromSlot(final int par1) {
        // Makes sure we don't drop the output slot
        if (par1 != 9 && this.inventoryCraft.getStackInSlot(par1) != ItemStack.EMPTY) {
            final ItemStack var2 = this.inventoryCraft.getStackInSlot(par1);
            this.inventoryCraft.setInventorySlotContents(par1, ItemStack.EMPTY);
            this.markDirty();
            return var2;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack decrStackSize(final int par1, final int par2) {
        if (this.inventoryCraft.getStackInSlot(par1) == ItemStack.EMPTY) {
            return ItemStack.EMPTY;
        }
        if (this.inventoryCraft.getStackInSlot(par1).getCount() <= par2) {
            final ItemStack var3 = this.inventoryCraft.getStackInSlot(par1);
            this.inventoryCraft.setInventorySlotContents(par1, ItemStack.EMPTY);
            if (this.inventoryCraft.eventHandler != null) {
                this.inventoryCraft.eventHandler.onCraftMatrixChanged(this);
            }
            this.markDirty();
            return var3;
        }
        final ItemStack var3 = this.inventoryCraft.getStackInSlot(par1).splitStack(par2);
        if (this.inventoryCraft.getStackInSlot(par1).getCount() == 0) {
            this.inventoryCraft.setInventorySlotContents(par1, ItemStack.EMPTY);
        }
        if (this.inventoryCraft.eventHandler != null) {
            this.inventoryCraft.eventHandler.onCraftMatrixChanged(this);
        }
        this.markDirty();
        return var3;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        this.inventoryCraft.eventHandler.onCraftMatrixChanged(this);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void openInventory(EntityPlayer var1) {
    }


    @Override
    public void closeInventory(EntityPlayer var1) {
    }

    //TODO
    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        return false;
    }


    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
    }

    @Override
    public boolean isEmpty() {
        return this.inventoryCraft.isEmpty();
    }

    @Override
    public void markDirty() {
    }


    @Override
    public boolean isUsableByPlayer(final EntityPlayer p_70300_1_) {
        return true;
    }
}