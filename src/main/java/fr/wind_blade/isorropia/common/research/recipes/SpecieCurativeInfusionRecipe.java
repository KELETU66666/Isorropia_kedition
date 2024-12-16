package fr.wind_blade.isorropia.common.research.recipes;

import fr.wind_blade.isorropia.Isorropia;
import fr.wind_blade.isorropia.common.Common;
import fr.wind_blade.isorropia.common.capabilities.LivingCapability;
import fr.wind_blade.isorropia.common.tiles.TileVat;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class SpecieCurativeInfusionRecipe extends CurativeInfusionRecipe {
    protected final Class<? extends EntityLiving> result;

    public SpecieCurativeInfusionRecipe(Builder<? extends Builder<?>> builder) {
        super(builder);
        this.result = builder.result;
        if (this.result == null) {
            Isorropia.logger.error("Specie Infusion Recipe can't have a null result");
            FMLCommonHandler.instance().exitJava(1, false);
        }
    }

    @Override
    public void applyWithCheat(EntityPlayer player, EntityLivingBase old, ItemStack stack) {
        EntityLiving entity = (EntityLiving)EntityRegistry.getEntry(this.result).newInstance(old.world);
        if (entity == null) {
            return;
        }
        ((LivingCapability)Common.getCap(entity)).uuidOwner = player.getUniqueID();
        entity.setPositionAndRotation(old.posX, old.posY, old.posX, old.rotationYaw, old.rotationPitch);
        old.world.spawnEntity(entity);
        old.world.removeEntity(old);
        super.applyWithCheat(player, entity, stack);
    }

    @Override
    public void onInfusionFinish(TileVat vat) {
        EntityLivingBase old = vat.getEntityContained();
        EntityLiving entity = (EntityLiving)EntityRegistry.getEntry(this.result).newInstance(vat.getWorld());
        if (entity == null) {
            return;
        }
        ((LivingCapability)Common.getCap(entity)).uuidOwner = vat.getRecipePlayer().getUniqueID();
        entity.setPositionAndRotation(old.posX, old.posY, old.posX, old.rotationYaw, old.rotationPitch);
        vat.getWorld().spawnEntity(entity);
        vat.getWorld().removeEntity(vat.setEntityContained(entity, old.rotationYaw));
        super.onInfusionFinish(vat);
    }

    public Class<? extends EntityLiving> getResult() {
        return this.result;
    }

    public static class Builder<T extends Builder<T>>
            extends CurativeInfusionRecipe.Builder<T> {
        protected Class<? extends EntityLiving> result = null;

        public T withResult(Class<? extends EntityLiving> result) {
            this.result = result;
            return this.self();
        }

        @Override
        public CurativeInfusionRecipe build() {
            return new SpecieCurativeInfusionRecipe(this);
        }
    }
}
