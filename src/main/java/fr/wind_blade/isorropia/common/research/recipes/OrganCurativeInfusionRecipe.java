 package fr.wind_blade.isorropia.common.research.recipes;
 
 import fr.wind_blade.isorropia.Isorropia;
 import fr.wind_blade.isorropia.common.Common;
 import fr.wind_blade.isorropia.common.IsorropiaAPI;
 import fr.wind_blade.isorropia.common.capabilities.LivingBaseCapability;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.nbt.NBTTagCompound;
 import net.minecraft.util.ResourceLocation;
 import net.minecraftforge.fml.common.FMLCommonHandler;
 
 public class OrganCurativeInfusionRecipe
   extends CurativeInfusionRecipe {
   protected final Organ organTarget;
   
   public OrganCurativeInfusionRecipe(Builder<? extends Builder<?>> builder) {
/* 18 */     super(builder);
     this.organTarget = builder.organTarget;
/* 20 */     if (this.organTarget == null) {
/* 21 */       Isorropia.logger.error("Organ Curative Infusion Recipe can't have a null organ target");
/* 22 */       FMLCommonHandler.instance().exitJava(1, false);
     } 
   }
 
   
   public void applyInfusion(EntityPlayer player, EntityLivingBase target) {
      LivingBaseCapability cap = Common.getCap(target);
      NBTTagCompound tag = (NBTTagCompound)cap.infusions.get(new ResourceLocation("isorropia", "organs"));
     
/* 31 */     tag = (tag == null) ? new NBTTagCompound() : tag;
/* 32 */     if (tag.hasKey(this.organTarget.registryName.toString()))
/* 33 */       ((CurativeInfusionRecipe)IsorropiaAPI.creatureInfusionRecipes.get(new ResourceLocation(tag.getString(this.organTarget.registryName.toString())))).onInfusionRemove(target); 
/* 34 */     super.applyInfusion(player, target);
/* 35 */     tag.setString(this.organTarget.registryName.toString(), ((ResourceLocation)IsorropiaAPI.creatureInfusionRecipesLocal.get(this)).toString());
/* 36 */     cap.infusions.put(new ResourceLocation("isorropia", "organs"), tag);
   }
   
   public Organ getOrganTarget() {
/* 40 */     return this.organTarget;
   }
 
   
   public static class Organ
   {
     public final ResourceLocation registryName;
/* 47 */     public static Organ HEART = new Organ(new ResourceLocation("isorropia", "heart"));
/* 48 */     public static Organ SKIN = new Organ(new ResourceLocation("isorropia", "skin"));
/* 49 */     public static Organ BLOOD = new Organ(new ResourceLocation("isorropia", "blood"));
/* 50 */     public static Organ MUSCLE = new Organ(new ResourceLocation("isorropia", "muscle"));
     
     public Organ(ResourceLocation registryName) {
/* 53 */       this.registryName = registryName;
     }
   }
   
   public static class Builder<T extends Builder<T>>
     extends CurativeInfusionRecipe.Builder<T> {
/* 59 */     protected OrganCurativeInfusionRecipe.Organ organTarget = null;
     
     public T withOrganTarget(OrganCurativeInfusionRecipe.Organ organTarget) {
/* 62 */       this.organTarget = organTarget;
/* 63 */       return self();
     }
 
     
     public CurativeInfusionRecipe build() {
/* 68 */       return new OrganCurativeInfusionRecipe(this);
     }
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\research\recipes\OrganCurativeInfusionRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */