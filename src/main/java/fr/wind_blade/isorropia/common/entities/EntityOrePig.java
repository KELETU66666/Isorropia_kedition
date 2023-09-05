 package fr.wind_blade.isorropia.common.entities;
 
 import fr.wind_blade.isorropia.common.config.Config;
 import fr.wind_blade.isorropia.common.config.DuoWeightedRandom;
 import fr.wind_blade.isorropia.common.config.OrePigEntry;
 import fr.wind_blade.isorropia.common.entities.ai.EntityAIEatStone;
 import fr.wind_blade.isorropia.common.entities.ai.IEatStone;
 import net.minecraft.entity.EntityLiving;
 import net.minecraft.entity.ai.EntityAIBase;
 import net.minecraft.entity.passive.EntityPig;
 import net.minecraft.init.Blocks;
 import net.minecraft.init.Items;
 import net.minecraft.item.ItemStack;
 import net.minecraft.nbt.NBTTagCompound;
 import net.minecraft.world.World;
 
 public class EntityOrePig
   extends EntityPig
   implements IEatStone
 {
   private OrePigEntry ore_entry;
   private int orePercent;
   
   public EntityOrePig(World world) {
     super(world);
      this.orePercent = 0;
   }
 
   
   protected void initEntityAI() {
/* 31 */     super.initEntityAI();
/* 32 */     this.tasks.addTask(9, new EntityAIEatStone(this));
   }
 
 
   
   public void eatStone() {
/* 38 */     this.orePercent += this.world.rand.nextInt(15) + 1;
/* 39 */     if (this.orePercent >= 100) {
/* 40 */       this.orePercent -= 100;
        excreteNugget();
     } 
   }

     private void excreteNugget() {
     ItemStack stack;
     OrePigEntry entry = this.ore_entry != null && this.ore_entry.isActive() ? DuoWeightedRandom.getRandomItemWithMain(this.rand, this.ore_entry, Config.ORE_PIG_ENTRIES) : DuoWeightedRandom.getRandomItem(this.rand, Config.ORE_PIG_ENTRIES);
     if (entry != null && (stack = Config.orePigOreDictionary.get(entry.getOreDictionary())) != null && !stack.isEmpty()) {
         this.entityDropItem(stack.copy(), 0.0f);
         return;
     }
     this.excreteNugget();
 }
 
   
   protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
/* 66 */     for (int j = this.rand.nextInt(3) + 1 + this.rand.nextInt(1 + p_70628_2_), k = 0; k < j; k++) {
/* 67 */       entityDropItem(new ItemStack(Blocks.STONE), 1.0F);
     }
/* 69 */     if (getSaddled()) {
/* 70 */       dropItem(Items.SADDLE, 1);
     }
   }
   
   public void setOre(String oreName) {
/* 75 */     this
/* 76 */       .ore_entry = Config.ORE_PIG_ENTRIES.stream().filter(entry -> entry.getOreDictionary().equals(oreName)).findFirst().get();
   }
 
   
   public void writeEntityToNBT(NBTTagCompound compound) {
/* 81 */     super.writeEntityToNBT(compound);
/* 82 */     compound.setInteger("percent", this.orePercent);
/* 83 */     if (this.ore_entry != null) {
/* 84 */       compound.setString("oreName", this.ore_entry.getOreDictionary());
     }
   }
   
   public void readEntityFromNBT(NBTTagCompound compound) {
/* 89 */     super.readEntityFromNBT(compound);
/* 90 */     this.orePercent = compound.getInteger("percent");
/* 91 */     this
       
/* 93 */       .ore_entry = Config.ORE_PIG_ENTRIES.stream().filter(entry -> entry.getOreDictionary().equals(compound.getString("oreName"))).findFirst().orElse(null);
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\entities\EntityOrePig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */