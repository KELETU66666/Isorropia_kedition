package fr.wind_blade.isorropia.common.entities.ai;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;

public class EntityAIEatStone<T extends EntityLiving & IEatStone> extends EntityAIBase
{
    private final T eater;
    private Entity targetEntity;
    int count;

    public EntityAIEatStone(T eater) {
        this.count = 0;
        this.eater = eater;
    }


    public boolean shouldExecute() {
        return findItem();
    }

    private boolean findItem() {
        float dmod = 16.0F;
        List<Entity> targets = this.eater.world.getEntitiesWithinAABBExcludingEntity(this.eater, new AxisAlignedBB(this.eater.posX - 16.0D, this.eater.posY - 16.0D, this.eater.posZ - 16.0D, this.eater.posX + 16.0D, this.eater.posY + 16.0D, this.eater.posZ + 16.0D));


        if (targets.size() == 0) {
            return false;
        }
        for (Entity e : targets) {
            if (e instanceof EntityItem && ((EntityItem)e).getItem().getItem() == Item.getItemFromBlock(Blocks.COBBLESTONE)) {
                double distance2 = e.getDistanceSq(this.eater.posX, this.eater.posY, this.eater.posZ);
                if (distance2 >= 256.0D) {
                    continue;
                }
                this.targetEntity = e;
                break;
            }
        }
        return (this.targetEntity != null);
    }

    public boolean continueExecuting() {
        return (this.count-- > 0 && !this.eater.getNavigator().noPath() && this.targetEntity.isEntityAlive());
    }


    public void resetTask() {
        this.count = 0;
        this.targetEntity = null;
        this.eater.getNavigator().clearPath();
    }


    public void updateTask() {
        this.eater.getLookHelper().setLookPositionWithEntity(this.targetEntity, 30.0F, 30.0F);
        double dist = this.eater.getDistanceSq(this.targetEntity);
        if (dist <= 2.0D) {
            pickUp();
        } else {
            this.eater.getNavigator().tryMoveToEntityLiving(this.targetEntity, (this.eater.getAIMoveSpeed() + 1.0F));
        }
    }

    private void pickUp() {
        if (this.targetEntity instanceof EntityItem) {
            ItemStack entityItem = ((EntityItem)this.targetEntity).getItem();
            if (!entityItem.isEmpty() && entityItem.getItem() != Items.AIR) {
                this.eater.eatStone();
                entityItem.shrink(1);
                if (((EntityItem)this.targetEntity).getItem().getCount() <= 0) {
                    this.targetEntity.setDead();
                }
                this.eater.playSound(SoundEvents.ENTITY_PLAYER_BURP, 0.2F, ((this.eater.world.rand
                        .nextFloat() - this.eater.world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            }
        }
    }


    public void startExecuting() {
        this.count = 500;
        this.eater.getNavigator().tryMoveToEntityLiving(this.targetEntity, (this.eater.getAIMoveSpeed() + 1.0F));
    }
}