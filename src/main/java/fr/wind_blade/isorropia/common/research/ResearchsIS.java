package fr.wind_blade.isorropia.common.research;

import fr.wind_blade.isorropia.common.IsorropiaAPI;
import static fr.wind_blade.isorropia.common.IsorropiaAPI.*;
import fr.wind_blade.isorropia.common.blocks.BlocksIS;
import fr.wind_blade.isorropia.common.celestial.CelestialBody;
import fr.wind_blade.isorropia.common.config.Config;
import fr.wind_blade.isorropia.common.curative.JellyRabbitRecipe;
import fr.wind_blade.isorropia.common.curative.OreBoarRecipe;
import fr.wind_blade.isorropia.common.entities.*;
import fr.wind_blade.isorropia.common.items.ItemsIS;
import fr.wind_blade.isorropia.common.items.misc.ItemCat;
import fr.wind_blade.isorropia.common.research.recipes.CurativeInfusionRecipe;
import fr.wind_blade.isorropia.common.research.recipes.OrganCurativeInfusionRecipe;
import fr.wind_blade.isorropia.common.research.recipes.SelfInfusionRecipe;
import fr.wind_blade.isorropia.common.research.recipes.SpecieCurativeInfusionRecipe;
import net.minecraft.block.Block;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.api.entities.ITaintedMob;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ScanningManager;

import java.util.Iterator;
import java.util.UUID;
import java.util.stream.Collectors;

public class ResearchsIS {
    private static AspectList getAspects(ItemStack stack) {
        AspectList list = AspectHelper.getObjectAspects(stack);
        return list != null ? list : new AspectList();
    }

    private static void addAspects(ItemStack stack, AspectList aspects) {
        AspectList list = ResearchsIS.getAspects(stack);
        list.add(aspects);
        ThaumcraftApi.registerObjectTag(stack, list);
    }

    private static void addAspects(Item item, AspectList list) {
        ResearchsIS.addAspects(new ItemStack(item), list);
    }

    private static void addAspects(Block block, AspectList list) {
        ResearchsIS.addAspects(new ItemStack(block), list);
    }

    public static void initAspects(FMLPostInitializationEvent event) {
        Iterator iterator = ForgeRegistries.ITEMS.iterator();
        while (iterator.hasNext()) {
            Item item = (Item) iterator.next();
            if (!(item instanceof ItemFood)) continue;
            ItemFood food = (ItemFood) item;
            ItemStack stack = new ItemStack(item);
            AspectList list = AspectHelper.getObjectAspects(stack);
            list = (list == null ? new AspectList() : list).add(IsorropiaAPI.HUNGER, Math.round(food.getHealAmount(stack)));
            for (int raw : OreDictionary.getOreIDs(stack)) {
                if (!OreDictionary.getOreName(raw).contains("raw")) continue;
                list.add(IsorropiaAPI.FLESH, food.getHealAmount(stack));
                break;
            }
            if ((double) food.getSaturationModifier(stack) < 0.6) {
                ThaumcraftApi.registerObjectTag(stack, list);
                continue;
            }
            ThaumcraftApi.registerObjectTag(stack, list.add(IsorropiaAPI.GLUTTONY, Math.round(food.getSaturationModifier(stack) * (float) food.getHealAmount(stack))));
        }
        ResearchsIS.addAspects(Items.RABBIT, new AspectList().add(IsorropiaAPI.FLESH, 2));
        ResearchsIS.addAspects(Items.PORKCHOP, new AspectList().add(IsorropiaAPI.FLESH, 2));
        ResearchsIS.addAspects(Items.BEEF, new AspectList().add(IsorropiaAPI.FLESH, 2));
        ResearchsIS.addAspects(Items.CHICKEN, new AspectList().add(IsorropiaAPI.FLESH, 2));
        ResearchsIS.addAspects(Items.MUTTON, new AspectList().add(IsorropiaAPI.FLESH, 2));
        ResearchsIS.addAspects(Blocks.BED, new AspectList().add(IsorropiaAPI.SLOTH, 2));

        if (!Loader.isModLoaded("forbiddenmagicre")) {
            ResearchsIS.addAspects(new ItemStack(Items.GHAST_TEAR), AspectHelper.getObjectAspects(new ItemStack(Items.GHAST_TEAR)).add(ENVY, 5));
            ResearchsIS.addAspects(new ItemStack(Blocks.TNT), AspectHelper.getObjectAspects(new ItemStack(Blocks.TNT)).add(WRATH, 15));
            ResearchsIS.addAspects(new ItemStack(Items.FIRE_CHARGE), AspectHelper.getObjectAspects(new ItemStack(Items.FIRE_CHARGE)).add(WRATH, 2));
            ResearchsIS.addAspects(new ItemStack(Items.SKULL, 1, 4), AspectHelper.getObjectAspects(new ItemStack(Items.SKULL, 1, 4)).add(WRATH, 5));
            ResearchsIS.addAspects(new ItemStack(Items.SADDLE), AspectHelper.getObjectAspects(new ItemStack(Items.SADDLE)).add(LUST, 5));
            ResearchsIS.addAspects(new ItemStack(Items.GOLDEN_SWORD, 1, 32767), AspectHelper.getObjectAspects(new ItemStack(Items.GOLDEN_SWORD, 1, 32767)).add(PRIDE, 8));
            ResearchsIS.addAspects(new ItemStack(Items.GOLDEN_HELMET, 1, 32767), AspectHelper.getObjectAspects(new ItemStack(Items.GOLDEN_HELMET, 1, 32767)).add(PRIDE, 8));
            ResearchsIS.addAspects(new ItemStack(Items.GOLDEN_CHESTPLATE, 1, 32767), AspectHelper.getObjectAspects(new ItemStack(Items.GOLDEN_CHESTPLATE, 1, 32767)).add(PRIDE, 4));
            ResearchsIS.addAspects(new ItemStack(Items.GOLDEN_LEGGINGS, 1, 32767), AspectHelper.getObjectAspects(new ItemStack(Items.GOLDEN_LEGGINGS, 1, 32767)).add(PRIDE, 4));
            ResearchsIS.addAspects(new ItemStack(Items.GOLDEN_BOOTS, 1, 32767), AspectHelper.getObjectAspects(new ItemStack(Items.GOLDEN_BOOTS, 1, 32767)).add(PRIDE, 4));
            ResearchsIS.addAspects(new ItemStack(Items.NETHER_STAR), AspectHelper.getObjectAspects(new ItemStack(Items.NETHER_STAR)).add(PRIDE, 10).add(NETHER, 20));
            ResearchsIS.addAspects(new ItemStack(Items.LEAD), AspectHelper.getObjectAspects(new ItemStack(Items.LEAD)).add(LUST, 5));
            ResearchsIS.addAspects(new ItemStack(Items.BED, 1, 32767), AspectHelper.getObjectAspects(new ItemStack(Items.BED, 1, 32767)).add(SLOTH, 10));
            ResearchsIS.addAspects(new ItemStack(Items.ENDER_PEARL), AspectHelper.getObjectAspects(new ItemStack(Items.ENDER_PEARL)).add(ENVY, 5));
            ResearchsIS.addAspects(new ItemStack(Items.COMPARATOR), AspectHelper.getObjectAspects(new ItemStack(Items.COMPARATOR)).add(ENVY, 10));
            ResearchsIS.addAspects(new ItemStack(Items.CAKE), AspectHelper.getObjectAspects(new ItemStack(Items.CAKE)).add(GLUTTONY, 10));
            ResearchsIS.addAspects(new ItemStack(Items.COOKIE), AspectHelper.getObjectAspects(new ItemStack(Items.COOKIE)).add(GLUTTONY, 1));

            ResearchsIS.addAspects(new ItemStack(Blocks.NETHERRACK), AspectHelper.getObjectAspects(new ItemStack(Blocks.NETHERRACK)).add(NETHER, 2));
            ResearchsIS.addAspects(new ItemStack(Blocks.QUARTZ_ORE), AspectHelper.getObjectAspects(new ItemStack(Blocks.QUARTZ_ORE)).add(NETHER, 5));
            ResearchsIS.addAspects(new ItemStack(Items.NETHER_WART), AspectHelper.getObjectAspects(new ItemStack(Items.NETHER_WART)).add(NETHER, 2));
            ResearchsIS.addAspects(new ItemStack(Items.SKULL, 1, 1), AspectHelper.getObjectAspects(new ItemStack(Items.SKULL, 1, 1)).add(NETHER, 10));
        }

    }

