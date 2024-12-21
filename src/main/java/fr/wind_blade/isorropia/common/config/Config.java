package fr.wind_blade.isorropia.common.config;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Config {
    public static final NonNullList<ItemStack> CRYSTALS = NonNullList.create();
    public static final List<OrePigEntry> ORE_PIG_ENTRIES = new ArrayList<>();

    public static File dir;

    public static Configuration config;

    public static Ingredient oreWithDust;
    public static Ingredient oreWithNugget;
    public static Ingredient orePig;
    private static final String CREATURES_CATEGORY = "Creatures";
    public static int taint_pig_cooldown;
    public static Map<String, ItemStack> orePigOreDictionary;

    public static void init(FMLPreInitializationEvent event) {
        dir = event.getModConfigurationDirectory();
        config = new Configuration(new File(dir + "/isorropia/Thaumic Isorropia.cfg"));
        config.addCustomCategoryComment("Creatures", "The configuration for the creatures of the mod");

        Property taint_cooldown = config.get("Creatures", "taint_eat_cooldown", 3);
        taint_cooldown.setComment("The cooldown that use the taint pig before eat another taint material in ticks (20 ticks = 1 second)");

        taint_pig_cooldown = taint_cooldown.getInt();

        initConfig();
        syncConfigurable();
        config.save();
    }

    public static void save() {
        config.save();
    }


    public static void syncConfigurable() {
    }


    public static void initConfig() {
        ConfigContainment.init();
    }


    public static void initOreDictionary() {
        List<ItemStack> t1 = new ArrayList<>();
        List<ItemStack> t2 = new ArrayList<>();
        List<String> ore_pig_entries = new ArrayList<>();
        Map<String, ItemStack> ore_dic_stack = new HashMap<>();

        for (String dicName : OreDictionary.getOreNames()) {
            if (dicName.startsWith("nugget")) {
                for (String oreName : OreDictionary.getOreNames()) {
                    if (oreName.startsWith("ore") &&
                            oreName.substring(3).equals(dicName.substring(6))) {
                        NonNullList<ItemStack> ores = OreDictionary.getOres(dicName);
                        NonNullList<ItemStack> stacks = OreDictionary.getOres(oreName);
                        if (!ores.isEmpty() && !stacks.isEmpty()) {
                            ItemStack stack = stacks.get(0);
                            t1.add(stack);
                            ore_pig_entries.add(oreName);
                            ore_dic_stack.put(oreName, ores.get(0));
                        }
                    }
                }
            } else if (dicName.startsWith("dust")) {
                for (String oreName : OreDictionary.getOreNames()) {
                    if (oreName.startsWith("ore") &&
                            oreName.substring(3).equals(dicName.substring(4))) {
                        NonNullList<ItemStack> ores = OreDictionary.getOres(dicName);
                        NonNullList<ItemStack> stacks = OreDictionary.getOres(oreName);
                        if (!ores.isEmpty() && !stacks.isEmpty()) {
                            ItemStack stack = stacks.get(0);
                            t2.add(stack);
                            ore_pig_entries.add(oreName);
                            ore_dic_stack.put(oreName, ores.get(0));
                        }
                    }
                }
            }
        }

        oreWithNugget = Ingredient.fromStacks(t1.toArray(new ItemStack[0]));
        oreWithDust = Ingredient.fromStacks(t2.toArray(new ItemStack[0]));
        t1.addAll(t2);
        orePig = Ingredient.fromStacks(t1.toArray(new ItemStack[0]));
        orePigOreDictionary = ore_dic_stack;

        for (String str : ore_pig_entries) {
            ORE_PIG_ENTRIES.add(new OrePigEntry(str, 1.0F, 1.0F));
        }
    }
}