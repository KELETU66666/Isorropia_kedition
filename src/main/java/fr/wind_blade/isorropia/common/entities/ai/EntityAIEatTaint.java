 package fr.wind_blade.isorropia.common.entities.ai;
 
 import fr.wind_blade.isorropia.common.Common;
 import fr.wind_blade.isorropia.common.config.Config;
 import fr.wind_blade.isorropia.common.network.ParticuleDestroyMessage;
 import java.util.HashMap;
 import java.util.Map;
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
 
 
 
 
 public class EntityAIEatTaint
   extends EntityAIBase
 {
/*  33 */   public static final Map<Biome, BlockPlanks.EnumType> WOOD_IN_BIOMES = new HashMap<>(); private EntityLiving living;
   
   static {
/*  36 */     WOOD_IN_BIOMES.put(Biomes.EXTREME_HILLS, BlockPlanks.EnumType.SPRUCE);
/*  37 */     WOOD_IN_BIOMES.put(Biomes.TAIGA, BlockPlanks.EnumType.SPRUCE);
/*  38 */     WOOD_IN_BIOMES.put(Biomes.TAIGA_HILLS, BlockPlanks.EnumType.SPRUCE);
/*  39 */     WOOD_IN_BIOMES.put(Biomes.EXTREME_HILLS_EDGE, BlockPlanks.EnumType.SPRUCE);
/*  40 */     WOOD_IN_BIOMES.put(Biomes.COLD_TAIGA, BlockPlanks.EnumType.SPRUCE);
/*  41 */     WOOD_IN_BIOMES.put(Biomes.COLD_TAIGA_HILLS, BlockPlanks.EnumType.SPRUCE);
/*  42 */     WOOD_IN_BIOMES.put(Biomes.REDWOOD_TAIGA, BlockPlanks.EnumType.SPRUCE);
/*  43 */     WOOD_IN_BIOMES.put(Biomes.REDWOOD_TAIGA_HILLS, BlockPlanks.EnumType.SPRUCE);
/*  44 */     WOOD_IN_BIOMES.put(Biomes.RIVER, BlockPlanks.EnumType.SPRUCE);
/*  45 */     WOOD_IN_BIOMES.put(Biomes.MUTATED_EXTREME_HILLS, BlockPlanks.EnumType.SPRUCE);
/*  46 */     WOOD_IN_BIOMES.put(Biomes.MUTATED_TAIGA, BlockPlanks.EnumType.SPRUCE);
/*  47 */     WOOD_IN_BIOMES.put(Biomes.MUTATED_TAIGA_COLD, BlockPlanks.EnumType.SPRUCE);
/*  48 */     WOOD_IN_BIOMES.put(Biomes.MUTATED_REDWOOD_TAIGA, BlockPlanks.EnumType.SPRUCE);
/*  49 */     WOOD_IN_BIOMES.put(Biomes.MUTATED_REDWOOD_TAIGA_HILLS, BlockPlanks.EnumType.SPRUCE);
/*  50 */     WOOD_IN_BIOMES.put(Biomes.MUTATED_EXTREME_HILLS_WITH_TREES, BlockPlanks.EnumType.SPRUCE);
/*  51 */     WOOD_IN_BIOMES.put(Biomes.BIRCH_FOREST, BlockPlanks.EnumType.BIRCH);
/*  52 */     WOOD_IN_BIOMES.put(Biomes.BIRCH_FOREST_HILLS, BlockPlanks.EnumType.BIRCH);
/*  53 */     WOOD_IN_BIOMES.put(Biomes.MUTATED_BIRCH_FOREST, BlockPlanks.EnumType.BIRCH);
/*  54 */     WOOD_IN_BIOMES.put(Biomes.MUTATED_BIRCH_FOREST_HILLS, BlockPlanks.EnumType.BIRCH);
/*  55 */     WOOD_IN_BIOMES.put(Biomes.JUNGLE, BlockPlanks.EnumType.JUNGLE);
/*  56 */     WOOD_IN_BIOMES.put(Biomes.JUNGLE_HILLS, BlockPlanks.EnumType.JUNGLE);
/*  57 */     WOOD_IN_BIOMES.put(Biomes.JUNGLE_EDGE, BlockPlanks.EnumType.JUNGLE);
/*  58 */     WOOD_IN_BIOMES.put(Biomes.MUTATED_JUNGLE, BlockPlanks.EnumType.JUNGLE);
/*  59 */     WOOD_IN_BIOMES.put(Biomes.MUTATED_JUNGLE_EDGE, BlockPlanks.EnumType.JUNGLE);
/*  60 */     WOOD_IN_BIOMES.put(Biomes.SAVANNA, BlockPlanks.EnumType.ACACIA);
/*  61 */     WOOD_IN_BIOMES.put(Biomes.SAVANNA_PLATEAU, BlockPlanks.EnumType.ACACIA);
/*  62 */     WOOD_IN_BIOMES.put(Biomes.MUTATED_SAVANNA, BlockPlanks.EnumType.ACACIA);
/*  63 */     WOOD_IN_BIOMES.put(Biomes.MUTATED_SAVANNA_ROCK, BlockPlanks.EnumType.ACACIA);
/*  64 */     WOOD_IN_BIOMES.put(Biomes.ROOFED_FOREST, BlockPlanks.EnumType.DARK_OAK);
/*  65 */     WOOD_IN_BIOMES.put(Biomes.MUTATED_ROOFED_FOREST, BlockPlanks.EnumType.DARK_OAK);
   }
 
   
   private BlockPos targetPos;
   int cooldown;
   
   public EntityAIEatTaint(EntityLiving living) {
/*  73 */     this.living = living;
   }
 
   
   public boolean shouldExecute() {
/*  78 */     if (this.cooldown > 0) {
/*  79 */       this.cooldown--;
/*  80 */       return false;
     } 
/*  82 */     return findTaint();
   }
   
   private boolean findTaint() {
/*  86 */     this.targetPos = null;
/*  87 */     for (BlockPos pos : BlockPos.getAllInBoxMutable(this.living.getPosition().add(-5, -2, -5), this.living
/*  88 */         .getPosition().add(5, 5, 5))) {
/*  89 */       if (this.living.world.getBlockState(pos).getMaterial() != ThaumcraftMaterials.MATERIAL_TAINT || 
/*  90 */         this.living.getNavigator().getPathToXYZ(pos.getX(), pos.getY(), pos.getZ()) == null)
         continue; 
/*  92 */       this.targetPos = pos;
/*  93 */       return true;
     } 
 
     
     for (int tries = 0; tries < 30; ) {
       int x2 = (int)this.living.posX + this.living.world.rand.nextInt(17) - 8;
/*  99 */       int z = (int)this.living.posZ + this.living.world.rand.nextInt(17) - 8;
/* 100 */       int y2 = (int)this.living.posY + this.living.world.rand.nextInt(5) - 2;
/* 101 */       if (!this.living.world.isAirBlock(new BlockPos(x2, y2 + 1, z)) || this.living.world
/* 102 */         .getBlockState(new BlockPos(x2, y2, z)).getMaterial() != ThaumcraftMaterials.MATERIAL_TAINT || 
/* 103 */         this.living.getNavigator().getPathToXYZ(x2, y2, z) == null) {
         tries++; continue;
/* 105 */       }  this.targetPos = new BlockPos(x2, y2, z);
/* 106 */       return true;
     } 
     
/* 109 */     return false;
   }
 
   
   public boolean shouldContinueExecuting() {
/* 114 */     return (!this.living.getNavigator().noPath() && this.targetPos != null && this.living
/* 115 */       .getNavigator().getPathToPos(this.targetPos) != null);
   }
 
   
   public void resetTask() {
/* 120 */     this.targetPos = null;
/* 121 */     this.living.getNavigator().clearPath();
   }
 
   
   public void updateTask() {
/* 126 */     this.living.getLookHelper().setLookPosition(this.targetPos.getX() + 0.5D, this.targetPos.getY() + 0.5D, this.targetPos
/* 127 */         .getZ() + 0.5D, 30.0F, 30.0F);
/* 128 */     double dist = this.living.getDistanceSq(this.targetPos.getX() + 0.5D, this.targetPos.getY() + 0.5D, this.targetPos
/* 129 */         .getZ() + 0.5D);
/* 130 */     if (dist <= 4.0D) {
/* 131 */       eatTaint();
     } else {
       this.living.getNavigator().tryMoveToXYZ(this.targetPos.getX() + 0.5D, this.targetPos.getY() + 0.5D, this.targetPos
/* 134 */           .getZ() + 0.5D, 1.0D);
     } 
   }
   
   private void eatTaint() {
/* 139 */     IBlockState state = this.living.world.getBlockState(this.targetPos);
     
/* 141 */     if (state.getMaterial() == ThaumcraftMaterials.MATERIAL_TAINT || state.getBlock() instanceof thaumcraft.common.blocks.world.taint.ITaintBlock) {
       
/* 143 */       if (state.getBlock() == BlocksTC.taintLog) {
/* 144 */         Biome biome = this.living.world.getBiome(this.targetPos);
/* 145 */         BlockPlanks.EnumType type = WOOD_IN_BIOMES.containsKey(biome) ? WOOD_IN_BIOMES.get(biome) : BlockPlanks.EnumType.OAK;
/* 146 */         this.living.world.setBlockState(this.targetPos, Blocks.LOG
/* 147 */             .getDefaultState().withProperty((IProperty)BlockOldLog.VARIANT, (Comparable)type).withProperty((IProperty)BlockLog.LOG_AXIS, 
               
/* 149 */               (Comparable)BlockLog.EnumAxis.fromFacingAxis((EnumFacing.Axis)state.getValue((IProperty)BlockTaintLog.AXIS))));
       }
/* 151 */       else if (state.getBlock() == BlocksTC.taintRock) {
/* 152 */         this.living.world.setBlockState(this.targetPos, Blocks.STONE.getDefaultState());
/* 153 */       } else if (state.getBlock() == BlocksTC.taintSoil) {
/* 154 */         this.living.world.setBlockState(this.targetPos, Blocks.DIRT.getDefaultState());
       } else {
/* 156 */         this.living.world.setBlockToAir(this.targetPos);
       } 
 
       
/* 160 */       Common.INSTANCE.sendToAllAround((IMessage)new ParticuleDestroyMessage(this.targetPos), new NetworkRegistry.TargetPoint(this.living.world.provider
/* 161 */             .getDimension(), this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ(), 10.0D));
/* 162 */       this.living.playSound(SoundEvents.ENTITY_PLAYER_BURP, 0.2F, ((this.living.world.rand
/* 163 */           .nextFloat() - this.living.world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
/* 164 */       this.living.heal(1.0F);
/* 165 */       this.cooldown = Config.taint_pig_cooldown;
     } else {
/* 167 */       resetTask();
     } 
   }
   
   public void startExecuting() {
/* 172 */     if (this.targetPos != null)
/* 173 */       this.living.getNavigator().tryMoveToXYZ(this.targetPos.getX() + 0.5D, this.targetPos.getY() + 0.5D, this.targetPos
/* 174 */           .getZ() + 0.5D, 1.0D); 
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\entities\ai\EntityAIEatTaint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */