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
/*  26 */     this.living = living;
/*  27 */     this.bindChunk = bindChunk;
/*  28 */     this.chunkRange = chunkRange;
/*  29 */     this.entry = entry;
     
/*  31 */     living.tasks.taskEntries.remove(entry);
   }
   protected int chunkRange; protected EntityAITasks.EntityAITaskEntry entry;
   
   public boolean shouldExecute() {
/*  36 */     Chunk livingChunk = this.living.world.getChunk((int)this.living.posX >> 4, (int)this.living.posZ >> 4);
     
/*  38 */     int distance = getDistance(livingChunk);
/*  39 */     boolean havePath = !this.living.getNavigator().noPath();
     
/*  41 */     if (distance > this.chunkRange) {
/*  42 */       if (havePath) {
/*  43 */         this.living.getNavigator().clearPath();
       }
/*  45 */       return true;
     } 
     
/*  48 */     if (havePath) {
/*  49 */       PathPoint point = this.living.getNavigator().getPath().getTarget();
       
/*  51 */       if (point != null) {
/*  52 */         Chunk targetChunk = this.living.world.getChunk(point.x >> 4, point.z >> 4);
/*  53 */         distance = getDistance(targetChunk);
         
/*  55 */         if (distance > this.chunkRange) {
/*  56 */           this.living.getNavigator().clearPath();
         }
       } 
     } 
     
/*  61 */     return false;
   }
 
 
   
   public void updateTask() {
/*  67 */     double distance = getDistance((int)this.living.posX >> 4, (int)this.living.posZ >> 4);
/*  68 */     if (distance <= this.living.getNavigator().getPathSearchRange()) {
/*  69 */       BlockPos pos = findSuitableBlockPos(this.bindChunk);
       
/*  71 */       if (pos != null && 
/*  72 */         this.living.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 1.0D)) {
         return;
       }
     } 
 
     
/*  78 */     tpToBindedChunk();
   }
   
   public void tpToBindedChunk() {
/*  82 */     BlockPos pos = findSuitableBlockPos(this.bindChunk);
     
/*  84 */     if (pos != null) {
/*  85 */       PacketHandler.INSTANCE.sendToAllTracking((IMessage)new PacketFXBlockArc(pos, (Entity)this.living, 0.5F + (new Random())
/*  86 */             .nextFloat() * 0.2F, 0.0F, 0.0F), (Entity)this.living);
       
/*  88 */       this.living.playSound(SoundsTC.wandfail, 1.0F, 1.0F);
       
/*  90 */       this.living.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), this.living.rotationYaw, this.living.rotationPitch);
       
/*  92 */       this.living.getNavigator().clearPath();
     } 
   }
   
   public BlockPos findSuitableBlockPos(Chunk chunk) {
     int i = chunk.x * 16 + 8;
     int j = chunk.z * 16 + 8;
     
/* 100 */     for (int y = 255; y > 0; y--) {
/* 101 */       for (int l = 0; l <= 4; l++) {
/* 102 */         for (int i1 = 0; i1 <= 4; i1++) {
/* 103 */           if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && isTeleportFriendlyBlock(i, j, y, l, i1)) {
/* 104 */             return new BlockPos(((i + l) + 0.5F), y, ((j + i1) + 0.5F));
           }
         } 
       } 
     } 
/* 109 */     return null;
   }
   
   public boolean isTeleportFriendlyBlock(int x, int z, int y, int xOffset, int zOffset) {
/* 113 */     int distance = getDistance(this.living.world.getChunk(x >> 4, z >> 4));
/* 114 */     if (distance > this.chunkRange) {
/* 115 */       return false;
     }
     
/* 118 */     BlockPos blockpos = new BlockPos(x + xOffset, y - 1, z + zOffset);
/* 119 */     IBlockState iblockstate = this.living.world.getBlockState(blockpos);
/* 120 */     return (iblockstate.getBlockFaceShape((IBlockAccess)this.living.world, blockpos, EnumFacing.DOWN) == BlockFaceShape.SOLID && iblockstate
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