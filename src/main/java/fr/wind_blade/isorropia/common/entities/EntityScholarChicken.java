 package fr.wind_blade.isorropia.common.entities;
 
 import fr.wind_blade.isorropia.common.items.ItemsIS;
 import net.minecraft.entity.passive.EntityChicken;
 import net.minecraft.init.Items;
 import net.minecraft.init.SoundEvents;
 import net.minecraft.nbt.NBTTagCompound;
 import net.minecraft.world.World;
 
 
 
 public class EntityScholarChicken
   extends EntityChicken
 {
   public int timeUntilNextFeather;
   
   public EntityScholarChicken(World world) {
      super(world);
     this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
/* 20 */     this.timeUntilNextFeather = this.rand.nextInt(4000) + 4000;
   }
 
   
   public void onLivingUpdate() {
     int notTime = this.timeUntilNextEgg;
      this.timeUntilNextEgg = Integer.MAX_VALUE;
      super.onLivingUpdate();
      this.timeUntilNextEgg = notTime;
     
/* 30 */     if (this.world.isRemote || isChild() || isChickenJockey()) {
       return;
     }
/* 33 */     if (--this.timeUntilNextEgg <= 0) {
/* 34 */       playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (this.rand
/* 35 */           .nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
/* 36 */       dropItem(ItemsIS.itemInkEgg, 1);
/* 37 */       this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
     } 
     
/* 40 */     if (--this.timeUntilNextFeather <= 0) {
        playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (this.rand
/* 42 */           .nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
/* 43 */       dropItem(Items.FEATHER, 1);
/* 44 */       this.timeUntilNextFeather = this.rand.nextInt(4000) + 4000;
     } 
   }
 
   
   public void writeEntityToNBT(NBTTagCompound compound) {
/* 50 */     super.writeEntityToNBT(compound);
/* 51 */     compound.setInteger("egg", this.timeUntilNextEgg);
/* 52 */     compound.setInteger("feather", this.timeUntilNextFeather);
   }
 
   
   public void readEntityFromNBT(NBTTagCompound compound) {
/* 57 */     super.readEntityFromNBT(compound);
      this.timeUntilNextEgg = (compound.getInteger("egg") != 0) ? compound.getInteger("egg") : this.timeUntilNextEgg;
/* 59 */     this.timeUntilNextFeather = (compound.getInteger("feather") != 0) ? compound.getInteger("feather") : this.timeUntilNextFeather;
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\entities\EntityScholarChicken.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */