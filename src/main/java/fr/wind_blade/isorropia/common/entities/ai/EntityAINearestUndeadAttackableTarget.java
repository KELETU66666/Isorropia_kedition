package fr.wind_blade.isorropia.common.entities.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;





public class EntityAINearestUndeadAttackableTarget<T extends EntityLivingBase>
        extends EntityAITarget
{
    private final int targetChance;
    protected final EntityAINearestAttackableTarget.Sorter theNearestAttackableTargetSorter;
    protected EntityLivingBase targetEntity;

    public EntityAINearestUndeadAttackableTarget(EntityCreature creature, boolean checkSight) {
        this(creature, checkSight, false);
    }

    public EntityAINearestUndeadAttackableTarget(EntityCreature creature, boolean checkSight, boolean onlyNearby) {
        this(creature, 10, checkSight, onlyNearby);
    }


    public EntityAINearestUndeadAttackableTarget(EntityCreature creature, int chance, boolean checkSight, boolean onlyNearby) {
        super(creature, checkSight, onlyNearby);
        this.targetChance = chance;
        this.theNearestAttackableTargetSorter = new EntityAINearestAttackableTarget.Sorter(creature);
        setMutexBits(1);
    }


    public boolean shouldExecute() {
        if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0) {
            return false;
        }
       List<EntityLivingBase> lt = this.taskOwner.world.getEntitiesWithinAABB(EntityLivingBase.class, getTargetableArea(getTargetDistance()));
       ArrayList<EntityLivingBase> list = new ArrayList<>();
       for (EntityLivingBase living : lt) {
           if (living.isEntityUndead())
               list.add(living);
        }
        if (list.isEmpty()) {
            return false;
        }

        Collections.sort(list, this.theNearestAttackableTargetSorter);
        this.targetEntity = list.get(0);
        return true;
    }



    protected AxisAlignedBB getTargetableArea(double targetDistance) {

        return this.taskOwner.getEntityBoundingBox().expand(targetDistance, 4.0D, targetDistance);
    }


    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.targetEntity);
        super.startExecuting();
    }

    public static class Sorter implements Comparator<Entity> {
        private final Entity theEntity;

        public Sorter(Entity theEntityIn) {
            this.theEntity = theEntityIn;
        }


        public int compare(Entity p_compare_1_, Entity p_compare_2_) {
            double d0 = this.theEntity.getDistanceSq(p_compare_1_);
            double d1 = this.theEntity.getDistanceSq(p_compare_2_);
            return (d0 < d1) ? -1 : ((d0 > d1) ? 1 : 0);
        }
    }
}