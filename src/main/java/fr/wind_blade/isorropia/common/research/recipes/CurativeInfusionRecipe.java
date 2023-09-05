// Decompiled with: CFR 0.152
// Class Version: 8
package fr.wind_blade.isorropia.common.research.recipes;

import fr.wind_blade.isorropia.Isorropia;
import fr.wind_blade.isorropia.common.Common;
import fr.wind_blade.isorropia.common.IsorropiaAPI;
import fr.wind_blade.isorropia.common.capabilities.LivingBaseCapability;
import fr.wind_blade.isorropia.common.celestial.CelestialBody;
import fr.wind_blade.isorropia.common.celestial.ICelestialBody;
import fr.wind_blade.isorropia.common.items.ItemsIS;
import fr.wind_blade.isorropia.common.tiles.TileVat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftInvHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.crafting.InfusionRecipe;

public class CurativeInfusionRecipe
        extends InfusionRecipe {
    protected final Predicate<EntityLivingBase> predicate;
    protected final ICelestialBody celestialBody;
    protected final int celestialAura;
    protected final float vis;
    protected final float fluxRejection;
    protected final NBTTagCompound informationNBT;
    protected final NonNullList<Ingredient> optionalComponents;
    protected final HashMap<String, AttributeModifier> modifiers;
    protected final HashMap<String, UUID> modifierUUID = new HashMap();

    protected CurativeInfusionRecipe(Builder builder) {
        super(builder.knwoledgeRequirement, builder.fakeItemOuput, builder.instability, builder.aspects, builder.fakeItemInput, builder.components.toArray());
        this.predicate = builder.predicate;
        this.aspects = builder.aspects;
        this.instability = builder.instability;
        this.celestialBody = builder.celestialBody;
        this.celestialAura = builder.celestialAura;
        this.vis = builder.aura;
        this.fluxRejection = builder.fluxRejection;
        this.informationNBT = builder.informationNBT;
        this.optionalComponents = builder.optionalComponents;
        this.modifiers = builder.modifiers;
        for (String name : this.modifiers.keySet()) {
            this.modifierUUID.put(name, this.modifiers.get(name).getID());
        }
    }

    public boolean matches(List<ItemStack> input, EntityLivingBase entity, World world, EntityPlayer player, TileVat vat) {
        if (!this.research.isEmpty() && !ThaumcraftCapabilities.knowsResearch(player, new String[]{this.research})) {
            return false;
        }
        if (!this.predicate.test(entity)) {
            return false;
        }
        return RecipeMatcher.findMatches(input, this.getComponents()) != null;
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

    public AspectList getCurrentAspect(EntityPlayer infuser, World worldIn, EntityLivingBase base, float stability, float totalInstability, List<Ingredient> optionalsIngredientsInfused) {
        return this.getAspects();
    }

    public void applyInfusion(EntityPlayer player, EntityLivingBase target) {
        LivingBaseCapability cap = Common.getCap(target);
        cap.infusions.put(IsorropiaAPI.creatureInfusionRecipesLocal.get(this), this.getInformationNBT());
        if (target instanceof EntityTameable) {
            ((EntityTameable)target).setTamedBy(player);
        }
        for (String name : this.modifiers.keySet()) {
            AttributeModifier modifier;
            IAttributeInstance attribute = target.getAttributeMap().getAttributeInstanceByName(name);
            if (attribute.hasModifier(modifier = this.modifiers.get(name))) {
                Isorropia.logger.error("Tried to apply attributes modifier " + name + " on curative recipe : " + IsorropiaAPI.creatureInfusionRecipesLocal.get(this) + " when they already been applied");
                continue;
            }
            attribute.applyModifier(this.modifiers.get(name));
        }
    }

    public void applyWithCheat(EntityPlayer player, EntityLivingBase target, ItemStack stack) {
        this.applyInfusion(player, target);
    }

    public void onInfusionFinish(TileVat vat) {
        this.applyInfusion(vat.getRecipePlayer(), vat.getEntityContained());
    }

    public void onInfusionRemove(EntityLivingBase target) {
        if (this.getInformationNBT() != null) {
            LivingBaseCapability cap = Common.getCap(target);
            cap.infusions.remove(IsorropiaAPI.creatureInfusionRecipesLocal.get(this));
        }
        for (String name : this.modifiers.keySet()) {
            target.getAttributeMap().getAttributeInstanceByName(name).removeModifier(this.modifierUUID.get(name));
        }
    }

    public int getInstability() {
        return this.instability;
    }

    public ICelestialBody getCelestialBody() {
        return this.celestialBody;
    }

    public float getVis() {
        return this.vis;
    }

    public float getFluxRejection() {
        return this.fluxRejection;
    }

    public Predicate<EntityLivingBase> getPredicate() {
        return this.predicate;
    }

    public NBTTagCompound getInformationNBT() {
        return this.informationNBT;
    }

    public int getCelestialAura() {
        return this.celestialAura;
    }

    public NonNullList<Ingredient> getOptionalComponents() {
        return this.optionalComponents;
    }

    public static class Builder<T extends Builder<T>> {
        protected static final Ingredient DEFAULT_FAKE_INGREDIENT = Ingredient.fromItem((Item)ItemsIS.itemCat);
        protected static final Predicate<EntityLivingBase> DEFAULT_PREDICATE = entity -> entity instanceof EntityLivingBase;
        protected Predicate<EntityLivingBase> predicate = DEFAULT_PREDICATE;
        protected String knwoledgeRequirement = "FIRSTSTEPS";
        protected AspectList aspects = new AspectList();
        protected int instability;
        protected NonNullList<Ingredient> components;
        protected ICelestialBody celestialBody = CelestialBody.NONE;
        protected int celestialAura;
        protected float aura;
        protected float fluxRejection;
        protected NBTTagCompound informationNBT = new NBTTagCompound();
        protected NonNullList<Ingredient> optionalComponents;
        protected HashMap<String, AttributeModifier> modifiers = new HashMap();
        protected Ingredient fakeItemInput = DEFAULT_FAKE_INGREDIENT;
        protected ItemStack fakeItemOuput = new ItemStack(ItemsIS.itemCat);

        public Builder() {
            this.components = NonNullList.create();
            this.optionalComponents = NonNullList.create();
        }

        public T withKnowledgeRequirement(String knowledge) {
            this.knwoledgeRequirement = knowledge;
            return this.self();
        }

        public T withPredicate(Predicate<EntityLivingBase> predicate) {
            if (predicate != null) {
                this.predicate = predicate;
            }
            return this.self();
        }

        public T withAspects(AspectList aspects) {
            this.aspects = aspects;
            return this.self();
        }

        public T withInstability(int instability) {
            this.instability = instability;
            return this.self();
        }

        public T withComponents(Ingredient ... components) {
            this.components.addAll(Arrays.asList(components));
            return this.self();
        }

        public T withOptionalsComponents(Ingredient ... components) {
            this.optionalComponents.addAll(Arrays.asList(components));
            return this.self();
        }

        public T withFakeIngredients(Ingredient io) {
            return this.withFakeIngredients(io, io.getMatchingStacks()[0]);
        }

        public T withFakeIngredients(Ingredient fakeInput, ItemStack fakeOutput) {
            this.fakeItemInput = fakeInput;
            this.fakeItemOuput = fakeOutput;
            return this.self();
        }

        public T withCelestialAura(ICelestialBody celestialBody, int auraAmount) {
            this.celestialBody = celestialBody;
            this.celestialAura = auraAmount;
            return this.self();
        }

        public T withFluxRejection(float fluxRejection) {
            this.fluxRejection = fluxRejection;
            return this.self();
        }

        public T withInformationNBT(NBTTagCompound nbt) {
            this.informationNBT = nbt;
            return this.self();
        }

        public T withVis(float visNeeded) {
            this.aura = visNeeded;
            return this.self();
        }

        public T withModifier(String name, AttributeModifier modifier) {
            this.modifiers.put(name, modifier);
            return this.self();
        }

        protected T self() {
            return (T)this;
        }

        public CurativeInfusionRecipe build() {
            return new CurativeInfusionRecipe(this);
        }
    }
}
