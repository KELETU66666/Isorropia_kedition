 package fr.wind_blade.isorropia.common.entities.projectile;
 
 import fr.wind_blade.isorropia.common.items.ItemsIS;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.entity.passive.EntityChicken;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.entity.projectile.EntityEgg;
 import net.minecraft.entity.projectile.EntityThrowable;
 import net.minecraft.item.Item;
 import net.minecraft.util.DamageSource;
 import net.minecraft.util.EnumParticleTypes;
 import net.minecraft.util.datafix.DataFixer;
 import net.minecraft.util.math.RayTraceResult;
 import net.minecraft.world.World;
 
 
 public class EntityIncubatedEgg
   extends EntityEgg
 {
   public EntityIncubatedEgg(World worldIn) {
/* 22 */     super(worldIn);
   }
   
   public EntityIncubatedEgg(World worldIn, EntityPlayer playerIn) {
      super(worldIn, (EntityLivingBase)playerIn);
   }
   
   public static void registerFixesEgg(DataFixer fixer) {
/* 30 */     EntityThrowable.registerFixesThrowable(fixer, "ThrownEgg");
   }
 
   
   protected void onImpact(RayTraceResult result) {
/* 35 */     if (result.entityHit != null) {
/* 36 */       result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage((Entity)this, (Entity)getThrower()), 0.0F);
     }
/* 38 */     if (!this.world.isRemote) {
/* 39 */       EntityChicken entitychicken = new EntityChicken(this.world);
/* 40 */       entitychicken.setGrowingAge(-24000);
        entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
/* 42 */       this.world.spawnEntity((Entity)entitychicken);
     } 
     
/* 45 */     for (int i = 0; i < 8; i++) {
/* 46 */       this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, (this.rand
/* 47 */           .nextFloat() - 0.5D) * 0.08D, (this.rand.nextFloat() - 0.5D) * 0.08D, (this.rand
/* 48 */           .nextFloat() - 0.5D) * 0.08D, new int[] { Item.getIdFromItem(ItemsIS.itemIncubatedEgg) });
     } 
     
/* 51 */     if (!this.world.isRemote)
/* 52 */       setDead(); 
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\entities\projectile\EntityIncubatedEgg.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */