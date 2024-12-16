package fr.wind_blade.isorropia.common.entities.ai;

import fr.wind_blade.isorropia.common.libs.helpers.IRMathHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXWispZap;








public class EntityAIZapAttack
        extends EntityAIBase
{
    protected final int attackCooldown;
    protected EntityLiving attacker;
    private final float range;
    boolean longMemory;
    Path path;

    public EntityAIZapAttack(EntityLiving attacker, float range, int cooldownTicks, boolean useLongMemory) {
        this.attacker = attacker;
        this.range = range;
        this.attackCooldown = cooldownTicks;
        this.longMemory = useLongMemory;
    }



    public void startExecuting() {}



    public boolean shouldExecute() {
        EntityLivingBase target = this.attacker.getAttackTarget();

        if (target == null &&
                this.attacker instanceof EntityTameable) {
            EntityTameable tameable = (EntityTameable)this.attacker;
            if (!tameable.isTamed() || tameable.getOwner() == null) {
                return false;
            }

            tameable.setRevengeTarget(tameable.getOwner().getAttackingEntity());
            target = this.attacker.getAttackTarget();

            if (target == null) {
                return false;
            }
        }


        return target.isEntityAlive();
    }


    public boolean shouldContinueExecuting() {
        EntityLivingBase target = this.attacker.getAttackTarget();

        if (target == null || !target.isEntityAlive()) {
            return false;
        }

        if (!(target instanceof EntityPlayer) || (
                !((EntityPlayer)target).isSpectator() && !((EntityPlayer)target).isCreative())) {
            return (IRMathHelper.getTchebychevDistance(this.attacker, target) <= getAttackRange(target));
        }

        return false;
    }


    public void updateTask() {
        EntityLivingBase target = this.attacker.getAttackTarget();

        if (this.attacker.ticksExisted % this.attackCooldown != 0) {
            return;
        }

        this.attacker.playSound(SoundsTC.zap, 1.0F, 1.1F);
        PacketHandler.INSTANCE.sendToAllAround(new PacketFXWispZap(this.attacker.getEntityId(), target.getEntityId()), new NetworkRegistry.TargetPoint(this.attacker.world.provider
                .getDimension(), this.attacker.posX, this.attacker.posY, this.attacker.posZ, 32.0D));


        float damage = (float)this.attacker.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        if (Math.abs(target.motionX) > 0.10000000149011612D || Math.abs(target.motionY) > 0.10000000149011612D ||
                Math.abs(target.motionZ) > 0.10000000149011612D) {
            target.attackEntityFrom(DamageSource.causeMobDamage(this.attacker), damage);
        } else {
            target.attackEntityFrom(DamageSource.causeMobDamage(this.attacker), damage + 1.0F);
        }
    }

    private float getAttackRange(EntityLivingBase target) {
        return this.range;
    }
}