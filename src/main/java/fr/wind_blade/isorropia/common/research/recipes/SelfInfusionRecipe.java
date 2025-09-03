//
// Decompiled by Procyon v0.5.30
//

package fr.wind_blade.isorropia.common.research.recipes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftInvHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;

import java.util.ArrayList;

public class SelfInfusionRecipe {

    protected AspectList aspects;
    protected String research;
    private final ItemStack[] components;
    protected int instability;
    protected int id;

    public SelfInfusionRecipe(final String research, final int inst, final AspectList aspects2, final ItemStack[] recipe, final int id) {
        this.research = research;
        this.instability = inst;
        this.aspects = aspects2.copy();
        this.components = recipe;
        this.id = id;
    }

    public boolean matches(final ArrayList<ItemStack> input, final World world, final EntityPlayer player) {
        if (this.research.length() > 0 && !ThaumcraftCapabilities.knowsResearch(player, this.research)) {
            return false;
        }
        final ArrayList<ItemStack> ii = new ArrayList<>();
        for (final ItemStack is : input) {
            ii.add(is.copy());
        }
        for (final ItemStack comp : this.getComponents()) {
            boolean b = false;
            for (int a = 0; a < ii.size(); ++a) {
                final ItemStack i2 = ii.get(a).copy();
                if (comp.getItemDamage() == 32767) {
                    i2.setItemDamage(32767);
                }
                if (areItemStacksEqual(i2, comp, true)) {
                    ii.remove(a);
                    b = true;
                    break;
                }
            }
            if (!b) {
                return false;
            }
        }
        return ii.size() == 0;
    }

    public static boolean areItemStacksEqual(ItemStack stack0, ItemStack stack1, boolean fuzzy) {
        int[] od;
        if (stack0.isEmpty() && stack1.isEmpty()) {
            return true;
        }
        if (stack0.isEmpty() || stack1.isEmpty()) {
            return false;
        }
        boolean t1 = ThaumcraftInvHelper.areItemStackTagsEqualForCrafting(stack0, stack1);
        if (!t1) {
            return false;
        }
        if (fuzzy && (od = OreDictionary.getOreIDs(stack0)).length > 0) {
            ArrayList ores = new ArrayList();
            ArrayList<String> oresName = new ArrayList<String>();
            for (int id : od) {
                oresName.add(OreDictionary.getOreName(id));
            }
            if (ThaumcraftInvHelper.containsMatch(false, new ItemStack[]{stack1}, ores)) {
                return true;
            }
        }
        boolean damage = stack0.getItemDamage() == stack1.getItemDamage() || stack1.getItemDamage() == Short.MAX_VALUE;
        return stack0.getItem() == stack1.getItem() && damage && stack0.getCount() <= stack0.getMaxStackSize();
    }

    public AspectList getAspects() {
        return this.aspects;
    }

    public int getInstability() {
        return this.instability;
    }

    public String getResearch() {
        return this.research;
    }

    public ItemStack[] getComponents() {
        return this.components;
    }

    public int getID() {
        return this.id;
    }
}