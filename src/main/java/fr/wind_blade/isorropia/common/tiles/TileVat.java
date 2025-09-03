package fr.wind_blade.isorropia.common.tiles;

import fr.wind_blade.isorropia.client.fx.ISFXDispatcher;
import fr.wind_blade.isorropia.common.Common;
import fr.wind_blade.isorropia.common.IsorropiaAPI;
import fr.wind_blade.isorropia.common.blocks.BlocksIS;
import fr.wind_blade.isorropia.common.celestial.CelestialBody;
import fr.wind_blade.isorropia.common.celestial.ICelestialBody;
import fr.wind_blade.isorropia.common.curative.ICurativeEffectProvider;
import fr.wind_blade.isorropia.common.events.SoundsIR;
import fr.wind_blade.isorropia.common.items.ItemsIS;
import fr.wind_blade.isorropia.common.libs.helpers.IsorropiaHelper;
import fr.wind_blade.isorropia.common.network.ISPacketFXInfusionSource;
import fr.wind_blade.isorropia.common.research.recipes.CurativeInfusionRecipe;
import fr.wind_blade.isorropia.common.research.recipes.SelfInfusionRecipe;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.util.Random;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.ThaumcraftInvHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.crafting.IInfusionStabiliser;
import thaumcraft.api.crafting.IInfusionStabiliserExt;
import thaumcraft.api.crafting.IStabilizable;
import thaumcraft.api.potions.PotionFluxTaint;
import thaumcraft.api.potions.PotionVisExhaust;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.blocks.devices.BlockPedestal;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXBlockArc;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.tiles.TileThaumcraft;
import thaumcraft.common.tiles.crafting.TilePedestal;
import thaumcraft.common.world.aura.AuraChunk;
import thaumcraft.common.world.aura.AuraHandler;

import java.util.*;

public class TileVat extends TileThaumcraft implements IAspectContainer, ITickable, IStabilizable {
    public static final String NBT_TAG_PREVENT_DESPAWN = "PreventDespawn";
    private int mode = 0;
    private int stabilityCap = 25;
    private float stability = 0.0f;
    private float totalInstability;
    private float startUp;
    private Map<String, SourceFX> sourceFX = new HashMap<String, SourceFX>();
    private EntityLivingBase entityContained;
    private Aspect currentlySucking;
    private final Set<TilePedestal> pedestals = new HashSet<TilePedestal>();
    private boolean active = false;
    private float costMult;
    private int cycleTime = 20;
    private int curativeCount;
    private float fluxStocked;
    private float stabilityReplenish = 0.0f;
    private AspectList essentiaNeeded = new AspectList();
    private UUID entityUUID;
    private ICelestialBody celestialBody = CelestialBody.NONE;
    private float celestialAura;
    private int celestialAuraCap = 100;
    private boolean expunge;
    private int count = 0;
    private int craftCount = 0;
    private float soundExpunge = 0.0f;
    private int countDelay = this.cycleTime / 2;
    private int itemCount = 0;
    private final ArrayList<BlockPos> problemBlocks = new ArrayList<BlockPos>();
    HashMap<Block, Integer> tempBlockCount = new HashMap<>();
    private boolean infusing = false;
    private CurativeInfusionRecipe recipe;
    private SelfInfusionRecipe recipe2;
    private ArrayList<ItemStack> recipeIngredients = new ArrayList();
    private NonNullList<ItemStack> stacksUsed = NonNullList.create();
    private float celestialAuraNeeded;
    private final List<ItemStack> optionalComponents = new ArrayList<ItemStack>();
    private EntityPlayer recipePlayer = null;
    private int recipeXP;
    private ICurativeEffectProvider curativeEffect;
    private List<ItemStack> loots = new ArrayList<ItemStack>();
    public boolean checkSurroundings = true;
    private boolean hasEffigy = false;
    private float selfInfusionHealth = 20.0f;
    private int myEssentia = 0;
    public int[] selfInfusions = new int[12];
    public int recipeType = 0;
    private int recipeOutput = 0;