    public static void init() {
        ResearchCategories.registerCategory("ISORROPIA", "HEDGEALCHEMY", null, new ResourceLocation("isorropia", "textures/misc/logo.png"), new ResourceLocation("isorropia", "textures/research/background.jpg"));
        ThaumcraftApi.registerResearchLocation(new ResourceLocation("isorropia", "research/isorropia.json"));
        Config.CRYSTALS.addAll(Aspect.aspects.values().stream().map(aspect -> ThaumcraftApiHelper.makeCrystal(aspect)).collect(Collectors.toList()));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("isorropia", "wheat_growth"), new CrucibleRecipe("CULTIVATION", new ItemStack(Items.WHEAT), new ItemStack(Items.WHEAT_SEEDS), new AspectList().add(Aspect.LIGHT, 2).add(Aspect.EARTH, 2).add(Aspect.WATER, 2)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("isorropia", "carrot_dupe"), new CrucibleRecipe("CULTIVATION", new ItemStack(Items.CARROT, 2), new ItemStack(Items.CARROT), new AspectList().add(Aspect.LIGHT, 2).add(Aspect.EARTH, 2).add(Aspect.WATER, 2)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("isorropia", "potato_dupe"), new CrucibleRecipe("CULTIVATION", new ItemStack(Items.POTATO, 2), new ItemStack(Items.POTATO), new AspectList().add(Aspect.LIGHT, 2).add(Aspect.EARTH, 2).add(Aspect.WATER, 2)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("isorropia", "beet_dupe"), new CrucibleRecipe("CULTIVATION", new ItemStack(Items.BEETROOT), new ItemStack(Items.BEETROOT_SEEDS), new AspectList().add(Aspect.LIGHT, 2).add(Aspect.EARTH, 2).add(Aspect.WATER, 2)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("isorropia", "melon_growth"), new CrucibleRecipe("CULTIVATION", new ItemStack(Blocks.MELON_BLOCK), new ItemStack(Items.MELON_SEEDS), new AspectList().add(Aspect.LIGHT, 2).add(Aspect.EARTH, 2).add(Aspect.WATER, 2)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("isorropia", "pumpkin_growth"), new CrucibleRecipe("CULTIVATION", new ItemStack(Blocks.PUMPKIN), new ItemStack(Items.PUMPKIN_SEEDS), new AspectList().add(Aspect.LIGHT, 2).add(Aspect.EARTH, 2).add(Aspect.WATER, 2)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("isorropia", "egg_incubation"), new CrucibleRecipe("INCUBATION", new ItemStack(ItemsIS.itemIncubatedEgg), new ItemStack(Items.EGG), new AspectList().add(Aspect.LIFE, 5).add(Aspect.BEAST, 5)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("isorropia", "meat_dupe1"), new CrucibleRecipe("GROWTH", new ItemStack(Items.MUTTON, 2), new ItemStack(Items.MUTTON), new AspectList().add(Aspect.LIFE, 10)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("isorropia", "meat_dupe2"), new CrucibleRecipe("GROWTH", new ItemStack(Items.RABBIT, 2), new ItemStack(Items.RABBIT), new AspectList().add(Aspect.LIFE, 10)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("isorropia", "meat_dupe3"), new CrucibleRecipe("GROWTH", new ItemStack(Items.BEEF, 2), new ItemStack(Items.BEEF), new AspectList().add(Aspect.LIFE, 10)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("isorropia", "meat_dupe4"), new CrucibleRecipe("GROWTH", new ItemStack(Items.PORKCHOP, 2), new ItemStack(Items.PORKCHOP), new AspectList().add(Aspect.LIFE, 10)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("isorropia", "meat_dupe5"), new CrucibleRecipe("GROWTH", new ItemStack(Items.CHICKEN, 2), new ItemStack(Items.CHICKEN), new AspectList().add(Aspect.LIFE, 10)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("isorropia", "harding1"), new CrucibleRecipe("HARDNING", new ItemStack(Items.LEATHER), new ItemStack(Items.MUTTON), new AspectList().add(Aspect.EXCHANGE, 1).add(Aspect.CRAFT, 1)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("isorropia", "harding2"), new CrucibleRecipe("HARDNING", new ItemStack(Items.LEATHER), new ItemStack(Items.RABBIT), new AspectList().add(Aspect.EXCHANGE, 1).add(Aspect.CRAFT, 1)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("isorropia", "harding3"), new CrucibleRecipe("HARDNING", new ItemStack(Items.LEATHER), new ItemStack(Items.BEEF), new AspectList().add(Aspect.EXCHANGE, 1).add(Aspect.CRAFT, 1)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("isorropia", "harding4"), new CrucibleRecipe("HARDNING", new ItemStack(Items.LEATHER), new ItemStack(Items.PORKCHOP), new AspectList().add(Aspect.EXCHANGE, 1).add(Aspect.CRAFT, 1)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("isorropia", "harding5"), new CrucibleRecipe("HARDNING", new ItemStack(Items.LEATHER), new ItemStack(Items.CHICKEN), new AspectList().add(Aspect.EXCHANGE, 1).add(Aspect.CRAFT, 1)));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation("isorropia", "base_lens"), new ShapedArcaneRecipe(new ResourceLocation(""), "ARCANELENSES@1", 20, new AspectList().add(Aspect.AIR, 1).add(Aspect.EARTH, 1).add(Aspect.WATER, 1).add(Aspect.FIRE, 1).add(Aspect.ORDER, 1).add(Aspect.ENTROPY, 1), new ItemStack(ItemsIS.itemBaseLens), "GSG", "SPS", "GSG", Character.valueOf('G'), Items.GOLD_INGOT, Character.valueOf('S'), ItemsTC.salisMundus, Character.valueOf('P'), Blocks.GLASS_PANE));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("isorropia", "lens_fire"), new InfusionRecipe("FIRELENS", new ItemStack(ItemsIS.itemFireLens), 1, new AspectList().add(Aspect.LIGHT, 40).add(Aspect.ENERGY, 20).add(Aspect.SENSES, 30), new ItemStack(ItemsIS.itemBaseLens), ItemsTC.amber, ThaumcraftApiHelper.makeCrystal(Aspect.FIRE), ItemsTC.amber, ThaumcraftApiHelper.makeCrystal(Aspect.FIRE)));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("isorropia", "lens_air"), new InfusionRecipe("AIRLENS", new ItemStack(ItemsIS.itemAirLens), 1, new AspectList().add(Aspect.AURA, 15).add(Aspect.SENSES, 20), new ItemStack(ItemsIS.itemBaseLens), Items.GOLD_INGOT, ThaumcraftApiHelper.makeCrystal(Aspect.AIR), Items.GOLD_INGOT, ThaumcraftApiHelper.makeCrystal(Aspect.AIR)));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("isorropia", "lens_ordo"), new InfusionRecipe("ORDOLENS", new ItemStack(ItemsIS.itemOrdoLens), 1, new AspectList().add(Aspect.MIND, 32).add(Aspect.MAGIC, 32).add(Aspect.SENSES, 20), ItemsIS.itemBaseLens, ItemsTC.scribingTools, ThaumcraftApiHelper.makeCrystal(Aspect.ORDER), Items.BOOK, ThaumcraftApiHelper.makeCrystal(Aspect.ORDER)));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("isorropia", "lens_envy"), new InfusionRecipe("ENVYLENS", new ItemStack(ItemsIS.itemEnvyLens), 6, new AspectList().add(Aspect.DESIRE, 32).add(IsorropiaAPI.HUNGER, 32).add(LUST, 32).add(ENVY, 64), ItemsIS.itemBaseLens, Items.DIAMOND, ThaumcraftApiHelper.makeCrystal(ENVY), Items.PAPER, ThaumcraftApiHelper.makeCrystal(ENVY)));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("isorropia", "somatic_brain"), new InfusionRecipe("SOMATICBRAIN", new ItemStack(ItemsIS.itemSomaticBrain), 4, new AspectList().add(Aspect.MIND, 150).add(Aspect.DESIRE, 100).add(ENVY, 20), BlocksTC.jarBrain, new ItemStack(ItemsTC.plate), new ItemStack(ItemsTC.mind, 1, 1), Blocks.HOPPER, new ItemStack(ItemsTC.mind, 1, 1)));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("isorropia", "modified_matrix"), new InfusionRecipe("CREATUREINFUSIONS@1", new ItemStack(BlocksIS.blockModifiedMatrix), 8, new AspectList().add(Aspect.LIFE, 100).add(Aspect.CRAFT, 100).add(Aspect.MAGIC, 100).add(Aspect.BEAST, 100), BlocksTC.infusionMatrix, ItemsTC.salisMundus, ItemsTC.visResonator, ItemsTC.salisMundus, ItemsTC.visResonator));
        IsorropiaAPI.registerCreatureInfusionRecipe(new ResourceLocation("isorropia", "quicksilver_limbs"), new OrganCurativeInfusionRecipe.Builder().withOrganTarget(OrganCurativeInfusionRecipe.Organ.MUSCLE).withModifier("generic.movementSpeed", new AttributeModifier(UUID.fromString("b3f15142-2b27-11eb-adc1-0242ac120002"), "QUICKSILVER_SPEED", 0.08, 0)).withModifier("forge.swimSpeed", new AttributeModifier(UUID.fromString("c1719976-2b27-11eb-adc1-0242ac120002"), "QUICKSILVER_SWIM_SPEED", 0.2, 0)).build());
        IsorropiaAPI.registerCreatureInfusionRecipe(new ResourceLocation("isorropia", "jelly_rabbit"), new JellyRabbitRecipe());
        IsorropiaAPI.registerCreatureInfusionRecipe(new ResourceLocation("isorropia", "ore_boar"), new OreBoarRecipe());
        IsorropiaAPI.registerCreatureInfusionRecipe(new ResourceLocation("isorropia", "taintfeeder"),
                ((SpecieCurativeInfusionRecipe.Builder) new SpecieCurativeInfusionRecipe.Builder().withAspects(
                                new AspectList().add(IsorropiaAPI.HUNGER, 100).add(Aspect.PLANT, 50).add(Aspect.LIFE, 50).add(Aspect.FLUX, 45))
                        .withComponents(Ingredient.fromItem(ItemsTC.bottleTaint),
                                Ingredient.fromItem(Item.getItemFromBlock(BlocksTC.logSilverwood)),
                                Ingredient.fromItem(Item.getItemFromBlock(BlocksTC.jarVoid)),
                                Ingredient.fromItem(Item.getItemFromBlock(BlocksTC.logSilverwood)))
                        .withInstability(4).withKnowledgeRequirement("TAINTFEEDER"))
                        .withResult(EntityTaintPig.class)
                        .withPredicate(entity -> entity.getClass() == EntityPig.class)
                        .withFakeIngredients(Ingredient.fromStacks(ItemCat.createCat(ItemCat.EnumCat.PIG, "Pig"))
                                , ItemCat.createCat(ItemCat.EnumCat.PIG, "Taintfeeder")).build());

        IsorropiaAPI.registerCreatureInfusionRecipe(new ResourceLocation("isorropia", "selfshearing"), new CurativeInfusionRecipe.Builder().withAspects(new AspectList().add(Aspect.TOOL, 60).add(Aspect.MECHANISM, 30)).withComponents(Ingredient.fromItem(Items.SHEARS), Ingredient.fromItem(Items.COMPARATOR)).withInstability(2).withKnowledgeRequirement("SELFSHEARING").withPredicate(entity -> entity instanceof IShearable).withFakeIngredients(Ingredient.fromStacks(ItemCat.createCat(ItemCat.EnumCat.SHEEP, "Shearable Mob")), ItemCat.createCat(ItemCat.EnumCat.SELFSHEARING, "Self Shearing Mob")).withInformationNBT(new NBTTagCompound()).build());
        IsorropiaAPI.registerCreatureInfusionRecipe(new ResourceLocation("isorropia", "scholarschicken"), ((SpecieCurativeInfusionRecipe.Builder) new SpecieCurativeInfusionRecipe.Builder().withAspects(new AspectList().add(Aspect.SENSES, 60).add(Aspect.EXCHANGE, 30).add(Aspect.DARKNESS, 60)).withComponents(Ingredient.fromStacks(new ItemStack(Items.DYE, 1, 0)), Ingredient.fromItem(Items.SHEARS)).withInstability(2)).withResult(EntityScholarChicken.class).withKnowledgeRequirement("SCHOLARSCHICKEN").withPredicate(entity -> entity.getClass() == EntityChicken.class).withFakeIngredients(Ingredient.fromStacks(ItemCat.createCat(ItemCat.EnumCat.CHICKEN, "Chicken")), ItemCat.createCat(ItemCat.EnumCat.CHICKEN, "Scholar's Chicken")).build());
        IsorropiaAPI.registerCreatureInfusionRecipe(new ResourceLocation("isorropia", "chromaticsheep"), ((SpecieCurativeInfusionRecipe.Builder) new SpecieCurativeInfusionRecipe.Builder().withAspects(new AspectList().add(Aspect.SENSES, 60).add(Aspect.EXCHANGE, 30).add(Aspect.DARKNESS, 60)).withComponents(Ingredient.fromStacks(new ItemStack(Items.DYE, 1, 1)), Ingredient.fromStacks(new ItemStack(Items.DYE, 1, 2)), Ingredient.fromStacks(new ItemStack(Items.DYE, 1, 4))).withInstability(2)).withResult(EntityChromaticSheep.class).withKnowledgeRequirement("CHROMATICSHEEP").withPredicate(entity -> entity.getClass() == EntitySheep.class).withFakeIngredients(Ingredient.fromStacks(ItemCat.createCat(ItemCat.EnumCat.SHEEP, "Sheep")), ItemCat.createCat(ItemCat.EnumCat.SHEEP, "Chromatic Sheep")).build());
        IsorropiaAPI.registerCreatureInfusionRecipe(new ResourceLocation("isorropia", "gravekeeper"), ((SpecieCurativeInfusionRecipe.Builder) new SpecieCurativeInfusionRecipe.Builder().withAspects(new AspectList().add(Aspect.LIGHT, 100).add(Aspect.UNDEAD, 60).add(Aspect.ORDER, 80)).withComponents(Ingredient.fromItem(Items.BONE), Ingredient.fromItem(Item.getItemFromBlock(Blocks.GOLD_BLOCK)), Ingredient.fromItem(ItemsTC.amber), Ingredient.fromItem(Item.getItemFromBlock(BlocksTC.logSilverwood))).withInstability(6)).withResult(EntityGravekeeper.class).withKnowledgeRequirement("GRAVEKEEPERINFUSION").withCelestialAura(CelestialBody.SUN, 50).withPredicate(entity -> entity.getClass() == EntityOcelot.class).withFakeIngredients(Ingredient.fromStacks(ItemCat.createCat(ItemCat.EnumCat.OCELOT, "Feline")), ItemCat.createCat(ItemCat.EnumCat.OCELOT, "Gravekeeper")).build());
        IsorropiaAPI.registerCreatureInfusionRecipe(new ResourceLocation("isorropia", "enderheart"), (new OrganCurativeInfusionRecipe.Builder())
                .withOrganTarget(OrganCurativeInfusionRecipe.Organ.HEART)
                .withAspects((new AspectList()).add(IsorropiaAPI.FLESH, 60).add(Aspect.MOTION, 60).add(Aspect.ELDRITCH, 60))
                .withComponents(Ingredient.fromItem(Items.EGG),
                        Ingredient.fromItem(Items.EGG),
                        Ingredient.fromItem(Items.ENDER_PEARL))
                .withFakeIngredients(Ingredient.fromStacks(ItemCat.createCat(ItemCat.EnumCat.PIG, "Creature")), ItemCat.createCat(ItemCat.EnumCat.ENDERHEART, "Ender Heart"))
                .build());
        IsorropiaAPI.registerCreatureInfusionRecipe(new ResourceLocation("isorropia", "shockskin"), (new OrganCurativeInfusionRecipe.Builder())
                .withOrganTarget(OrganCurativeInfusionRecipe.Organ.SKIN)
                .withAspects((new AspectList()).add(Aspect.ENERGY, 30).add(Aspect.AVERSION, 30).add(Aspect.AIR, 5))
                .withComponents(Ingredient.fromItem(Items.QUARTZ),
                        Ingredient.fromItem(Items.REDSTONE),
                        Ingredient.fromItem(Items.QUARTZ),
                        Ingredient.fromStacks(ThaumcraftApiHelper.makeCrystal(Aspect.AIR)))
                .withFakeIngredients(Ingredient.fromStacks(ItemCat.createCat(ItemCat.EnumCat.PIG, "Creature")), ItemCat.createCat(ItemCat.EnumCat.SHOCK, "Shock Skin"))
                .build());
        IsorropiaAPI.registerCreatureInfusionRecipe(new ResourceLocation("isorropia", "awakened_blood"), (new OrganCurativeInfusionRecipe.Builder())
                .withOrganTarget(OrganCurativeInfusionRecipe.Organ.BLOOD)
                .withAspects((new AspectList()).add(Aspect.LIFE, 50).add(Aspect.MIND, 50))
                .withComponents(Ingredient.fromItem(Items.SPECKLED_MELON),
                        Ingredient.fromItem(Items.SPECKLED_MELON),
                        Ingredient.fromStacks(new ItemStack(Items.GOLDEN_APPLE)),
                        Ingredient.fromItem(Item.getItemFromBlock(BlocksTC.logSilverwood)))
                .withFakeIngredients(Ingredient.fromStacks(ItemCat.createCat(ItemCat.EnumCat.PIG, "Creature")), ItemCat.createCat(ItemCat.EnumCat.AWAKENED_BLOOD, "Awakened Blood"))
                .build());
        IsorropiaAPI.registerCreatureInfusionRecipe(new ResourceLocation("isorropia", "portability"), (new OrganCurativeInfusionRecipe.Builder())
                .withOrganTarget(OrganCurativeInfusionRecipe.Organ.VOID)
                .withAspects((new AspectList()).add(Aspect.VOID, 50).add(Aspect.MOTION, 50))
                .withComponents(Ingredient.fromItem(Items.ENDER_PEARL),
                        Ingredient.fromItem(Item.getItemFromBlock(BlocksTC.jarNormal)))
                .withFakeIngredients(Ingredient.fromStacks(ItemCat.createCat(ItemCat.EnumCat.PIG, "Creature")), ItemCat.createCat(ItemCat.EnumCat.PORTABILITY, "Portability"))
                .build());
        IsorropiaAPI.registerCreatureInfusionRecipe(new ResourceLocation("isorropia", "diamond_skin"), (new OrganCurativeInfusionRecipe.Builder())

                .withOrganTarget(OrganCurativeInfusionRecipe.Organ.SKIN)
                .withModifier("generic.armor", new AttributeModifier(UUID.fromString("6bc2ebe8-2b1c-11eb-adc1-0242ac120002"), "DIAMOND_SKIN", 20.0D, 0))
                .withModifier("generic.armorToughness", new AttributeModifier(UUID.fromString("21afc412-2b1d-11eb-adc1-0242ac120002"), "DIAMOND_SKIN_TOUGHNESS", 8.0D, 0))
                .withAspects((new AspectList()).add(Aspect.BEAST, 10).add(Aspect.PROTECT, 30).add(Aspect.CRYSTAL, 30))
                .withComponents(Ingredient.fromItem(Items.DIAMOND),
                        Ingredient.fromItem(Items.DIAMOND),
                        Ingredient.fromItem(Items.LEATHER))
                .withFakeIngredients(Ingredient.fromStacks(ItemCat.createCat(ItemCat.EnumCat.PIG, "Creature")), ItemCat.createCat(ItemCat.EnumCat.DIAMOND_SKIN, "Diamond Skin"))
                .build());
        IsorropiaAPI.registerCreatureInfusionRecipe(new ResourceLocation("isorropia", "mooshroom"),
                ((SpecieCurativeInfusionRecipe.Builder) new SpecieCurativeInfusionRecipe.Builder()
                        .withAspects(new AspectList().add(Aspect.BEAST, 10).add(Aspect.PLANT, 30))
                        .withComponents(Ingredient.fromStacks(new ItemStack(Blocks.BROWN_MUSHROOM)),
                                Ingredient.fromItem(Item.getItemFromBlock(Blocks.RED_MUSHROOM)),
                                Ingredient.fromItem(Item.getItemFromBlock(Blocks.BROWN_MUSHROOM)),
                                Ingredient.fromItem(Item.getItemFromBlock(Blocks.RED_MUSHROOM)))
                        .withInstability(4).withKnowledgeRequirement("MONSTEREXCHANGE"))
                        .withResult(EntityMooshroom.class)
                        .withPredicate(entity -> entity.getClass() == EntityCow.class)
                        .withFakeIngredients(Ingredient.fromStacks(ItemCat.createCat(ItemCat.EnumCat.COW, "Cow"))
                                , ItemCat.createCat(ItemCat.EnumCat.COW, "Mooshroom")).build());

        IsorropiaAPI.registerCreatureInfusionRecipe(new ResourceLocation("isorropia", "chocow"),
                ((SpecieCurativeInfusionRecipe.Builder) new SpecieCurativeInfusionRecipe.Builder()
                        .withAspects(new AspectList().add(IsorropiaAPI.FLESH, 30).add(Aspect.EXCHANGE, 10))
                        .withComponents(Ingredient.fromStacks(new ItemStack(Items.DYE, 1, 3)),
                                Ingredient.fromItem(Items.SUGAR),
                                Ingredient.fromStacks(new ItemStack(Items.DYE, 1, 3)),
                                Ingredient.fromItem(Items.SUGAR))
                        .withInstability(2).withKnowledgeRequirement("CHOCOCOW"))
                        .withResult(EntityChocow.class)
                        .withPredicate(entity -> entity.getClass() == EntityCow.class)
                        .withFakeIngredients(Ingredient.fromStacks(ItemCat.createCat(ItemCat.EnumCat.COW, "Cow"))
                                , ItemCat.createCat(ItemCat.EnumCat.COW, "ChocoCow")).build());

        IsorropiaAPI.registerCreatureInfusionRecipe(new ResourceLocation("isorropia", "goldchicken"),
                ((SpecieCurativeInfusionRecipe.Builder) new SpecieCurativeInfusionRecipe.Builder()
                        .withAspects(new AspectList().add(Aspect.CRAFT, 45).add(Aspect.DESIRE, 30).add(Aspect.METAL, 30))
                        .withComponents(Ingredient.fromItem(Items.GOLDEN_APPLE),
                                Ingredient.fromStacks(new ItemStack(BlocksTC.crucible)),
                                Ingredient.fromItem(Items.GOLDEN_APPLE),
                                Ingredient.fromStacks(new ItemStack(BlocksTC.metalAlchemical)))
                        .withInstability(2).withKnowledgeRequirement("GOLDCHICKEN"))
                        .withResult(EntityGoldenChicken.class)
                        .withPredicate(entity -> entity.getClass() == EntityChicken.class)
                        .withFakeIngredients(Ingredient.fromStacks(ItemCat.createCat(ItemCat.EnumCat.CHICKEN, "Chicken"))
                                , ItemCat.createCat(ItemCat.EnumCat.CHICKEN, "GoldChicken")).build());

        IsorropiaAPI.registerCreatureInfusionRecipe(new ResourceLocation("isorropia", "guardian_panther"), ((SpecieCurativeInfusionRecipe.Builder) new SpecieCurativeInfusionRecipe.Builder()
                .withAspects(new AspectList().add(Aspect.BEAST, 30).add(Aspect.AVERSION, 30))
                .withComponents(Ingredient.fromStacks(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.STRONG_STRENGTH)), Ingredient.fromItem(Items.SADDLE))
                .withInstability(2).withKnowledgeRequirement("GUARDIAN_PANTHER"))
                .withResult(EntityGuardianPanther.class)
                .withPredicate(entity -> entity.getClass() == EntityOcelot.class)
                .withFakeIngredients(Ingredient.fromStacks(ItemCat.createCat(ItemCat.EnumCat.OCELOT, "Ocelot"))
                        , ItemCat.createCat(ItemCat.EnumCat.OCELOT, "panther")).build());

        IsorropiaAPI.registerCreatureInfusionRecipe(new ResourceLocation("isorropia", "nether_hound"), ((SpecieCurativeInfusionRecipe.Builder) new SpecieCurativeInfusionRecipe.Builder()
                .withAspects(new AspectList().add(Aspect.FIRE, 30).add(Aspect.AVERSION, 30).add(Aspect.ALCHEMY, 30))
                .withComponents(Ingredient.fromItem(Items.LAVA_BUCKET), Ingredient.fromItem(Items.BLAZE_ROD), Ingredient.fromStacks(ThaumcraftApiHelper.makeCrystal(Aspect.FIRE)), Ingredient.fromItem(Items.BLAZE_ROD))
                .withInstability(2).withKnowledgeRequirement("NETHER_HOUND"))
                .withResult(EntityHellHound.class)
                .withPredicate(entity -> entity.getClass() == EntityWolf.class)
                .withFakeIngredients(Ingredient.fromStacks(ItemCat.createCat(ItemCat.EnumCat.WOLF, "Dog"))
                        , ItemCat.createCat(ItemCat.EnumCat.WOLF, "hellhound")).build());

        Ingredient food = Ingredient.fromItems(Items.CARROT, Items.POTATO, Items.BEETROOT, Items.WHEAT, Items.APPLE, Items.BONE, Items.BEETROOT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.WHEAT_SEEDS);
        IsorropiaAPI.registerCreatureInfusionRecipe(new ResourceLocation("isorropia", "instilledfidelity"), new CurativeInfusionRecipe.Builder().withAspects(new AspectList().add(Aspect.BEAST, 30).add(Aspect.MIND, 30)).withComponents(food, food, food).withInstability(2).withKnowledgeRequirement("INSTILLEDFIDELITY").withPredicate(entity -> entity instanceof EntityTameable).withFakeIngredients(Ingredient.fromStacks(ItemCat.createCat(ItemCat.EnumCat.WOLF, "Tameable Mob")), ItemCat.createCat(ItemCat.EnumCat.LOVE, "Tamed Mob")).build());

        if (Loader.isModLoaded("thaumicbases")) {
            ThaumcraftApi.registerResearchLocation(new ResourceLocation("isorropia", "research/tb_compat.json"));
            EntityDopeSquid.makeDopeSquidRecipe();
        }

        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("isorropia", "corpse_effigy"),
                new InfusionRecipe("CLONESELF@0", new ItemStack(ItemsIS.corpseEffigy), 3,
                        new AspectList().add(Aspect.CRAFT, 30).add(Aspect.MAN, 30),
                        ItemsTC.brain,
                        Items.ROTTEN_FLESH,
                        Items.BONE,
                        Items.ROTTEN_FLESH,
                        Items.BONE,
                        Items.ROTTEN_FLESH,
                        Items.BONE,
                        Items.ROTTEN_FLESH,
                        Items.BONE));

        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("isorropia", "soul_beacon"),
                new InfusionRecipe("CLONESELF", new ItemStack(BlocksIS.blockSoulBeacon), 8,
                        new AspectList().add(Aspect.MOTION, 100).add(Aspect.DEATH, 100).add(Aspect.SOUL, 50).add(Aspect.TRAP, 100),
                        Ingredient.fromItem(ItemsTC.primordialPearl),
                        Item.getItemFromBlock(BlocksTC.jarVoid),
                        "ingotVoid",
                        Items.ENDER_EYE,
                        Item.getItemFromBlock(Blocks.BEACON),
                        Items.ENDER_EYE,
                        "ingotVoid"));

        IsorropiaAPI.registerSelfInfusionRecipe(new ResourceLocation("isorropia", "selfInfusions.test"),
                new SelfInfusionRecipe(
                        /*"selfInfusion"*/"",
                        1,
                        (new AspectList()).add(Aspect.AIR, 1),
                        new ItemStack[]{new ItemStack(Items.APPLE)},
                        4), "test");

        IsorropiaAPI.registerSelfInfusionRecipe(new ResourceLocation("isorropia", "selfInfusions.quicksilverLimb"),
                new SelfInfusionRecipe(
                        "SELFINFUSION",
                        4,
                        new AspectList().add(Aspect.MOTION, 100).add(Aspect.MECHANISM, 100).add(Aspect.FLIGHT, 100),
                        new ItemStack[]{
                                PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.STRONG_SWIFTNESS),
                                new ItemStack(ItemsTC.ingots, 1, 1),
                                new ItemStack(ItemsTC.quicksilver),
                                PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.STRONG_SWIFTNESS),
                                new ItemStack(ItemsTC.ingots, 1, 1),
                                new ItemStack(ItemsTC.quicksilver)},
                        1), "QuickSilver Limb");
        IsorropiaAPI.registerSelfInfusionRecipe(new ResourceLocation("isorropia", "selfInfusions.awakenedBlood"),
                new SelfInfusionRecipe(
                        "SELFINFUSION",
                        5,
                        new AspectList().add(Aspect.LIFE, 100).add(Aspect.LIFE, 100).add(Aspect.MIND, 50),
                        new ItemStack[]{new ItemStack(Items.GOLDEN_APPLE), new ItemStack(Items.SPECKLED_MELON),
                                new ItemStack(Items.SPECKLED_MELON), new ItemStack(ItemsTC.brain),
                                new ItemStack(Items.SPECKLED_MELON), new ItemStack(Items.SPECKLED_MELON)},
                        3), "Awakened Blood");

        IsorropiaAPI.registerSelfInfusionRecipe(new ResourceLocation("isorropia", "selfInfusions.diamondSkin"),
                new SelfInfusionRecipe(
                        "SELFINFUSION",
                        6,
                        new AspectList().add(Aspect.PROTECT, 30).add(Aspect.CRYSTAL, 30).add(Aspect.MAN, 30),
                        new ItemStack[]{new ItemStack(Items.LEATHER_HELMET), new ItemStack(Items.DIAMOND),
                                new ItemStack(Items.LEATHER_CHESTPLATE), new ItemStack(Blocks.DIAMOND_BLOCK),
                                new ItemStack(Items.LEATHER_LEGGINGS), new ItemStack(Items.DIAMOND),
                                new ItemStack(Items.LEATHER_BOOTS), new ItemStack(Blocks.DIAMOND_BLOCK)},
                        4), "Diamond Skin");
        IsorropiaAPI.registerSelfInfusionRecipe(new ResourceLocation("isorropia", "selfInfusions.silverHeart"),
                new SelfInfusionRecipe(
                        "SILVERHEART",
                        7,
                        (new AspectList()).add(Aspect.ORDER, 50).add(Aspect.LIFE, 50).add(Aspect.EXCHANGE, 30),
                        new ItemStack[]{new ItemStack(BlocksTC.saplingSilverwood, 1, 0),
                                new ItemStack(BlocksTC.logSilverwood, 1, 0),
                                new ItemStack(BlocksTC.shimmerleaf, 1, 0),
                                new ItemStack(BlocksTC.logSilverwood, 1, 0)},
                        5), "silverHeart");
        IsorropiaAPI.registerSelfInfusionRecipe(new ResourceLocation("isorropia", "selfInfusions.synthSkin"),
                new SelfInfusionRecipe(
                        "SYNTHSKIN",
                        6,
                        new AspectList().add(Aspect.PLANT, 100).add(IsorropiaAPI.HUNGER, 100).add(Aspect.LIGHT, 100)
                                .add(Aspect.MAN, 16),
                        new ItemStack[]{new ItemStack(Blocks.LEAVES, 1, 32767),
                                new ItemStack(Blocks.SAPLING, 1, 32767), new ItemStack(Blocks.RED_FLOWER, 1, 32767),
                                new ItemStack(Blocks.VINE)},
                        6), "Sync Skin");
        IsorropiaAPI.registerSelfInfusionRecipe(new ResourceLocation("isorropia", "selfInfusions.amphibious"),
                new SelfInfusionRecipe(
                        "AMPHIBIOUS",
                        7,
                        new AspectList().add(Aspect.WATER, 100).add(Aspect.AIR, 100).add(Aspect.LIFE, 50)
                                .add(Aspect.EXCHANGE, 16),
                        new ItemStack[]{new ItemStack(Items.FISH, 1, 32767), PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.LONG_WATER_BREATHING),
                                new ItemStack(Items.FISH, 1, 32767), PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.LONG_WATER_BREATHING)},
                        7), "amphibious");
        IsorropiaAPI.registerSelfInfusionRecipe(new ResourceLocation("isorropia", "selfInfusions.warpedTumor"),
                new SelfInfusionRecipe(
                        "WARPEDTUMOR",
                        12,
                        new AspectList().add(Aspect.FLUX, 100).add(Aspect.ELDRITCH, 75).add(Aspect.TRAP, 50)
                                .add(IsorropiaAPI.FLESH, 45).add(Aspect.EXCHANGE, 30),
                        new ItemStack[]{new ItemStack(BlocksTC.fleshBlock),
                                new ItemStack(ItemsTC.bathSalts, 1, 0), new ItemStack(Items.NETHER_STAR, 1, 0),
                                new ItemStack(Blocks.OBSIDIAN, 1, 0),
                                new ItemStack(Items.NETHER_STAR, 1, 0),
                                new ItemStack(ItemsTC.bathSalts, 1, 0)},
                        8), "warpedTumor");
        IsorropiaAPI.registerSelfInfusionRecipe(new ResourceLocation("isorropia", "selfInfusions.spiderClimb"),
                new SelfInfusionRecipe(
                        "SPIDERCLIMB",
                        8,
                        new AspectList().add(Aspect.BEAST, 50).add(Aspect.MOTION, 75).add(Aspect.ALCHEMY, 50),
                        new ItemStack[]{new ItemStack(Blocks.WEB), new ItemStack(ItemsTC.fabric),
                                new ItemStack(Blocks.WEB), new ItemStack(Blocks.LADDER), new ItemStack(Blocks.WEB),
                                new ItemStack(ItemsTC.fabric), new ItemStack(Blocks.WEB),
                                new ItemStack(Blocks.LADDER)},
                        9), "spiderClimb");
        IsorropiaAPI.registerSelfInfusionRecipe(new ResourceLocation("isorropia", "selfInfusions.morphicFingers"),
                new SelfInfusionRecipe(
                        "MORPHICFINGERS",
                        6,
                        (new AspectList()).add(Aspect.TOOL, 50).add(Aspect.CRAFT, 50).add(Aspect.MAN, 50).add(Aspect.EXCHANGE, 30),
                        new ItemStack[]{
                                new ItemStack(BlocksTC.arcaneWorkbench, 1, 0),
                                new ItemStack(ItemsTC.ingots, 1, 1),
                                new ItemStack(ItemsTC.salisMundus, 1, 0),
                                new ItemStack(ItemsTC.ingots, 1, 1)},
                        2), "MorphicFingers");
        IsorropiaAPI.registerSelfInfusionRecipe(new ResourceLocation("isorropia", "selfInfusions.chameleonSkin"),
                new SelfInfusionRecipe(
                        "CHAMELEONSKIN",
                        7,
                        new AspectList().add(Aspect.SENSES, 75).add(Aspect.EXCHANGE, 50).add(Aspect.VOID, 50),
                        new ItemStack[]{new ItemStack(Items.SPIDER_EYE), new ItemStack(Items.DYE, 1, 1),
                                new ItemStack(Items.DYE, 1, 4), new ItemStack(Items.DYE, 1, 11),
                                new ItemStack(Items.DYE, 1, 0), new ItemStack(Items.DYE, 1, 15)},
                        10), "chameleonSkin");

        ScanningManager.addScannableThing(new ScanEntityResearch("!scan.animal", EntityAnimal.class, true, "CREATUREINFUSIONS@1", "research.scan.animal.text"));
        ScanningManager.addScannableThing(new ScanEntityResearch("!scan.taint", ITaintedMob.class, true, "CURATIVEVAT@1", "research.scan.taint.text"));
        ScanningManager.addScannableThing(new ScanEntityResearch("!scan.chicken", EntityChicken.class, false, "SIMILITUDOINFUSIONS@1", "research.scan.chicken.text"));
        ScanningManager.addScannableThing(new ScanEntityResearch("!scan.cow", EntityCow.class, false, "SIMILITUDOINFUSIONS@1", "research.scan.cow.text"));
        ScanningManager.addScannableThing(new ScanEntityResearch("!scan.pig", EntityPig.class, false, "SIMILITUDOINFUSIONS@1", "research.scan.pig.text"));
        ScanningManager.addScannableThing(new ScanEntityResearch("!scan.pigman", EntityPigZombie.class, false, "SIMILITUDOINFUSIONS@2", "research.scan.pigman.text"));
        ScanningManager.addScannableThing(new ScanEntityResearch("!scan.enderman", EntityEnderman.class, false, "SIMILITUDOINFUSIONS@3", "research.scan.enderman.text"));
        ScanningManager.addScannableThing(new ScanEntityResearch("!scan.golem", EntityIronGolem.class, false, "SIMILITUDOINFUSIONS@3", "research.scan.golem.text"));
        ScanningManager.addScannableThing(new ScanEntityResearch("!scan.villager", EntityVillager.class, false, "SIMILITUDOINFUSIONS@3", "research.scan.villager.text"));
        ScanningManager.addScannableThing(new ScanEntityResearch("!scan.sheep", EntitySheep.class, false, "SIMILITUDOINFUSIONS@3", "research.scan.sheep.text"));
        ScanningManager.addScannableThing(new ScanEntityResearch("!scan.slime", EntitySlime.class, false, "JELLYRABBIT@0", "research.scan.slime.text"));
        ScanningManager.addScannableThing(new ScanEntityResearch("!scan.rabbit", EntityRabbit.class, false, "JELLYRABBIT@0", "research.scan.rabbit.text"));
        ScanningManager.addScannableThing(new ScanFidelity());
        ScanningManager.addScannableThing(new ScanTameable());
        ScanningManager.addScannableThing(new ScanSun());
    }
}
