package fr.wind_blade.isorropia.common.lenses;

import baubles.api.BaublesApi;
import fr.wind_blade.isorropia.common.IsorropiaAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.items.IGoggles;
import thaumcraft.api.items.IRevealer;

public class LensManager {
    @SideOnly(Side.CLIENT)
    public static void putLens(EntityPlayer player, Lens lens, LENSSLOT type) {
        ItemStack revealer = getRevealer(player);


        Lens old = revealer.hasTagCompound() ? IsorropiaAPI.lensRegistry.getValue(new ResourceLocation(revealer.getTagCompound().getString(type.getName()))) : null;


        if (old != null)
            old.handleRemoval(player.world, player);
        setLens(getRevealer((Minecraft.getMinecraft()).player), lens, type);
    }

    public static Lens getLens(ItemStack revealer, LENSSLOT type) {
        if (revealer.hasTagCompound() && revealer.getTagCompound().hasKey(type.getName()))
            return IsorropiaAPI.lensRegistry
                    .getValue(new ResourceLocation(revealer.getTagCompound().getString(type.getName())));
        return null;
    }

    public static void setLens(ItemStack revealer, Lens lens, LENSSLOT type) {
        if (lens == null)
            return;
        if (!revealer.hasTagCompound())
            revealer.setTagCompound(new NBTTagCompound());
        NBTTagCompound compound = revealer.getTagCompound();
        compound.setString(type.getName(), lens.getRegistryName().toString());
        revealer.setTagCompound(compound);
    }

    public static void removeLens(EntityPlayer player, ItemStack revealer, LENSSLOT type) {
        if (revealer.hasTagCompound() && revealer.getTagCompound().hasKey(type.getName())) {
            String oldLens = revealer.getTagCompound().getString(type.getName());
            if (!oldLens.isEmpty()) {
                Lens lens2 = IsorropiaAPI.lensRegistry.getValue(new ResourceLocation(oldLens));
                lens2.handleRemoval(player.world, player);
                if (!player.inventory.addItemStackToInventory(new ItemStack(lens2.getItemLens())))
                    player.dropItem(new ItemStack(lens2.getItemLens()), false);
                revealer.getTagCompound().removeTag(type.getName());
            }
        }
    }

    public static boolean changeLens(EntityPlayer player, Lens lens, LENSSLOT type) {
        ItemStack revealer = getRevealer(player);
        if (!revealer.isEmpty()) {
            removeLens(player, revealer, type);
            if (lens != null)
                setLens(revealer, lens, type);
            return true;
        }
        return false;
    }

    public static ItemStack getGoggles(EntityPlayer player) {
        boolean find = false;
        ItemStack goggles = ItemStack.EMPTY;
        for (int i = 0; i < BaublesApi.getBaublesHandler(player).getSlots(); i++) {
            goggles = BaublesApi.getBaubles(player).getStackInSlot(i);
            if (goggles.getItem() instanceof IGoggles && ((IGoggles)goggles.getItem()).showIngamePopups(goggles, player)) {
                find = true;
                break;
            }
        }
        if (!find) {
            for (ItemStack stack : player.getArmorInventoryList()) {
                if (stack.getItem() instanceof IGoggles && ((IGoggles)stack.getItem()).showIngamePopups(stack, player)) {
                    goggles = stack;
                    break;
                }
            }
        }
        return goggles;
    }

    public static ItemStack getRevealer(EntityPlayer player) {
        if(!getGoggles(player).isEmpty())
            return getGoggles(player);

        boolean find = false;
        ItemStack revealer = ItemStack.EMPTY;
        for (int i = 0; i < BaublesApi.getBaublesHandler(player).getSlots(); i++) {
            revealer = BaublesApi.getBaubles(player).getStackInSlot(i);
            if (revealer.getItem() instanceof IRevealer && ((IRevealer)revealer.getItem()).showNodes(revealer, player)) {
                find = true;
                break;
            }
        }
        if (!find) {
            for (ItemStack stack : player.getArmorInventoryList()) {
                if (stack.getItem() instanceof IRevealer && ((IRevealer)stack.getItem()).showNodes(stack, player)) {
                    revealer = stack;
                    break;
                }
            }
        }
        return revealer;
    }

    public enum LENSSLOT {
        LEFT("LeftLens", 42.0F), RIGHT("RightLens", -42.0F);

        private final float angle;
        private final String type;

        LENSSLOT(String type, float angle) {
            this.type = type;
            this.angle = angle;
        }

        public static LENSSLOT getByString(String name) {
            for (LENSSLOT slot : values()) {
                if (slot.getName().equals(name)) {
                    return slot;
                }
            }

            return LEFT;
        }

        public String getName() {
            return this.type;
        }

        public float getAngle() {
            return this.angle;
        }
    }
}