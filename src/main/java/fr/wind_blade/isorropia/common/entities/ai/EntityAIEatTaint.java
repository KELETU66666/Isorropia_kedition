package fr.wind_blade.isorropia.common.entities.ai;

import fr.wind_blade.isorropia.common.Common;
import fr.wind_blade.isorropia.common.config.Config;
import fr.wind_blade.isorropia.common.network.ParticuleDestroyMessage;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import thaumcraft.api.ThaumcraftMaterials;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.blocks.world.taint.BlockTaintLog;

import java.util.HashMap;
import java.util.Map;




public class EntityAIEatTaint
        extends EntityAIBase
{
    public static final Map<Biome, BlockPlanks.EnumType> WOOD_IN_BIOMES = new HashMap<>(); private EntityLiving living;

    static {
        WOOD_IN_BIOMES.put(Biomes.EXTREME_HILLS, BlockPlanks.EnumType.SPRUCE);
        WOOD_IN_BIOMES.put(Biomes.TAIGA, BlockPlanks.EnumType.SPRUCE);
        WOOD_IN_BIOMES.put(Biomes.TAIGA_HILLS, BlockPlanks.EnumType.SPRUCE);
        WOOD_IN_BIOMES.put(Biomes.EXTREME_HILLS_EDGE, BlockPlanks.EnumType.SPRUCE);
        WOOD_IN_BIOMES.put(Biomes.COLD_TAIGA, BlockPlanks.EnumType.SPRUCE);
        WOOD_IN_BIOMES.put(Biomes.COLD_TAIGA_HILLS, BlockPlanks.EnumType.SPRUCE);
        WOOD_IN_BIOMES.put(Biomes.REDWOOD_TAIGA, BlockPlanks.EnumType.SPRUCE);
        WOOD_IN_BIOMES.put(Biomes.REDWOOD_TAIGA_HILLS, BlockPlanks.EnumType.SPRUCE);
        WOOD_IN_BIOMES.put(Biomes.RIVER, BlockPlanks.EnumType.SPRUCE);
        WOOD_IN_BIOMES.put(Biomes.MUTATED_EXTREME_HILLS, BlockPlanks.EnumType.SPRUCE);
        WOOD_IN_BIOMES.put(Biomes.MUTATED_TAIGA, BlockPlanks.EnumType.SPRUCE);
        WOOD_IN_BIOMES.put(Biomes.MUTATED_TAIGA_COLD, BlockPlanks.EnumType.SPRUCE);
        WOOD_IN_BIOMES.put(Biomes.MUTATED_REDWOOD_TAIGA, BlockPlanks.EnumType.SPRUCE);
        WOOD_IN_BIOMES.put(Biomes.MUTATED_REDWOOD_TAIGA_HILLS, BlockPlanks.EnumType.SPRUCE);
        WOOD_IN_BIOMES.put(Biomes.MUTATED_EXTREME_HILLS_WITH_TREES, BlockPlanks.EnumType.SPRUCE);
        WOOD_IN_BIOMES.put(Biomes.BIRCH_FOREST, BlockPlanks.EnumType.BIRCH);
        WOOD_IN_BIOMES.put(Biomes.BIRCH_FOREST_HILLS, BlockPlanks.EnumType.BIRCH);
        WOOD_IN_BIOMES.put(Biomes.MUTATED_BIRCH_FOREST, BlockPlanks.EnumType.BIRCH);
        WOOD_IN_BIOMES.put(Biomes.MUTATED_BIRCH_FOREST_HILLS, BlockPlanks.EnumType.BIRCH);
        WOOD_IN_BIOMES.put(Biomes.JUNGLE, BlockPlanks.EnumType.JUNGLE);
        WOOD_IN_BIOMES.put(Biomes.JUNGLE_HILLS, BlockPlanks.EnumType.JUNGLE);
        WOOD_IN_BIOMES.put(Biomes.JUNGLE_EDGE, BlockPlanks.EnumType.JUNGLE);
        WOOD_IN_BIOMES.put(Biomes.MUTATED_JUNGLE, BlockPlanks.EnumType.JUNGLE);
        WOOD_IN_BIOMES.put(Biomes.MUTATED_JUNGLE_EDGE, BlockPlanks.EnumType.JUNGLE);
        WOOD_IN_BIOMES.put(Biomes.SAVANNA, BlockPlanks.EnumType.ACACIA);
        WOOD_IN_BIOMES.put(Biomes.SAVANNA_PLATEAU, BlockPlanks.EnumType.ACACIA);
        WOOD_IN_BIOMES.put(Biomes.MUTATED_SAVANNA, BlockPlanks.EnumType.ACACIA);
        WOOD_IN_BIOMES.put(Biomes.MUTATED_SAVANNA_ROCK, BlockPlanks.EnumType.ACACIA);
        WOOD_IN_BIOMES.put(Biomes.ROOFED_FOREST, BlockPlanks.EnumType.DARK_OAK);
        WOOD_IN_BIOMES.put(Biomes.MUTATED_ROOFED_FOREST, BlockPlanks.EnumType.DARK_OAK);
    }


    private BlockPos targetPos;
    int cooldown;

    public EntityAIEatTaint(EntityLiving living) {
        this.living = living;
    }


    public boolean shouldExecute() {
        if (this.cooldown > 0) {
            this.cooldown--;
            return false;
        }
        return findTaint();
    }

    private boolean findTaint() {
        this.targetPos = null;
        for (BlockPos pos : BlockPos.getAllInBoxMutable(this.living.getPosition().add(-5, -2, -5), this.living
                .getPosition().add(5, 5, 5))) {
            if (this.living.world.getBlockState(pos).getMaterial() != ThaumcraftMaterials.MATERIAL_TAINT ||
                    this.living.getNavigator().getPathToXYZ(pos.getX(), pos.getY(), pos.getZ()) == null)
                continue;
            this.targetPos = pos;
            return true;
        }


        for (int tries = 0; tries < 30; ) {
            int x2 = (int)this.living.posX + this.living.world.rand.nextInt(17) - 8;
            int z = (int)this.living.posZ + this.living.world.rand.nextInt(17) - 8;
            int y2 = (int)this.living.posY + this.living.world.rand.nextInt(5) - 2;
            if (!this.living.world.isAirBlock(new BlockPos(x2, y2 + 1, z)) || this.living.world
                    .getBlockState(new BlockPos(x2, y2, z)).getMaterial() != ThaumcraftMaterials.MATERIAL_TAINT ||
                    this.living.getNavigator().getPathToXYZ(x2, y2, z) == null) {
                tries++; continue;
            }  this.targetPos = new BlockPos(x2, y2, z);
            return true;
        }

        return false;
    }


    public boolean shouldContinueExecuting() {
        return (!this.living.getNavigator().noPath() && this.targetPos != null && this.living.getNavigator().getPathToPos(this.targetPos) != null);
    }


    public void resetTask() {
        this.targetPos = null;
        this.living.getNavigator().clearPath();
    }


    public void updateTask() {
        this.living.getLookHelper().setLookPosition(this.targetPos.getX() + 0.5D, this.targetPos.getY() + 0.5D, this.targetPos
                .getZ() + 0.5D, 30.0F, 30.0F);
        double dist = this.living.getDistanceSq(this.targetPos.getX() + 0.5D, this.targetPos.getY() + 0.5D, this.targetPos
                .getZ() + 0.5D);
        if (dist <= 4.0D) {
            eatTaint();
        } else {
            this.living.getNavigator().tryMoveToXYZ(this.targetPos.getX() + 0.5D, this.targetPos.getY() + 0.5D, this.targetPos
                    .getZ() + 0.5D, 1.0D);
        }
    }

    private void eatTaint() {
        IBlockState state = this.living.world.getBlockState(this.targetPos);

        if (state.getMaterial() == ThaumcraftMaterials.MATERIAL_TAINT || state.getBlock() instanceof thaumcraft.common.blocks.world.taint.ITaintBlock) {

            if (state.getBlock() == BlocksTC.taintLog) {
                Biome biome = this.living.world.getBiome(this.targetPos);
                BlockPlanks.EnumType type = WOOD_IN_BIOMES.containsKey(biome) ? WOOD_IN_BIOMES.get(biome) : BlockPlanks.EnumType.OAK;
                this.living.world.setBlockState(this.targetPos, Blocks.LOG.getDefaultState().withProperty((IProperty)BlockOldLog.VARIANT, (Comparable)type).withProperty((IProperty)BlockLog.LOG_AXIS,


                        (Comparable)BlockLog.EnumAxis.fromFacingAxis((EnumFacing.Axis)state.getValue((IProperty)BlockTaintLog.AXIS))));
            }
            else if (state.getBlock() == BlocksTC.taintRock) {
                this.living.world.setBlockState(this.targetPos, Blocks.STONE.getDefaultState());
            } else if (state.getBlock() == BlocksTC.taintSoil) {
                this.living.world.setBlockState(this.targetPos, Blocks.DIRT.getDefaultState());
            } else {
                this.living.world.setBlockToAir(this.targetPos);
            }


            Common.INSTANCE.sendToAllAround((IMessage)new ParticuleDestroyMessage(this.targetPos), new NetworkRegistry.TargetPoint(this.living.world.provider.getDimension(), this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ(), 10.0D));
            this.living.playSound(SoundEvents.ENTITY_PLAYER_BURP, 0.2F, ((this.living.world.rand.nextFloat() - this.living.world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            this.living.heal(1.0F);
            this.cooldown = Config.taint_pig_cooldown;
        } else {
            resetTask();
        }
    }

    public void startExecuting() {
        if (this.targetPos != null)
            this.living.getNavigator().tryMoveToXYZ(this.targetPos.getX() + 0.5D, this.targetPos.getY() + 0.5D, this.targetPos
                    .getZ() + 0.5D, 1.0D);
    }
}