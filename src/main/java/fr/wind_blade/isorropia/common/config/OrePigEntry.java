 package fr.wind_blade.isorropia.common.config;
 
 public class OrePigEntry
   extends DuoWeightedRandom.DuoItem {
   private final String oreDictionary;
   
   public OrePigEntry(String oreDictionary, float weight, float auxiliaryWeight) {
/*  8 */     this(oreDictionary, weight, auxiliaryWeight, true);
   }
   
   public OrePigEntry(String oreDictionary, float weight, float auxiliary_weight, boolean active) {
/* 12 */     super(weight, auxiliary_weight, active);
/* 13 */     this.oreDictionary = oreDictionary;
   }
   
   public String getOreDictionary() {
/* 17 */     return this.oreDictionary;
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\config\OrePigEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */