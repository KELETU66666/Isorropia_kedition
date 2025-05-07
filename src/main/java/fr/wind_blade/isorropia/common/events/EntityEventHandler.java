package fr.wind_blade.isorropia.common.events;

import baubles.api.BaublesApi;
import fr.wind_blade.isorropia.common.Common;
import fr.wind_blade.isorropia.common.blocks.material.MaterialIR;
import fr.wind_blade.isorropia.common.capabilities.LivingBaseCapability;
import fr.wind_blade.isorropia.common.capabilities.LivingCapability;
import fr.wind_blade.isorropia.common.config.Config;
import fr.wind_blade.isorropia.common.entities.EntityHangingLabel;
import fr.wind_blade.isorropia.common.items.ItemsIS;
import fr.wind_blade.isorropia.common.lenses.Lens;
import fr.wind_blade.isorropia.common.lenses.LensManager;
import fr.wind_blade.isorropia.common.libs.helpers.IsorropiaHelper;
import fr.wind_blade.isorropia.common.network.LensRemoveMessage;
import fr.wind_blade.isorropia.common.research.recipes.OrganCurativeInfusionRecipe;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.casters.ICaster;
import thaumcraft.api.items.IGoggles;
import thaumcraft.api.items.IRevealer;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.api.research.ResearchEvent;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.items.tools.ItemThaumometer;
import thaumcraft.common.lib.research.ResearchManager;

import java.util.ArrayList;
import java.util.Random;


public class EntityEventHandler {
    private static final ItemStack SHEARS = new ItemStack(Items.SHEARS);
    private Lens theRightLens;
    private static final Random randy = new Random();
    private Lens theLeftLens;

    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            World world = event.player.world;
            ItemStack revealer = LensManager.getRevealer(event.player);

            if (!revealer.isEmpty() && revealer.hasTagCompound() && revealer.getTagCompound().getString("LeftLens") != null) {
                this.theLeftLens = LensManager.getLens(revealer, LensManager.LENSSLOT.LEFT);
                this.theRightLens = LensManager.getLens(revealer, LensManager.LENSSLOT.RIGHT);
                boolean doubleLens = (this.theRightLens != null && this.theRightLens.equals(this.theLeftLens));


                if (this.theLeftLens != null) {
                    this.theLeftLens.handleTicks(world, event.player, doubleLens);
                }
                if (!doubleLens && this.theRightLens != null) {
                    this.theRightLens.handleTicks(world, event.player, false);
                }
            }
        }
    }

    @SubscribeEvent
    public void onAttachCapability(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof EntityLiving) {
            event.addCapability(new ResourceLocation("isorropia", "LIVING_CAPABILITY"), new LivingCapability((EntityLiving) event.getObject()));
        } else if (entity instanceof EntityLivingBase) {
            event.addCapability(new ResourceLocation("isorropia", "LIVING_BASE_CAPABILITY"), new LivingBaseCapability((EntityLivingBase) event.getObject()));
        }
    }


    @SubscribeEvent
    public void onRevealerRemoved(LivingEquipmentChangeEvent event) {
        ItemStack stack = event.getFrom();

        if (!(event.getEntityLiving() instanceof EntityPlayer))
            return;

        if (stack.getItem() instanceof IRevealer && ((IRevealer) stack.getItem()).showNodes(stack, event.getEntityLiving())) {

            Lens lens = LensManager.getLens(stack, LensManager.LENSSLOT.LEFT);

            Lens lens2 = LensManager.getLens(stack, LensManager.LENSSLOT.RIGHT);

            if (lens != null)
                lens.handleRemoval((event.getEntityLiving()).world, (EntityPlayer) event.getEntityLiving());

            if (lens2 != null)
                lens2.handleRemoval((event.getEntityLiving()).world, (EntityPlayer) event.getEntityLiving());

            Common.INSTANCE.sendTo(new LensRemoveMessage(lens, lens2), (EntityPlayerMP) event.getEntityLiving());
        }
    }

    @SubscribeEvent
    public void onRevealerDestroy(PlayerDestroyItemEvent event) {
        ItemStack stack = event.getOriginal();
        EntityPlayer player = event.getEntityPlayer();

        if (stack.getItem() instanceof IRevealer && ((IRevealer) stack.getItem()).showNodes(stack, event.getEntityLiving())) {

            Lens lens = LensManager.getLens(stack, LensManager.LENSSLOT.LEFT);

            Lens lens2 = LensManager.getLens(stack, LensManager.LENSSLOT.RIGHT);

            if (lens != null && !player.inventory.addItemStackToInventory(new ItemStack(lens.getItemLens())))
                player.dropItem(new ItemStack(lens.getItemLens()), false);

            if (lens2 != null &&
                    !player.inventory.addItemStackToInventory(new ItemStack(lens.getItemLens()))) {

                player.dropItem(new ItemStack(lens.getItemLens()), false);
            }
        }
    }


    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
    }


    @SubscribeEvent
    public void onLivingUpdateEvent(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        LivingBaseCapability cap = Common.getCap(entity);

        cap.update();
        if (entity.isInsideOfMaterial(MaterialIR.LIQUID_VAT)) {
            entity.setAir(300);
        }
        if (!entity.world.isRemote) {
            if (entity instanceof IShearable) {
                IShearable shear = (IShearable) entity;

                if (entity.ticksExisted % 100 == 0 && cap.infusions.containsKey(new ResourceLocation("isorropia:selfshearing")) && shear.isShearable(SHEARS, entity.world, entity.getPosition())) {
                    ArrayList<ItemStack> drops = (ArrayList<ItemStack>) shear.onSheared(SHEARS, entity.world, entity.getPosition(), 0);
                    Random rand = new Random();
                    for (ItemStack stack : drops) {

                        EntityItem entityDropItem = entity.entityDropItem(stack, 1.0F), ent = entityDropItem;
                        entityDropItem.motionY += (rand.nextFloat() * 0.05F);
                        EntityItem entityItem = ent;
                        entityItem.motionX += ((rand.nextFloat() - rand.nextFloat()) * 0.1F);
                        EntityItem entityItem2 = ent;
                        entityItem2.motionZ += ((rand.nextFloat() - rand.nextFloat()) * 0.1F);
                    }
                }
            }
            if (entity.ticksExisted % 50 == 0 && cap.entityHasOrgan(OrganCurativeInfusionRecipe.Organ.BLOOD, "isorropia:awakened_blood") &&
                    entity.getHealth() < entity.getMaxHealth())
                entity.heal(1.0F);
        }
    }

    @SubscribeEvent
    public void onStartTrack(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof EntityLivingBase) {
            Common.getCap((EntityLivingBase) event.getTarget())
                    .synchStartTracking((EntityPlayerMP) event.getEntityPlayer());
        }
    }

    @SubscribeEvent
    public void onPlayerJoin(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event) {
        Common.getCap(event.player).synchStartTracking((EntityPlayerMP) event.player);
    }

    @SubscribeEvent
    public void onPlayerBlockInterract(PlayerInteractEvent.RightClickBlock event) {

        ItemStack stack = event.getEntityPlayer().getHeldItem(event.getHand());

        World world = event.getWorld();

        if (!world.isRemote && stack.getItem() == ItemsTC.label &&
                ThaumcraftCapabilities.getKnowledge(event.getEntityPlayer()).isResearchKnown("HANGINGLABEL") &&
                !(world.getBlockState(event.getPos()).getBlock() instanceof thaumcraft.api.blocks.ILabelable) && event.getFace() != EnumFacing.DOWN && event.getFace() != EnumFacing.UP && event.getEntityPlayer().canPlayerEdit(event.getPos().offset(event.getFace()), event.getFace(), stack)) {

            AspectList aspects = ((IEssentiaContainerItem) stack.getItem()).getAspects(stack);


            EntityHangingLabel entityMessage = new EntityHangingLabel(world, true, event.getPos().offset(event.getFace()), event.getFace(), (aspects != null) ? aspects.getAspects()[0] : null);


            if (entityMessage.onValidSurface()) {

                entityMessage.playPlaceSound();

                world.spawnEntity(entityMessage);

                stack.shrink(1);
            }
        }
    }

    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {

        ItemStack stack = event.getItemStack();

        if (event.getEntityPlayer() != null && event.getTarget() instanceof EntityZombieVillager) {

            EntityZombieVillager entity = (EntityZombieVillager) event.getTarget();

            if (stack.getItem() == Items.GOLDEN_APPLE && stack.getMetadata() == 0 && entity.isPotionActive(MobEffects.WEAKNESS) && !entity.isConverting()) {

                IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(event.getEntityPlayer());

                if (!knowledge.isResearchKnown("!experiment.villager_zombie")) {

                    knowledge.addResearch("!experiment.villager_zombie");

                    knowledge.sync((EntityPlayerMP) event.getEntity());

                    ((EntityPlayer) event.getEntity()).sendStatusMessage(new TextComponentString(TextFormatting.DARK_PURPLE +

                            I18n.format("research.experiment.villager_zombie.text", new Object[0])), false);
                }
            }
        }
    }


    @SubscribeEvent
    public void onItemRightClick(PlayerInteractEvent.RightClickItem event) {
        ItemStack stack = event.getItemStack();

        if (!event.getWorld().isRemote && (stack.getItem() instanceof IRevealer || stack.getItem() instanceof IGoggles) && stack.hasTagCompound()) {
            String lens = stack.getTagCompound().getString(LensManager.LENSSLOT.LEFT.getName());

            if (!lens.equals("isorropia:ordo_lens")) {
                lens = stack.getTagCompound().getString(LensManager.LENSSLOT.RIGHT.getName());
            }

            if (lens.equals("isorropia:ordo_lens")) {
                ((ItemThaumometer) ItemsTC.thaumometer).doScan(event.getWorld(), event.getEntityPlayer());
            }
        }
    }

    @SubscribeEvent
    public void onItemUse(PlayerInteractEvent.EntityInteract event) {
        ItemStack stack = event.getItemStack();
        if (event.getTarget() instanceof EntityLivingBase && !event.getWorld().isRemote) {
            EntityLivingBase target = (EntityLivingBase) event.getTarget();
            LivingBaseCapability cap = Common.getCap(target);
            if (target instanceof EntityLiving && cap.entityHasOrgan(OrganCurativeInfusionRecipe.Organ.VOID, "isorropia:portability") && stack.getItem() instanceof ICaster) {
                if (!IsorropiaHelper.doPlayerHaveJar(event.getEntityPlayer(), false)) {
                    return;
                }
                if (!IsorropiaHelper.canEntityBeJarred((EntityLiving) target)) {
                    event.getEntityPlayer().sendMessage(new TextComponentString(String.valueOf(TextFormatting.ITALIC) + TextFormatting.GRAY + I18n.format("isorropia.containment.fail")));
                    return;
                }
                IsorropiaHelper.playerJarEntity(event.getEntityPlayer(), (EntityLiving) target);

                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onAttack(LivingAttackEvent event) {
        EntityLivingBase base = event.getEntityLiving();
        DamageSource source = event.getSource();
        LivingBaseCapability cap = Common.getCap(base);

        if (cap.entityHasOrgan(OrganCurativeInfusionRecipe.Organ.HEART, "isorropia:enderheart") && !base.isEntityInvulnerable(source) && source instanceof EntityDamageSourceIndirect) {
            base.world.playSound(null, base.prevPosX, base.prevPosY, base.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, base.getSoundCategory(), 1.0F, 1.0F);
            base.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
            base.attemptTeleport(base.posX, base.posY, base.posZ);
            base.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 3));
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onAttack(LivingDamageEvent event) {
        EntityLivingBase base = event.getEntityLiving();
        LivingBaseCapability cap = Common.getCap(base);
        Entity target = event.getSource().getTrueSource();

        if (target instanceof EntityLivingBase && cap.entityHasOrgan(OrganCurativeInfusionRecipe.Organ.SKIN, "isorropia:shockskin")) {

            FXDispatcher.INSTANCE.arcLightning(base.posX, base.posY, base.posZ, target.posX, target.posY, target.posZ, 0.2F, 0.8F, 0.8F, 1.0F);

            target.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 4.0F);
        }
    }


    @SubscribeEvent
    public void onPlayerResearch(ResearchEvent.Research event) {
    }


    private boolean hardCodedAntiRecursive = false;


    @SubscribeEvent
    public void onPlayerGetKnowledge(ResearchEvent.Knowledge event) {
        ItemStack stack = event.getPlayer().getItemStackFromSlot(EntityEquipmentSlot.HEAD);

        if (stack.getItem() != ItemsIS.itemSomaticBrain) {
            stack = BaublesApi.getBaublesHandler(event.getPlayer()).getStackInSlot(4);
        }

        if (stack.getItem() == ItemsIS.itemSomaticBrain && !this.hardCodedAntiRecursive) {
            this.hardCodedAntiRecursive = true;
            ResearchManager.addKnowledge(event.getPlayer(), event.getType(), event.getCategory(),
                    (int) Math.round(event.getAmount() * ((event.getType() == IPlayerKnowledge.EnumKnowledgeType.THEORY) ? 0.3D : 0.6D)));
            this.hardCodedAntiRecursive = false;
        }
    }

    @SubscribeEvent
    public void onExperienceDrop(LivingExperienceDropEvent event) {
        EntityPlayer player = event.getAttackingPlayer();

        if (player != null) {
            ItemStack stack = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

            if (stack.getItem() != ItemsIS.itemSomaticBrain) {
                stack = BaublesApi.getBaublesHandler(player).getStackInSlot(4);
            }
            if (stack.getItem() == ItemsIS.itemSomaticBrain) {
                event.setDroppedExperience(event.getDroppedExperience() * 2);
            }
        }
    }

    public void addDrop(LivingDropsEvent event, ItemStack drop) {
        EntityItem entityitem = new EntityItem((event.getEntityLiving()).world, (event.getEntityLiving()).posX, (event.getEntityLiving()).posY, (event.getEntityLiving()).posZ, drop);
        entityitem.setPickupDelay(10);
        event.getDrops().add(entityitem);
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if (eventArgs.getModID().equals("Thaumic Isorropia")) {
            Config.syncConfigurable();
            if (Config.config != null && Config.config.hasChanged())
                Config.save();
        }
    }
}