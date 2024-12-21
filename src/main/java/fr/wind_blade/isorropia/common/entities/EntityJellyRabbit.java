package fr.wind_blade.isorropia.common.entities;

import fr.wind_blade.isorropia.common.items.ItemsIS;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aura.AuraHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EntityJellyRabbit
        extends EntityRabbit implements IShearable {
    private static final DataParameter<String> aspect = EntityDataManager.createKey(EntityJellyRabbit.class, DataSerializers.STRING);

    private static final DataParameter<Integer> jelly = EntityDataManager.createKey(EntityJellyRabbit.class, DataSerializers.VARINT);

    private static final int JELLY_BY_TICKS = 8000;

    private static final int AURA_BY_JELLY = 100;

    private static final int cooldown = 80;

    private int count;

    private Color color = new Color(Aspect.ORDER.getColor());

    public EntityJellyRabbit(World worldIn) {
        super(worldIn);
    }


    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(aspect, Aspect.ORDER.getTag());
        this.dataManager.register(jelly, 0);
    }


    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        Aspect[] displayAspects = (Aspect[]) Aspect.aspects.values().toArray((Object[]) new Aspect[0]);
        setAspect(displayAspects[this.world.rand.nextInt(displayAspects.length)]);
        setJellySize(2);
        return super.onInitialSpawn(difficulty, livingdata);
    }


    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.fallDistance = 0.0F;

        if (this.world.isRemote || getJellySize() > 8) {
            return;
        }
        if (this.count % 80 == 0) {
            if (getAspect() == Aspect.FLUX) {
                if (AuraHelper.getFlux(getEntityWorld(), getPosition()) > 0.0F) {
                    AuraHelper.addVis(this.world, getPosition(), AuraHelper.drainFlux(this.world, getPosition(), 10.0F, false));
                }
            } else {
                AuraHelper.drainVis(getEntityWorld(), getPosition(), 100.0F, false);
            }
        }

        if (this.count >= 8000) {
            setJellySize(getJellySize() + 1);
            this.count = 0;
        }

        this.count++;
    }


    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (getJellySize() > 0 && source != DamageSource.OUT_OF_WORLD) {

            Entity entity = source.getTrueSource();
            if (entity != null) {
                double d1 = entity.posX - this.posX;

                double d0;
                for (d0 = entity.posZ - this.posZ; d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D) {
                    d1 = (Math.random() - Math.random()) * 0.01D;
                }

                this.attackedAtYaw = (float) (MathHelper.atan2(d0, d1) * 57.29577951308232D - this.rotationYaw);
                knockBack(entity, 0.4F, d1, d0);

                if (entity instanceof EntityLivingBase) {
                    setRevengeTarget((EntityLivingBase) entity);
                }
            }
            playSound((getJellySize() > 0) ? SoundEvents.ENTITY_SLIME_HURT : SoundEvents.ENTITY_RABBIT_HURT, 1.0F,
                    getSoundPitch());

            if (!this.world.isRemote) {
                entityDropItem(getJelly(), 0.0F);
            }

            return false;
        }
        return super.attackEntityFrom(source, amount);
    }

    public ItemStack getJelly() {
        ItemStack stack = new ItemStack(ItemsIS.itemJelly);
        Aspect apsect = getAspect();

        if (apsect != null) {
            ItemsIS.itemJelly.setAspects(stack, (new AspectList()).add(apsect, 5));
        }

        setJellySize(getJellySize() - 1);
        return stack;
    }


    public String getName() {
        if (hasCustomName()) {
            return getCustomNameTag();
        }
        String tag = this.dataManager.get(aspect);
        String s = EntityList.getEntityString(this);
        String name = "";

        if (s == null)
            s = "generic";
        if (tag == null || tag.isEmpty()) {
            name = I18n.format("entity." + s + ".name");
        } else {

            name = I18n.hasKey("entity." + s + "." + tag + ".name") ? I18n.format("entity." + s + "." + tag + ".name") : I18n.format("entity." + s + ".aspect.name", tag);
        }
        return name;
    }


    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        Aspect aspect = getAspect();
        compound.setString("tag", (aspect != null) ? aspect.getTag() : "");
        compound.setInteger("jelly", this.dataManager.get(jelly).intValue());
    }


    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setAspect(Aspect.getAspect(compound.getString("tag")));
        this.dataManager.set(jelly, Integer.valueOf(compound.getInteger("jelly")));
    }


    public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos) {
        return (getJellySize() > 0);
    }


    public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        List<ItemStack> stacks = new ArrayList<>();
        stacks.add(getJelly());
        return stacks;
    }

    public void setAspect(Aspect jellyAspect) {
        this.dataManager.set(aspect, jellyAspect.getTag());

        if (this.world.isRemote) {
            this.color = new Color(jellyAspect.getColor());
        }
    }

    public Aspect getAspect() {
        return Aspect.getAspect(this.dataManager.get(aspect));
    }

    public int getJellySize() {
        return this.dataManager.get(jelly).intValue();
    }

    public void setJellySize(int newJellySize) {
        this.dataManager.set(jelly, Integer.valueOf(newJellySize));
    }


    public Color getColor() {
        return this.color;
    }
}