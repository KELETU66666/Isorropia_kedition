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
        super(worldIn);
    }

    public EntityIncubatedEgg(World worldIn, EntityPlayer playerIn) {
        super(worldIn, (EntityLivingBase)playerIn);
    }

    public static void registerFixesEgg(DataFixer fixer) {
        EntityThrowable.registerFixesThrowable(fixer, "ThrownEgg");
    }


    protected void onImpact(RayTraceResult result) {
        if (result.entityHit != null) {
            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage((Entity)this, (Entity)getThrower()), 0.0F);
        }
        if (!this.world.isRemote) {
            EntityChicken entitychicken = new EntityChicken(this.world);
            entitychicken.setGrowingAge(-24000);
            entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
            this.world.spawnEntity((Entity)entitychicken);
        }

        for (int i = 0; i < 8; i++) {
            this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, (this.rand
                    .nextFloat() - 0.5D) * 0.08D, (this.rand.nextFloat() - 0.5D) * 0.08D, (this.rand
                    .nextFloat() - 0.5D) * 0.08D, new int[] { Item.getIdFromItem(ItemsIS.itemIncubatedEgg) });
        }

        if (!this.world.isRemote)
            setDead();
    }
}