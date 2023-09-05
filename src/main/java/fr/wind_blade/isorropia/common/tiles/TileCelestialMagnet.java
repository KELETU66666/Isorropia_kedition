 package fr.wind_blade.isorropia.common.tiles;
 
 import java.util.List;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.EntityLiving;
 import net.minecraft.entity.item.EntityItem;
 import net.minecraft.tileentity.TileEntity;
 import net.minecraft.util.ITickable;
 import net.minecraft.util.math.AxisAlignedBB;
 import net.minecraft.util.math.Vec3d;
 import thaumcraft.api.aspects.Aspect;
 import thaumcraft.api.aspects.AspectHelper;
 import thaumcraft.api.aspects.AspectList;
 
 
 
 
 public class TileCelestialMagnet
   extends TileEntity
   implements ITickable
 {
/*  22 */   private EntityFilter entityFilter = EntityFilter.both;
/*  23 */   private AspectList aspectFilters = new AspectList();
   public boolean aspectFilter = true;
/*  25 */   public int area = 2;
   
   public AspectList getAspectFilters() {
/*  28 */     return this.aspectFilters;
   }
   
   public void setAspectFilters(AspectList aspectFilters) {
/*  32 */     this.aspectFilters = aspectFilters;
   }
   
   public boolean addAspectFilter(Aspect aspectIn) {
/*  36 */     if (hasFilter(aspectIn))
/*  37 */       return false; 
/*  38 */     this.aspectFilters.add(aspectIn, 1);
/*  39 */     return true;
   }
   
   public void removeAspectFilter(Aspect aspectIn) {
/*  43 */     this.aspectFilters.remove(aspectIn);
   }
   
   public boolean hasFilter(Aspect aspectIn) {
/*  47 */     for (Aspect aspect : this.aspectFilters.getAspects()) {
/*  48 */       if (aspect == aspectIn)
/*  49 */         return true; 
     } 
/*  51 */     return false;
   }
   
   public EntityFilter getEntityFilter() {
/*  55 */     return this.entityFilter;
   }
   
   public void setFilter(EntityFilter entityFilter) {
/*  59 */     this.entityFilter = entityFilter;
   }
 
   
   public void update() {
/*  64 */     if (this.entityFilter == EntityFilter.both || this.entityFilter == EntityFilter.item) {
/*  65 */       List<EntityItem> items = this.world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB((
/*  66 */             getPos().getX() - 10), (getPos().getY() - 3), (getPos().getZ() - 10), (
/*  67 */             getPos().getX() + 10), (getPos().getY() + 3), (getPos().getZ() + 10)));
/*  68 */       attrack((List)items);
     } 
/*  70 */     if (this.entityFilter == EntityFilter.both || this.entityFilter == EntityFilter.mob) {
/*  71 */       List<EntityLiving> livings = this.world.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB((
/*  72 */             getPos().getX() - 10), (getPos().getY() - 3), (getPos().getZ() - 10), (
/*  73 */             getPos().getX() + 10), (getPos().getY() + 3), (getPos().getZ() + 10)));
/*  74 */       attrack((List)livings);
     } 
   }
   
   public void attrack(List<? extends Entity> entityListIn) {
/*  79 */     for (Entity entity : entityListIn) {
/*  80 */       boolean attrack = true;
/*  81 */       if (this.aspectFilter) {
/*  82 */         attrack = false;
/*  83 */         AspectList aspects = null;
/*  84 */         if (entity instanceof EntityItem)
/*  85 */           aspects = AspectHelper.getObjectAspects(((EntityItem)entity).getItem()); 
/*  86 */         if (entity instanceof EntityLiving)
/*  87 */           aspects = AspectHelper.getEntityAspects(entity); 
/*  88 */         if (aspects != null)
/*  89 */           for (Aspect aspect : aspects.getAspects()) {
/*  90 */             for (Aspect filter : this.aspectFilters.getAspects()) {
/*  91 */               if (filter == aspect) {
/*  92 */                 attrack = true;
                 break;
               } 
             } 
/*  96 */             if (attrack) {
               break;
             }
           }  
       } 
/* 101 */       if (attrack) {
/* 102 */         Vec3d len = new Vec3d(entity.posX - getPos().getX(), 0.0D, entity.posZ - getPos().getZ());
/* 103 */         float depth = (float)Math.sqrt(len.x * len.x + len.z * len.z);
/* 104 */         if (depth > this.area) {
/* 105 */           entity.motionX = (entity.posX - getPos().getX() < 0.0D) ? (entity.motionX + 0.01D) : (entity.motionX - 0.01D);
           
/* 107 */           entity.motionY = (entity.posY - getPos().getY() < 0.0D) ? (entity.motionY + 0.1D) : (entity.motionY - 0.1D);
           
/* 109 */           entity.motionZ = (entity.posZ - getPos().getZ() < 0.0D) ? (entity.motionZ + 0.01D) : (entity.motionZ - 0.01D);
         } 
       } 
     } 
   }
 
   
   public enum EntityFilter
   {
/* 118 */     item(0), mob(1), both(2), none(3);
     
     private final int meta;
     
     EntityFilter(int meta) {
/* 123 */       this.meta = meta;
     }
     
     public int getMeta() {
/* 127 */       return this.meta;
     }
     
     public static EntityFilter getByMeta(int meta) {
/* 131 */       for (EntityFilter filter : values()) {
/* 132 */         if (filter.getMeta() == meta)
           return filter; 
       } 
/* 135 */       return both;
     }
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\tiles\TileCelestialMagnet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */