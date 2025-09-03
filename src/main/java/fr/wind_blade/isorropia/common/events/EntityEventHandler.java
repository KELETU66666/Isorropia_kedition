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
import fr.wind_blade.isorropia.common.libs.helpers.EmptyTeleporter;
import fr.wind_blade.isorropia.common.libs.helpers.IsorropiaHelper;
import fr.wind_blade.isorropia.common.network.LensRemoveMessage;
import fr.wind_blade.isorropia.common.network.PacketPlayerInfusionSync;
import fr.wind_blade.isorropia.common.research.recipes.OrganCurativeInfusionRecipe;
import fr.wind_blade.isorropia.common.tiles.TileSoulBeacon;
import fr.wind_blade.isorropia.common.tiles.TileVat;
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
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.casters.ICaster;
import thaumcraft.api.damagesource.DamageSourceThaumcraft;
import thaumcraft.api.items.IGoggles;
import thaumcraft.api.items.IRevealer;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.api.potions.PotionFluxTaint;
import thaumcraft.api.potions.PotionVisExhaust;
import thaumcraft.api.research.ResearchEvent;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.items.tools.ItemThaumometer;
import thaumcraft.common.lib.potions.PotionInfectiousVisExhaust;
import thaumcraft.common.lib.potions.PotionThaumarhia;
import thaumcraft.common.lib.research.ResearchManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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

            if(!world.isRemote){
                LivingBaseCapability lc = Common.getCap(event.player);
                if (lc != null) {
                    Common.getCap(event.player).syncLivingBaseCapability(event.player);
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
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            LivingBaseCapability prop = Common.getCap(player);
            if (prop.hasPlayerInfusion(5) && (player.getActivePotionEffect(MobEffects.POISON) != null
                    || player.getActivePotionEffect(MobEffects.WITHER) != null
                    || player.getActivePotionEffect(PotionInfectiousVisExhaust.instance) != null
                    || player.getActivePotionEffect(PotionVisExhaust.instance) != null
                    || player.getActivePotionEffect(PotionThaumarhia.instance) != null
                    || player.getActivePotionEffect(PotionFluxTaint.instance) != null)) {

                final Collection activePotionEffects = event.getEntityLiving().getActivePotionEffects();
                final ArrayList<PotionEffect> toAdd = new ArrayList<>();

                for (Object activePotionEffect : activePotionEffects) {
                    final PotionEffect effect = (PotionEffect) activePotionEffect;
                    if (effect.getPotion() == MobEffects.POISON || effect.getPotion() == MobEffects.WITHER
                            || effect.getPotion() == PotionInfectiousVisExhaust.instance
                            || effect.getPotion() == PotionVisExhaust.instance
                            || effect.getPotion() == PotionThaumarhia.instance
                            || effect.getPotion() == PotionFluxTaint.instance) {
                        final Potion id = effect.getPotion();
                        final int amplifier = 0;
                        final int duration = effect.getDuration() - 1;
                        toAdd.add(new PotionEffect(id, duration, amplifier, false, true));
                    } else {
                        toAdd.add(effect);
                    }
                }
                event.getEntityLiving().clearActivePotions();
                for (final PotionEffect effect : toAdd) {
                    event.getEntityLiving().addPotionEffect(effect);
                }
            }
            if (prop.hasPlayerInfusion(6) && event.getEntity().ticksExisted % 200 == 0
                    && player.world.isDaytime()
                    && player.world.canBlockSeeSky(
                    new BlockPos(MathHelper.floor(player.posX),
                            MathHelper.floor(player.posY),
                            MathHelper.floor(player.posZ)))) {
                player.getFoodStats().addStats(1, 0.0f);
            }
            if (prop.hasPlayerInfusion(7) && player.isInWater()) {
                player.setAir(300);
            }

            if (player.ticksExisted % 30 == 0) {
                if (prop.hasPlayerInfusion(8) && !player.world.isRemote) {
                    //todo add a config for this.
                    this.warpTumor(player, /*ThaumicHorizons.warpedTumorValue*/50 - prop.tumorWarpPermanent - prop.tumorWarp - prop.tumorWarpTemp);
                }
                this.applyPlayerPotionInfusions(player, prop.playerInfusions, prop.toggleInvisible);
            }
            if (prop.hasPlayerInfusion(9) && !prop.toggleClimb) {
                if (event.getEntityLiving().collidedHorizontally) {
                    event.getEntityLiving().motionY = 0.2;
                    if (event.getEntityLiving().isSneaking()) {
                        event.getEntityLiving().motionY = 0.0;
                    }
                    event.getEntity().fallDistance = 0.0f;
                } else {
                    final boolean listy = event.getEntityLiving().world.collidesWithAnyBlock(
                            new AxisAlignedBB(
                                    event.getEntityLiving().posX - event.getEntityLiving().width / 1.5,
                                    event.getEntityLiving().posY,
                                    event.getEntityLiving().posZ - event.getEntityLiving().width / 1.5,
                                    event.getEntityLiving().posX + event.getEntityLiving().width / 1.5,
                                    event.getEntityLiving().posY + event.getEntityLiving().height * 0.9,
                                    event.getEntityLiving().posZ + event.getEntityLiving().width / 1.5));
                    if (listy) {
                        if (event.getEntityLiving().isSneaking()) {
                            event.getEntityLiving().motionY = 0.0;
                        } else {
                            event.getEntityLiving().motionY = -0.15;
                        }
                        event.getEntity().fallDistance = 0.0f;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        EntityPlayer original = event.getOriginal();
        EntityPlayer player = event.getEntityPlayer();

        if (player.world.isRemote) return;

        // Handle both death and dimension change cloning
        LivingBaseCapability oldCap = Common.getCap(original);
        LivingBaseCapability newCap = Common.getCap(player);

        if (oldCap != null && newCap != null) {
            Common.getCap(player).syncLivingBaseCapability(event.getEntityPlayer());
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
        Common.getCap(event.player).syncLivingBaseCapability(event.player);
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

    @SubscribeEvent
    public void EntityJoinWorld(final EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityLivingBase) {
            this.applyInfusions((EntityLivingBase) event.getEntity());
        }
        /*if (event.getWorld().isRemote && event.entity instanceof EntityNightmare
                && event.entity.getEntityId() == EventHandlerEntity.clientNightmareID) {
            event.entity.world.getEntityByID(EventHandlerEntity.clientPlayerID).ridingEntity = event.entity;
            event.entity.riddenByEntity = event.entity.world.getEntityByID(EventHandlerEntity.clientPlayerID);
            EventHandlerEntity.clientNightmareID = -2;
            EventHandlerEntity.clientPlayerID = -2;
        }*/
    }

    @SubscribeEvent
    public void Respawn(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent event) {
        // Exit Portal fix
        if (!event.player.world.isRemote) {
            this.applyInfusions(event.player);
        }
    }

    @SubscribeEvent
    public void onPlayerHurt(final LivingHurtEvent event) {
        final LivingBaseCapability prop = Common.getCap(event.getEntityLiving());
        if (prop.hasPlayerInfusion(5) && event.getSource() == DamageSourceThaumcraft.taint) {
            event.setCanceled(true);
            event.setAmount(0.0f);
            return;
        }
        if (!event.getEntity().world.isRemote && event.getEntity() instanceof EntityPlayer && event.getEntityLiving().getHealth() - event.getAmount() <= 0.0f) {
            final EntityPlayer player = (EntityPlayer) event.getEntity();
            if (prop.tumorWarpPermanent > 0 || prop.tumorWarp > 0 || prop.tumorWarpTemp > 0) {
                ThaumcraftCapabilities.getWarp(player).add(IPlayerWarp.EnumWarpType.PERMANENT, prop.tumorWarpPermanent);
                ThaumcraftCapabilities.getWarp(player).add(IPlayerWarp.EnumWarpType.NORMAL, prop.tumorWarp);
                ThaumcraftCapabilities.getWarp(player).add(IPlayerWarp.EnumWarpType.TEMPORARY, prop.tumorWarpTemp);
            }
            prop.resetPlayerInfusions();
        }
        if (!event.getEntity().world.isRemote && event.getEntityLiving() instanceof EntityPlayer
                && event.getEntityLiving().getHealth() - event.getAmount() <= 0.0f
                && event.getEntityLiving().getEntityData().getBoolean("soulBeacon")) {
            final EntityPlayer player = (EntityPlayer) event.getEntity();
            final int dim = player.getEntityData().getInteger("soulBeaconDim");
            final World world = player.world.getMinecraftServer().getWorld(dim);
            final int x = player.getEntityData().getIntArray("soulBeaconCoords")[0];
            final int y = player.getEntityData().getIntArray("soulBeaconCoords")[1];
            final int z = player.getEntityData().getIntArray("soulBeaconCoords")[2];
            if (world.getTileEntity(new BlockPos(x, y, z)) instanceof TileSoulBeacon && world.getTileEntity(new BlockPos(x, y - 1, z)) instanceof TileVat && ((TileVat) world.getTileEntity(new BlockPos(x, y - 1, z))).getMode() == 4) {
                event.setCanceled(true);
                if (!world.isRemote) {
                    world.createExplosion(
                            null,
                            player.posX,
                            player.posY + player.getEyeHeight(),
                            player.posZ,
                            0.0f,
                            false);
                    for (int a2 = 0; a2 < 25; ++a2) {
                        final int xx = (int) player.posX + world.rand.nextInt(8) - world.rand.nextInt(8);
                        final int yy = (int) player.posY + world.rand.nextInt(8) - world.rand.nextInt(8);
                        final int zz = (int) player.posZ + world.rand.nextInt(8) - world.rand.nextInt(8);
                        BlockPos pos = new BlockPos(xx, yy, zz);
                        if (world.isAirBlock(pos)) {
                            if (yy <= (int) player.posY + 1) {
                                world.setBlockState(pos, BlocksTC.fluxGoo.getStateFromMeta(8), 3);
                            } else {
                                world.setBlockState(pos, BlocksTC.fluxGoo.getStateFromMeta(8), 3);
                            }
                        }
                    }
                }
                player.inventory.dropAllItems();
                final IInventory baubles2 = BaublesApi.getBaubles(player);
                for (int j = 0; j < baubles2.getSizeInventory(); ++j) {
                    if (baubles2.getStackInSlot(j) != ItemStack.EMPTY) {
                        final EntityItem bauble = new EntityItem(
                                world,
                                player.posX,
                                player.posY,
                                player.posZ,
                                baubles2.getStackInSlot(j));
                        world.spawnEntity(bauble);
                        baubles2.setInventorySlotContents(j, ItemStack.EMPTY);
                    }
                }
                baubles2.markDirty();
                player.inventory.markDirty();
                //PacketHandler.INSTANCE.sendTo(new PacketNoMoreItems(), (EntityPlayerMP) player);
                player.curePotionEffects(new ItemStack(Items.MILK_BUCKET));
                player.heal(999.0f);
                if (dim != player.world.provider.getDimension()) {
                    player.changeDimension(dim, new EmptyTeleporter());
                }
                player.setPositionAndUpdate(x + 0.5, y - 2.5, z + 0.5);
                //Thaumcraft.proxy.blockSparkle(world, x, y - 2, z, 16777215, 20);
                //Thaumcraft.proxy.blockSparkle(world, x, y - 3, z, 16777215, 20);
                //world.playSoundEffect(x + 0.5, y + 0.5, z + 0.5, "thaumcraft:whispers", 1.0f, world.rand.nextFloat());
                this.applyPlayerInfusions(player, (TileVat) world.getTileEntity(new BlockPos(x, y - 1, z)));
                Common.getCap(player).syncLivingBaseCapability(player);
                ((TileVat) world.getTileEntity(new BlockPos(x, y - 1, z))).selfInfusions = new int[12];
                ((TileVat) world.getTileEntity(new BlockPos(x, y - 1, z))).setMode(0);
                ((TileVat) world.getTileEntity(new BlockPos(x, y - 1, z))).setEntityContained(player);
                ((TileVat) world.getTileEntity(new BlockPos(x, y - 1, z))).setHasEffigy(false);
                ((TileVat) world.getTileEntity(new BlockPos(x, y - 1, z))).setMyEssentia(0);
                world.getTileEntity(new BlockPos(x, y - 1, z)).markDirty();
                ((TileVat) world.getTileEntity(new BlockPos(x, y - 1, z))).syncTile(false);
            }
        } else if (event.getEntity().world.isRemote && event.getEntityLiving() instanceof EntityPlayer
                && event.getEntityLiving().getHealth() - event.getAmount() <= 0.0f
                && event.getEntityLiving().getEntityData().getBoolean("soulBeacon")) {
            final EntityPlayer player = (EntityPlayer) event.getEntity();
            Arrays.fill(player.inventory.mainInventory.toArray(), ItemStack.EMPTY);
            Arrays.fill(player.inventory.armorInventory.toArray(), ItemStack.EMPTY);
            Arrays.fill(player.inventory.offHandInventory.toArray(), ItemStack.EMPTY);
            final IInventory baubles3 = BaublesApi.getBaubles(player);
            for (int i = 0; i < baubles3.getSizeInventory(); i++) {
                baubles3.setInventorySlotContents(i, ItemStack.EMPTY);
            }
        }
    }

    public void applyPlayerPotionInfusions(final EntityPlayer entity, final int[] infusions, final boolean toggled) {
        for (int infusion : infusions) {
            if (infusion == 1) {
                PotionEffect effect = new PotionEffect(MobEffects.JUMP_BOOST, Integer.MAX_VALUE, 0, true, false);
                effect.setCurativeItems(new ArrayList<>());
                entity.addPotionEffect(effect);
                effect = new PotionEffect(MobEffects.SPEED, Integer.MAX_VALUE, 0, true, false);
                effect.setCurativeItems(new ArrayList<>());
                entity.addPotionEffect(effect);
            } else if (infusion == 3) {
                final PotionEffect effect = new PotionEffect(MobEffects.REGENERATION, Integer.MAX_VALUE, 0, true, false);
                effect.setCurativeItems(new ArrayList<>());
                entity.addPotionEffect(effect);
            } else if (infusion == 4) {
                final PotionEffect effect = new PotionEffect(MobEffects.RESISTANCE, Integer.MAX_VALUE, 0, true, false);
                effect.setCurativeItems(new ArrayList<>());
                entity.addPotionEffect(effect);
            } else if (infusion == 10 && !toggled) {
                final PotionEffect effect = new PotionEffect(MobEffects.INVISIBILITY, Integer.MAX_VALUE, 0, true, false);
                effect.setCurativeItems(new ArrayList<>());
                entity.addPotionEffect(effect);
                entity.setInvisible(true);
            }
        }
    }

    public void warpTumor(final EntityPlayer entity, int capacity) {
        if (capacity <= 0) {
            return;
        }
        final int warpPermanent = ThaumcraftCapabilities.getWarp(entity).get(IPlayerWarp.EnumWarpType.PERMANENT);
        final int warp = ThaumcraftCapabilities.getWarp(entity).get(IPlayerWarp.EnumWarpType.NORMAL);
        final int tempWarp = ThaumcraftCapabilities.getWarp(entity).get(IPlayerWarp.EnumWarpType.TEMPORARY);
        LivingBaseCapability cap = Common.getCap(entity);

        if (warpPermanent > capacity) {
            ThaumcraftCapabilities.getWarp(entity).add(IPlayerWarp.EnumWarpType.PERMANENT, -capacity);
            cap.tumorWarpPermanent += capacity;
            capacity = 0;
        } else {
            capacity -= warpPermanent;
            ThaumcraftCapabilities.getWarp(entity).add(IPlayerWarp.EnumWarpType.PERMANENT, -warpPermanent);
            cap.tumorWarpPermanent += warpPermanent;
            if (warp > capacity) {
                ThaumcraftCapabilities.getWarp(entity).add(IPlayerWarp.EnumWarpType.NORMAL, -capacity);
                cap.tumorWarp += capacity;
                capacity = 0;
            } else {
                capacity -= warp;
                ThaumcraftCapabilities.getWarp(entity).add(IPlayerWarp.EnumWarpType.NORMAL, -warp);
                cap.tumorWarp += warp;
                if (tempWarp > capacity) {
                    ThaumcraftCapabilities.getWarp(entity).add(IPlayerWarp.EnumWarpType.TEMPORARY, -capacity);
                    cap.tumorWarpTemp += capacity;
                    capacity = 0;
                } else {
                    capacity -= tempWarp;
                    ThaumcraftCapabilities.getWarp(entity).add(IPlayerWarp.EnumWarpType.TEMPORARY, -tempWarp);
                    cap.tumorWarpTemp += tempWarp;
                }
            }
        }
    }

    public void applyInfusions(final EntityLivingBase entity) {
        LivingBaseCapability infusionProperties = Common.getCap(entity);
        if (entity instanceof EntityPlayer) {
            if (infusionProperties != null) {
                if (infusionProperties.hasPlayerInfusion(8) && !entity.world.isRemote) {
                    this.warpTumor((EntityPlayer) entity,/*ThaumicHorizons.warpedTumorValue*/50 - infusionProperties.tumorWarpPermanent - infusionProperties.tumorWarp - infusionProperties.tumorWarpTemp);
                }

                this.applyPlayerPotionInfusions((EntityPlayer) entity, infusionProperties.playerInfusions, infusionProperties.toggleInvisible);
                if (!entity.world.isRemote) {
                    Common.INSTANCE.sendToAll(new PacketPlayerInfusionSync(entity.getName(), infusionProperties.getPlayerInfusions(), infusionProperties.toggleClimb, infusionProperties.toggleInvisible));
                }
            }
        }
    }

    void applyPlayerInfusions(final EntityPlayer player, final TileVat tile) {
        final LivingBaseCapability prop = Common.getCap(player);
        for (int i = 0; i < tile.selfInfusions.length; ++i) {
            if (tile.selfInfusions[i] != 0) {
                prop.addPlayerInfusion(tile.selfInfusions[i]);
            }
        }
        this.applyInfusions(player);
    }
}