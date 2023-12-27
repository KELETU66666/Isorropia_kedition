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

public class EntityOrePig extends EntityPig implements IEatStone
{
    private OrePigEntry ore_entry;
    private int orePercent;

    public EntityOrePig(World world) {
        super(world);
        this.orePercent = 0;
    }


    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(9, new EntityAIEatStone(this));
    }



    public void eatStone() {
        this.orePercent += this.world.rand.nextInt(15) + 1;
        if (this.orePercent >= 100) {
            this.orePercent -= 100;
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
        for (int j = this.rand.nextInt(3) + 1 + this.rand.nextInt(1 + p_70628_2_), k = 0; k < j; k++) {
            entityDropItem(new ItemStack(Blocks.STONE), 1.0F);
        }
        if (getSaddled()) {
            dropItem(Items.SADDLE, 1);
        }
    }

    public void setOre(String oreName) {
        this.ore_entry = Config.ORE_PIG_ENTRIES.stream().filter(entry -> entry.getOreDictionary().equals(oreName)).findFirst().get();
    }


    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("percent", this.orePercent);
        if (this.ore_entry != null) {
            compound.setString("oreName", this.ore_entry.getOreDictionary());
        }
    }

    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.orePercent = compound.getInteger("percent");
        this.ore_entry = Config.ORE_PIG_ENTRIES.stream().filter(entry -> entry.getOreDictionary().equals(compound.getString("oreName"))).findFirst().orElse(null);
    }
}