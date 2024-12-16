package fr.wind_blade.isorropia.common.entities;

import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

import java.util.ArrayList;
import java.util.List;

public class EntitySheeder extends EntitySpider implements IShearable {
    private int sheepTimer;
    private static final DataParameter<Boolean> SHEER = EntityDataManager.createKey(EntitySheeder.class, DataSerializers.BOOLEAN);


    public EntitySheeder(World p_i1743_1_) {
        super(p_i1743_1_);
    }


    public void entityInit() {
        super.entityInit();
        this.dataManager.register(SHEER, Boolean.FALSE);
    }


    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 0.5D));
        this.tasks.addTask(2, new EntityAITempt(this, 0.44D, Items.WHEAT, false));
        this.tasks.addTask(3, new EntityAIEatGrass(this));
        this.tasks.addTask(4, new EntityAILeapAtTarget(this, 0.4F));
        this.tasks.addTask(5, new EntityAIWander(this, 0.8D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
    }


    public void onUpdate() {
        super.onUpdate();
        if (this.isDead && !this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL &&
                getHealth() > 0.0F) {
            this.isDead = false;
        }
    }


    public void onLivingUpdate() {
        if (this.world.isRemote) {
            this.sheepTimer = Math.max(0, this.sheepTimer - 1);
        }
        super.onLivingUpdate();
    }


    public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<>();
        setSheared(true);
        for (int i = 2 + this.rand.nextInt(4), j = 0; j < i; j++)
            ret.add(new ItemStack(Items.STRING, 1, 0));
        playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
        return ret;
    }

    public void setSheared(boolean isShearing) {
        this.dataManager.set(SHEER, isShearing);
    }

    public boolean getSheared() {
        return this.dataManager.get(SHEER);
    }


    public void eatGrassBonus() {
        setSheared(false);
    }


    public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos) {
        return (!item.isEmpty() && item.getItem() == Items.SHEARS && !getSheared());
    }
}