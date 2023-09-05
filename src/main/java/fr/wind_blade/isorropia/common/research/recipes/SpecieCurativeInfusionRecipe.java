 package fr.wind_blade.isorropia.common.research.recipes;

 import fr.wind_blade.isorropia.Isorropia;
 import fr.wind_blade.isorropia.common.Common;
 import fr.wind_blade.isorropia.common.capabilities.LivingCapability;
 import fr.wind_blade.isorropia.common.tiles.TileVat;
 import net.minecraft.entity.Entity;
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
/* 20 */     this.result = builder.result;
/* 21 */     if (this.result == null) {
/* 22 */       Isorropia.logger.error("Specie Infusion Recipe can't have a null result");
/* 23 */       FMLCommonHandler.instance().exitJava(1, false);
     }
   }


   public void applyWithCheat(EntityPlayer player, EntityLivingBase old, ItemStack stack) {
      EntityLiving entity = (EntityLiving)EntityRegistry.getEntry(this.result).newInstance(old.world);

/* 31 */     if (entity == null)
       return;
/* 33 */     ((LivingCapability)Common.getCap((EntityLivingBase)entity)).uuidOwner = player.getUniqueID();
/* 34 */     entity.setPositionAndRotation(old.posX, old.posY, old.posX, old.rotationYaw, old.rotationPitch);
/* 35 */     old.world.spawnEntity((Entity)entity);
/* 36 */     old.world.removeEntity((Entity)old);
/* 37 */     super.applyWithCheat(player, (EntityLivingBase)entity, stack);
   }


   public void onInfusionFinish(TileVat vat) {
/* 42 */     EntityLivingBase old = vat.getEntityContained();
/* 43 */     EntityLiving entity = (EntityLiving)EntityRegistry.getEntry(this.result).newInstance(vat.getWorld());

/* 45 */     if (entity == null)
       return;
/* 47 */     ((LivingCapability)Common.getCap((EntityLivingBase)entity)).uuidOwner = vat.getRecipePlayer().getUniqueID();
/* 48 */     entity.setPositionAndRotation(old.posX, old.posY, old.posX, old.rotationYaw, old.rotationPitch);
/* 49 */     vat.getWorld().spawnEntity((Entity)entity);
/* 50 */     vat.getWorld().removeEntity((Entity)vat.setEntityContained((EntityLivingBase)entity, old.rotationYaw));
/* 51 */     super.onInfusionFinish(vat);
   }

   public Class<? extends EntityLiving> getResult() {
/* 55 */     return this.result;
   }

   public static class Builder<T extends Builder<T>> extends CurativeInfusionRecipe.Builder<T> {
/* 59 */     protected Class<? extends EntityLiving> result = null;

     public T withResult(Class<? extends EntityLiving> result) {
/* 62 */       this.result = result;
/* 63 */       return self();
     }


     public CurativeInfusionRecipe build() {
/* 68 */       return new SpecieCurativeInfusionRecipe(this);
     }
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\research\recipes\SpecieCurativeInfusionRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */