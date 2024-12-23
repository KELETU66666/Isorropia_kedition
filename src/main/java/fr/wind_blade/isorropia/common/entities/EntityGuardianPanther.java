package fr.wind_blade.isorropia.common.entities;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import java.util.ArrayList;

public class EntityGuardianPanther extends EntityOcelot {

    private static final DataParameter<Integer> GuardianPanther = EntityDataManager.createKey(EntityGuardianPanther.class, DataSerializers.VARINT);

    public EntityGuardianPanther(final World p_i1688_1_) {
        super(p_i1688_1_);
        this.setSize(1.2f, 1.6f);
        this.aiSit = new EntityAISit(this);
        final ArrayList<EntityAITasks.EntityAITaskEntry> toRemove = new ArrayList<>(this.tasks.taskEntries);
        for (final EntityAITasks.EntityAITaskEntry task : toRemove) {
            this.tasks.removeTask(task.action);
        }
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.5f));
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0, true));
        this.tasks.addTask(5, new EntityAIFollowOwner(this, 1.0, 10.0f, 5.0f));
        this.tasks.addTask(6, new EntityAIMate(this, 0.8));
        this.tasks.addTask(7, new EntityAIWander(this, 0.8));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0f));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(24.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5000000119209289);
    }

    public boolean processInteract(final EntityPlayer p_70085_1_, EnumHand hand) {
        final ItemStack itemstack = p_70085_1_.inventory.getCurrentItem();
        if (itemstack != ItemStack.EMPTY && itemstack.getItem() instanceof ItemFood) {
            ItemFood itemfood = (ItemFood) itemstack.getItem();
            if (this.getHealth() < this.getMaxHealth()) {
                itemstack.setCount(itemstack.getCount() - 1);
                this.heal((float) itemfood.getHealAmount(itemstack));
                if (itemstack.getCount() <= 0) {
                    p_70085_1_.inventory.setInventorySlotContents(p_70085_1_.inventory.currentItem, ItemStack.EMPTY);
                }
                return true;
            }
        }
        return super.processInteract(p_70085_1_, hand);
    }

    public boolean attackEntityAsMob(final Entity p_70652_1_) {
        return p_70652_1_.attackEntityFrom(DamageSource.causeMobDamage(this), 6.0f);
    }

    public String getCommandSenderName() {
        return this.hasCustomName() ? this.getCustomNameTag()
                : (this.isTamed() ? I18n.format("entity.ThaumicHorizons.GuardianPanther.name")
                        : super.getCustomNameTag());
    }

    public void updateAITasks() {
        super.updateAITasks();
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(GuardianPanther, 0);
        final Integer b0 = this.dataManager.get(GuardianPanther);
        this.dataManager.set(GuardianPanther, b0 | 0x4);
    }

    public boolean isTamed() {
        return true;
    }

    public void resetStats() {
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(24.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5000000119209289);
    }
}