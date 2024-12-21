package fr.wind_blade.isorropia.common.entities.ai;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXBlockArc;

import java.util.Random;

public class EntityAILigo extends EntityAIBase {
    protected EntityLiving living;
    protected Chunk bindChunk;

    public EntityAILigo(EntityLiving living, Chunk bindChunk, int chunkRange, EntityAITasks.EntityAITaskEntry entry) {
        this.living = living;
        this.bindChunk = bindChunk;
        this.chunkRange = chunkRange;
        this.entry = entry;

        living.tasks.taskEntries.remove(entry);
    }

    protected int chunkRange;
    protected EntityAITasks.EntityAITaskEntry entry;

    public boolean shouldExecute() {
        Chunk livingChunk = this.living.world.getChunk((int) this.living.posX >> 4, (int) this.living.posZ >> 4);

        int distance = getDistance(livingChunk);
        boolean havePath = !this.living.getNavigator().noPath();

        if (distance > this.chunkRange) {
            if (havePath) {
                this.living.getNavigator().clearPath();
            }
            return true;
        }

        if (havePath) {
            PathPoint point = this.living.getNavigator().getPath().getTarget();

            if (point != null) {
                Chunk targetChunk = this.living.world.getChunk(point.x >> 4, point.z >> 4);
                distance = getDistance(targetChunk);

                if (distance > this.chunkRange) {
                    this.living.getNavigator().clearPath();
                }
            }
        }

        return false;
    }


    public void updateTask() {
        double distance = getDistance((int) this.living.posX >> 4, (int) this.living.posZ >> 4);
        if (distance <= this.living.getNavigator().getPathSearchRange()) {

            BlockPos pos = findSuitableBlockPos(this.bindChunk);

            if (pos != null && this.living.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 1.0D)) {
                return;
            }
        }


        tpToBindedChunk();
    }

    public void tpToBindedChunk() {
        BlockPos pos = findSuitableBlockPos(this.bindChunk);

        if (pos != null) {
            PacketHandler.INSTANCE.sendToAllTracking(new PacketFXBlockArc(pos, this.living, 0.5F + (new Random())
                    .nextFloat() * 0.2F, 0.0F, 0.0F), this.living);

            this.living.playSound(SoundsTC.wandfail, 1.0F, 1.0F);

            this.living.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), this.living.rotationYaw, this.living.rotationPitch);

            this.living.getNavigator().clearPath();
        }
    }

    public BlockPos findSuitableBlockPos(Chunk chunk) {
        int i = chunk.x * 16 + 8;
        int j = chunk.z * 16 + 8;

        for (int y = 255; y > 0; y--) {
            for (int l = 0; l <= 4; l++) {
                for (int i1 = 0; i1 <= 4; i1++) {
                    if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && isTeleportFriendlyBlock(i, j, y, l, i1)) {
                        return new BlockPos(((i + l) + 0.5F), y, ((j + i1) + 0.5F));
                    }
                }
            }
        }
        return null;
    }

    public boolean isTeleportFriendlyBlock(int x, int z, int y, int xOffset, int zOffset) {
        int distance = getDistance(this.living.world.getChunk(x >> 4, z >> 4));

        if (distance > this.chunkRange) {

            return false;
        }

        BlockPos blockpos = new BlockPos(x + xOffset, y - 1, z + zOffset);

        IBlockState iblockstate = this.living.world.getBlockState(blockpos);
        return (iblockstate.getBlockFaceShape(this.living.world, blockpos, EnumFacing.DOWN) == BlockFaceShape.SOLID && iblockstate.canEntitySpawn(this.living) && this.living.world.isAirBlock(blockpos.up()) && this.living.world.isAirBlock(blockpos.up(2)));
    }

    public EntityAITasks.EntityAITaskEntry getAi() {
        return this.entry;
    }

    public int getDistance(Chunk targetChunk) {
        return getDistance(targetChunk.x, targetChunk.z);
    }

    public int getDistance(int targetX, int targetZ) {

        return (int) Math.sqrt(Math.pow((this.bindChunk.x - targetX), 2.0D) + Math.pow((this.bindChunk.z - targetZ), 2.0D));
    }

    public int getChunkRange() {
        return this.chunkRange;
    }
}