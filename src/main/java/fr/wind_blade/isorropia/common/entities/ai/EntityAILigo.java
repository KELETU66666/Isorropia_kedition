 package fr.wind_blade.isorropia.common.entities.ai;
 
 import java.util.Random;
 import net.minecraft.block.state.BlockFaceShape;
 import net.minecraft.block.state.IBlockState;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.EntityLiving;
 import net.minecraft.entity.ai.EntityAIBase;
 import net.minecraft.entity.ai.EntityAITasks;
 import net.minecraft.pathfinding.PathPoint;
 import net.minecraft.util.EnumFacing;
 import net.minecraft.util.math.BlockPos;
 import net.minecraft.world.IBlockAccess;
 import net.minecraft.world.chunk.Chunk;
 import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
 import thaumcraft.common.lib.SoundsTC;
 import thaumcraft.common.lib.network.PacketHandler;
 import thaumcraft.common.lib.network.fx.PacketFXBlockArc;
 
 public class EntityAILigo
   extends EntityAIBase {
   protected EntityLiving living;
   protected Chunk bindChunk;
   
   public EntityAILigo(EntityLiving living, Chunk bindChunk, int chunkRange, EntityAITasks.EntityAITaskEntry entry) {
     this.living = living;
    this.bindChunk = bindChunk;
/*  28 */     this.chunkRange = chunkRange;
      this.entry = entry;
     
      living.tasks.taskEntries.remove(entry);
   }
   protected int chunkRange; protected EntityAITasks.EntityAITaskEntry entry;
   
   public boolean shouldExecute() {
      Chunk livingChunk = this.living.world.getChunk((int)this.living.posX >> 4, (int)this.living.posZ >> 4);
     
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
      double distance = getDistance((int)this.living.posX >> 4, (int)this.living.posZ >> 4);
      if (distance <= this.living.getNavigator().getPathSearchRange()) {
/*  69 */       BlockPos pos = findSuitableBlockPos(this.bindChunk);
       
        if (pos != null && 
/*  72 */         this.living.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 1.0D)) {
         return;
       }
     } 
 
     
      tpToBindedChunk();
   }
   
   public void tpToBindedChunk() {
      BlockPos pos = findSuitableBlockPos(this.bindChunk);
     
      if (pos != null) {
        PacketHandler.INSTANCE.sendToAllTracking((IMessage)new PacketFXBlockArc(pos, (Entity)this.living, 0.5F + (new Random())
              .nextFloat() * 0.2F, 0.0F, 0.0F), (Entity)this.living);
       
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
/* 102 */         for (int i1 = 0; i1 <= 4; i1++) {
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
/* 114 */     if (distance > this.chunkRange) {
/* 115 */       return false;
     }
     
/* 118 */     BlockPos blockpos = new BlockPos(x + xOffset, y - 1, z + zOffset);
/* 119 */     IBlockState iblockstate = this.living.world.getBlockState(blockpos);
    return (iblockstate.getBlockFaceShape((IBlockAccess)this.living.world, blockpos, EnumFacing.DOWN) == BlockFaceShape.SOLID && iblockstate
/* 121 */       .canEntitySpawn((Entity)this.living) && this.living.world.isAirBlock(blockpos.up()) && this.living.world
/* 122 */       .isAirBlock(blockpos.up(2)));
   }
   
   public EntityAITasks.EntityAITaskEntry getAi() {
/* 126 */     return this.entry;
   }
   
   public int getDistance(Chunk targetChunk) {
/* 130 */     return getDistance(targetChunk.x, targetChunk.z);
   }
   
   public int getDistance(int targetX, int targetZ) {
/* 134 */     return (int)Math.sqrt(Math.pow((this.bindChunk.x - targetX), 2.0D) + Math.pow((this.bindChunk.z - targetZ), 2.0D));
   }
   
   public int getChunkRange() {
/* 138 */     return this.chunkRange;
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\entities\ai\EntityAILigo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */