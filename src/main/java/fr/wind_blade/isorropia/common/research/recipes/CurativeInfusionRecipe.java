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
   extends InfusionRecipe
 {
   protected final Predicate<EntityLivingBase> predicate;
   protected final ICelestialBody celestialBody;
   protected final int celestialAura;
   protected final float vis;
   protected final float fluxRejection;
   protected final NBTTagCompound informationNBT;
   protected final NonNullList<Ingredient> optionalComponents;
   protected final HashMap<String, AttributeModifier> modifiers;
/*  45 */   protected final HashMap<String, UUID> modifierUUID = new HashMap<>();
   
   protected CurativeInfusionRecipe(Builder builder) {
/*  48 */     super(builder.knwoledgeRequirement, builder.fakeItemOuput, builder.instability, builder.aspects, builder.fakeItemInput, builder.components
/*  49 */         .toArray());
     
/*  51 */     this.predicate = builder.predicate;
/*  52 */     this.aspects = builder.aspects;
/*  53 */     this.instability = builder.instability;
/*  54 */     this.celestialBody = builder.celestialBody;
/*  55 */     this.celestialAura = builder.celestialAura;
/*  56 */     this.vis = builder.aura;
/*  57 */     this.fluxRejection = builder.fluxRejection;
/*  58 */     this.informationNBT = builder.informationNBT;
/*  59 */     this.optionalComponents = builder.optionalComponents;
/*  60 */     this.modifiers = builder.modifiers;
/*  61 */     for (String name : this.modifiers.keySet()) {
/*  62 */       this.modifierUUID.put(name, ((AttributeModifier)this.modifiers.get(name)).getID());
     }
   }
 
   
   public boolean matches(List<ItemStack> input, EntityLivingBase entity, World world, EntityPlayer player, TileVat vat) {
/*  68 */     if (!this.research.isEmpty() && !ThaumcraftCapabilities.knowsResearch(player, new String[] { this.research })) {
/*  69 */       return false;
     }
/*  71 */     if (!this.predicate.test(entity)) {
/*  72 */       return false;
     }
/*  74 */     return (RecipeMatcher.findMatches(input, (List)getComponents()) != null);
   }
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
   
   public static boolean areItemStacksEqual(ItemStack stack0, ItemStack stack1, boolean fuzzy) {
/*  94 */     if (stack0.isEmpty() && stack1.isEmpty()) {
/*  95 */       return true;
     }
     
     if (stack0.isEmpty() || stack1.isEmpty()) {
/*  99 */       return false;
     }
     
/* 102 */     boolean t1 = ThaumcraftInvHelper.areItemStackTagsEqualForCrafting(stack0, stack1);
/* 103 */     if (!t1) {
/* 104 */       return false;
     }
/* 106 */     if (fuzzy) {
/* 107 */       int[] od = OreDictionary.getOreIDs(stack0);
/* 108 */       if (od.length > 0) {
/* 109 */         List<ItemStack> ores = new ArrayList<>();
/* 110 */         List<String> oresName = new ArrayList<>();
         
/* 112 */         for (int id : od) {
/* 113 */           oresName.add(OreDictionary.getOreName(id));
         }
/* 115 */         if (ThaumcraftInvHelper.containsMatch(false, new ItemStack[] { stack1 }, ores)) {
/* 116 */           return true;
         }
       } 
     } 
/* 120 */     boolean damage = (stack0.getItemDamage() == stack1.getItemDamage() || stack1.getItemDamage() == 32767);
/* 121 */     return (stack0.getItem() == stack1.getItem() && damage && stack0.getCount() <= stack0.getMaxStackSize());
   }
 
   
   public AspectList getCurrentAspect(EntityPlayer infuser, World worldIn, EntityLivingBase base, float stability, float totalInstability, List<Ingredient> optionalsIngredientsInfused) {
/* 126 */     return getAspects();
   }
   
   public void applyInfusion(EntityPlayer player, EntityLivingBase target) {
/* 130 */     LivingBaseCapability cap = Common.getCap(target);
     
/* 132 */     cap.infusions.put(IsorropiaAPI.creatureInfusionRecipesLocal.get(this), getInformationNBT());
     if (target instanceof EntityTameable)
/* 134 */       ((EntityTameable)target).setTamedBy(player); 
/* 135 */     for (String name : this.modifiers.keySet()) {
/* 136 */       IAttributeInstance attribute = target.getAttributeMap().getAttributeInstanceByName(name);
/* 137 */       AttributeModifier modifier = this.modifiers.get(name);
/* 138 */       if (attribute.hasModifier(modifier)) {
/* 139 */         Isorropia.logger.error("Tried to apply attributes modifier " + name + " on curative recipe : " + IsorropiaAPI.creatureInfusionRecipesLocal.get(this) + " when they already been applied");
         continue;
       } 
/* 142 */       attribute.applyModifier(this.modifiers.get(name));
     } 
   }
 
 
 
   
   public void applyWithCheat(EntityPlayer player, EntityLivingBase target, ItemStack stack) {
/* 150 */     applyInfusion(player, target);
   }
   
   public void onInfusionFinish(TileVat vat) {
/* 154 */     applyInfusion(vat.getRecipePlayer(), vat.getEntityContained());
   }
   
   public void onInfusionRemove(EntityLivingBase target) {
/* 158 */     if (getInformationNBT() != null) {
/* 159 */       LivingBaseCapability cap = Common.getCap(target);
       
/* 161 */       cap.infusions.remove(IsorropiaAPI.creatureInfusionRecipesLocal.get(this));
     } 
/* 163 */     for (String name : this.modifiers.keySet())
/* 164 */       target.getAttributeMap().getAttributeInstanceByName(name).removeModifier(this.modifierUUID.get(name)); 
   }
   
   public int getInstability() {
/* 168 */     return this.instability;
   }
   
   public ICelestialBody getCelestialBody() {
/* 172 */     return this.celestialBody;
   }
   
   public float getVis() {
/* 176 */     return this.vis;
   }
   
   public float getFluxRejection() {
/* 180 */     return this.fluxRejection;
   }
   
   public Predicate<EntityLivingBase> getPredicate() {
/* 184 */     return this.predicate;
   }
   
   public NBTTagCompound getInformationNBT() {
/* 188 */     return this.informationNBT;
   }
   
   public int getCelestialAura() {
/* 192 */     return this.celestialAura;
   }
   
   public NonNullList<Ingredient> getOptionalComponents() {
/* 196 */     return this.optionalComponents;
   }
   
   public static class Builder<T extends Builder<T>> {
     protected static final Predicate<EntityLivingBase> DEFAULT_PREDICATE;
/* 201 */     protected static final Ingredient DEFAULT_FAKE_INGREDIENT = Ingredient.fromItem(ItemsIS.itemCat); static {
/* 202 */       DEFAULT_PREDICATE = (entity -> entity instanceof EntityLivingBase);
     }
/* 204 */     protected Predicate<EntityLivingBase> predicate = DEFAULT_PREDICATE;
/* 205 */     protected String knwoledgeRequirement = "FIRSTSTEPS";
/* 206 */     protected AspectList aspects = new AspectList();
     protected int instability;
     protected NonNullList<Ingredient> components;
/* 209 */     protected ICelestialBody celestialBody = CelestialBody.NONE;
     protected int celestialAura;
     protected float aura;
     protected float fluxRejection;
/* 213 */     protected NBTTagCompound informationNBT = new NBTTagCompound();
     protected NonNullList<Ingredient> optionalComponents;
/* 215 */     protected HashMap<String, AttributeModifier> modifiers = new HashMap<>();
     
/* 217 */     protected Ingredient fakeItemInput = DEFAULT_FAKE_INGREDIENT;
/* 218 */     protected ItemStack fakeItemOuput = new ItemStack(ItemsIS.itemCat);
     
     public Builder() {
/* 221 */       this.components = NonNullList.create();
/* 222 */       this.optionalComponents = NonNullList.create();
     }
     
     public T withKnowledgeRequirement(String knowledge) {
/* 226 */       this.knwoledgeRequirement = knowledge;
/* 227 */       return self();
     }
     
     public T withPredicate(Predicate<EntityLivingBase> predicate) {
/* 231 */       if (predicate != null)
/* 232 */         this.predicate = predicate; 
/* 233 */       return self();
     }
     
     public T withAspects(AspectList aspects) {
/* 237 */       this.aspects = aspects;
/* 238 */       return self();
     }
     
     public T withInstability(int instability) {
/* 242 */       this.instability = instability;
/* 243 */       return self();
     }
     
     public T withComponents(Ingredient... components) {
/* 247 */       this.components.addAll(Arrays.asList(components));
/* 248 */       return self();
     }
     
     public T withOptionalsComponents(Ingredient... components) {
/* 252 */       this.optionalComponents.addAll(Arrays.asList(components));
/* 253 */       return self();
     }
     
     public T withFakeIngredients(Ingredient io) {
/* 257 */       return withFakeIngredients(io, io.getMatchingStacks()[0]);
     }
     
     public T withFakeIngredients(Ingredient fakeInput, ItemStack fakeOutput) {
/* 261 */       this.fakeItemInput = fakeInput;
/* 262 */       this.fakeItemOuput = fakeOutput;
/* 263 */       return self();
     }
     
     public T withCelestialAura(ICelestialBody celestialBody, int auraAmount) {
/* 267 */       this.celestialBody = celestialBody;
/* 268 */       this.celestialAura = auraAmount;
/* 269 */       return self();
     }
     
     public T withFluxRejection(float fluxRejection) {
/* 273 */       this.fluxRejection = fluxRejection;
/* 274 */       return self();
     }
     
     public T withInformationNBT(NBTTagCompound nbt) {
/* 278 */       this.informationNBT = nbt;
/* 279 */       return self();
     }
     
     public T withVis(float visNeeded) {
/* 283 */       this.aura = visNeeded;
/* 284 */       return self();
     }
     
     public T withModifier(String name, AttributeModifier modifier) {
/* 288 */       this.modifiers.put(name, modifier);
/* 289 */       return self();
     }
 
     
     protected T self() {
/* 294 */       return (T)this;
     }
     
     public CurativeInfusionRecipe build() {
/* 298 */       return new CurativeInfusionRecipe(this);
     }
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\research\recipes\CurativeInfusionRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */