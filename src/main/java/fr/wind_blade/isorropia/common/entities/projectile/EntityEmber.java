package fr.wind_blade.isorropia.common.entities.projectile;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;


public class EntityEmber extends EntityThrowable implements IEntityAdditionalSpawnData {
    public int duration;
    public int firey;
    public float damage;

    public EntityEmber(World par1World) {
        super(par1World);
        this.duration = 20;
        this.firey = 0;
        this.damage = 1.0F;
    }

    public EntityEmber(World par1World, EntityLivingBase par2EntityLiving, float scatter) {
        super(par1World, par2EntityLiving);
        this.duration = 20;
        this.firey = 0;
        this.damage = 1.0F;
        shoot(this.motionX, this.motionY, this.motionZ, lolreplace(), scatter);
    }

    protected float getGravityVelocity() {
        return 0.0F;
    }

    protected float lolreplace() {
        return 1.0F;
    }


    public void onEntityUpdate() {
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
        super.onEntityUpdate();
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
                if (!mop.entityHit.isImmuneToFire()) if (mop.entityHit.attackEntityFrom((new EntityDamageSourceIndirect("fireball", (Entity)this, (Entity)
                        getThrower())).setFireDamage(), this.damage)) {
                    mop.entityHit.setFire(3 + this.firey);
                }
            } else if (this.rand.nextFloat() < 0.025F * this.firey) {
                double i = mop.entityHit.posX;
                double j = mop.entityHit.posY;
                double k = mop.entityHit.posZ;
                switch (mop.sideHit.getIndex()) {
                    case 0:
                        j--;
                        break;
                        case 1:
                            j++;
                            break;
                            case 2:
                                k--;
                                break;
                                case 3:
                                    k++;
                                    break;
                                    case 4:
                                        i--;
                                        break;
                                        case 5:
                                            i++;
                                            break;
                }
                if (this.world.isAirBlock(new BlockPos(i, j, k))) {
                    this.world.setBlockState(new BlockPos(i, j, k), Blocks.FIRE.getDefaultState());
                }
            }
        }
        setDead();
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