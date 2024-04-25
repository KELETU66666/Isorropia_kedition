package fr.wind_blade.isorropia.common.research.recipes;

import fr.wind_blade.isorropia.Isorropia;
import fr.wind_blade.isorropia.common.Common;
import fr.wind_blade.isorropia.common.IsorropiaAPI;
import fr.wind_blade.isorropia.common.capabilities.LivingBaseCapability;
import fr.wind_blade.isorropia.common.research.recipes.CurativeInfusionRecipe;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class OrganCurativeInfusionRecipe extends CurativeInfusionRecipe {
    protected final Organ organTarget;

    public OrganCurativeInfusionRecipe(Builder<? extends Builder<?>> builder) {
        super(builder);
        this.organTarget = builder.organTarget;
        if (this.organTarget == null) {
            Isorropia.logger.error("Organ Curative Infusion Recipe can't have a null organ target");
            FMLCommonHandler.instance().exitJava(1, false);
        }
    }

    @Override
    public void applyInfusion(EntityPlayer player, EntityLivingBase target) {
        LivingBaseCapability cap = Common.getCap(target);
        NBTTagCompound tag = cap.infusions.get(new ResourceLocation("isorropia", "organs"));
        NBTTagCompound nBTTagCompound = tag = tag == null ? new NBTTagCompound() : tag;
        if (tag.hasKey(this.organTarget.registryName.toString())) {
            IsorropiaAPI.creatureInfusionRecipes.get(new ResourceLocation(tag.getString(this.organTarget.registryName.toString()))).onInfusionRemove(target);
        }
        super.applyInfusion(player, target);
        tag.setString(this.organTarget.registryName.toString(), IsorropiaAPI.creatureInfusionRecipesLocal.get(this).toString());
        cap.infusions.put(new ResourceLocation("isorropia", "organs"), tag);
    }

    public Organ getOrganTarget() {
        return this.organTarget;
    }

    public static class Builder<T extends Builder<T>>
            extends CurativeInfusionRecipe.Builder<T> {
        protected Organ organTarget = null;

        public T withOrganTarget(Organ organTarget) {
            this.organTarget = organTarget;
            return this.self();
        }

        @Override
        public CurativeInfusionRecipe build() {
            return new OrganCurativeInfusionRecipe(this);
        }
    }

    public static class Organ {
        public final ResourceLocation registryName;
        public static Organ HEART = new Organ(new ResourceLocation("isorropia", "heart"));
        public static Organ SKIN = new Organ(new ResourceLocation("isorropia", "skin"));
        public static Organ BLOOD = new Organ(new ResourceLocation("isorropia", "blood"));
        public static Organ MUSCLE = new Organ(new ResourceLocation("isorropia", "muscle"));
        public static Organ VOID = new Organ(new ResourceLocation("isorropia", "void"));

        public Organ(ResourceLocation registryName) {
            this.registryName = registryName;
        }
    }
}
