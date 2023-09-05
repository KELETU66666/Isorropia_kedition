 package fr.wind_blade.isorropia.common.config;
 
 import io.netty.util.internal.ThreadLocalRandom;
 import java.util.List;
 import java.util.Random;
 
 
 
 
 public class DuoWeightedRandom
 {
   public static <T extends DuoItem> double getTotalWeight(List<T> collection) {
/*  13 */     return collection.stream().filter(item -> item.isActive()).mapToDouble(DuoItem::getWeight).sum();
   }
   
   public static <T extends DuoItem> double getTotalWeightWithMain(List<T> collection, T main_item) {
/*  17 */     return collection.stream().filter(item -> (item.isActive() && item != main_item))
/*  18 */       .mapToDouble(DuoItem::getAuxiliaryWeight).sum() + main_item.getWeight();
   }
 
 
 
 
   
   public static <T extends DuoItem> T getRandomItem(Random random, List<T> collection, double totalWeight) {
/*  26 */     if (totalWeight <= 0.0D) {
/*  27 */       throw new IllegalArgumentException();
     }
/*  29 */     double i = ThreadLocalRandom.current().nextDouble(0.0D, totalWeight);
/*  30 */     return getRandomItem(collection, i);
   }
 
 
   
   public static <T extends DuoItem> T getRandomItemWithMain(Random random, List<T> collection, T main_item, double totalWeight) {
/*  36 */     if (totalWeight <= 0.0D) {
/*  37 */       throw new IllegalArgumentException();
     }
/*  39 */     double i = ThreadLocalRandom.current().nextDouble(0.0D, totalWeight);
/*  40 */     return getRandomItemWithMain(collection, main_item, i);
   }
 
   
   public static <T extends DuoItem> T getRandomItem(List<T> collection, double weight) {
/*  45 */     int i = 0;
     
/*  47 */     for (int j = collection.size(); i < j; i++) {
/*  48 */       DuoItem duoItem = (DuoItem)collection.get(i);
/*  49 */       if (duoItem.isActive()) {
 
         
/*  52 */         weight -= duoItem.weight;
         
/*  54 */         if (weight < 0.0D) {
/*  55 */           return (T)duoItem;
         }
       } 
     } 
/*  59 */     return null;
   }
 
   
   public static <T extends DuoItem> T getRandomItemWithMain(List<T> collection, T main_item, double weight) {
/*  64 */     int i = 0;
     
/*  66 */     for (int j = collection.size(); i < j; i++) {
/*  67 */       DuoItem duoItem = (DuoItem)collection.get(i);
/*  68 */       if (duoItem.isActive()) {
 
         
/*  71 */         weight -= (duoItem == main_item) ? duoItem.weight : duoItem.getAuxiliaryWeight();
         
/*  73 */         if (weight < 0.0D) {
/*  74 */           return (T)duoItem;
         }
       } 
     } 
/*  78 */     return null;
   }
 
 
 
   
   public static <T extends DuoItem> T getRandomItem(Random random, List<T> collection) {
/*  85 */     return getRandomItem(random, collection, getTotalWeight(collection));
   }
   
   public static <T extends DuoItem> T getRandomItemWithMain(Random random, T main_item, List<T> collection) {
/*  89 */     return getRandomItemWithMain(random, collection, main_item, getTotalWeightWithMain(collection, main_item));
   }
 
   
   public static class DuoItem
   {
     public final double weight;
     
     public final double auxiliary_weight;
     
     private boolean active = true;
     
     public DuoItem(double weight) {
/* 102 */       this(weight, 0.0D, true);
     }
     
     public DuoItem(double weight, boolean active) {
/* 106 */       this(weight, 0.0D, active);
     }
     
     public DuoItem(double weight, double auxiliary_weight) {
/* 110 */       this(weight, auxiliary_weight, true);
     }
     
     public DuoItem(double weight, double auxiliary_weight, boolean active) {
/* 114 */       this.weight = weight;
/* 115 */       this.auxiliary_weight = auxiliary_weight;
/* 116 */       this.active = active;
     }
     
     public double getWeight() {
/* 120 */       return this.weight;
     }
     
     public double getAuxiliaryWeight() {
/* 124 */       return this.auxiliary_weight;
     }
     
     public boolean isActive() {
/* 128 */       return this.active;
     }
     
     public void setActive(boolean active) {
/* 132 */       this.active = active;
     }
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\config\DuoWeightedRandom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */