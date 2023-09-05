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
 
 
 public class EntityEmber
   extends EntityThrowable
   implements IEntityAdditionalSpawnData
 {
   public int duration;
   public int firey;
   public float damage;
   
   public EntityEmber(World par1World) {
/*  26 */     super(par1World);
/*  27 */     this.duration = 20;
/*  28 */     this.firey = 0;
/*  29 */     this.damage = 1.0F;
   }
   
   public EntityEmber(World par1World, EntityLivingBase par2EntityLiving, float scatter) {
/*  33 */     super(par1World, par2EntityLiving);
/*  34 */     this.duration = 20;
/*  35 */     this.firey = 0;
/*  36 */     this.damage = 1.0F;
/*  37 */     shoot(this.motionX, this.motionY, this.motionZ, lolreplace(), scatter);
   }
   
   protected float getGravityVelocity() {
/*  41 */     return 0.0F;
   }
   
   protected float lolreplace() {
/*  45 */     return 1.0F;
   }
 
   
   public void onEntityUpdate() {
/*  50 */     if (this.ticksExisted > this.duration) {
/*  51 */       setDead();
     }
/*  53 */     if (this.duration <= 20) {
/*  54 */       this.motionX *= 0.95D;
/*  55 */       this.motionY *= 0.95D;
/*  56 */       this.motionZ *= 0.95D;
     } else {
/*  58 */       this.motionX *= 0.975D;
/*  59 */       this.motionY *= 0.975D;
/*  60 */       this.motionZ *= 0.975D;
     } 
/*  62 */     if (this.onGround) {
/*  63 */       this.motionX *= 0.66D;
/*  64 */       this.motionY *= 0.66D;
/*  65 */       this.motionZ *= 0.66D;
     } 
/*  67 */     super.onEntityUpdate();
   }
 
   
   public void writeSpawnData(ByteBuf data) {
/*  72 */     data.writeByte(this.duration);
   }
 
   
   public void readSpawnData(ByteBuf data) {
/*  77 */     this.duration = data.readByte();
   }
 
   
   protected void onImpact(RayTraceResult mop) {
/*  82 */     if (!this.world.isRemote) {
/*  83 */       if (mop.entityHit != null) {
/*  84 */         if (!mop.entityHit.isImmuneToFire()) if (mop.entityHit.attackEntityFrom((new EntityDamageSourceIndirect("fireball", (Entity)this, (Entity)
/*  85 */                 getThrower())).setFireDamage(), this.damage)) {
/*  86 */             mop.entityHit.setFire(3 + this.firey);
           } 
/*  88 */       } else if (this.rand.nextFloat() < 0.025F * this.firey) {
/*  89 */         double i = mop.entityHit.posX;
/*  90 */         double j = mop.entityHit.posY;
/*  91 */         double k = mop.entityHit.posZ;
/*  92 */         switch (mop.sideHit.getIndex()) {
           case 0:
/*  94 */             j--;
             break;
           
           case 1:
             j++;
             break;
           
           case 2:
/* 102 */             k--;
             break;
           
           case 3:
/* 106 */             k++;
             break;
           
           case 4:
/* 110 */             i--;
             break;
           
           case 5:
/* 114 */             i++;
             break;
         } 
/* 117 */         if (this.world.isAirBlock(new BlockPos(i, j, k))) {
/* 118 */           this.world.setBlockState(new BlockPos(i, j, k), Blocks.FIRE.getDefaultState());
         }
       } 
     }
/* 122 */     setDead();
   }
   
   protected boolean canTriggerWalking() {
/* 126 */     return false;
   }
   
   public float getShadowSize() {
/* 130 */     return 0.0F;
   }
 
   
   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
/* 135 */     super.writeEntityToNBT(par1NBTTagCompound);
/* 136 */     par1NBTTagCompound.setFloat("damage", this.damage);
/* 137 */     par1NBTTagCompound.setInteger("firey", this.firey);
/* 138 */     par1NBTTagCompound.setInteger("duration", this.duration);
   }
 
   
   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
/* 143 */     super.readEntityFromNBT(par1NBTTagCompound);
/* 144 */     this.damage = par1NBTTagCompound.getFloat("damage");
/* 145 */     this.firey = par1NBTTagCompound.getInteger("firey");
/* 146 */     this.duration = par1NBTTagCompound.getInteger("duration");
   }
   
   public boolean canBeCollidedWith() {
/* 150 */     return false;
   }
 
   
   public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
/* 155 */     return false;
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\entities\projectile\EntityEmber.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */