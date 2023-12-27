 package fr.wind_blade.isorropia.common.entities;
 
 import fr.wind_blade.isorropia.common.items.ItemsIS;
 import java.awt.Color;
 import java.util.ArrayList;
 import java.util.List;
 import net.minecraft.client.resources.I18n;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.EntityList;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.entity.IEntityLivingData;
 import net.minecraft.entity.passive.EntityRabbit;
 import net.minecraft.init.SoundEvents;
 import net.minecraft.item.Item;
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
     this.dataManager.register(jelly, Integer.valueOf(0));
   }
 
   
   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
      Aspect[] displayAspects = (Aspect[])Aspect.aspects.values().toArray((Object[])new Aspect[0]);
      setAspect(displayAspects[this.world.rand.nextInt(displayAspects.length)]);
      setJellySize(2);
      return super.onInitialSpawn(difficulty, livingdata);
   }
 
   
   public void onLivingUpdate() {
      super.onLivingUpdate();
/*  69 */     this.fallDistance = 0.0F;
     
      if (this.world.isRemote || getJellySize() > 8) {
       return;
     }
      if (this.count % 80 == 0) {
        if (getAspect() == Aspect.FLUX) {
          if (AuraHelper.getFlux(getEntityWorld(), getPosition()) > 0.0F) {
            AuraHelper.addVis(this.world, getPosition(), AuraHelper.drainFlux(this.world, getPosition(), 10.0F, false));
         }
       } else {
/*  80 */         AuraHelper.drainVis(getEntityWorld(), getPosition(), 100.0F, false);
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
       
/*  96 */       Entity entity = source.getTrueSource();
       if (entity != null) {
         double d1 = entity.posX - this.posX;
         
         double d0;
          for (d0 = entity.posZ - this.posZ; d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D)
         {
          d1 = (Math.random() - Math.random()) * 0.01D;
         }
         
          this.attackedAtYaw = (float)(MathHelper.atan2(d0, d1) * 57.29577951308232D - this.rotationYaw);
          knockBack(entity, 0.4F, d1, d0);
         
         if (entity instanceof EntityLivingBase) {
            setRevengeTarget((EntityLivingBase)entity);
         }
       } 
     playSound((getJellySize() > 0) ? SoundEvents.ENTITY_SLIME_HURT : SoundEvents.ENTITY_RABBIT_HURT, 1.0F, 
/* 114 */           getSoundPitch());
       
/* 116 */       if (!this.world.isRemote) {
/* 117 */         entityDropItem(getJelly(), 0.0F);
       }
       
      return false;
     } 
/* 122 */     return super.attackEntityFrom(source, amount);
   }
   
   public ItemStack getJelly() {
/* 126 */     ItemStack stack = new ItemStack((Item)ItemsIS.itemJelly);
/* 127 */     Aspect apsect = getAspect();
     
/* 129 */     if (apsect != null) {
/* 130 */       ItemsIS.itemJelly.setAspects(stack, (new AspectList()).add(apsect, 5));
     }
     
     setJellySize(getJellySize() - 1);
/* 134 */     return stack;
   }
 
   
   public String getName() {
/* 139 */     if (hasCustomName()) {
/* 140 */       return getCustomNameTag();
     }
/* 142 */     String tag = (String)this.dataManager.get(aspect);
/* 143 */     String s = EntityList.getEntityString((Entity)this);
/* 144 */     String name = "";
     
/* 146 */     if (s == null)
/* 147 */       s = "generic"; 
/* 148 */     if (tag == null || tag.isEmpty()) {
/* 149 */       name = I18n.format("entity." + s + ".name", new Object[0]);
     }
     else {
       
/* 153 */       name = I18n.hasKey("entity." + s + "." + tag + ".name") ? I18n.format("entity." + s + "." + tag + ".name", new Object[0]) : I18n.format("entity." + s + ".aspect.name", new Object[] { tag });
/* 154 */     }  return name;
   }
 
 
   
   public void writeEntityToNBT(NBTTagCompound compound) {
/* 160 */     super.writeEntityToNBT(compound);
/* 161 */     Aspect aspect = getAspect();
/* 162 */     compound.setString("tag", (aspect != null) ? aspect.getTag() : "");
/* 163 */     compound.setInteger("jelly", ((Integer)this.dataManager.get(jelly)).intValue());
   }
 
   
   public void readEntityFromNBT(NBTTagCompound compound) {
/* 168 */     super.readEntityFromNBT(compound);
/* 169 */     setAspect(Aspect.getAspect(compound.getString("tag")));
/* 170 */     this.dataManager.set(jelly, Integer.valueOf(compound.getInteger("jelly")));
   }
 
   
   public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos) {
/* 175 */     return (getJellySize() > 0);
   }
 
   
   public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
/* 180 */     List<ItemStack> stacks = new ArrayList<>();
/* 181 */     stacks.add(getJelly());
/* 182 */     return stacks;
   }
   
   public void setAspect(Aspect jellyAspect) {
/* 186 */     this.dataManager.set(aspect, jellyAspect.getTag());
     
/* 188 */     if (this.world.isRemote) {
/* 189 */       this.color = new Color(jellyAspect.getColor());
     }
   }
   
   public Aspect getAspect() {
/* 194 */     return Aspect.getAspect((String)this.dataManager.get(aspect));
   }
   
   public int getJellySize() {
/* 198 */     return ((Integer)this.dataManager.get(jelly)).intValue();
   }
   
   public void setJellySize(int newJellySize) {
/* 202 */     this.dataManager.set(jelly, Integer.valueOf(newJellySize));
   }
 
   
   public Color getColor() {
/* 207 */     return this.color;
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\entities\EntityJellyRabbit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */