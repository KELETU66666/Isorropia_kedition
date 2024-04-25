//
// Decompiled by Procyon v0.5.30
//

package fr.wind_blade.isorropia.common.entities;

import fr.wind_blade.isorropia.common.entities.projectile.EntityEmber;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.common.lib.SoundsTC;

public class EntityHellHound extends EntityWolf {

    long soundDelay;

    public EntityHellHound(final World p_i1696_1_) {
        super(p_i1696_1_);
        this.soundDelay = 0L;
        this.isImmuneToFire = true;
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        EntityLivingBase target = null;
        if (this.getAttackTarget() != null) {
            target = this.getAttackTarget();
        }
        if (this.getAttackTarget() != null) {
            target = this.getAttackTarget();
        }
        if (target != null) {
            if (!this.world.isRemote && this.soundDelay < System.currentTimeMillis()) {
                this.world.playSound(null, this.getPosition(), SoundsTC.zap, SoundCategory.NEUTRAL, 0.33f, 2.0f);
                this.soundDelay = System.currentTimeMillis() + 500L;
            }
            final float scatter = 8.0f;
            final EntityEmber orb = new EntityEmber(this.world, this, scatter);
            orb.damage = 1.0f;
            orb.firey = 1;
            Vec3d v = this.getLookVec();
            orb.setLocationAndAngles(this.posX + v.x / 2.0, this.posY + (double)this.getEyeHeight() + v.y / 2.0, this.posZ + v.z / 2.0, this.rotationYaw, this.rotationPitch);
            orb.shoot(this, this.rotationPitch, this.rotationYaw, 0.0F, 1.0f, scatter);
            this.world.spawnEntity(orb);
        }
    }
}