    public TileVat() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(pos.add(-3, -3, -3), pos.add(4, 4, 4));
    }

    public IStabilizable.EnumStability getStability() {
        if (this.stability <= -25.0f) {
            return IStabilizable.EnumStability.VERY_UNSTABLE;
        } else if (this.stability < 0.0f) {
            return IStabilizable.EnumStability.UNSTABLE;
        } else if (this.stability > (float) (this.stabilityCap / 2)) {
            return IStabilizable.EnumStability.VERY_STABLE;
        } else {
            return IStabilizable.EnumStability.STABLE;
        }
    }

    public float getLossPerCycle() {
        return this.recipe == null ? this.recipe2 == null ? 0.0f : (float) this.recipe2.getInstability() / this.getModFromCurrentStability() : (float) this.recipe.getInstability() / this.getModFromCurrentStability();
    }

    public float getModFromCurrentStability() {
        switch (this.getStability()) {
            case VERY_STABLE: {
                return 5.0f;
            }
            case STABLE: {
                return 6.0f;
            }
            case UNSTABLE: {
                return 7.0f;
            }
            case VERY_UNSTABLE: {
                return 8.0f;
            }
        }
        return 1.0f;
    }

    public void update() {
        ++this.count;
        if (this.checkSurroundings) {
            this.checkSurroundings = false;
            this.getSurroundings();
        }
        if (this.fluxStocked > 300.0f) {
            if (this.infusing) {
                this.infusingFinish(null, null, true);
            }
            this.destroyMultiBlock();

            this.syncTile(false);
            this.markDirty();
        }

        this.updateStartUp();
        if (this.world.isRemote) {
            this.doEffects();
        } else {
            if (this.getEntityContained() != null && this.entityContained.isDead) {
                this.destroyMultiBlock();

                this.syncTile(false);
                this.markDirty();
            }
            if (!this.expunge && this.fluxStocked >= 30.0f) {
                if (this.mode == 2) {
                    this.infusingFinish(this.recipe, this.recipe2, true);
                }
                this.expunge = true;

                this.syncTile(false);
                this.markDirty();
            }
            if (this.expunge && this.count % this.countDelay * 2 == 0) {
                this.fluxStocked -= 1.0f;
                AuraHelper.polluteAura(this.world, this.pos.up(), 1.0f, false);
                if (this.fluxStocked <= 0.0f) {
                    this.expunge = false;
                    this.fluxStocked = 0.0f;
                }
                this.syncTile(false);
                this.markDirty();
                return;
            }
            if (this.hasEffigy && this.getEntityContained() == null && this.mode != 2 && this.mode != 4) {
                this.mode = 3;
                this.curativeUpdateEffigy();

                this.markDirty();
                this.syncTile(false);
            }
            if (this.getEntityContained() != null) {
                if (this.getEntityContained() != null && this.getEntityContained().isBurning()) {
                    this.getEntityContained().extinguish();
                }
                if (this.mode == 0 && !this.expunge) {
                    this.curativeUpdate();
                    this.essentiaStep();

                    this.syncTile(false);
                    this.markDirty();
                }
            }
            if (this.mode == 2 && this.active && this.count % this.countDelay == 0) {
                this.infusionCycle();

                this.syncTile(false);
                this.markDirty();
            }
        }
    }

    private void updateStartUp() {
        this.startUp = !this.active || !this.infusing && this.expunge ? (this.startUp > 0.0f ? this.startUp - Math.max(this.startUp / 10.0f, 0.001f) : 0.0f) : (this.startUp < 1.0f ? this.startUp + Math.max(this.startUp / 10.0f, 0.001f) : 1.0f);
    }

    public int addToContainer(Aspect aspectIn, int amount) {
        if (this.mode == 0 && this.curativeEffect != null && this.getEntityContained() != null && this.curativeCount <= 0) {
            if (aspectIn == this.curativeEffect.getAspect() && amount > 0 && this.curativeEffect.effectCanApply(this.entityContained, this)) {
                this.curativeEffect.onApply(this.entityContained, this);
                this.curativeCount += this.curativeEffect.getCooldown(this.entityContained, this);
                this.curativeEffect = null;
                this.syncTile(false);
                return 0;
            }
        } else if (this.mode != 0) {
            this.essentiaNeeded.remove(aspectIn, amount);
            return 0;
        }
        return amount;
    }

    private void curativeUpdate() {
        this.essentiaNeeded = new AspectList();
        this.curativeCount = Math.max(this.curativeCount - 1, 0);
        EntityLivingBase contained = this.getEntityContained();
        if (this.world.isRemote || contained == null) {
            return;
        }
        Optional<ICurativeEffectProvider> effect = IsorropiaAPI.curativeEffects.stream().filter(var -> var.effectCanApply(contained, this) && this.getEssentiaNeeded().getAmount(var.getAspect()) < 1).findFirst();
        if (effect.isPresent()) {
            this.curativeEffect = effect.get();
            this.essentiaNeeded.add(effect.get().getAspect(), 1);
        }
        if (this.doNeedEssentia()) {
            this.drawnAllEssentia();
        }
    }

    private void curativeUpdateEffigy() {
        int oldTotalNeeded = this.essentiaNeeded.getAmount(Aspect.LIFE);

        this.essentiaNeeded = new AspectList();
        if (this.world.isRemote || mode != 3) {
            return;
        }

        if (this.myEssentia <= 30) {
            this.essentiaNeeded = new AspectList().add(Aspect.LIFE, 1);
        } else {
            this.mode = 4;
            this.selfInfusionHealth = 20.0f;
            this.essentiaNeeded = new AspectList();

            this.markDirty();
            this.syncTile(false);
        }

        int newTotalNeeded = 0;
        for (Aspect aspect : this.essentiaNeeded.getAspects()) {
            newTotalNeeded += this.essentiaNeeded.getAmount(aspect);
        }

        int diff = newTotalNeeded - oldTotalNeeded;
        if (diff > 0) {
            this.myEssentia += diff;
        }

        if (this.doNeedEssentia()) {
            this.drawnAllEssentia();
        }
    }

    private void doEffects() {
        for (int i = 0; i < 10; ++i) {
        }
        if (this.infusing) {
            if (this.celestialAuraNeeded > 0.0f && this.canDrawnCelestialAura()) {
                ISFXDispatcher.fxAbsorption((float) this.pos.getX() + 0.5f, (double) ((float) this.pos.getY() + 1.5f) + (this.celestialAuraNeeded > this.celestialAura ? 3.0 : -3.5), (float) this.pos.getZ() + 0.5f, 0.49019608f, 0.5568628f, 0.63529414f, this.celestialAuraNeeded > this.celestialAura);
            }
            if (this.craftCount == 0) {
                this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundsIR.curative_infusion_start, SoundCategory.BLOCKS, 0.5f, 1.0f, false);
            } else if (this.craftCount == 0 || this.craftCount % 65 == 0) {
                this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundsIR.curative_infusion_loop, SoundCategory.BLOCKS, 0.5f, 1.0f, false);
            }
            ++this.craftCount;
            FXDispatcher.INSTANCE.blockRunes(this.pos.getX(), (double) this.pos.getY() - 2.5, this.pos.getZ(), this.world.rand.nextFloat() * 0.2f, 0.1f, 0.7f + this.world.rand.nextFloat() * 0.3f, 25, -0.03f);
        } else if (this.craftCount > 0) {
            this.craftCount -= 2;
            if (this.craftCount < 0) {
                this.craftCount = 0;
            }
            if (this.craftCount > 50) {
                this.craftCount = 50;
            }
        }
        if (this.active && !this.infusing) {
            if (this.expunge) {
                if (this.soundExpunge == 0.0f && this.fluxStocked * 2.0f > 5.821f) {
                    this.world.playSound(this.pos.getX(), this.pos.getY() + 1, this.pos.getZ(), SoundsIR.curative_expunge_flux_start, SoundCategory.BLOCKS, 0.5f, 1.0f, false);
                    this.soundExpunge = -1.0f;
                } else if (this.fluxStocked * 2.0f > 5.821f && (double) Minecraft.getMinecraft().player.ticksExisted % Math.floor(116.42f) == 0.0 || this.soundExpunge == -1.0f) {
                    this.world.playSound(this.pos.getX(), this.pos.getY() + 1, this.pos.getZ(), SoundsIR.curative_expunge_flux_loop, SoundCategory.BLOCKS, 0.5f, 1.0f, false);
                    this.soundExpunge = (float) Math.floor(116.42f) + 1.0f;
                }
            }
            if (this.fluxStocked * 2.0f <= 5.821f && this.soundExpunge == 1.0f) {
                this.world.playSound(this.pos.getX(), this.pos.getY() + 1, this.pos.getZ(), SoundsIR.curative_expunge_flux_end, SoundCategory.BLOCKS, 0.5f, 1.0f, false);
                this.soundExpunge = 0.0f;
            }
            if (this.soundExpunge > 1.0f) {
                this.soundExpunge -= 1.0f;
            }
        }
        this.drawnIngredientParticles();
        if (this.infusing && this.stability < 0.0f && (float) this.world.rand.nextInt(200) <= Math.abs(this.stability)) {
            float xx = (float) this.pos.getX() + 0.5f + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 4.0f;
            float zz = (float) this.pos.getZ() + 0.5f + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 4.0f;
            int yy = this.pos.getY() + 1;
            while (!this.world.isAirBlock(new BlockPos(xx, yy, zz))) {
                ++yy;
            }
            FXDispatcher.INSTANCE.arcLightning((float) this.pos.getX() + 0.5f, (float) this.pos.getY() + 1.5f, (float) this.pos.getZ() + 0.5f, xx, yy, zz, 0.8f, 0.1f, 1.0f, 0.1f);
        }
    }

    @SideOnly(value = Side.CLIENT)
    private void drawnIngredientParticles() {
        Iterator<String> i = this.sourceFX.keySet().iterator();
        while (i.hasNext()) {
            String fxk = i.next();
            SourceFX fx = this.sourceFX.get(fxk);
            if (fx.ticks <= 0) {
                i.remove();
                continue;
            }
            if (fx.loc.equals(this.pos)) {
                Entity player = this.world.getEntityByID(fx.color);
                if (player != null) {
                    for (int a = 0; a < 4; ++a) {
                        FXDispatcher.INSTANCE.drawInfusionParticles4(player.posX + (double) ((this.world.rand.nextFloat() - this.world.rand.nextFloat()) * player.width), player.getEntityBoundingBox().minY + (double) (this.world.rand.nextFloat() * player.height), player.posZ + (double) ((this.world.rand.nextFloat() - this.world.rand.nextFloat()) * player.width), this.pos.getX(), this.pos.getY() + 1, this.pos.getZ());
                    }
                }
            } else {
                TileEntity tile = this.world.getTileEntity(fx.loc);
                if (tile instanceof TilePedestal) {
                    ItemStack is = ((TilePedestal) tile).getSyncedStackInSlot(0);
                    if (!is.isEmpty()) {
                        if (this.world.rand.nextInt(3) == 0) {
                            FXDispatcher.INSTANCE.drawInfusionParticles3((float) fx.loc.getX() + this.world.rand.nextFloat(), (float) fx.loc.getY() + this.world.rand.nextFloat() + 1.0f, (float) fx.loc.getZ() + this.world.rand.nextFloat(), this.pos.getX(), this.pos.getY() + 1, this.pos.getZ());
                        } else {
                            int a;
                            Item bi = is.getItem();
                            int md = is.getItemDamage();
                            if (bi instanceof ItemBlock) {
                                for (a = 0; a < 4; ++a) {
                                    FXDispatcher.INSTANCE.drawInfusionParticles2((float) fx.loc.getX() + this.world.rand.nextFloat(), (float) fx.loc.getY() + this.world.rand.nextFloat() + 1.0f, (float) fx.loc.getZ() + this.world.rand.nextFloat(), new BlockPos(this.pos.getX(), this.pos.getY() + 1, this.pos.getZ()), Block.getBlockFromItem(bi).getDefaultState(), md);
                                }
                            } else {
                                for (a = 0; a < 4; ++a) {
                                    FXDispatcher.INSTANCE.drawInfusionParticles1((float) fx.loc.getX() + 0.4f + this.world.rand.nextFloat() * 0.2f, (float) fx.loc.getY() + 1.23f + this.world.rand.nextFloat() * 0.2f, (float) fx.loc.getZ() + 0.4f + this.world.rand.nextFloat() * 0.2f, this.pos.up(), is);
                                }
                            }
                        }
                    }
                } else {
                    fx.ticks = 0;
                }
            }
            --fx.ticks;
            this.sourceFX.put(fxk, fx);
        }
    }

    public boolean onBlockRigthClick(EntityPlayer playerIn, EnumFacing facing, boolean master) {
        EntityLivingBase contained = this.getEntityContained();
        ItemStack stack = playerIn.getHeldItemMainhand();
        Block block = Block.getBlockFromItem(stack.getItem());
        if (this.world.isRemote) {
            return true;
        }
        if (block == BlocksIS.blockJarSoul && stack.hasTagCompound() && stack.getTagCompound().hasKey("ENTITY_DATA")) {
            if (contained == null && playerIn.inventory.addItemStackToInventory(new ItemStack(BlocksTC.jarNormal))) {
                this.setEntityContained(IsorropiaHelper.nbtToLiving(stack.getTagCompound(), this.world, new BlockPos((double) this.pos.getX() + 0.5, this.pos.getY() - 2, (double) this.pos.getZ() + 0.5)), facing.getOpposite().getHorizontalAngle() - 90.0f);
                stack.shrink(1);
                return true;
            }
        } else if (stack.getItem() == ItemsIS.corpseEffigy && contained == null && !hasEffigy) {
            this.hasEffigy = true;
            stack.shrink(1);
            return true;
        } else if (contained instanceof EntityLiving) {
            if (!this.loots.isEmpty()) {
                this.loots.stream().filter(loot -> !playerIn.addItemStackToInventory(loot)).forEach(drop -> playerIn.dropItem(drop, false));
                this.loots.clear();
                return true;
            }
            if (block == BlocksTC.jarNormal) {
                IsorropiaHelper.playerJarEntity(playerIn, (EntityLiving) this.setEntityContained(null, 0.0f));
                return true;
            }
        } else if (block == BlocksIS.blockModifiedMatrix) {
            return false;
        }
        if (contained == null) {
            if (!this.hasEffigy) {
                if (master)
                    this.setEntityContained(playerIn);
            } else {
                if(this.myEssentia > 30){
                    return false;
                }
                else {
                    playerIn.inventory.addItemStackToInventory(new ItemStack(ItemsIS.corpseEffigy));
                    this.hasEffigy = false;
                    this.myEssentia = 0;
                    this.mode = 0;
                }
            }
            return true;
        }
        if (contained instanceof EntityPlayer && playerIn.equals(contained)) {
            this.setEntityContained(null);
            return true;
        }
        return false;
    }

    public void craftingStart(EntityPlayer player) {
        /*if (this.getEntityContained() != null) {
            this.active = false;
            this.markDirty();
            this.syncTile(false);
        } else {*/
        this.getSurroundings();

        ArrayList<ItemStack> components = new ArrayList();

        for (TilePedestal ped : this.pedestals) {
            if (ped != null) {
                if (!ped.getStackInSlot(0).isEmpty()) {
                    components.add(ped.getStackInSlot(0).copy());
                }
            }
        }

        if (components.size() <= 0) {
            return;
        }

        if (this.mode != 4) {
            if (this.getEntityContained() == null || this.getEntityContained().getHealth() <= 0) {
                return;
            }
            this.recipe = IsorropiaAPI.findMatchingCreatureInfusionRecipe(this.getEntityContained(), components, player);
            if ((double) this.costMult < 0.5) {
                this.costMult = 0.5F;
            }

            if (recipe == null) {
                return;
            }

            AspectList al = this.recipe.getCurrentAspect(player, this.world, this.entityContained, this.stability, this.totalInstability, null);
            AspectList al2 = new AspectList();
            for (Aspect as : al.getAspects()) {
                if (al.getAmount(as) * this.costMult <= 0) continue;
                al2.add(as, (int) (al.getAmount(as) * this.costMult));
            }

            this.recipeType = 0;
            this.essentiaNeeded = al2;
            this.celestialAuraNeeded = this.recipe.getCelestialAura();
            this.celestialBody = this.recipe.getCelestialBody();

        } else {
            this.recipe2 = IsorropiaAPI.findMatchingCSelfInfusionRecipe(components, player);
            if ((double) this.costMult < 0.5) {
                this.costMult = 0.5F;
            }
            if (recipe2 == null) {
                return;
            }

            for (int selfInfusion : this.selfInfusions) {
                if (selfInfusion == recipe2.getID()) {
                    return;
                }
            }

            AspectList al = this.recipe2.getAspects();
            AspectList al2 = new AspectList();
            for (Aspect as : al.getAspects()) {
                if (al.getAmount(as) * this.costMult <= 0) continue;
                al2.add(as, (int) (al.getAmount(as) * this.costMult));
            }

            this.recipeType = 1;
            this.essentiaNeeded = al2;
            this.recipeOutput = recipe2.getID();
        }
        this.mode = 2;
        this.recipeIngredients = components;

        this.recipePlayer = player;
        this.infusing = true;

        this.world.playSound(null, this.pos, SoundsIR.curative_infusion_start, SoundCategory.BLOCKS, 0.5f, 1.0f);
        this.syncTile(false);
        this.markDirty();
        //}
    }

    private void getSurroundings() {
        Set<Long> stuff = new HashSet<Long>();
        pedestals.clear();
        tempBlockCount.clear();
        problemBlocks.clear();
        cycleTime = 10;
        stabilityReplenish = 0.0f;
        costMult = 1.0f;
        try {
            for (int xx = -12; xx <= 12; ++xx) {
                for (int zz = -12; zz <= 12; ++zz) {
                    for (int yy = -10; yy <= 10; ++yy) {
                        if (xx != 0 || zz != 0) {
                            int x = pos.getX() + xx;
                            int y = pos.getY() - yy;
                            int z = pos.getZ() + zz;
                            BlockPos bp = new BlockPos(x, y, z);
                            Block bi = world.getBlockState(bp).getBlock();
                            if (bi instanceof BlockPedestal) {
                                pedestals.add((TilePedestal) world.getTileEntity(bp));
                            }
                            try {
                                if (bi == Blocks.SKULL || (bi instanceof IInfusionStabiliser && ((IInfusionStabiliser) bi).canStabaliseInfusion(getWorld(), bp))) {
                                    stuff.add(bp.toLong());
                                }
                            } catch (Exception ex) {
                            }
                        }
                    }
                }
            }
            while (!stuff.isEmpty()) {
                Long[] posArray = stuff.toArray(new Long[stuff.size()]);
                if (posArray == null) {
                    break;
                }
                if (posArray[0] == null) {
                    break;
                }
                long lp = posArray[0];
                try {
                    BlockPos c1 = BlockPos.fromLong(lp);
                    int x2 = pos.getX() - c1.getX();
                    int z2 = pos.getZ() - c1.getZ();
                    int x3 = pos.getX() + x2;
                    int z3 = pos.getZ() + z2;
                    BlockPos c2 = new BlockPos(x3, c1.getY(), z3);
                    Block sb1 = world.getBlockState(c1).getBlock();
                    Block sb2 = world.getBlockState(c2).getBlock();
                    float amt1 = 0.1f;
                    float amt2 = 0.1f;
                    if (sb1 instanceof IInfusionStabiliserExt) {
                        amt1 = ((IInfusionStabiliserExt) sb1).getStabilizationAmount(getWorld(), c1);
                    }
                    if (sb2 instanceof IInfusionStabiliserExt) {
                        amt2 = ((IInfusionStabiliserExt) sb2).getStabilizationAmount(getWorld(), c2);
                    }
                    if (sb1 == sb2 && amt1 == amt2) {
                        if (sb1 instanceof IInfusionStabiliserExt && ((IInfusionStabiliserExt) sb1).hasSymmetryPenalty(getWorld(), c1, c2)) {
                            stabilityReplenish -= ((IInfusionStabiliserExt) sb1).getSymmetryPenalty(getWorld(), c1);
                            problemBlocks.add(c1);
                        } else {
                            stabilityReplenish += calcDeminishingReturns(sb1, amt1);
                        }
                    } else {
                        stabilityReplenish -= Math.max(amt1, amt2);
                        problemBlocks.add(c1);
                    }
                    stuff.remove(c2.toLong());
                } catch (Exception ex2) {
                }
                stuff.remove(lp);
            }
            int[] xm = {-1, 0, 1, 0};
            int[] zm = {0, -1, 0, 1};
            for (int a = 0; a < 4; ++a) {
                Block b = world.getBlockState(pos.add(xm[a], -4, zm[a])).getBlock();
                if (b == BlocksTC.matrixSpeed) {
                    --cycleTime;
                    costMult += 0.01f;
                }
                if (b == BlocksTC.matrixCost) {
                    ++cycleTime;
                    costMult -= 0.02f;
                }
            }
            countDelay = cycleTime / 2;
            int apc = 0;
            for (TilePedestal cc : pedestals) {
                boolean items = false;
                int x4 = pos.getX() - cc.getPos().getX();
                int z4 = pos.getZ() - cc.getPos().getZ();
                Block bb = world.getBlockState(cc.getPos()).getBlock();
                if (bb == BlocksTC.pedestalEldritch) {
                    costMult += 0.0025f;
                }
                if (bb == BlocksTC.pedestalAncient) {
                    costMult -= 0.01f;
                }
            }
        } catch (Exception ex3) {
        }
    }

    private float calcDeminishingReturns(Block b, float base) {
        float bb = base;
        int c = tempBlockCount.containsKey(b) ? tempBlockCount.get(b) : 0;
        if (c > 0) {
            bb *= (float) Math.pow(0.75, c);
        }
        tempBlockCount.put(b, c + 1);
        return bb;
    }

    public void infusionCycle() {
        if (this.recipeType == 0 && this.recipe == null) {
            this.infusingFinish(null, null, true);
            return;
        }
        if (this.recipeType == 1 && this.recipe2 == null) {
            this.infusingFinish(null, null, true);
            return;
        }

        if (this.recipeType == 0 && this.getEntityContained() == null) {
            this.destroyMultiBlock();
        }
        boolean valid = this.getEntityContained() != null || recipeType == 1;
        this.stability -= world.rand.nextFloat() * getLossPerCycle();
        this.stability += this.stabilityReplenish;
        this.stability = Math.min(Math.max(this.stability, -100.0f), (float) this.stabilityCap);
        if (this.stability < 0.0f && (float) this.world.rand.nextInt(1500) <= Math.abs(this.stability)) {
            this.inEvRandom();
            this.stability += 5.0f + this.world.rand.nextFloat() * 5.0f;
            this.inResAdd();
            if (valid) {
                return;
            }
        }
        if (!valid) {
            this.infusing = false;
            this.essentiaNeeded = new AspectList();
            this.syncTile(false);
            this.world.playSound(null, this.pos, SoundsTC.craftfail, SoundCategory.BLOCKS, 1.0f, 0.6f);
            this.markDirty();
            return;
        }
        if (!this.celestialAuraStep()) {
            return;
        }
        if (!this.xpStep()) {
            return;
        }
        this.countDelay = Math.max(this.countDelay, 1);
        if (!this.essentiaStep()) {
            return;
        }
        if (!this.ingredientStep()) {
            return;
        }
        if (!this.visStep()) {
            return;
        }
        this.fluxStocked += this.recipeType == 0 ? this.recipe.getFluxRejection() : 0;
        this.infusing = false;
        this.infusingFinish(this.recipe, this.recipe2, false);
        this.syncTile(false);
        this.markDirty();
    }

    public boolean celestialAuraStep() {
        if (this.celestialAuraNeeded > 0.0f) {
            if (this.celestialAura < this.celestialAuraNeeded) {
                this.drawnCelestialAura();
            } else {
                this.celestialAura -= 1.0f;
                this.celestialAuraNeeded -= 1.0f;
            }
            return false;
        }
        return true;
    }

    public boolean xpStep() {
        if (this.recipeXP > 0) {
            List<EntityPlayer> targets = this.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), this.getPos().getX() + 1, this.getPos().getY() + 1, this.getPos().getZ() + 1).grow(10.0, 10.0, 10.0));
            if (!targets.isEmpty()) {
                for (EntityPlayer target : targets) {
                    if (!target.capabilities.isCreativeMode && target.experienceLevel <= 0) continue;
                    if (!target.capabilities.isCreativeMode) {
                        target.addExperienceLevel(-1);
                    }
                    --this.recipeXP;
                    target.attackEntityFrom(DamageSource.MAGIC, this.world.rand.nextInt(2));
                    Common.INSTANCE.sendToAllAround(new ISPacketFXInfusionSource(this.pos, this.pos, target.getEntityId()), new NetworkRegistry.TargetPoint(this.getWorld().provider.getDimension(), this.pos.getX(), this.pos.getY(), this.pos.getZ(), 32.0));
                    target.playSound(SoundEvents.BLOCK_LAVA_EXTINGUISH, 1.0f, 2.0f + this.world.rand.nextFloat() * 0.4f);
                    this.countDelay = this.cycleTime;
                    return false;
                }
                Aspect[] ingEss = this.recipeType == 0 ? this.recipe.getAspects().getAspects() : this.recipe2.getAspects().getAspects();
                if (ingEss != null && ingEss.length > 0 && this.world.rand.nextInt(3) == 0) {
                    Aspect as = ingEss[this.world.rand.nextInt(ingEss.length)];
                    this.fluxStocked += 1.0f;
                    this.essentiaNeeded.add(as, 1);
                    this.stability -= 0.25f;
                    this.syncTile(false);
                    this.markDirty();
                }
            }
            return false;
        }
        return true;
    }

    public boolean essentiaStep() {
        if (this.essentiaNeeded.visSize() > 0) {
            if (this.doNeedEssentia()) {
                this.drawnAllEssentia();
            }
            return false;
        }
        return true;
    }

    public boolean ingredientStep() {
        if (this.recipeIngredients.isEmpty()) {
            return true;
        }
        for (int a = 0; a < this.recipeIngredients.size(); ++a) {
            for (TilePedestal pedestal : this.pedestals) {
                if (pedestal.getStackInSlot(0).isEmpty() || !ThaumcraftInvHelper.areItemStacksEqualForCrafting(pedestal.getStackInSlot(0), this.recipeIngredients.get(a)))
                    continue;
                if (this.itemCount == 0) {
                    this.itemCount = 5;
                    Common.INSTANCE.sendToAllAround(new ISPacketFXInfusionSource(this.pos, pedestal.getPos(), 0), new NetworkRegistry.TargetPoint(this.getWorld().provider.getDimension(), this.pos.getX(), this.pos.getY(), this.pos.getZ(), 32.0));
                } else if (this.itemCount-- <= 1) {
                    ItemStack is = pedestal.getStackInSlot(0).getItem().getContainerItem(pedestal.getStackInSlot(0));
                    pedestal.setInventorySlotContents(0, is == null || is.isEmpty() ? ItemStack.EMPTY : is.copy());
                    pedestal.markDirty();
                    pedestal.syncTile(false);
                    this.stacksUsed.add(this.recipeIngredients.get(a));
                    this.recipeIngredients.remove(a);
                    this.markDirty();
                }
                return false;
            }
            Aspect[] ingEss = this.recipeType == 0 ? this.recipe.getAspects().getAspects() : this.recipe2.getAspects().getAspects();
            if (ingEss == null || ingEss.length <= 0 || this.world.rand.nextInt(1 + a) != 0) continue;
            Aspect as = ingEss[this.world.rand.nextInt(ingEss.length)];
            this.fluxStocked += 1.0f;
            this.essentiaNeeded.add(as, 1);
            this.stability -= 0.25f;
            this.syncTile(false);
            this.markDirty();
        }
        checkSurroundings = true;
        return false;
    }

    public boolean visStep() {
        if (this.recipeType == 1) {
            return true;
        }
        if (this.getVis() < this.recipe.getVis()) {
            return false;
        }
        this.spendVis(this.recipe.getVis());
        return true;
    }

    public float getVis() {
        int sx = this.pos.getX() >> 4;
        int sz = this.pos.getZ() >> 4;
        float vis = 0.0f;
        for (int xx = -1; xx <= 1; ++xx) {
            for (int zz = -1; zz <= 1; ++zz) {
                AuraChunk ac = AuraHandler.getAuraChunk(this.world.provider.getDimension(), sx + xx, sz + zz);
                if (ac == null) continue;
                vis += ac.getVis();
            }
        }
        return vis;
    }

    public void spendVis(float vis) {
        float q = vis;
        float z = Math.max(1.0f, vis / 9.0f);
        int attempts = 0;
        while (q > 0.0f) {
            ++attempts;
            for (int xx = -1; xx <= 1; ++xx) {
                for (int zz = -1; zz <= 1; ++zz) {
                    if (z > q) {
                        z = q;
                    }
                    if (!((q = (float) ((int) (q - AuraHandler.drainVis(this.getWorld(), this.getPos().add(xx * 16, 0, zz * 16), z, false)))) <= 0.0f) && attempts <= 1000)
                        continue;
                    return;
                }
            }
        }
    }

    public void inEvRandom() {
        this.fluxStocked += (float) this.world.rand.nextInt(10);
        switch (this.world.rand.nextInt(26)) {
            case 0:
            case 1:
            case 2:
            case 3: {
                this.inEvEjectItem(0);
                break;
            }
            case 4:
            case 5:
            case 6: {
                this.inEvWarp();
                break;
            }
            case 7:
            case 8:
            case 9: {
                this.inEvZap(false);
                break;
            }
            case 10:
            case 11: {
                this.inEvZap(true);
                break;
            }
            case 12:
            case 13: {
                this.inEvEjectItem(1);
                break;
            }
            case 14:
            case 15: {
                this.inEvEjectItem(2);
                break;
            }
            case 16: {
                this.inEvEjectItem(3);
                break;
            }
            case 17: {
                this.inEvEjectItem(4);
                break;
            }
            case 18:
            case 19: {
                this.inEvHarm(false);
                break;
            }
            case 20:
            case 21: {
                this.inEvEjectItem(5);
                break;
            }
            case 22: {
                this.inEvHarm(true);
                break;
            }
            case 23: {
                this.world.createExplosion(null, (double) this.pos.getX() + 0.5, (double) this.pos.getY() + 0.5, (double) this.pos.getZ() + 0.5, 1.5f + this.world.rand.nextFloat(), false);
                break;
            }
            case 24:
            case 25: {
                this.invDamageEntity();
            }
        }
    }

    public void invDamageEntity() {
        if (this.entityContained != null) {
            this.entityContained.attackEntityFrom(DamageSource.MAGIC, this.world.rand.nextInt(3));
        }
    }

    public void inEvZap(boolean all) {
        List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), this.getPos().getX() + 1, this.getPos().getY() + 1, this.getPos().getZ() + 1).grow(10.0, 10.0, 10.0));
        if (targets != null && !targets.isEmpty()) {
            for (EntityLivingBase target : targets) {
                PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockArc(this.pos, target, 0.3f - this.world.rand.nextFloat() * 0.1f, 0.0f, 0.3f - this.world.rand.nextFloat() * 0.1f), new NetworkRegistry.TargetPoint(this.world.provider.getDimension(), this.pos.getX(), this.pos.getY(), this.pos.getZ(), 32.0));
                target.attackEntityFrom(DamageSource.MAGIC, 4 + this.world.rand.nextInt(4));
                if (all) continue;
            }
        }
    }

    public void inEvHarm(boolean all) {
        List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), this.getPos().getX() + 1, this.getPos().getY() + 1, this.getPos().getZ() + 1).grow(10.0, 10.0, 10.0));
        if (targets != null && !targets.isEmpty()) {
            for (EntityLivingBase target : targets) {
                if (this.world.rand.nextBoolean()) {
                    target.addPotionEffect(new PotionEffect(PotionFluxTaint.instance, 120, 0, false, true));
                } else {
                    PotionEffect pe = new PotionEffect(PotionVisExhaust.instance, 2400, 0, true, true);
                    pe.getCurativeItems().clear();
                    target.addPotionEffect(pe);
                }
                if (all) continue;
            }
        }
    }

    public void inResAdd() {
        List<EntityPlayer> targets = this.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), this.getPos().getX() + 1, this.getPos().getY() + 1, this.getPos().getZ() + 1).grow(10.0));
        if (!targets.isEmpty()) {
            for (EntityPlayer player : targets) {
                IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(player);
                if (knowledge.isResearchKnown("!INSTABILITY")) continue;
                knowledge.addResearch("!INSTABILITY");
                knowledge.sync((EntityPlayerMP) player);
                player.sendStatusMessage(new TextComponentString(TextFormatting.DARK_PURPLE + I18n.translateToLocal("got.instability")), true);
            }
        }
    }

    public void inEvWarp() {
        List targets = this.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), this.getPos().getX() + 1, this.getPos().getY() + 1, this.getPos().getZ() + 1).grow(10.0));
        if (targets != null && !targets.isEmpty()) {
            EntityPlayer target = (EntityPlayer) targets.get(this.world.rand.nextInt(targets.size()));
            if (this.world.rand.nextFloat() < 0.25f) {
                ThaumcraftApi.internalMethods.addWarpToPlayer(target, 1, IPlayerWarp.EnumWarpType.NORMAL);
            } else {
                ThaumcraftApi.internalMethods.addWarpToPlayer(target, 2 + this.world.rand.nextInt(4), IPlayerWarp.EnumWarpType.TEMPORARY);
            }
        }
    }

    public void inEvEjectItem(int type) {
        TilePedestal[] peds = this.pedestals.toArray(new TilePedestal[this.pedestals.size()]);
        for (int q = 0; q < 50 && !this.pedestals.isEmpty(); ++q) {
            Random rand = new Random();
            TilePedestal te = peds[rand.nextInt(peds.length)];
            BlockPos pos = te.getPos();
            if (te.getStackInSlot(0).isEmpty()) continue;
            if (type <= 3 || type == 5) {
                InventoryUtils.dropItems(this.world, pos);
            } else {
                te.setInventorySlotContents(0, ItemStack.EMPTY);
            }
            te.markDirty();
            te.syncTile(false);
            if (type == 1 || type == 3) {
                this.world.setBlockState(pos.up(), BlocksTC.fluxGoo.getDefaultState());
                this.world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 0.3f, 1.0f);
            } else if (type == 2 || type == 4) {
                int a = 5 + this.world.rand.nextInt(5);
                AuraHelper.polluteAura(this.world, pos, a, true);
            } else if (type == 5) {
                this.world.createExplosion(null, (float) pos.getX() + 0.5f, (float) pos.getY() + 0.5f, (float) pos.getZ() + 0.5f, 1.0f, false);
            }
            this.world.addBlockEvent(pos, this.world.getBlockState(pos).getBlock(), 11, 0);
            PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockArc(pos, pos.up(), 0.3f - this.world.rand.nextFloat() * 0.1f, 0.0f, 0.3f - this.world.rand.nextFloat() * 0.1f), new NetworkRegistry.TargetPoint(this.world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 32.0));
            return;
        }
    }

    public void infusingFinish(CurativeInfusionRecipe recipe, SelfInfusionRecipe recipe2, boolean fail) {
        if (recipeType == 0) {
            if (!fail) {
                recipe.onInfusionFinish(this);
            }
            this.mode = 0;
        } else {
            for (int i = 0; i < this.selfInfusions.length; ++i) {
                if (this.selfInfusions[i] == 0) {
                    this.selfInfusions[i] = this.recipeOutput;
                    break;
                }
            }
            this.mode = 4;
        }

        this.infusing = false;
        this.essentiaNeeded = new AspectList();
        this.optionalComponents.clear();
        this.stacksUsed.clear();
        this.stability = 0.0f;
        this.syncTile(false);
        this.markDirty();
    }

    public boolean doNeedEssentia() {
        this.currentlySucking = null;
        int n = 0;
        Aspect[] aspectArray = this.essentiaNeeded.getAspects();
        int n2 = aspectArray.length;
        if (n < n2) {
            Aspect asp;
            this.currentlySucking = asp = aspectArray[n];
        }
        return this.currentlySucking != null;
    }

    public void dischargeCelestialAura() {
        this.celestialBody = CelestialBody.NONE;
        this.celestialAura = 0.0f;
    }

    public boolean drawnAllEssentia() {
        boolean drew = false;
        Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();
        while (iterator.hasNext()) {
            EnumFacing enumfacing = (EnumFacing) iterator.next();
            TileEntity te = this.world.getTileEntity(this.pos.down(3).offset(enumfacing, 1));
            if (!(te instanceof TileVatConnector)) continue;
            TileVatConnector connector = (TileVatConnector) te;
            drew |= this.drawnEssentia(connector);
        }
        return drew;
    }

    public boolean drawnEssentia(TileVatConnector conn) {
        if (conn == null) return false;

        IEssentiaTransport te = null;
        for (EnumFacing dir : EnumFacing.values()) {
            te = (IEssentiaTransport) ThaumcraftApiHelper.getConnectableTile(this.world, conn.getPos(), dir);
            if (te == null || te.getEssentiaAmount(dir.getOpposite()) <= 0) continue;
            for (Aspect asp : this.essentiaNeeded.getAspects()) {
                int ess = te.takeEssentia(asp, 1, dir.getOpposite());
                if (ess <= 0) continue;
                this.addToContainer(asp, ess);
                this.essentiaNeeded.reduce(asp, ess);
                if (this.essentiaNeeded.getAmount(asp) <= 0) {
                    this.essentiaNeeded.remove(asp);
                }
                return true;
            }
        }
        return false;
    }

    public void drawnCelestialAura() {
        if (this.getCelestialBody().equals(CelestialBody.NONE) && !this.infusing) {
            this.setType(IsorropiaHelper.getCurrentCelestialBody(this.world));
        }
        if (this.canDrawnCelestialAura() && this.celestialAura < (float) this.celestialAuraCap) {
            this.celestialAura += this.getCelestialBody().auraDrainedFactor(this.recipePlayer, this.world);
        }
    }

    public boolean canDrawnCelestialAura() {
        if (world == null || !world.isBlockLoaded(pos)) return false;

        return world.provider.getDimensionType() == DimensionType.OVERWORLD
                && world.canSeeSky(pos.up(2))
                && !celestialBody.equals(CelestialBody.NONE)
                && celestialBody.canBeDrained(recipePlayer, world);
    }

    public void destroyMultiBlock() {
        if (this.getEntityContained() != null) {
            this.killSubject();
        }
        this.createStructure();
    }

    public void createStructure() {
        BlockPos.getAllInBoxMutable(pos.add(-1, -3, -1), pos.add(1, 0, 1)).forEach(blockPos -> {
            if (!world.isBlockLoaded(blockPos)) return;

            boolean isCenter = blockPos.getX() == pos.getX() && blockPos.getZ() == pos.getZ();
            int y = pos.getY() - blockPos.getY();

            if (isCenter) {
                if (y == 0 || y == 3) {
                    world.setBlockState(blockPos, BlocksTC.metalAlchemical.getDefaultState());
                } else {
                    world.setBlockState(blockPos, Blocks.WATER.getDefaultState());
                }
            } else {
                if (y == 0 || y == 3) {
                    world.setBlockState(blockPos, BlocksTC.plankGreatwood.getDefaultState());
                } else {
                    world.setBlockState(blockPos, Blocks.GLASS.getDefaultState());
                }
            }
        });
    }

    public void killSubject() {
        if (this.infusing) {
            this.infusing = false;
        }
        if (!this.world.isRemote) {
            this.world.createExplosion(null, (double) this.pos.getX() + 0.5, (double) this.pos.getY() + 0.5, (double) this.pos.getZ() + 0.5, this.infusing ? 2.0f : 0.5f, this.infusing);
            AuraHelper.polluteAura(this.world, this.pos, this.infusing ? 155.0f : 50.0f, true);
            if (this.getEntityContained() != null) {
                this.entityContained.setHealth(0.0f);
                //TODO: prevent entity dead when entity leave the vat
                if (this.entityContained instanceof EntityLiving) {
                    this.entityContained.setDead();
                }
                this.setEntityContained(null, 0.0f);
            }
        } else {
            ISFXDispatcher.fluxExplosion(this.world, (double) this.pos.getX() + 0.5, (double) this.pos.getY() + 1.5, (double) this.pos.getZ() + 0.5);
        }
    }

    public EntityLivingBase getEntityContained() {
        if (world != null && !world.isRemote && entityContained == null && entityUUID != null) {
            try {
                EntityLivingBase entity = world.getEntities(EntityLivingBase.class, e -> e.getUniqueID().equals(entityUUID)).stream().findFirst().orElse(null);
                if (entity != null) {
                    setEntityContained(entity);
                    entityUUID = null;
                }
            } catch (Exception e) {
                entityUUID = null;
            }
        }
        return entityContained;
    }

    public EntityLivingBase setEntityContained(EntityLivingBase entity) {
        return this.setEntityContained(entity, entity != null ? entity.rotationYaw : 0.0f);
    }

    public EntityLivingBase setEntityContained(EntityLivingBase entity, float rotation) {
        EntityLivingBase old = this.entityContained;
        if (!this.world.isRemote) {
            this.entityContained = entity;
            if (this.entityContained != null) {
                if (this.entityContained instanceof EntityLiving) {
                    ((EntityLiving) this.entityContained).setNoAI(true);
                }
                this.entityContained.rotationYaw = rotation;
                this.entityContained.setPositionAndUpdate((double) this.pos.getX() + 0.5, this.pos.getY() - 2, (double) this.pos.getZ() + 0.5);

                NBTTagCompound entityForgeData = this.entityContained.getEntityData();
                entityForgeData.setBoolean(NBT_TAG_PREVENT_DESPAWN, true);

            }
            if (old instanceof EntityLiving) {
                ((EntityLiving) old).setNoAI(false);
            } else if (old != null && old.getHealth() > 0.0f) {
                old.setRotationYawHead(0.0f);
                old.rotationPitch = 0.0f;
                old.setPositionAndUpdate((double) this.pos.getX() + 0.5, this.pos.getY() + 2, (double) this.pos.getZ() + 0.5);
                NBTTagCompound oldEntityForgeData = old.getEntityData();
                oldEntityForgeData.setBoolean(NBT_TAG_PREVENT_DESPAWN, false);

            }
        }
        return old;
    }

    protected void setWorldCreate(World worldIn) {
        super.setWorldCreate(worldIn);
        if (!this.hasWorld()) {
            this.setWorld(worldIn);
        }
    }

    public void readSyncNBT(NBTTagCompound nbt) {
        this.mode = nbt.getInteger("mode");
        this.active = nbt.getBoolean("active");
        this.startUp = nbt.getFloat("startUp");
        this.infusing = nbt.getBoolean("infusing");
        this.stability = nbt.getFloat("stability");
        this.fluxStocked = nbt.getFloat("fluxStocked");
        this.celestialAura = nbt.getFloat("celestialAura");
        this.celestialAuraNeeded = nbt.getFloat("celestialAuraNeeded");
        this.expunge = nbt.getBoolean("expunge");
        this.setType(IsorropiaAPI.getCelestialBodyByRegistryName(new ResourceLocation(nbt.getString("type"))));
        int id = nbt.getInteger("entityID");
        if ((this.getEntityContained() == null || this.getEntityContained().getEntityId() != id) && id != 0 && this.world != null) {
            this.setEntityContained((EntityLivingBase) this.world.getEntityByID(id));
        }
        this.hasEffigy = nbt.getBoolean("hasEffigy");
        this.selfInfusionHealth = nbt.getFloat("selfInfusionHealth");
        this.myEssentia = nbt.getInteger("myEssentia");
        this.selfInfusions = nbt.getIntArray("selfInfusions");
        if (this.selfInfusions.length == 0) {
            this.selfInfusions = new int[12];
        }
        this.essentiaNeeded.readFromNBT(nbt);
        this.recipeType = nbt.getInteger("recipeType");
    }

    public NBTTagCompound writeSyncNBT(NBTTagCompound nbt) {
        nbt.setInteger("mode", this.getMode());
        nbt.setBoolean("active", this.isActive());
        nbt.setFloat("startUp", this.getStartUp());
        nbt.setBoolean("infusing", this.isInfusing());
        nbt.setFloat("stability", this.stability);
        nbt.setFloat("fluxStocked", this.getFluxStocked());
        nbt.setFloat("celestialAura", this.getCelestialAura());
        nbt.setFloat("celestialAuraNeeded", this.getCelestialAuraNeeded());
        nbt.setString("type", this.getCelestialBody().getRegistryName().toString());
        nbt.setBoolean("expunge", this.isExpunging());
        if (this.getEntityContained() != null) {
            nbt.setInteger("entityID", this.getEntityContained().getEntityId());
        }
        nbt.setBoolean("hasEffigy", this.getHasEffigy());
        nbt.setFloat("selfInfusionHealth", this.getSelfInfusionHealth());
        nbt.setInteger("myEssentia", this.getMyEssentia());
        nbt.setIntArray("selfInfusions", this.selfInfusions);
        nbt.setInteger("recipeType", this.recipeType);
        this.essentiaNeeded.writeToNBT(nbt);
        return nbt;
    }


    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        try {
            if (nbt.hasKey("mode")) {
                this.mode = nbt.getInteger("mode");
            }
            if (nbt.hasKey("stability")) {
                this.stability = nbt.getFloat("stability");
            }
            NBTTagCompound nbttagcompound1;
            int i;
            super.readFromNBT(nbt);
            NBTTagList nbttaglist = nbt.getTagList("recipein", 10);
            this.recipeIngredients = new ArrayList();
            for (i = 0; i < nbttaglist.tagCount(); ++i) {
                nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
                this.recipeIngredients.add(new ItemStack(nbttagcompound1));
            }
            nbttaglist = nbt.getTagList("stacksUsed", 10);
            this.stacksUsed = NonNullList.create();
            for (i = 0; i < nbttaglist.tagCount(); ++i) {
                nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
                this.stacksUsed.add(new ItemStack(nbttagcompound1));
            }
            this.recipeXP = nbt.getInteger("recipexp");
            UUID s = nbt.getUniqueId("recipeplayer");
            if (!this.world.isRemote && s != null) {
                this.recipePlayer = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(s);
            }
            this.expunge = nbt.getBoolean("expunge");
            if (nbt.hasKey("recipe")) {
                this.recipe = IsorropiaAPI.creatureInfusionRecipes.get(new ResourceLocation(nbt.getString("recipe")));
            }
            if (nbt.hasKey("entity")) {
                this.entityUUID = nbt.getCompoundTag("entity").getUniqueId("id");
            }
            if (nbt.hasKey("recipe2")) {
                this.recipe2 = IsorropiaAPI.selfInfusionRecipes.get(new ResourceLocation(nbt.getString("recipe2")));
            }
            if (nbt.hasKey("recipeOutput")) {
                this.recipeOutput = nbt.getInteger("recipeout");
            }
        } catch (Exception e) {
            //ThaumicHorizons.log.error("Error reading NBT data for TileVat", e);
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagCompound stackCompound;
        NBTTagList nbttaglist;
        super.writeToNBT(nbt);
        if (this.getEntityContained() != null) {
            NBTTagCompound entityData = new NBTTagCompound();
            if (this.getEntityContained() != null) {
                entityData.setUniqueId("id", this.getEntityContained().getPersistentID());
            }
            nbt.setTag("entity", entityData);
        }
        if (!this.recipeIngredients.isEmpty()) {
            nbttaglist = new NBTTagList();
            for (ItemStack stack : this.recipeIngredients) {
                if (stack.isEmpty()) continue;
                stackCompound = new NBTTagCompound();
                stack.writeToNBT(stackCompound);
                nbttaglist.appendTag(stackCompound);
            }
            nbt.setTag("recipein", nbttaglist);
        }
        if (!this.stacksUsed.isEmpty()) {
            nbttaglist = new NBTTagList();
            for (ItemStack stack : this.stacksUsed) {
                if (stack.isEmpty()) continue;
                stackCompound = new NBTTagCompound();
                stack.writeToNBT(stackCompound);
                nbttaglist.appendTag(stackCompound);
            }
            nbt.setTag("stacksUsed", nbttaglist);
        }
        nbt.setInteger("recipexp", this.recipeXP);
        if (this.recipePlayer != null) {
            nbt.setUniqueId("recipeplayer", this.recipePlayer.getUniqueID());
        }
        nbt.setBoolean("expunge", this.expunge);
        if (this.recipe != null) {
            nbt.setString("recipe", IsorropiaAPI.creatureInfusionRecipes.entrySet().stream().filter(entry -> IsorropiaAPI.creatureInfusionRecipes.get(entry.getKey()) == this.recipe).findFirst().get().getKey().toString());
        }
        if (this.recipe2 != null) {
            nbt.setString("recipe2", IsorropiaAPI.selfInfusionRecipes.entrySet().stream().filter(entry -> IsorropiaAPI.selfInfusionRecipes.get(entry.getKey()) == this.recipe2).findFirst().get().getKey().toString());
        }
        nbt.setTag("recipeout", new NBTTagInt(this.recipeOutput));
        return nbt;
    }

    public void invalidate() {
        super.invalidate();
        if (this.getEntityContained() != null) {
            this.world.removeEntity(this.entityContained);
        }
    }

    public boolean doesContainerAccept(Aspect paramAspect) {
        return false;
    }

    public boolean takeFromContainer(Aspect paramAspect, int paramInt) {
        return false;
    }

    public boolean takeFromContainer(AspectList paramAspectList) {
        return false;
    }

    public boolean doesContainerContainAmount(Aspect paramAspect, int paramInt) {
        return false;
    }

    public boolean doesContainerContain(AspectList paramAspectList) {
        return false;
    }

    public AspectList getAspects() {
        return this.getEssentiaNeeded();
    }

    public void setAspects(AspectList paramAspectList) {
    }

    public int containerContains(Aspect tag) {
        return this.essentiaNeeded.getAmount(tag);
    }

    public Aspect getSuctionType() {
        return this.currentlySucking;
    }

    public int addEssentia(Aspect aspect, int amount, EnumFacing face) {
        return amount - this.addToContainer(aspect, amount);
    }

    public void addStability() {
    }

    public int getMode() {
        return this.mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getStabilityCap() {
        return this.stabilityCap;
    }

    public void setStabilityCap(int stabilityCap) {
        this.stabilityCap = stabilityCap;
    }

    public float getStartUp() {
        return this.startUp;
    }

    public void setStartUp(float startUp) {
        this.startUp = startUp;
    }

    public Map<String, SourceFX> getSourceFX() {
        return this.sourceFX;
    }

    public void setSourceFX(Map<String, SourceFX> sourceFX) {
        this.sourceFX = sourceFX;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public float getSymmetryStability() {
        return this.stabilityReplenish;
    }

    public AspectList getEssentiaNeeded() {
        return this.essentiaNeeded;
    }

    public int getCraftCount() {
        return this.craftCount;
    }

    public boolean isInfusing() {
        return this.infusing;
    }

    public EntityPlayer getRecipePlayer() {
        return this.recipePlayer;
    }

    public float getRawStability() {
        return this.stability;
    }

    public ICelestialBody getCelestialBody() {
        return this.celestialBody;
    }

    public void setType(ICelestialBody celestialBody) {
        if (!this.celestialBody.equals(celestialBody)) {
            if (!this.celestialBody.equals(CelestialBody.NONE) && !this.celestialBody.isAuraEquals(this.recipePlayer, this.world, celestialBody)) {
                this.dischargeCelestialAura();
            }
            this.celestialBody = celestialBody;
        }
    }

    public float getCelestialAura() {
        return this.celestialAura;
    }

    public float getCelestialAuraNeeded() {
        return this.celestialAuraNeeded;
    }

    public float getFluxStocked() {
        return this.fluxStocked;
    }

    public void setFluxStocked(int fluxStocked) {
        this.fluxStocked = fluxStocked;
    }

    public UUID getEntityTag() {
        return this.entityUUID;
    }

    public void setEntityUUID(UUID entityTag) {
        this.entityUUID = entityTag;
    }

    public int getCelestialAuraCap() {
        return this.celestialAuraCap;
    }

    public void setCelestialAuraCap(int celestialAuraCap) {
        this.celestialAuraCap = celestialAuraCap;
    }

    public void setCelestialBody(ICelestialBody celestialBody) {
        this.celestialBody = celestialBody;
    }

    public void setCelestialAura(float celestialAura) {
        this.celestialAura = celestialAura;
    }

    public void setCelestialAuraNeeded(float celestialAuraNeeded) {
        this.celestialAuraNeeded = celestialAuraNeeded;
    }

    public boolean isExpunging() {
        return this.expunge;
    }

    public int getCurativeCount() {
        return this.curativeCount;
    }

    public void setCurativeCount(int curativeCount) {
        this.curativeCount = curativeCount;
    }

    public boolean isExpunge() {
        return this.expunge;
    }

    public void setExpunge(boolean expunge) {
        this.expunge = expunge;
    }

    public float getSoundExpunge() {
        return this.soundExpunge;
    }

    public void setSoundExpunge(float soundExpunge) {
        this.soundExpunge = soundExpunge;
    }

    public NonNullList<ItemStack> getStacksUsed() {
        return this.stacksUsed;
    }

    public void setStacksUsed(NonNullList<ItemStack> stacksUsed) {
        this.stacksUsed = stacksUsed;
    }

    public ICurativeEffectProvider getCurativeEffect() {
        return this.curativeEffect;
    }

    public void setCurativeEffect(ICurativeEffectProvider curativeEffect) {
        this.curativeEffect = curativeEffect;
    }

    public List<ItemStack> getLoots() {
        return this.loots;
    }

    public void setLoots(List<ItemStack> loots) {
        this.loots = loots;
    }

    public UUID getEntityUUID() {
        return this.entityUUID;
    }

    public void setFluxStocked(float fluxStocked) {
        this.fluxStocked = fluxStocked;
    }

    public boolean getHasEffigy() {
        return this.hasEffigy;
    }

    public void setHasEffigy(boolean effigy) {
        this.hasEffigy = effigy;
    }

    public float getSelfInfusionHealth() {
        return this.selfInfusionHealth;
    }

    public void setSelfInfusionHealth(float health) {
        this.selfInfusionHealth = health;
    }

    public int getMyEssentia() {
        return this.myEssentia;
    }

    public void setMyEssentia(int health) {
        this.myEssentia = health;
    }

    public static class SourceFX {
        public BlockPos loc;
        public int ticks;
        public int color;
        public int entity;

        public SourceFX(BlockPos loc, int ticks, int color) {
            this.loc = loc;
            this.ticks = ticks;
            this.color = color;
        }
    }


    @SubscribeEvent
    public void onLivingAllowDespawn(LivingSpawnEvent.AllowDespawn event) {
        if (event.getEntityLiving() instanceof EntityLiving) {
            EntityLiving entity = (EntityLiving) event.getEntityLiving();

            NBTTagCompound entityForgeData = entity.getEntityData();

            if (entityForgeData.hasKey(NBT_TAG_PREVENT_DESPAWN) && entityForgeData.getBoolean(NBT_TAG_PREVENT_DESPAWN)) {
                event.setResult(Event.Result.DENY);
            }
        }
    }
}
