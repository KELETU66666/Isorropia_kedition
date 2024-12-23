package fr.wind_blade.isorropia.common.entities;

import com.rumaruka.thaumicbases.api.ITobacco;
import com.rumaruka.thaumicbases.init.TBItems;
import static com.rumaruka.thaumicbases.init.TBItems.*;
import fr.wind_blade.isorropia.common.IsorropiaAPI;
import fr.wind_blade.isorropia.common.items.misc.ItemCat;
import fr.wind_blade.isorropia.common.research.recipes.SpecieCurativeInfusionRecipe;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class EntityDopeSquid extends EntityTameable {
    public float squidPitch;
    public float prevSquidPitch;
    public float squidYaw;
    public float prevSquidYaw;
    public float squidRotation;
    public float prevSquidRotation;
    public float tentacleAngle;
    public float lastTentacleAngle;
    private float randomMotionSpeed;
    private float rotationVelocity;
    private float rotateSpeed;
    private float randomMotionVecX;
    private float randomMotionVecY;
    private float randomMotionVecZ;

    public EntityDopeSquid(World worldIn) {
        super(worldIn);
        this.setSize(0.95F, 0.95F);
        this.rand.setSeed(1 + this.getEntityId());
        this.rotationVelocity = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(0, new EntityDopeSquid.AIMoveRandom(this));
    }

    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return null;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(TOBACCO_TYPE, (byte) 9);
    }

    // Tobacco type data parameter
    private static final DataParameter<Byte> TOBACCO_TYPE = EntityDataManager.createKey(EntityDopeSquid.class, DataSerializers.BYTE);

    public void setTobacco(byte t) {
        this.dataManager.set(TOBACCO_TYPE, t);
    }

    public byte getTobacco() {
        return this.dataManager.get(TOBACCO_TYPE);
    }

    private int getTobaccoType(ITobacco stack) {
        if (stack == TBItems.tobacco_pile)
            return 0;
        if (stack == TBItems.tobacco_eldritch)
            return 1;
        if (stack == TBItems.tobacco_fighting)
            return 2;
        if (stack == TBItems.tobacco_hunger)
            return 3;
        if (stack == TBItems.tobacco_knowledge)
            return 4;
        if (stack == TBItems.tobacco_mining)
            return 5;
        if (stack == TBItems.tobacco_sanity)
            return 6;
        if (stack == TBItems.tobacco_tainted)
            return 7;
        if (stack == TBItems.tobacco_wispy)
            return 8;
        return 0;
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if (hand == EnumHand.MAIN_HAND)
            if (!player.world.isRemote) {
                if (this.isTamed() && this.getOwner() == player) {
                    if (!stack.isEmpty() && stack.getItem() instanceof ITobacco) {
                        setTobacco((byte) getTobaccoType((ITobacco) stack.getItem()));
                        stack.shrink(1);
                    } else {
                        this.setSitting(!this.isSitting());
                    }
                }

                if (!stack.isEmpty() && stack.getItem() == TBItems.tobacco_tainted) {
                    if (!this.isTamed()) {
                        this.setTamedBy(player);
                        this.setSitting(true);
                        this.playTameEffect(true);
                        this.world.setEntityState(this, (byte) 7);
                    } else if (this.getOwner() == player) {
                        this.heal(5.0F);
                        this.playTameEffect(true);
                    }
                    stack.shrink(1);
                }
            }

        return super.processInteract(player, hand);
    }

    public void fall(float a, float b) {
    }

    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
    }

    public float getEyeHeight() {
        return this.height * 0.5F;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SQUID_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_SQUID_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SQUID_DEATH;
    }

    protected float getSoundVolume() {
        return 0.4F;
    }

    protected Item getDropItem() {
        return Item.getItemById(0);
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (this.isEntityAlive()) {
            // Update water check
            boolean isInWater = this.isInWater();

            // Handle air underwater
            if (!isInWater) {
                int air = this.getAir();
                --air;
                this.setAir(air);
                if (this.getAir() == -20) {
                    this.setAir(0);
                    this.attackEntityFrom(DamageSource.DROWN, 2.0F);
                }
            } else {
                this.setAir(300);
            }

            // Update animation values
            this.prevSquidPitch = this.squidPitch;
            this.prevSquidYaw = this.squidYaw;
            this.prevSquidRotation = this.squidRotation;
            this.lastTentacleAngle = this.tentacleAngle;
            this.squidRotation += this.rotationVelocity;

            if (!this.isSitting() && isInWater) {
                // Rotation logic
                if (this.squidRotation > 6.283185307179586D) {
                    this.squidRotation = 0.0F;
                    if (this.rand.nextInt(10) == 0) {
                        this.rotationVelocity = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
                    }
                }

                // Movement and animation
                if (this.squidRotation < 3.1415927F) {
                    float f = this.squidRotation / 3.1415927F;
                    this.tentacleAngle = MathHelper.sin(f * f * 3.1415927F) * 3.1415927F * 0.25F;

                    if ((double) f > 0.75D) {
                        this.randomMotionSpeed = 1.0F;
                        this.rotateSpeed = 1.0F;
                    } else {
                        this.rotateSpeed *= 0.8F;
                    }
                } else {
                    this.tentacleAngle = 0.0F;
                    this.randomMotionSpeed *= 0.9F;
                    this.rotateSpeed *= 0.99F;
                }

                // Apply motion
                if (!this.world.isRemote) {
                    double moveSpeed = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
                    this.motionX = this.randomMotionVecX * this.randomMotionSpeed * moveSpeed;
                    this.motionY = this.randomMotionVecY * this.randomMotionSpeed * moveSpeed;
                    this.motionZ = this.randomMotionVecZ * this.randomMotionSpeed * moveSpeed;

                    // Actually move the entity
                    this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                }

                // Update rotation
                float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                this.renderYawOffset += (-((float) MathHelper.atan2(this.motionX, this.motionZ)) * (180F / (float) Math.PI) - this.renderYawOffset) * 0.1F;
                this.rotationYaw = this.renderYawOffset;
                this.squidYaw = (float) ((double) this.squidYaw + Math.PI * (double) this.rotateSpeed * 1.5D);
                this.squidPitch += (-((float) MathHelper.atan2(f, this.motionY)) * (180F / (float) Math.PI) - this.squidPitch) * 0.1F;
            } else {
                // Out of water or sitting behavior
                this.tentacleAngle = MathHelper.abs(MathHelper.sin(this.squidRotation)) * (float) Math.PI * 0.25F;

                if (!this.world.isRemote) {
                    this.motionX = 0.0D;
                    this.motionY -= 0.08D;
                    this.motionY *= 0.9800000190734863D;
                    this.motionZ = 0.0D;

                    // Move entity when falling
                    this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                }

                this.squidPitch = (float) ((double) this.squidPitch + (double) (-90.0F - this.squidPitch) * 0.02D);
            }
        }

        if (!this.world.isRemote) {
            EntityLivingBase owner = this.getOwner();
            if (ticksExisted % 400 == 0 && owner instanceof EntityPlayer && getDistanceSq(owner) < 15f) {
                EntityPlayer player = (EntityPlayer) owner;
                performTobaccoEffect(player, getTobacco());
            }
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setByte("Tobacco", this.getTobacco());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setTobacco(compound.getByte("Tobacco"));
    }

    private void performTobaccoEffect(EntityPlayer player, byte tobaccoType) {
        // Use TB's tobacco effect system directly
        ItemStack tobaccoStack = getTobaccoStack(tobaccoType);
        if (!tobaccoStack.isEmpty() && tobaccoStack.getItem() instanceof ITobacco) {
            ((ITobacco) tobaccoStack.getItem()).performTobaccoEffect(player, tobaccoStack, false);
        }

        // Spawn smoke particles for visual feedback
        if (world.isRemote) {
            for (int i = 0; i < 8; i++) {
                double dx = rand.nextGaussian() * 0.02D;
                double dy = rand.nextGaussian() * 0.02D;
                double dz = rand.nextGaussian() * 0.02D;

                world.spawnParticle(
                        EnumParticleTypes.SMOKE_NORMAL,
                        posX + (rand.nextDouble() - 0.5D) * width,
                        posY + rand.nextDouble() * height,
                        posZ + (rand.nextDouble() - 0.5D) * width,
                        dx, dy, dz
                );
            }
        }
    }

    public void setMovementVector(float randomMotionVecXIn, float randomMotionVecYIn, float randomMotionVecZIn) {
        this.randomMotionVecX = randomMotionVecXIn;
        this.randomMotionVecY = randomMotionVecYIn;
        this.randomMotionVecZ = randomMotionVecZIn;
    }

    public boolean hasMovementVector() {
        return this.randomMotionVecX != 0.0F || this.randomMotionVecY != 0.0F || this.randomMotionVecZ != 0.0F;
    }

    @Override
    public boolean isInWater() {
        return this.world.handleMaterialAcceleration(this.getEntityBoundingBox(), Material.WATER, this);
    }

    // Update AI task to properly check water state
    static class AIMoveRandom extends EntityAIBase {
        private final EntityDopeSquid squid;

        public AIMoveRandom(EntityDopeSquid p_i45859_1_) {
            this.squid = p_i45859_1_;
            this.setMutexBits(2);  // Add this to prevent conflicting AI tasks
        }

        @Override
        public boolean shouldExecute() {
            return !squid.isSitting() && squid.isInWater();  // Only execute when in water
        }

        @Override
        public void updateTask() {
            if (squid.getIdleTime() > 100) {
                squid.setMovementVector(0.0F, 0.0F, 0.0F);
            } else if (squid.getRNG().nextInt(50) == 0 || !squid.isInWater() || !squid.hasMovementVector()) {
                float f = squid.getRNG().nextFloat() * ((float) Math.PI * 2F);
                float f1 = MathHelper.cos(f) * 0.2F;
                float f2 = -0.1F + squid.getRNG().nextFloat() * 0.2F;
                float f3 = MathHelper.sin(f) * 0.2F;
                squid.setMovementVector(f1, f2, f3);
            }
        }
    }

    public static ItemStack getTobaccoStack(byte type) {
        Item item;
        switch (type) {
            case 0:
                item = tobacco_pile;
                break;
            case 1:
                item = tobacco_eldritch;
                break;
            case 2:
                item = tobacco_fighting;
                break;
            case 3:
                item = tobacco_hunger;
                break;
            case 4:
                item = tobacco_knowledge;
                break;
            case 5:
                item = tobacco_mining;
                break;
            case 6:
                item = tobacco_sanity;
                break;
            case 7:
                item = tobacco_tainted;
                break;
            case 8:
                item = tobacco_wispy;
                break;
            default:
                item = tobacco_pile;
                break;
        }
        return new ItemStack(item);
    }

    public static void makeDopeSquidRecipe() {
        IsorropiaAPI.registerCreatureInfusionRecipe(new ResourceLocation("isorropia", "dope_squid"), ((SpecieCurativeInfusionRecipe.Builder)
                new SpecieCurativeInfusionRecipe.Builder().withAspects(new AspectList().add(Aspect.PLANT, 50).add(Aspect.MAN, 50).add(Aspect.MAGIC, 50).add(Aspect.FLIGHT, 50))
                        .withComponents(
                                Ingredient.fromStacks(ThaumcraftApiHelper.makeCrystal(Aspect.FIRE)),
                                Ingredient.fromItem(tobacco_knowledge),
                                Ingredient.fromItem(tobacco_eldritch),
                                Ingredient.fromItem(tobacco_fighting),
                                Ingredient.fromItem(tobacco_hunger),
                                Ingredient.fromStacks(ThaumcraftApiHelper.makeCrystal(Aspect.AIR)),
                                Ingredient.fromItem(tobacco_mining),
                                Ingredient.fromItem(tobacco_wispy),
                                Ingredient.fromItem(tobacco_sanity),
                                Ingredient.fromItem(tobacco_tainted))
                        .withInstability(8))
                .withResult(EntityDopeSquid.class)
                .withKnowledgeRequirement("DOPESQUIDINFUSION")
                .withPredicate(entity -> entity.getClass() == EntitySquid.class)
                .withFakeIngredients(Ingredient.fromStacks(ItemCat.createCat(ItemCat.EnumCat.SQUID, "Squid")), ItemCat.createCat(ItemCat.EnumCat.SQUID, "DopeSquid")).build());
    }
}