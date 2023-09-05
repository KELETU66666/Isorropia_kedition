 package fr.wind_blade.isorropia.common.entities;
 
 import io.netty.buffer.ByteBuf;
 import net.minecraft.block.state.IBlockState;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.EntityHanging;
 import net.minecraft.item.ItemStack;
 import net.minecraft.nbt.NBTTagCompound;
 import net.minecraft.util.EnumFacing;
 import net.minecraft.util.math.BlockPos;
 import net.minecraft.world.World;
 import net.minecraftforge.fml.common.network.ByteBufUtils;
 import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 import thaumcraft.api.aspects.Aspect;
 import thaumcraft.api.aspects.AspectList;
 import thaumcraft.api.aspects.IEssentiaContainerItem;
 import thaumcraft.api.items.ItemsTC;
 
 
 
 public class EntityHangingLabel
   extends EntityHanging
   implements IEntityAdditionalSpawnData
 {
   private Aspect aspect;
   
   public EntityHangingLabel(World worldIn) {
/*  30 */     super(worldIn);
/*  31 */     this.aspect = null;
   }
   
   public EntityHangingLabel(World world, boolean b, BlockPos offset, EnumFacing face, Aspect aspectIn) {
/*  35 */     super(world, offset);
/*  36 */     updateFacingWithBoundingBox(face);
/*  37 */     this.aspect = aspectIn;
   }
   
   public Aspect getAspect() {
/*  41 */     return this.aspect;
   }
 
   
   public int getWidthPixels() {
/*  46 */     return 16;
   }
 
   
   public int getHeightPixels() {
/*  51 */     return 16;
   }
 
   
   public void onBroken(Entity brokenEntity) {
/*  56 */     playPlaceSound();
     
/*  58 */     if (this.world.getGameRules().getBoolean("doEntityDrops")) {
/*  59 */       ItemStack stack = new ItemStack(ItemsTC.label);
/*  60 */       if (this.aspect != null) {
/*  61 */         ((IEssentiaContainerItem)ItemsTC.label).setAspects(stack, (new AspectList()).add(this.aspect, 1));
/*  62 */         stack.setItemDamage(1);
       } 
/*  64 */       entityDropItem(stack, 0.0F);
     } 
   }
 
   
   public void playPlaceSound() {
/*  70 */     BlockPos hangingBlock = this.hangingPosition.offset(this.facingDirection.getOpposite());
/*  71 */     IBlockState state = this.world.getBlockState(hangingBlock);
/*  72 */     playSound(state.getBlock().getSoundType(state, this.world, hangingBlock, (Entity)this).getHitSound(), 1.5F, 1.0F);
   }
 
   
   public float getCollisionBorderSize() {
/*  77 */     return 0.0F;
   }
 
   
   @SideOnly(Side.CLIENT)
   public boolean isInRangeToRenderDist(double distance) {
/*  83 */     double d0 = 16.0D;
/*  84 */     d0 = d0 * 64.0D * getRenderDistanceWeight();
/*  85 */     return (distance < d0 * d0);
   }
 
   
   public void writeSpawnData(ByteBuf buffer) {
/*  90 */     buffer.writeInt(this.facingDirection.getHorizontalIndex());
/*  91 */     ByteBufUtils.writeUTF8String(buffer, (this.aspect != null) ? this.aspect.getTag() : "");
   }
 
   
   public void readSpawnData(ByteBuf additionalData) {
/*  96 */     updateFacingWithBoundingBox(EnumFacing.byHorizontalIndex(additionalData.readInt()));
     String tag = ByteBufUtils.readUTF8String(additionalData);
     if (!tag.isEmpty()) {
/*  99 */       this.aspect = Aspect.getAspect(tag);
     }
   }
   
   public void writeEntityToNBT(NBTTagCompound compound) {
/* 104 */     super.writeEntityToNBT(compound);
/* 105 */     compound.setString("aspect", (this.aspect != null) ? this.aspect.getTag() : "");
   }
 
   
   public void readEntityFromNBT(NBTTagCompound compound) {
/* 110 */     super.readEntityFromNBT(compound);
/* 111 */     String tag = compound.getString("aspect");
/* 112 */     if (!tag.isEmpty())
/* 113 */       this.aspect = Aspect.getAspect(tag); 
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\entities\EntityHangingLabel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */