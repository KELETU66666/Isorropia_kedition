 package fr.wind_blade.isorropia.common.config;
 
 import java.io.File;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import net.minecraft.item.ItemStack;
 import net.minecraft.item.crafting.Ingredient;
 import net.minecraft.util.NonNullList;
 import net.minecraftforge.common.config.Configuration;
 import net.minecraftforge.common.config.Property;
 import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
 import net.minecraftforge.oredict.OreDictionary;
 
 
 
 
 
 
 public class Config
 {
/*  23 */   public static final NonNullList<ItemStack> CRYSTALS = NonNullList.create();
/*  24 */   public static final List<OrePigEntry> ORE_PIG_ENTRIES = new ArrayList<>();
   
   public static File dir;
   
   public static Configuration config;
   
   public static Ingredient oreWithDust;
   public static Ingredient oreWithNugget;
   public static Ingredient orePig;
   private static final String CREATURES_CATEGORY = "Creatures";
   public static int taint_pig_cooldown;
   public static Map<String, ItemStack> orePigOreDictionary;
   
   public static void init(FMLPreInitializationEvent event) {
/*  38 */     dir = event.getModConfigurationDirectory();
/*  39 */     config = new Configuration(new File(dir + "/isorropia/Thaumic Isorropia.cfg"));
/*  40 */     config.addCustomCategoryComment("Creatures", "The configuration for the creatures of the mod");
     
/*  42 */     Property taint_cooldown = config.get("Creatures", "taint_eat_cooldown", 3);
/*  43 */     taint_cooldown.setComment("The cooldown that use the taint pig before eat another taint material in ticks (20 ticks = 1 second)");
     
/*  45 */     taint_pig_cooldown = taint_cooldown.getInt();
     
/*  47 */     initConfig();
/*  48 */     syncConfigurable();
/*  49 */     config.save();
   }
   
   public static void save() {
/*  53 */     config.save();
   }
 
   
   public static void syncConfigurable() {}
 
   
   public static void initConfig() {
/*  61 */     ConfigContainment.init();
   }
 
   
   public static void initOreDictionary() {
/*  66 */     List<ItemStack> t1 = new ArrayList<>();
/*  67 */     List<ItemStack> t2 = new ArrayList<>();
/*  68 */     List<String> ore_pig_entries = new ArrayList<>();
/*  69 */     Map<String, ItemStack> ore_dic_stack = new HashMap<>();
     
/*  71 */     for (String dicName : OreDictionary.getOreNames()) {
/*  72 */       if (dicName.startsWith("nugget")) {
/*  73 */         for (String oreName : OreDictionary.getOreNames()) {
/*  74 */           if (oreName.startsWith("ore") && 
/*  75 */             oreName.substring(3).equals(dicName.substring(6))) {
/*  76 */             NonNullList<ItemStack> ores = OreDictionary.getOres(dicName);
/*  77 */             NonNullList<ItemStack> stacks = OreDictionary.getOres(oreName);
/*  78 */             if (!ores.isEmpty() && !stacks.isEmpty()) {
/*  79 */               ItemStack stack = (ItemStack)stacks.get(0);
/*  80 */               t1.add(stack);
/*  81 */               ore_pig_entries.add(oreName);
/*  82 */               ore_dic_stack.put(oreName, ores.get(0));
             } 
           } 
         } 
/*  86 */       } else if (dicName.startsWith("dust")) {
/*  87 */         for (String oreName : OreDictionary.getOreNames()) {
/*  88 */           if (oreName.startsWith("ore") && 
/*  89 */             oreName.substring(3).equals(dicName.substring(4))) {
/*  90 */             NonNullList<ItemStack> ores = OreDictionary.getOres(dicName);
/*  91 */             NonNullList<ItemStack> stacks = OreDictionary.getOres(oreName);
/*  92 */             if (!ores.isEmpty() && !stacks.isEmpty()) {
/*  93 */               ItemStack stack = (ItemStack)stacks.get(0);
/*  94 */               t2.add(stack);
/*  95 */               ore_pig_entries.add(oreName);
/*  96 */               ore_dic_stack.put(oreName, ores.get(0));
             } 
           } 
         } 
       } 
     } 
     
/* 103 */     oreWithNugget = Ingredient.fromStacks(t1.<ItemStack>toArray(new ItemStack[0]));
/* 104 */     oreWithDust = Ingredient.fromStacks(t2.<ItemStack>toArray(new ItemStack[0]));
/* 105 */     t1.addAll(t2);
/* 106 */     orePig = Ingredient.fromStacks(t1.<ItemStack>toArray(new ItemStack[0]));
/* 107 */     orePigOreDictionary = ore_dic_stack;
     
/* 109 */     for (String str : ore_pig_entries) {
/* 110 */       ORE_PIG_ENTRIES.add(new OrePigEntry(str, 1.0F, 1.0F));
     }
   }
   
   public static String[] getIngotsName() {
/* 115 */     ArrayList<String> ingotNames = new ArrayList<>();
/* 116 */     for (String oreName : OreDictionary.getOreNames()) {
/* 117 */       if (oreName.startsWith("ingot")) {
/* 118 */         ingotNames.add(oreName);
       }
     } 
/* 121 */     return (String[])ingotNames.toArray();
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\config\Config.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */