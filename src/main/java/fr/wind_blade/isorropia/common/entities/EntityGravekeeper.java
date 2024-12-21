package fr.wind_blade.isorropia.common.entities;

import fr.wind_blade.isorropia.common.entities.ai.EntityAINearestUndeadAttackableTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import thaumcraft.client.fx.FXDispatcher;

import java.util.List;

public class EntityGravekeeper extends EntityOcelot {
    public EntityGravekeeper(World world) {
        super(world);
    }


    protected void initEntityAI() {
        this.aiSit = new EntityAISit(this);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(5, new EntityAIFollowOwner(this, 1.0D, 10.0F, 5.0F));
        this.tasks.addTask(6, new EntityAIOcelotSit(this, 0.8D));
        this.tasks.addTask(7, new EntityAILeapAtTarget(this, 0.3F));
        this.tasks.addTask(8, new EntityAIOcelotAttack(this));
        this.tasks.addTask(9, new EntityAIWander(this, 0.8D));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
        this.targetTasks.addTask(1, new EntityAITargetNonTamed(this, EntityChicken.class, false, null));
        this.targetTasks.addTask(3, new EntityAINearestUndeadAttackableTarget(this, 1, false, false));
        this.targetTasks.addTask(4, new EntityAIAttackMelee(this, 1.4D, false));
    }


    public boolean attackEntityAsMob(Entity target) {
        return ((target instanceof EntityLivingBase && ((EntityLivingBase) target).isEntityUndead()) || target
                .attackEntityFrom(DamageSource.causeMobDamage(this), 2.0F));
    }


    public void onUpdate() {
        super.onUpdate();
        List<EntityLivingBase> critters = this.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(this.posX - 5.0D, this.posY - 5.0D, this.posZ - 5.0D, this.posX + 5.0D, this.posY + 5.0D, this.posZ + 5.0D));

        for (EntityLivingBase ent : critters) {
            if (ent.isEntityUndead()) {
                ent.setFire(1);

                if (this.world.isRemote)
                    FXDispatcher.INSTANCE.beamBore(this.posX, this.posY + (this.height / 2.0F), this.posZ, ent.posX, ent.posY + (ent.height / 2.0F), ent.posZ, 0, 16773444, false, 2.5F,
                            Integer.valueOf(1), 1);
            }
        }
    }
}