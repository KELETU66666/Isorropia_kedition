 package fr.wind_blade.isorropia.common.config;
 
 import java.util.ArrayList;
 import java.util.List;
 import net.minecraft.entity.Entity;
 
 
 public class ConfigContainment
 {
/* 10 */   public static Enum TYPE = Enum.BLACKLIST;
   public static List<Class<? extends Entity>> ENTRIES = new ArrayList<>();
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
   
   public static void init() {}
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
   
   public enum Enum
   {
/* 58 */     WHITELIST("whitelist"), BLACKLIST("blacklist");
     
     private final String name;
     
     Enum(String name) {
/* 63 */       this.name = name;
     }
     
     public static Enum getByName(String name) {
/* 67 */       for (Enum type : values()) {
/* 68 */         if (type.name.equals(name))
/* 69 */           return type; 
       } 
/* 71 */       return null;
     }
     
     public String getName() {
/* 75 */       return this.name;
     }
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\config\ConfigContainment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */