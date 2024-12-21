package fr.wind_blade.isorropia.common.entities;

import com.google.common.collect.Sets;
import fr.wind_blade.isorropia.common.entities.ai.EntityAIEatTaint;
import fr.wind_blade.isorropia.common.entities.ai.EntityAITaintTempt;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.world.World;
import thaumcraft.api.damagesource.DamageSourceThaumcraft;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.api.potions.PotionFluxTaint;
import thaumcraft.api.potions.PotionVisExhaust;
import thaumcraft.common.entities.monster.tainted.EntityTaintCrawler;
import thaumcraft.common.lib.potions.PotionInfectiousVisExhaust;

import java.util.Set;


public class EntityTaintPig extends EntityPig {
    private static final Set<Item> TEMPTATION_ITEMS = Sets.newHashSet(ItemsTC.bottleTaint);

    public EntityTaintPig(World worldIn) {
        super(worldIn);
    }


    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.25D));
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(3, new EntityAIEatTaint(this));
        this.tasks.addTask(4, new EntityAITaintTempt(this, 1.2D, false, TEMPTATION_ITEMS));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityTaintCrawler.class, true));
    }


    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
    }


    public void updateAITasks() {
        super.updateAITasks();

        if (isPotionActive(PotionFluxTaint.instance))
            removePotionEffect(PotionFluxTaint.instance);
        if (isPotionActive(PotionVisExhaust.instance))
            removePotionEffect(PotionVisExhaust.instance);
        if (isPotionActive(PotionInfectiousVisExhaust.instance)) {
            removePotionEffect(PotionInfectiousVisExhaust.instance);
        }
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        setLastAttackedEntity(entityIn);

        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this),
                (float) getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());

        if (flag) {
            applyEnchantments(this, entityIn);
        }

        return flag;
    }


    public boolean isEntityInvulnerable(DamageSource source) {
        if (source instanceof EntityDamageSource) {
            Entity entity = source.getTrueSource();

            if (entity instanceof thaumcraft.api.entities.ITaintedMob) {
                return true;
            }
        } else if (source instanceof DamageSourceThaumcraft && source.damageType.equals("taint")) {
            return true;
        }

        return super.isEntityInvulnerable(source);
    }


    public boolean isInLove() {
        return true;
    }


    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }
}