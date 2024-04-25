package fr.wind_blade.isorropia.common.entities.projectile;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;


public class EntityEmber extends EntityThrowable implements IEntityAdditionalSpawnData {
    public int duration = 20;
    public int firey = 0;
    public float damage = 1.0F;

    public EntityEmber(World par1World) {
        super(par1World);
    }

    public EntityEmber(World par1World, EntityLivingBase par2EntityLiving, float scatter) {
        super(par1World, par2EntityLiving);
        Vec3d v = par2EntityLiving.getLookVec();
        this.setLocationAndAngles(par2EntityLiving.posX + v.x / 2.0, par2EntityLiving.posY + (double)par2EntityLiving.getEyeHeight() + v.y / 2.0, par2EntityLiving.posZ + v.z / 2.0, par2EntityLiving.rotationYaw, par2EntityLiving.rotationPitch);
        this.shoot(this.motionX, this.motionY, this.motionZ, getVelocity(), scatter);
    }

    protected float getGravityVelocity() {
        return 0.0F;
    }

    protected float getVelocity() {
        return 1.0F;
    }

    @Override
    public void onUpdate() {
        if (this.ticksExisted > this.duration) {
            setDead();
        }
        if (this.duration <= 20) {
            this.motionX *= 0.95D;
            this.motionY *= 0.95D;
            this.motionZ *= 0.95D;
        } else {
            this.motionX *= 0.975D;
            this.motionY *= 0.975D;
            this.motionZ *= 0.975D;
        }
        if (this.onGround) {
            this.motionX *= 0.66D;
            this.motionY *= 0.66D;
            this.motionZ *= 0.66D;
        }
        super.onUpdate();
    }


    public void writeSpawnData(ByteBuf data) {
        data.writeByte(this.duration);
    }


    public void readSpawnData(ByteBuf data) {
        this.duration = data.readByte();
    }

    protected void onImpact(RayTraceResult mop) {
        if (!this.world.isRemote) {
            if (mop.entityHit != null) {
                if (!mop.entityHit.isImmuneToFire() && mop.entityHit.attackEntityFrom(new EntityDamageSourceIndirect("fireball", (Entity)this, (Entity)this.getThrower()).setFireDamage(), this.damage)) {
                    mop.entityHit.setFire(3 + this.firey);
                }
            } else if (this.rand.nextFloat() < 0.025f * (float)this.firey) {
                BlockPos blockpos;
                boolean flag = true;
                if (this.getThrower() != null && this.getThrower() instanceof EntityLiving) {
                    flag = this.world.getGameRules().getBoolean("mobGriefing");
                }
                if (flag && this.world.isAirBlock(blockpos = mop.getBlockPos().offset(mop.sideHit))) {
                    this.world.setBlockState(blockpos, Blocks.FIRE.getDefaultState());
                }
            }
        }
        this.setDead();
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    public float getShadowSize() {
        return 0.0F;
    }


    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setFloat("damage", this.damage);
        par1NBTTagCompound.setInteger("firey", this.firey);
        par1NBTTagCompound.setInteger("duration", this.duration);
    }


    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readEntityFromNBT(par1NBTTagCompound);
        this.damage = par1NBTTagCompound.getFloat("damage");
        this.firey = par1NBTTagCompound.getInteger("firey");
        this.duration = par1NBTTagCompound.getInteger("duration");
    }

    public boolean canBeCollidedWith() {
        return false;
    }


    public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
        return false;
    }
}