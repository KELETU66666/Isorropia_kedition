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
      super(worldIn);
      this.aspect = null;
   }
   
   public EntityHangingLabel(World world, boolean b, BlockPos offset, EnumFacing face, Aspect aspectIn) {
      super(world, offset);
      updateFacingWithBoundingBox(face);
      this.aspect = aspectIn;
   }
   
   public Aspect getAspect() {
      return this.aspect;
   }
 
   
   public int getWidthPixels() {
     return 16;
   }
 
   
   public int getHeightPixels() {
      return 16;
   }
 
   
   public void onBroken(Entity brokenEntity) {
      playPlaceSound();
     
      if (this.world.getGameRules().getBoolean("doEntityDrops")) {
        ItemStack stack = new ItemStack(ItemsTC.label);
        if (this.aspect != null) {
          ((IEssentiaContainerItem)ItemsTC.label).setAspects(stack, (new AspectList()).add(this.aspect, 1));
          stack.setItemDamage(1);
       } 
/*  64 */       entityDropItem(stack, 0.0F);
     } 
   }
 
   
   public void playPlaceSound() {
/*  70 */     BlockPos hangingBlock = this.hangingPosition.offset(this.facingDirection.getOpposite());
      IBlockState state = this.world.getBlockState(hangingBlock);
/*  72 */     playSound(state.getBlock().getSoundType(state, this.world, hangingBlock, (Entity)this).getHitSound(), 1.5F, 1.0F);
   }
 
   
   public float getCollisionBorderSize() {
      return 0.0F;
   }
 
   
   @SideOnly(Side.CLIENT)
   public boolean isInRangeToRenderDist(double distance) {
      double d0 = 16.0D;
      d0 = d0 * 64.0D * getRenderDistanceWeight();
      return (distance < d0 * d0);
   }
 
   
   public void writeSpawnData(ByteBuf buffer) {
      buffer.writeInt(this.facingDirection.getHorizontalIndex());
      ByteBufUtils.writeUTF8String(buffer, (this.aspect != null) ? this.aspect.getTag() : "");
   }
 
   
   public void readSpawnData(ByteBuf additionalData) {
/*  96 */     updateFacingWithBoundingBox(EnumFacing.byHorizontalIndex(additionalData.readInt()));
     String tag = ByteBufUtils.readUTF8String(additionalData);
     if (!tag.isEmpty()) {
        this.aspect = Aspect.getAspect(tag);
     }
   }
   
   public void writeEntityToNBT(NBTTagCompound compound) {
      super.writeEntityToNBT(compound);
    compound.setString("aspect", (this.aspect != null) ? this.aspect.getTag() : "");
   }
 
   
   public void readEntityFromNBT(NBTTagCompound compound) {
      super.readEntityFromNBT(compound);
      String tag = compound.getString("aspect");
/* 112 */     if (!tag.isEmpty())
     this.aspect = Aspect.getAspect(tag); 
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\entities\EntityHangingLabel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */