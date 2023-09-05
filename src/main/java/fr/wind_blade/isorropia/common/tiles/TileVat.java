 package fr.wind_blade.isorropia.common.tiles;
 
 import fr.wind_blade.isorropia.client.fx.ISFXDispatcher;
 import fr.wind_blade.isorropia.common.Common;
 import fr.wind_blade.isorropia.common.IsorropiaAPI;
 import fr.wind_blade.isorropia.common.blocks.BlocksIS;
 import fr.wind_blade.isorropia.common.celestial.CelestialBody;
 import fr.wind_blade.isorropia.common.celestial.ICelestialBody;
 import fr.wind_blade.isorropia.common.curative.ICurativeEffectProvider;
 import fr.wind_blade.isorropia.common.events.SoundsIR;
 import fr.wind_blade.isorropia.common.libs.helpers.IsorropiaHelper;
 import fr.wind_blade.isorropia.common.network.ISPacketFXInfusionSource;
 import fr.wind_blade.isorropia.common.research.recipes.CurativeInfusionRecipe;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.HashSet;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;
 import java.util.Optional;
 import java.util.Set;
 import java.util.UUID;
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
 import net.minecraft.item.ItemStack;
 import net.minecraft.nbt.NBTBase;
 import net.minecraft.nbt.NBTTagCompound;
 import net.minecraft.nbt.NBTTagList;
 import net.minecraft.potion.PotionEffect;
 import net.minecraft.tileentity.TileEntity;
 import net.minecraft.util.DamageSource;
 import net.minecraft.util.EnumFacing;
 import net.minecraft.util.EnumHand;
 import net.minecraft.util.ITickable;
 import net.minecraft.util.NonNullList;
 import net.minecraft.util.ResourceLocation;
 import net.minecraft.util.SoundCategory;
 import net.minecraft.util.math.AxisAlignedBB;
 import net.minecraft.util.math.BlockPos;
 import net.minecraft.util.text.ITextComponent;
 import net.minecraft.util.text.TextComponentString;
 import net.minecraft.util.text.TextFormatting;
 import net.minecraft.util.text.translation.I18n;
 import net.minecraft.world.DimensionType;
 import net.minecraft.world.World;
 import net.minecraftforge.fml.common.FMLCommonHandler;
 import net.minecraftforge.fml.common.network.NetworkRegistry;
 import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
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
 import thaumcraft.common.lib.SoundsTC;
 import thaumcraft.common.lib.network.PacketHandler;
 import thaumcraft.common.lib.network.fx.PacketFXBlockArc;
 import thaumcraft.common.lib.utils.InventoryUtils;
 import thaumcraft.common.tiles.TileThaumcraft;
 import thaumcraft.common.tiles.crafting.TilePedestal;
 import thaumcraft.common.world.aura.AuraChunk;
 import thaumcraft.common.world.aura.AuraHandler;
 
 
 
 
 
 public class TileVat
   extends TileThaumcraft
   implements IAspectContainer, ITickable, IStabilizable
 {
   private static final int X_RANGE = 12;
   private static final int Y_RANGE = 10;
   private static final int Z_RANGE = 12;
   private static final short MIN_FLUX_PRESSURE = 30;
   private static final short MAX_FLUX_PRESSURE = 300;
   private static final byte FLUX_EXPUNGE_SECOND = 2;
/*  101 */   private int mode = 0;
/*  102 */   private int stabilityCap = 25;
/*  103 */   private float stability = 0.0F;
   private float totalInstability;
   private float startUp;
/*  106 */   private Map<String, SourceFX> sourceFX = new HashMap<>();
   private EntityLivingBase entityContained;
   private Aspect currentlySucking;
/*  109 */   private Set<TilePedestal> pedestals = new HashSet<>();
   private boolean active = false;
   private float costMult;
/*  112 */   private int cycleTime = 20;
   private int curativeCount;
   private float fluxStocked;
/*  115 */   private float stabilityReplenish = 0.0F;
/*  116 */   private AspectList essentiaNeeded = new AspectList();
   private UUID entityUUID;
/*  118 */   private ICelestialBody celestialBody = CelestialBody.NONE;
   private float celestialAura;
/*  120 */   private int celestialAuraCap = 100;
   
   private boolean expunge;
   
/*  124 */   private int count = 0;
/*  125 */   private int craftCount = 0;
/*  126 */   private float soundExpunge = 0.0F;
/*  127 */   private int countDelay = this.cycleTime / 2;
/*  128 */   private int itemCount = 0;
   
   private boolean infusing = false;
   private CurativeInfusionRecipe recipe;
/*  132 */   private ArrayList<ItemStack> recipeIngredients = new ArrayList<>();
/*  133 */   private NonNullList<ItemStack> stacksUsed = NonNullList.create();
   private float celestialAuraNeeded;
/*  135 */   private List<ItemStack> optionalComponents = new ArrayList<>();
/*  136 */   private EntityPlayer recipePlayer = null;
   private int recipeXP;
   private ICurativeEffectProvider curativeEffect;
/*  139 */   private List<ItemStack> loots = new ArrayList<>();
   
   public static class SourceFX {
     public BlockPos loc;
     public int ticks;
     
     public SourceFX(BlockPos loc, int ticks, int color) {
/*  146 */       this.loc = loc;
/*  147 */       this.ticks = ticks;
/*  148 */       this.color = color;
     }
 
     
     public int color;
     
     public int entity;
   }
   
   public IStabilizable.EnumStability getStability() {
/*  158 */     return (this.stability > -25.0F) ? IStabilizable.EnumStability.UNSTABLE : ((this.stability >= 0.0F) ? IStabilizable.EnumStability.STABLE : ((this.stability > (this.stabilityCap / 2)) ? IStabilizable.EnumStability.VERY_STABLE : IStabilizable.EnumStability.VERY_UNSTABLE));
   }
 
 
   
   public float getLossPerCycle() {
/*  164 */     return (this.recipe == null) ? 0.0F : (this.recipe.getInstability() / getModFromCurrentStability());
   }
   
   public float getModFromCurrentStability() {
/*  168 */     switch (getStability()) {
       case VERY_STABLE:
/*  170 */         return 5.0F;
       
       case STABLE:
/*  173 */         return 6.0F;
       
       case UNSTABLE:
/*  176 */         return 7.0F;
       
       case VERY_UNSTABLE:
/*  179 */         return 8.0F;
     } 
     
/*  182 */     return 1.0F;
   }
 
   
   public void update() {
/*  187 */     this.count++;
     
/*  189 */     if (this.fluxStocked > 300.0F) {
/*  190 */       if (this.infusing) {
/*  191 */         infusingFinish((CurativeInfusionRecipe)null, true);
       }
       
/*  194 */       destroyMultiBlock();
     } 
     
/*  197 */     updateStartUp();
     
/*  199 */     if (this.world.isRemote) {
/*  200 */       doEffects();
     } else {
       
/*  203 */       if (getEntityContained() != null && this.entityContained.isDead) {
/*  204 */         destroyMultiBlock();
       }
       
/*  207 */       if (!this.expunge && this.fluxStocked >= 30.0F) {
/*  208 */         if (this.mode == 2)
/*  209 */           infusingFinish(this.recipe, true); 
/*  210 */         this.expunge = true;
       } 
/*  212 */       if (this.expunge && this.count % this.countDelay * 2 == 0) {
/*  213 */         this.fluxStocked--;
/*  214 */         AuraHelper.polluteAura(this.world, this.pos.up(), 1.0F, false);
/*  215 */         if (this.fluxStocked <= 0.0F) {
/*  216 */           this.expunge = false;
/*  217 */           this.fluxStocked = 0.0F;
         } 
/*  219 */         syncTile(false);
         
         return;
       } 
/*  223 */       if (getEntityContained() != null) {
/*  224 */         this.pedestals = getSurrondingPedestals();
         
/*  226 */         if (getEntityContained() != null && getEntityContained().isBurning()) {
/*  227 */           getEntityContained().extinguish();
         }
 
         
/*  231 */         if (this.mode == 0 && !this.expunge) {
/*  232 */           curativeUpdate();
/*  233 */           essentiaStep();
 
         
         }
/*  237 */         else if (this.mode == 2 && 
/*  238 */           this.active && this.count % this.countDelay == 0) {
/*  239 */           infusionCycle();
         } 
       } 
     } 
/*  243 */     this.world.notifyBlockUpdate(this.pos, getBlockType().getStateFromMeta(getBlockMetadata()), 
/*  244 */         getBlockType().getStateFromMeta(getBlockMetadata()), 0);
/*  245 */     markDirty();
   }
   
   private void updateStartUp() {
/*  249 */     if (!this.active || (!this.infusing && this.expunge)) {
/*  250 */       if (this.startUp > 0.0F) {
/*  251 */         this.startUp -= Math.max(this.startUp / 10.0F, 0.001F);
       } else {
/*  253 */         this.startUp = 0.0F;
       }
     
/*  256 */     } else if (this.startUp < 1.0F) {
/*  257 */       this.startUp += Math.max(this.startUp / 10.0F, 0.001F);
     } else {
/*  259 */       this.startUp = 1.0F;
     } 
   }
 
 
 
   
   public int addToContainer(Aspect aspectIn, int amount) {
/*  267 */     if (this.mode == 0 && this.curativeEffect != null && getEntityContained() != null && this.curativeCount <= 0) {
/*  268 */       if (aspectIn == this.curativeEffect.getAspect() && amount > 0 && this.curativeEffect
/*  269 */         .effectCanApply(this.entityContained, this)) {
/*  270 */         this.curativeEffect.onApply(this.entityContained, this);
/*  271 */         this.curativeCount += this.curativeEffect.getCooldown(this.entityContained, this);
/*  272 */         this.curativeEffect = null;
/*  273 */         syncTile(false);
/*  274 */         return 0;
       }
     
     }
/*  278 */     else if (this.mode != 0) {
/*  279 */       this.essentiaNeeded.remove(aspectIn, amount);
/*  280 */       return 0;
     } 
     
/*  283 */     return amount;
   }
   
   private void curativeUpdate() {
/*  287 */     this.essentiaNeeded = new AspectList();
/*  288 */     this.curativeCount = Math.max(this.curativeCount - 1, 0);
/*  289 */     EntityLivingBase contained = getEntityContained();
     
/*  291 */     if (this.world.isRemote || contained == null) {
       return;
     }
 
     
/*  296 */     Optional<ICurativeEffectProvider> effect = IsorropiaAPI.curativeEffects.stream().filter(var -> (var.effectCanApply(contained, this) && getEssentiaNeeded().getAmount(var.getAspect()) < 1)).findFirst();
     
/*  298 */     if (effect.isPresent()) {
/*  299 */       this.curativeEffect = effect.get();
/*  300 */       this.essentiaNeeded.add(((ICurativeEffectProvider)effect.get()).getAspect(), 1);
     } 
     
/*  303 */     if (doNeedEssentia())
/*  304 */       drawnAllEssentia(); 
   }
   
   private void doEffects() {
/*  308 */     for (int i = 0; i < 10; i++);
 
 
 
 
 
 
 
 
 
     
/*  319 */     if (this.infusing) {
/*  320 */       if (this.celestialAuraNeeded > 0.0F && canDrawnCelestialAura()) {
/*  321 */         ISFXDispatcher.fxAbsorption((this.pos.getX() + 0.5F), (this.pos
/*  322 */             .getY() + 1.5F) + ((this.celestialAuraNeeded > this.celestialAura) ? 3.0D : -3.5D), (this.pos
/*  323 */             .getZ() + 0.5F), 0.49019608F, 0.5568628F, 0.63529414F, (this.celestialAuraNeeded > this.celestialAura));
       }
       
/*  326 */       if (this.craftCount == 0) {
/*  327 */         this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundsIR.curative_infusion_start, SoundCategory.BLOCKS, 0.5F, 1.0F, false);
       }
/*  329 */       else if (this.craftCount == 0 || this.craftCount % 65 == 0) {
/*  330 */         this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundsIR.curative_infusion_loop, SoundCategory.BLOCKS, 0.5F, 1.0F, false);
       } 
       
/*  333 */       this.craftCount++;
/*  334 */       FXDispatcher.INSTANCE.blockRunes(this.pos.getX(), this.pos.getY() - 2.5D, this.pos.getZ(), this.world.rand
/*  335 */           .nextFloat() * 0.2F, 0.1F, 0.7F + this.world.rand.nextFloat() * 0.3F, 25, -0.03F);
/*  336 */     } else if (this.craftCount > 0) {
/*  337 */       this.craftCount -= 2;
/*  338 */       if (this.craftCount < 0) {
/*  339 */         this.craftCount = 0;
       }
/*  341 */       if (this.craftCount > 50) {
/*  342 */         this.craftCount = 50;
       }
     } 
     
/*  346 */     if (this.active && !this.infusing) {
/*  347 */       if (this.expunge) {
/*  348 */         if (this.soundExpunge == 0.0F && this.fluxStocked * 2.0F > 5.821F) {
           
/*  350 */           this.world.playSound(this.pos.getX(), (this.pos.getY() + 1), this.pos.getZ(), SoundsIR.curative_expunge_flux_start, SoundCategory.BLOCKS, 0.5F, 1.0F, false);
           
/*  352 */           this.soundExpunge = -1.0F;
         
         }
/*  355 */         else if ((this.fluxStocked * 2.0F > 5.821F && 
/*  356 */           (Minecraft.getMinecraft()).player.ticksExisted % 
/*  357 */           Math.floor(116.41999816894531D) == 0.0D) || this.soundExpunge == -1.0F) {
           
/*  359 */           this.world.playSound(this.pos.getX(), (this.pos.getY() + 1), this.pos.getZ(), SoundsIR.curative_expunge_flux_loop, SoundCategory.BLOCKS, 0.5F, 1.0F, false);
           
/*  361 */           this.soundExpunge = (float)Math.floor(116.41999816894531D) + 1.0F;
         } 
       }
       
/*  365 */       if (this.fluxStocked * 2.0F <= 5.821F && this.soundExpunge == 1.0F) {
         
/*  367 */         this.world.playSound(this.pos.getX(), (this.pos.getY() + 1), this.pos.getZ(), SoundsIR.curative_expunge_flux_end, SoundCategory.BLOCKS, 0.5F, 1.0F, false);
         
/*  369 */         this.soundExpunge = 0.0F;
       } 
       
/*  372 */       if (this.soundExpunge > 1.0F) {
/*  373 */         this.soundExpunge--;
       }
     } 
     
/*  377 */     drawnIngredientParticles();
     
/*  379 */     if (this.infusing && this.stability < 0.0F && this.world.rand.nextInt(200) <= Math.abs(this.stability)) {
/*  380 */       float xx = this.pos.getX() + 0.5F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 4.0F;
/*  381 */       float zz = this.pos.getZ() + 0.5F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 4.0F;
/*  382 */       int yy = this.pos.getY() + 1;
/*  383 */       while (!this.world.isAirBlock(new BlockPos(xx, yy, zz))) {
/*  384 */         yy++;
       }
       
/*  387 */       FXDispatcher.INSTANCE.arcLightning((this.pos.getX() + 0.5F), (this.pos.getY() + 1.5F), (this.pos.getZ() + 0.5F), xx, yy, zz, 0.8F, 0.1F, 1.0F, 0.1F);
     } 
   }
 
   
   @SideOnly(Side.CLIENT)
   private void drawnIngredientParticles() {
/*  394 */     Iterator<String> i = this.sourceFX.keySet().iterator();
/*  395 */     while (i.hasNext()) {
/*  396 */       String fxk = i.next();
/*  397 */       SourceFX fx = this.sourceFX.get(fxk);
/*  398 */       if (fx.ticks <= 0) {
/*  399 */         i.remove();
         continue;
       } 
/*  402 */       if (fx.loc.equals(this.pos)) {
/*  403 */         Entity player = this.world.getEntityByID(fx.color);
/*  404 */         if (player != null) {
/*  405 */           for (int a = 0; a < 4; a++) {
/*  406 */             FXDispatcher.INSTANCE.drawInfusionParticles4(player.posX + ((this.world.rand
                 
/*  408 */                 .nextFloat() - this.world.rand.nextFloat()) * player.width), 
/*  409 */                 (player.getEntityBoundingBox()).minY + (this.world.rand.nextFloat() * player.height), player.posZ + ((this.world.rand
                 
/*  411 */                 .nextFloat() - this.world.rand.nextFloat()) * player.width), this.pos
/*  412 */                 .getX(), this.pos.getY() + 1, this.pos.getZ());
           }
         }
       } else {
/*  416 */         TileEntity tile = this.world.getTileEntity(fx.loc);
/*  417 */         if (tile instanceof TilePedestal) {
/*  418 */           ItemStack is = ((TilePedestal)tile).getSyncedStackInSlot(0);
/*  419 */           if (!is.isEmpty()) {
/*  420 */             if (this.world.rand.nextInt(3) == 0) {
/*  421 */               FXDispatcher.INSTANCE.drawInfusionParticles3((fx.loc.getX() + this.world.rand.nextFloat()), (fx.loc
/*  422 */                   .getY() + this.world.rand.nextFloat() + 1.0F), (fx.loc
/*  423 */                   .getZ() + this.world.rand.nextFloat()), this.pos.getX(), this.pos.getY() + 1, this.pos
/*  424 */                   .getZ());
             } else {
               
/*  427 */               Item bi = is.getItem();
/*  428 */               int md = is.getItemDamage();
/*  429 */               if (bi instanceof net.minecraft.item.ItemBlock) {
/*  430 */                 for (int a = 0; a < 4; a++) {
/*  431 */                   FXDispatcher.INSTANCE.drawInfusionParticles2((fx.loc
/*  432 */                       .getX() + this.world.rand.nextFloat()), (fx.loc
/*  433 */                       .getY() + this.world.rand.nextFloat() + 1.0F), (fx.loc
/*  434 */                       .getZ() + this.world.rand.nextFloat()), new BlockPos(this.pos
/*  435 */                         .getX(), this.pos.getY() + 1, this.pos.getZ()), 
/*  436 */                       Block.getBlockFromItem(bi).getDefaultState(), md);
                 }
               } else {
/*  439 */                 for (int a = 0; a < 4; a++) {
/*  440 */                   FXDispatcher.INSTANCE.drawInfusionParticles1((fx.loc
/*  441 */                       .getX() + 0.4F + this.world.rand.nextFloat() * 0.2F), (fx.loc
/*  442 */                       .getY() + 1.23F + this.world.rand.nextFloat() * 0.2F), (fx.loc
/*  443 */                       .getZ() + 0.4F + this.world.rand.nextFloat() * 0.2F), this.pos.up(), is);
                 }
               } 
             } 
           }
         } else {
           
/*  450 */           fx.ticks = 0;
         } 
       } 
/*  453 */       fx.ticks--;
/*  454 */       this.sourceFX.put(fxk, fx);
     } 
   }
 
   
   public boolean onCasterRightClick(World world, ItemStack wandstack, EntityPlayer player, BlockPos pos, EnumFacing side, EnumHand hand) {
/*  460 */     if (this.active && !this.infusing && !this.expunge) {
/*  461 */       if (infusionStart(player)) {
/*  462 */         this.mode = 2;
/*  463 */         return false;
/*  464 */       }  if (player.isSneaking() && !this.expunge && this.fluxStocked > 0.0F) {
/*  465 */         this.expunge = true;
       }
     } 
/*  468 */     if (!world.isRemote && !this.active) {
/*  469 */       world.playSound(null, pos, SoundsTC.craftstart, SoundCategory.BLOCKS, 0.5F, 1.0F);
/*  470 */       this.active = true;
/*  471 */       syncTile(false);
/*  472 */       markDirty();
/*  473 */       return true;
     } 
/*  475 */     return false;
   }
 
   
   public boolean onBlockRigthClick(EntityPlayer playerIn, EnumFacing facing, boolean master) {
/*  480 */     EntityLivingBase contained = getEntityContained();
/*  481 */     ItemStack stack = playerIn.getHeldItemMainhand();
/*  482 */     Block block = Block.getBlockFromItem(stack.getItem());
     
/*  484 */     if (this.world.isRemote)
/*  485 */       return true; 
/*  486 */     if (block == BlocksIS.blockJarSoul && stack.hasTagCompound() && stack
/*  487 */       .getTagCompound().hasKey("ENTITY_DATA")) {
/*  488 */       if (contained == null && playerIn.inventory.addItemStackToInventory(new ItemStack(BlocksTC.jarNormal))) {
/*  489 */         setEntityContained(
/*  490 */             (EntityLivingBase)IsorropiaHelper.nbtToLiving(stack.getTagCompound(), this.world, new BlockPos(this.pos
/*  491 */                 .getX() + 0.5D, (this.pos.getY() - 2), this.pos.getZ() + 0.5D)), facing
/*  492 */             .getOpposite().getHorizontalAngle() - 90.0F);
/*  493 */         ItemStack itemStack = stack;
/*  494 */         itemStack.shrink(1);
/*  495 */         return true;
       }
     
/*  498 */     } else if (contained instanceof EntityLiving) {
/*  499 */       if (!this.loots.isEmpty()) {
/*  500 */         this.loots.stream().filter(loot -> !playerIn.addItemStackToInventory(loot))
/*  501 */           .forEach(drop -> playerIn.dropItem(drop, false));
/*  502 */         this.loots.clear();
/*  503 */         return true;
/*  504 */       }  if (block == BlocksTC.jarNormal) {
/*  505 */         IsorropiaHelper.playerJarEntity(playerIn, (EntityLiving)setEntityContained((EntityLivingBase)null, 0.0F));
/*  506 */         return true;
       } 
/*  508 */     } else if (block == BlocksIS.blockModifiedMatrix) {
/*  509 */       return false;
     } 
     
/*  512 */     if (contained == null && master) {
/*  513 */       setEntityContained((EntityLivingBase)playerIn);
/*  514 */       return true;
/*  515 */     }  if (contained instanceof EntityPlayer && playerIn.equals(contained)) {
/*  516 */       setEntityContained((EntityLivingBase)null);
/*  517 */       return true;
     } 
     
/*  520 */     return false;
   }
   
   public boolean infusionStart(EntityPlayer player) {
/*  524 */     if (getEntityContained() == null) {
/*  525 */       return false;
     }
/*  527 */     this.essentiaNeeded = new AspectList();
/*  528 */     this.recipeIngredients.clear();
/*  529 */     this.stacksUsed.clear();
/*  530 */     this.pedestals.clear();
/*  531 */     this.pedestals = getSurrondingPedestals();
/*  532 */     this.optionalComponents.clear();
/*  533 */     updateSurroundings();
     
/*  535 */     for (TilePedestal ped : this.pedestals) {
/*  536 */       if (ped.getStackInSlot(0).isEmpty())
         continue; 
/*  538 */       this.recipeIngredients.add(ped.getStackInSlot(0).copy());
     } 
     
/*  541 */     this.recipe = IsorropiaAPI.findMatchingCreatureInfusionRecipe(getEntityContained(), this.recipeIngredients, player, this);
 
     
/*  544 */     if (this.recipe == null) {
/*  545 */       this.infusing = false;
/*  546 */       return false;
     } 
     
/*  549 */     if (this.costMult < 0.5D) {
/*  550 */       this.costMult = 0.5F;
     }
 
     
/*  554 */     AspectList al = this.recipe.getCurrentAspect(player, this.world, this.entityContained, this.stability, this.totalInstability, null);
/*  555 */     AspectList al2 = new AspectList();
/*  556 */     for (Aspect as : al.getAspects()) {
/*  557 */       if ((int)(al.getAmount(as) * this.costMult) > 0)
       {
/*  559 */         al2.add(as, (int)(al.getAmount(as) * this.costMult)); } 
     } 
/*  561 */     this.essentiaNeeded = al2;
/*  562 */     this.celestialAuraNeeded = this.recipe.getCelestialAura();
/*  563 */     this.celestialBody = this.recipe.getCelestialBody();
/*  564 */     this.recipePlayer = player;
/*  565 */     this.infusing = true;
/*  566 */     this.world.playSound(null, this.pos, SoundsTC.craftstart, SoundCategory.BLOCKS, 0.5F, 1.0F);
/*  567 */     syncTile(false);
/*  568 */     markDirty();
/*  569 */     return true;
   }
 
   
   public Set<TilePedestal> getSurrondingPedestals() {
/*  574 */     return getSurrondingPedestals(12, 10, 12, 12, 10, 12);
   }
   
   public Set<TilePedestal> getSurrondingPedestals(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
/*  578 */     Set<TilePedestal> peds = new HashSet<>();
     
/*  580 */     BlockPos vat_pos = getPos();
/*  581 */     Iterable<BlockPos.MutableBlockPos> blocks = BlockPos.getAllInBoxMutable(vat_pos.getX() - minX, vat_pos.getY() - minY, vat_pos
/*  582 */         .getZ() - minZ, vat_pos.getX() + maxX, vat_pos.getY() + maxY, vat_pos.getZ() + maxZ);
     
/*  584 */     for (BlockPos pos : blocks) {
/*  585 */       TileEntity te = this.world.getTileEntity(pos);
/*  586 */       if (te instanceof TilePedestal) {
/*  587 */         peds.add((TilePedestal)te);
       }
     } 
/*  590 */     return peds;
   }
 
   
   public Set<BlockPos> getSurroundingOccults() {
/*  595 */     return getSurroundingOccults(12, 10, 12, 12, 10, 12);
   }
   
   public Set<BlockPos> getSurroundingOccults(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
/*  599 */     Set<BlockPos> occults = new HashSet<>();
     
/*  601 */     BlockPos curativePos = getPos();
/*  602 */     Iterable<BlockPos.MutableBlockPos> blocks = BlockPos.getAllInBoxMutable(curativePos.getX() - minX, curativePos
/*  603 */         .getY() - minY, curativePos.getZ() - minZ, curativePos.getX() + maxX, curativePos
/*  604 */         .getY() + maxY, curativePos.getZ() + maxZ);
     
/*  606 */     for (BlockPos pos : blocks) {
/*  607 */       Block block = this.world.getBlockState(pos).getBlock();
/*  608 */       if (block == null || (block != Blocks.SKULL && (!(block instanceof IInfusionStabiliser) || 
/*  609 */         !((IInfusionStabiliser)block).canStabaliseInfusion(getWorld(), pos))))
         continue; 
/*  611 */       occults.add(pos);
     } 
     
/*  614 */     return occults;
   }
 
   
   private void updateSurroundings() {
/*  619 */     Set<BlockPos> occults = getSurroundingOccults();
     
/*  621 */     this.cycleTime = 10;
/*  622 */     this.stabilityReplenish = 0.0F;
/*  623 */     this.costMult = 1.0F;
/*  624 */     this.countDelay = this.cycleTime / 2;
     
/*  626 */     int[] xm = { -1, 1, 1, -1 };
/*  627 */     int[] zm = { -1, -1, 1, 1 };
/*  628 */     for (int a = 0; a < 4; a++) {
/*  629 */       Block b = this.world.getBlockState(this.pos.add(xm[a], -3, zm[a])).getBlock();
/*  630 */       if (b == BlocksTC.matrixSpeed) {
/*  631 */         this.cycleTime--;
/*  632 */         this.costMult += 0.01F;
       } 
/*  634 */       if (b == BlocksTC.matrixCost) {
         
/*  636 */         this.cycleTime++;
/*  637 */         this.costMult -= 0.02F;
       } 
     } 
 
 
 
     
/*  644 */     for (TilePedestal tile : this.pedestals) {
/*  645 */       Block bb = this.world.getBlockState(tile.getPos()).getBlock();
/*  646 */       if (bb == BlocksTC.pedestalEldritch)
/*  647 */         this.costMult += 0.0025F; 
/*  648 */       if (bb == BlocksTC.pedestalAncient) {
/*  649 */         this.costMult -= 0.01F;
       }
     } 
 
 
 
     
/*  656 */     for (BlockPos pos : occults) {
/*  657 */       int x1 = this.pos.getX() - pos.getX();
/*  658 */       int z1 = this.pos.getZ() - pos.getZ();
/*  659 */       int x2 = this.pos.getX() + x1;
/*  660 */       int z2 = this.pos.getZ() + z1;
/*  661 */       BlockPos c2 = new BlockPos(x2, pos.getY(), z2);
/*  662 */       Block sb1 = this.world.getBlockState(pos).getBlock();
/*  663 */       Block sb2 = this.world.getBlockState(c2).getBlock();
/*  664 */       float amt1 = 0.1F;
/*  665 */       float amt2 = 0.1F;
/*  666 */       if (sb1 instanceof IInfusionStabiliserExt)
/*  667 */         amt1 = ((IInfusionStabiliserExt)sb1).getStabilizationAmount(getWorld(), pos); 
/*  668 */       if (sb2 instanceof IInfusionStabiliserExt)
/*  669 */         amt2 = ((IInfusionStabiliserExt)sb2).getStabilizationAmount(getWorld(), c2); 
/*  670 */       if (sb1 == sb2 && amt1 == amt2) {
/*  671 */         this.stabilityReplenish += amt1; continue;
       } 
/*  673 */       this.stabilityReplenish -= Math.max(amt1, amt2);
     } 
   }
 
 
   
   public void infusionCycle() {
/*  680 */     if (this.recipe == null) {
/*  681 */       infusingFinish((CurativeInfusionRecipe)null, true);
       
       return;
     } 
/*  685 */     if (getEntityContained() == null) {
/*  686 */       destroyMultiBlock();
     }
 
     
/*  690 */     boolean valid = false;
/*  691 */     if (getEntityContained() != null)
/*  692 */       valid = true; 
/*  693 */     this.stability -= this.recipe.getInstability() / getModFromCurrentStability();
/*  694 */     this.stability += this.stabilityReplenish;
/*  695 */     this.stability = Math.min(Math.max(this.stability, -100.0F), this.stabilityCap);
     
/*  697 */     if (this.stability < 0.0F && this.world.rand.nextInt(1500) <= Math.abs(this.stability)) {
/*  698 */       inEvRandom();
/*  699 */       this.stability += 5.0F + this.world.rand.nextFloat() * 5.0F;
/*  700 */       inResAdd();
/*  701 */       if (valid)
         return; 
     } 
/*  704 */     if (!valid) {
/*  705 */       this.infusing = false;
/*  706 */       this.essentiaNeeded = new AspectList();
/*  707 */       syncTile(false);
/*  708 */       this.world.playSound(null, this.pos, SoundsTC.craftfail, SoundCategory.BLOCKS, 1.0F, 0.6F);
/*  709 */       markDirty();
       
       return;
     } 
/*  713 */     if (!celestialAuraStep()) {
       return;
     }
/*  716 */     if (!xpStep()) {
       return;
     }
/*  719 */     this.countDelay = Math.max(this.countDelay, 1);
     
/*  721 */     if (!essentiaStep()) {
       return;
     }
/*  724 */     if (!ingredientStep()) {
       return;
     }
/*  727 */     if (!visStep())
       return; 
/*  729 */     this.fluxStocked += this.recipe.getFluxRejection();
/*  730 */     this.infusing = false;
 
     
/*  733 */     infusingFinish(this.recipe, false);
/*  734 */     syncTile(false);
/*  735 */     markDirty();
   }
   
   public boolean celestialAuraStep() {
/*  739 */     if (this.celestialAuraNeeded > 0.0F) {
/*  740 */       if (this.celestialAura < this.celestialAuraNeeded) {
/*  741 */         drawnCelestialAura();
       } else {
/*  743 */         this.celestialAura--;
/*  744 */         this.celestialAuraNeeded--;
       } 
       
/*  747 */       return false;
     } 
/*  749 */     return true;
   }
 
 
   
   public boolean xpStep() {
/*  755 */     if (this.recipeXP > 0) {
/*  756 */       List<EntityPlayer> targets = this.world.getEntitiesWithinAABB(EntityPlayer.class, (new AxisAlignedBB(
/*  757 */             getPos().getX(), getPos().getY(), getPos().getZ(), (getPos().getX() + 1), (
/*  758 */             getPos().getY() + 1), (getPos().getZ() + 1))).grow(10.0D, 10.0D, 10.0D));
/*  759 */       if (!targets.isEmpty()) {
/*  760 */         for (Iterator<EntityPlayer> iterator = targets.iterator(); iterator.hasNext(); ) {
/*  761 */           EntityPlayer target = iterator.next();
/*  762 */           if (target.capabilities.isCreativeMode || target.experienceLevel > 0) {
/*  763 */             if (!target.capabilities.isCreativeMode) {
/*  764 */               target.addExperienceLevel(-1);
             }
/*  766 */             this.recipeXP--;
/*  767 */             target.attackEntityFrom(DamageSource.MAGIC, this.world.rand.nextInt(2));
/*  768 */             Common.INSTANCE.sendToAllAround((IMessage)new ISPacketFXInfusionSource(this.pos, this.pos, target.getEntityId()), new NetworkRegistry.TargetPoint(
/*  769 */                   (getWorld()).provider.getDimension(), this.pos.getX(), this.pos.getY(), this.pos.getZ(), 32.0D));
             
/*  771 */             target.playSound(SoundEvents.BLOCK_LAVA_EXTINGUISH, 1.0F, 2.0F + this.world.rand.nextFloat() * 0.4F);
/*  772 */             this.countDelay = this.cycleTime;
/*  773 */             return false;
           } 
         } 
         
/*  777 */         Aspect[] ingEss = this.recipe.getAspects().getAspects();
/*  778 */         if (ingEss != null && ingEss.length > 0 && this.world.rand.nextInt(3) == 0) {
/*  779 */           Aspect as = ingEss[this.world.rand.nextInt(ingEss.length)];
/*  780 */           this.fluxStocked++;
/*  781 */           this.essentiaNeeded.add(as, 1);
/*  782 */           this.stability -= 0.25F;
/*  783 */           syncTile(false);
/*  784 */           markDirty();
         } 
       } 
/*  787 */       return false;
     } 
     
/*  790 */     return true;
   }
   
   public boolean essentiaStep() {
/*  794 */     if (this.essentiaNeeded.visSize() > 0) {
/*  795 */       if (doNeedEssentia())
/*  796 */         drawnAllEssentia(); 
/*  797 */       return false;
     } 
     
/*  800 */     return true;
   }
 
   
   public boolean ingredientStep() {
/*  805 */     if (this.recipeIngredients.isEmpty()) {
/*  806 */       return true;
     }
     
/*  809 */     for (int a = 0; a < this.recipeIngredients.size(); a++) {
/*  810 */       for (TilePedestal pedestal : this.pedestals) {
/*  811 */         if (pedestal.getStackInSlot(0).isEmpty() || 
/*  812 */           !ThaumcraftInvHelper.areItemStacksEqualForCrafting(pedestal.getStackInSlot(0), this.recipeIngredients.get(a))) {
           continue;
         }
/*  815 */         if (this.itemCount == 0) {
/*  816 */           this.itemCount = 5;
/*  817 */           Common.INSTANCE.sendToAllAround((IMessage)new ISPacketFXInfusionSource(this.pos, pedestal.getPos(), 0), new NetworkRegistry.TargetPoint(
/*  818 */                 (getWorld()).provider.getDimension(), this.pos.getX(), this.pos
/*  819 */                 .getY(), this.pos.getZ(), 32.0D));
/*  820 */         } else if (this.itemCount-- <= 1) {
/*  821 */           ItemStack is = pedestal.getStackInSlot(0).getItem().getContainerItem(pedestal.getStackInSlot(0));
/*  822 */           pedestal.setInventorySlotContents(0, (is == null || is.isEmpty()) ? ItemStack.EMPTY : is.copy());
/*  823 */           pedestal.markDirty();
/*  824 */           pedestal.syncTile(false);
/*  825 */           this.stacksUsed.add(this.recipeIngredients.get(a));
/*  826 */           this.recipeIngredients.remove(a);
/*  827 */           markDirty();
         } 
/*  829 */         return false;
       } 
       
/*  832 */       Aspect[] ingEss = this.recipe.getAspects().getAspects();
/*  833 */       if (ingEss != null && ingEss.length > 0 && this.world.rand.nextInt(1 + a) == 0) {
/*  834 */         Aspect as = ingEss[this.world.rand.nextInt(ingEss.length)];
/*  835 */         this.fluxStocked++;
/*  836 */         this.essentiaNeeded.add(as, 1);
/*  837 */         this.stability -= 0.25F;
/*  838 */         syncTile(false);
/*  839 */         markDirty();
       } 
     } 
     
/*  843 */     return false;
   }
   
   public boolean visStep() {
/*  847 */     if (getVis() < this.recipe.getVis())
/*  848 */       return false; 
/*  849 */     spendVis(this.recipe.getVis());
/*  850 */     return true;
   }
   
   public float getVis() {
/*  854 */     int sx = this.pos.getX() >> 4;
/*  855 */     int sz = this.pos.getZ() >> 4;
/*  856 */     float vis = 0.0F;
/*  857 */     for (int xx = -1; xx <= 1; xx++) {
/*  858 */       for (int zz = -1; zz <= 1; zz++) {
/*  859 */         AuraChunk ac = AuraHandler.getAuraChunk(this.world.provider.getDimension(), sx + xx, sz + zz);
/*  860 */         if (ac != null)
/*  861 */           vis += ac.getVis(); 
       } 
     } 
/*  864 */     return vis;
   }
   
   public void spendVis(float vis) {
/*  868 */     float q = vis;
/*  869 */     float z = Math.max(1.0F, vis / 9.0F);
/*  870 */     int attempts = 0;
/*  871 */     while (q > 0.0F) {
/*  872 */       attempts++;
/*  873 */       for (int xx = -1; xx <= 1; xx++) {
/*  874 */         for (int zz = -1; zz <= 1; zz++) {
/*  875 */           if (z > q)
/*  876 */             z = q; 
/*  877 */           q = (int)(q - AuraHandler.drainVis(getWorld(), getPos().add(xx * 16, 0, zz * 16), z, false));
/*  878 */           if (q <= 0.0F || attempts > 1000)
             return; 
         } 
       } 
     } 
   }
   public void inEvRandom() {
/*  885 */     this.fluxStocked += this.world.rand.nextInt(10);
/*  886 */     switch (this.world.rand.nextInt(26)) {
       case 0:
       case 1:
       case 2:
       case 3:
/*  891 */         inEvEjectItem(0);
         break;
       case 4:
       case 5:
       case 6:
/*  896 */         inEvWarp();
         break;
       case 7:
       case 8:
       case 9:
/*  901 */         inEvZap(false);
         break;
       case 10:
       case 11:
/*  905 */         inEvZap(true);
         break;
       case 12:
       case 13:
/*  909 */         inEvEjectItem(1);
         break;
       case 14:
       case 15:
/*  913 */         inEvEjectItem(2);
         break;
       case 16:
/*  916 */         inEvEjectItem(3);
         break;
       case 17:
/*  919 */         inEvEjectItem(4);
         break;
       case 18:
       case 19:
/*  923 */         inEvHarm(false);
         break;
       case 20:
       case 21:
/*  927 */         inEvEjectItem(5);
         break;
       case 22:
/*  930 */         inEvHarm(true);
         break;
       case 23:
/*  933 */         this.world.createExplosion(null, this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D, 1.5F + this.world.rand
/*  934 */             .nextFloat(), false);
         break;
       case 24:
       case 25:
/*  938 */         invDamageEntity();
         break;
     } 
   }
 
   
   public void invDamageEntity() {
/*  945 */     if (this.entityContained != null) {
/*  946 */       this.entityContained.attackEntityFrom(DamageSource.MAGIC, this.world.rand.nextInt(3));
     }
   }
   
   public void inEvZap(boolean all) {
/*  951 */     List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, (new AxisAlignedBB(
/*  952 */           getPos().getX(), getPos().getY(), getPos().getZ(), (getPos().getX() + 1), (
/*  953 */           getPos().getY() + 1), (getPos().getZ() + 1))).grow(10.0D, 10.0D, 10.0D));
/*  954 */     if (targets != null && !targets.isEmpty()) {
/*  955 */       Iterator<EntityLivingBase> iterator = targets.iterator();
       
/*  957 */       while (iterator.hasNext()) {
 
         
/*  960 */         EntityLivingBase target = iterator.next();
/*  961 */         PacketHandler.INSTANCE.sendToAllAround((IMessage)new PacketFXBlockArc(this.pos, (Entity)target, 0.3F - this.world.rand
/*  962 */               .nextFloat() * 0.1F, 0.0F, 0.3F - this.world.rand
/*  963 */               .nextFloat() * 0.1F), new NetworkRegistry.TargetPoint(this.world.provider
               
/*  965 */               .getDimension(), this.pos.getX(), this.pos.getY(), this.pos.getZ(), 32.0D));
/*  966 */         target.attackEntityFrom(DamageSource.MAGIC, (4 + this.world.rand.nextInt(4)));
/*  967 */         if (!all)
           break; 
       } 
     } 
   } public void inEvHarm(boolean all) {
/*  972 */     List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, (new AxisAlignedBB(
/*  973 */           getPos().getX(), getPos().getY(), getPos().getZ(), (getPos().getX() + 1), (
/*  974 */           getPos().getY() + 1), (getPos().getZ() + 1))).grow(10.0D, 10.0D, 10.0D));
/*  975 */     if (targets != null && !targets.isEmpty()) {
/*  976 */       Iterator<EntityLivingBase> iterator = targets.iterator();
       
/*  978 */       while (iterator.hasNext()) {
 
         
/*  981 */         EntityLivingBase target = iterator.next();
/*  982 */         if (this.world.rand.nextBoolean()) {
/*  983 */           target.addPotionEffect(new PotionEffect(PotionFluxTaint.instance, 120, 0, false, true));
         } else {
/*  985 */           PotionEffect pe = new PotionEffect(PotionVisExhaust.instance, 2400, 0, true, true);
/*  986 */           pe.getCurativeItems().clear();
/*  987 */           target.addPotionEffect(pe);
         } 
/*  989 */         if (!all)
           break; 
       } 
     } 
   } public void inResAdd() {
/*  994 */     List<EntityPlayer> targets = this.world.getEntitiesWithinAABB(EntityPlayer.class, (new AxisAlignedBB(
/*  995 */           getPos().getX(), getPos().getY(), getPos().getZ(), (getPos().getX() + 1), (
/*  996 */           getPos().getY() + 1), (getPos().getZ() + 1))).grow(10.0D));
/*  997 */     if (!targets.isEmpty()) {
/*  998 */       Iterator<EntityPlayer> iterator = targets.iterator();
       
/* 1000 */       while (iterator.hasNext()) {
 
 
         
/* 1004 */         EntityPlayer player = iterator.next();
/* 1005 */         IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(player);
/* 1006 */         if (!knowledge.isResearchKnown("!INSTABILITY")) {
/* 1007 */           knowledge.addResearch("!INSTABILITY");
/* 1008 */           knowledge.sync((EntityPlayerMP)player);
/* 1009 */           player.sendStatusMessage((ITextComponent)new TextComponentString(TextFormatting.DARK_PURPLE + 
                 
/* 1011 */                 I18n.translateToLocal("got.instability")), true);
         } 
       } 
     } 
   }
 
   
   public void inEvWarp() {
/* 1019 */     List<EntityPlayer> targets = this.world.getEntitiesWithinAABB(EntityPlayer.class, (new AxisAlignedBB(
/* 1020 */           getPos().getX(), getPos().getY(), getPos().getZ(), (getPos().getX() + 1), (
/* 1021 */           getPos().getY() + 1), (getPos().getZ() + 1))).grow(10.0D));
/* 1022 */     if (targets != null && !targets.isEmpty()) {
/* 1023 */       EntityPlayer target = targets.get(this.world.rand.nextInt(targets.size()));
/* 1024 */       if (this.world.rand.nextFloat() < 0.25F) {
/* 1025 */         ThaumcraftApi.internalMethods.addWarpToPlayer(target, 1, IPlayerWarp.EnumWarpType.NORMAL);
       } else {
         
/* 1028 */         ThaumcraftApi.internalMethods.addWarpToPlayer(target, 2 + this.world.rand.nextInt(4), IPlayerWarp.EnumWarpType.TEMPORARY);
       } 
     } 
   }
 
   
   public void inEvEjectItem(int type) {
     TilePedestal[] peds = this.pedestals.<TilePedestal>toArray(new TilePedestal[this.pedestals.size()]);
     for (int q = 0; q < 50 && !this.pedestals.isEmpty(); q++) {
       Random rand = new Random();
       TilePedestal te = peds[rand.nextInt(peds.length)];
       BlockPos pos = te.getPos();

       if (!te.getStackInSlot(0).isEmpty()) {
         if (type <= 3 || type == 5) {
           InventoryUtils.dropItems(this.world, pos);
         } else {
/* 1045 */           te.setInventorySlotContents(0, ItemStack.EMPTY);
         } 
/* 1047 */         te.markDirty();
/* 1048 */         te.syncTile(false);
/* 1049 */         if (type == 1 || type == 3) {
/* 1050 */           this.world.setBlockState(pos.up(), BlocksTC.fluxGoo.getDefaultState());
/* 1051 */           this.world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 0.3F, 1.0F);
/* 1052 */         } else if (type == 2 || type == 4) {
/* 1053 */           int a = 5 + this.world.rand.nextInt(5);
/* 1054 */           AuraHelper.polluteAura(this.world, pos, a, true);
/* 1055 */         } else if (type == 5) {
/* 1056 */           this.world.createExplosion(null, (pos.getX() + 0.5F), (pos.getY() + 0.5F), (pos.getZ() + 0.5F), 1.0F, false);
         } 
/* 1058 */         this.world.addBlockEvent(pos, this.world.getBlockState(pos).getBlock(), 11, 0);
/* 1059 */         PacketHandler.INSTANCE.sendToAllAround((IMessage)new PacketFXBlockArc(pos, pos
/* 1060 */               .up(), 0.3F - this.world.rand.nextFloat() * 0.1F, 0.0F, 0.3F - this.world.rand
/* 1061 */               .nextFloat() * 0.1F), new NetworkRegistry.TargetPoint(this.world.provider
               
/* 1063 */               .getDimension(), pos.getX(), pos.getY(), pos.getZ(), 32.0D));
         return;
       } 
     } 
   }
 
   
   public void infusingFinish(CurativeInfusionRecipe recipe, boolean fail) {
/* 1071 */     this.mode = 0;
/* 1072 */     if (!fail)
/* 1073 */       recipe.onInfusionFinish(this); 
/* 1074 */     this.infusing = false;
/* 1075 */     this.essentiaNeeded = new AspectList();
/* 1076 */     this.optionalComponents.clear();
/* 1077 */     this.stacksUsed.clear();
/* 1078 */     this.stability = 0.0F;
/* 1079 */     syncTile(false);
/* 1080 */     markDirty();
   }
 
 
 
 
 
   
   public boolean doNeedEssentia() {
/* 1089 */     this.currentlySucking = null;
/* 1090 */     Aspect[] arrayOfAspect = this.essentiaNeeded.getAspects(); int i = arrayOfAspect.length; byte b = 0; if (b < i) { Aspect asp = arrayOfAspect[b];
/* 1091 */       this.currentlySucking = asp; }
 
     
/* 1094 */     return (this.currentlySucking != null);
   }
 
 
 
 
 
   
   public void dischargeCelestialAura() {
/* 1103 */     this.celestialBody = CelestialBody.NONE;
/* 1104 */     this.celestialAura = 0.0F;
   }
 
 
 
 
 
 
   
   public boolean drawnAllEssentia() {
/* 1114 */     boolean drew = false;
     
/* 1116 */     for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
/* 1117 */       TileEntity te = this.world.getTileEntity(this.pos.down(3).offset(enumfacing, 1));
/* 1118 */       if (te instanceof TileVatConnector) {
/* 1119 */         TileVatConnector connector = (TileVatConnector)te;
/* 1120 */         drew |= drawnEssentia(connector);
       } 
     } 
     
/* 1124 */     return drew;
   }
   
   public boolean drawnEssentia(TileVatConnector conn) {
/* 1128 */     IEssentiaTransport te = null;
/* 1129 */     for (EnumFacing dir : EnumFacing.values()) {
/* 1130 */       te = (IEssentiaTransport)ThaumcraftApiHelper.getConnectableTile(this.world, conn.getPos(), dir);
/* 1131 */       if (te != null && 
/* 1132 */         te.getEssentiaAmount(dir.getOpposite()) > 0) {
/* 1133 */         for (Aspect asp : this.essentiaNeeded.getAspects()) {
/* 1134 */           int ess = te.takeEssentia(asp, 1, dir.getOpposite());
/* 1135 */           if (ess > 0) {
/* 1136 */             addToContainer(asp, ess);
/* 1137 */             this.essentiaNeeded.reduce(asp, ess);
/* 1138 */             if (this.essentiaNeeded.getAmount(asp) <= 0) {
/* 1139 */               this.essentiaNeeded.remove(asp);
             }
/* 1141 */             return true;
           } 
         } 
       }
     } 
     
/* 1147 */     return false;
   }
 
   
   public void drawnCelestialAura() {
/* 1152 */     if (getCelestialBody().equals(CelestialBody.NONE) && !this.infusing) {
/* 1153 */       setType(IsorropiaHelper.getCurrentCelestialBody(this.world));
     }
     
/* 1156 */     if (canDrawnCelestialAura() && this.celestialAura < this.celestialAuraCap) {
/* 1157 */       this.celestialAura += 1.0F * getCelestialBody().auraDrainedFactor(this.recipePlayer, this.world);
     }
   }
   
   public boolean canDrawnCelestialAura() {
/* 1162 */     if (this.world.provider.getDimensionType() != DimensionType.OVERWORLD || !this.world.canSeeSky(getPos().up().up())) {
/* 1163 */       return false;
     }
/* 1165 */     if (this.celestialBody.equals(CelestialBody.NONE) || !this.celestialBody.canBeDrained(this.recipePlayer, this.world))
/* 1166 */       return false; 
/* 1167 */     return true;
   }
   
   public void destroyMultiBlock() {
/* 1171 */     if (getEntityContained() != null)
/* 1172 */       killSubject(); 
/* 1173 */     createStructure();
   }
 
   
   public void createStructure() {
/* 1178 */     for (int y = 0; y < 4; y++) {
/* 1179 */       for (int x = -1; x < 2; x++) {
/* 1180 */         for (int z = -1; z < 2; z++) {
/* 1181 */           if (x != 0 || z != 0) {
/* 1182 */             if (y == 0 || y == 3) {
/* 1183 */               this.world.setBlockToAir(new BlockPos(this.pos
/* 1184 */                     .getX() + x, this.pos.getY() - y, this.pos.getZ() + z));
/* 1185 */               this.world.setBlockState(new BlockPos(this.pos
/* 1186 */                     .getX() + x, this.pos.getY() - y, this.pos.getZ() + z), BlocksTC.plankGreatwood
/* 1187 */                   .getDefaultState());
             } else {
/* 1189 */               this.world.setBlockToAir(new BlockPos(this.pos
/* 1190 */                     .getX() + x, this.pos.getY() - y, this.pos.getZ() + z));
/* 1191 */               this.world.setBlockState(new BlockPos(this.pos
/* 1192 */                     .getX() + x, this.pos.getY() - y, this.pos.getZ() + z), Blocks.GLASS
/* 1193 */                   .getDefaultState(), 3);
             } 
/* 1195 */           } else if (y == 0 || y == 3) {
/* 1196 */             this.world.setBlockToAir(new BlockPos(this.pos
/* 1197 */                   .getX() + x, this.pos.getY() - y, this.pos.getZ() + z));
/* 1198 */             this.world.setBlockState(new BlockPos(this.pos
/* 1199 */                   .getX() + x, this.pos.getY() - y, this.pos.getZ() + z), BlocksTC.metalAlchemical
/* 1200 */                 .getDefaultState(), 3);
           } else {
/* 1202 */             this.world.setBlockToAir(new BlockPos(this.pos
/* 1203 */                   .getX() + x, this.pos.getY() - y, this.pos.getZ() + z));
/* 1204 */             this.world.setBlockState(new BlockPos(this.pos
/* 1205 */                   .getX() + x, this.pos.getY() - y, this.pos.getZ() + z), Blocks.WATER
/* 1206 */                 .getDefaultState(), 3);
           }
         } 
       } 
     } 
   }
   
   public void killSubject() {
/* 1214 */     if (this.infusing)
/* 1215 */       this.infusing = false; 
/* 1216 */     if (!this.world.isRemote) {
/* 1217 */       this.world.createExplosion(null, this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D, this.infusing ? 2.0F : 0.5F, this.infusing);
       
/* 1219 */       AuraHelper.polluteAura(this.world, this.pos, this.infusing ? 155.0F : 50.0F, true);
       
/* 1221 */       if (getEntityContained() != null) {
/* 1222 */         this.entityContained.setHealth(0.0F);
         
/* 1224 */         if (this.entityContained instanceof EntityLiving) {
/* 1225 */           this.entityContained.setDead();
         }
         
/* 1228 */         setEntityContained((EntityLivingBase)null, 0.0F);
       } 
     } else {
/* 1231 */       ISFXDispatcher.fluxExplosion(this.world, this.pos.getX() + 0.5D, this.pos.getY() + 1.5D, this.pos.getZ() + 0.5D);
     } 
   }
   
   public EntityLivingBase getEntityContained() {
/* 1236 */     if (this.world != null && !this.world.isRemote && this.entityContained == null && this.entityUUID != null) {
/* 1237 */       EntityLivingBase base = (EntityLivingBase)IsorropiaHelper.getEntityByUUID(this.entityUUID, getWorld());
/* 1238 */       if (base != null) {
/* 1239 */         setEntityContained(base);
/* 1240 */         this.entityUUID = null;
       } 
     } 
     
/* 1244 */     return this.entityContained;
   }
   
   public EntityLivingBase setEntityContained(EntityLivingBase entity) {
/* 1248 */     return setEntityContained(entity, (entity != null) ? entity.rotationYaw : 0.0F);
   }
   
   public EntityLivingBase setEntityContained(EntityLivingBase entity, float rotation) {
/* 1252 */     EntityLivingBase old = this.entityContained;
     
/* 1254 */     if (!this.world.isRemote) {
/* 1255 */       this.entityContained = entity;
/* 1256 */       if (this.entityContained != null) {
/* 1257 */         if (this.entityContained instanceof EntityLiving) {
/* 1258 */           ((EntityLiving)this.entityContained).setNoAI(true);
         }
         
/* 1261 */         this.entityContained.rotationYaw = rotation;
/* 1262 */         this.entityContained.setPositionAndUpdate(this.pos.getX() + 0.5D, (this.pos.getY() - 2), this.pos.getZ() + 0.5D);
       } 
       
/* 1265 */       if (old instanceof EntityLiving) {
/* 1266 */         ((EntityLiving)old).setNoAI(false);
/* 1267 */       } else if (old != null && old.getHealth() > 0.0F) {
/* 1268 */         old.setRotationYawHead(0.0F);
/* 1269 */         old.rotationPitch = 0.0F;
/* 1270 */         old.setPositionAndUpdate(this.pos.getX() + 0.5D, (this.pos.getY() + 2), this.pos.getZ() + 0.5D);
       } 
     } 
     
/* 1274 */     return old;
   }
 
   
   protected void setWorldCreate(World worldIn) {
/* 1279 */     super.setWorldCreate(worldIn);
/* 1280 */     if (!hasWorld()) {
/* 1281 */       setWorld(worldIn);
     }
   }
 
   
   public void readSyncNBT(NBTTagCompound nbt) {
/* 1287 */     this.mode = nbt.getInteger("mode");
/* 1288 */     this.active = nbt.getBoolean("active");
/* 1289 */     this.startUp = nbt.getFloat("startUp");
/* 1290 */     this.infusing = nbt.getBoolean("infusing");
/* 1291 */     this.stability = nbt.getFloat("stability");
/* 1292 */     this.fluxStocked = nbt.getFloat("fluxStocked");
/* 1293 */     this.celestialAura = nbt.getFloat("celestialAura");
/* 1294 */     this.celestialAuraNeeded = nbt.getFloat("celestialAuraNeeded");
/* 1295 */     this.expunge = nbt.getBoolean("expunge");
/* 1296 */     setType(IsorropiaAPI.getCelestialBodyByRegistryName(new ResourceLocation(nbt.getString("type"))));
/* 1297 */     int id = nbt.getInteger("entityID");
/* 1298 */     if ((getEntityContained() == null || getEntityContained().getEntityId() != id) && id != 0 && this.world != null)
     {
/* 1300 */       setEntityContained((EntityLivingBase)this.world.getEntityByID(id)); } 
/* 1301 */     this.essentiaNeeded.readFromNBT(nbt);
   }
 
   
   public NBTTagCompound writeSyncNBT(NBTTagCompound nbt) {
/* 1306 */     nbt.setInteger("mode", getMode());
/* 1307 */     nbt.setBoolean("active", isActive());
/* 1308 */     nbt.setFloat("startUp", getStartUp());
/* 1309 */     nbt.setBoolean("infusing", isInfusing());
/* 1310 */     nbt.setFloat("stability", this.stability);
/* 1311 */     nbt.setFloat("fluxStocked", getFluxStocked());
/* 1312 */     nbt.setFloat("celestialAura", getCelestialAura());
/* 1313 */     nbt.setFloat("celestialAuraNeeded", getCelestialAuraNeeded());
/* 1314 */     nbt.setString("type", getCelestialBody().getRegistryName().toString());
/* 1315 */     nbt.setBoolean("expunge", isExpunging());
/* 1316 */     if (getEntityContained() != null)
/* 1317 */       nbt.setInteger("entityID", getEntityContained().getEntityId()); 
/* 1318 */     this.essentiaNeeded.writeToNBT(nbt);
/* 1319 */     return nbt;
   }
 
   
   public void readFromNBT(NBTTagCompound nbt) {
/* 1324 */     super.readFromNBT(nbt);
/* 1325 */     NBTTagList nbttaglist = nbt.getTagList("recipein", 10);
/* 1326 */     this.recipeIngredients = new ArrayList<>(); int i;
/* 1327 */     for (i = 0; i < nbttaglist.tagCount(); i++) {
/* 1328 */       NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
/* 1329 */       this.recipeIngredients.add(new ItemStack(nbttagcompound1));
     } 
     
/* 1332 */     nbttaglist = nbt.getTagList("stacksUsed", 10);
/* 1333 */     this.stacksUsed = NonNullList.create();
/* 1334 */     for (i = 0; i < nbttaglist.tagCount(); i++) {
/* 1335 */       NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
/* 1336 */       this.stacksUsed.add(new ItemStack(nbttagcompound1));
     } 
     
/* 1339 */     this.recipeXP = nbt.getInteger("recipexp");
/* 1340 */     UUID s = nbt.getUniqueId("recipeplayer");
/* 1341 */     if (!this.world.isRemote && s != null) {
/* 1342 */       this.recipePlayer = (EntityPlayer)FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(s);
     }
     
/* 1345 */     this.expunge = nbt.getBoolean("expunge");
     
/* 1347 */     if (nbt.hasKey("recipe")) {
/* 1348 */       this.recipe = (CurativeInfusionRecipe)IsorropiaAPI.creatureInfusionRecipes.get(new ResourceLocation(nbt.getString("recipe")));
     }
     
/* 1351 */     if (nbt.hasKey("entity")) {
/* 1352 */       this.entityUUID = nbt.getCompoundTag("entity").getUniqueId("id");
     }
   }
 
   
   public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
/* 1358 */     super.writeToNBT(nbt);
     
/* 1360 */     if (getEntityContained() != null) {
/* 1361 */       NBTTagCompound entityData = new NBTTagCompound();
       
/* 1363 */       if (getEntityContained() != null) {
/* 1364 */         entityData.setUniqueId("id", getEntityContained().getPersistentID());
       }
/* 1366 */       nbt.setTag("entity", (NBTBase)entityData);
     } 
     
/* 1369 */     if (!this.recipeIngredients.isEmpty()) {
/* 1370 */       NBTTagList nbttaglist = new NBTTagList();
/* 1371 */       Iterator<ItemStack> iterator = this.recipeIngredients.iterator();
       
/* 1373 */       while (iterator.hasNext()) {
 
         
/* 1376 */         ItemStack stack = iterator.next();
/* 1377 */         if (!stack.isEmpty()) {
/* 1378 */           NBTTagCompound stackCompound = new NBTTagCompound();
/* 1379 */           stack.writeToNBT(stackCompound);
/* 1380 */           nbttaglist.appendTag((NBTBase)stackCompound);
         } 
       } 
/* 1383 */       nbt.setTag("recipein", (NBTBase)nbttaglist);
     } 
     
/* 1386 */     if (!this.stacksUsed.isEmpty()) {
/* 1387 */       NBTTagList nbttaglist = new NBTTagList();
/* 1388 */       Iterator<ItemStack> iterator = this.stacksUsed.iterator();
       
/* 1390 */       while (iterator.hasNext()) {
 
         
/* 1393 */         ItemStack stack = iterator.next();
/* 1394 */         if (!stack.isEmpty()) {
/* 1395 */           NBTTagCompound stackCompound = new NBTTagCompound();
/* 1396 */           stack.writeToNBT(stackCompound);
/* 1397 */           nbttaglist.appendTag((NBTBase)stackCompound);
         } 
       } 
/* 1400 */       nbt.setTag("stacksUsed", (NBTBase)nbttaglist);
     } 
     
/* 1403 */     nbt.setInteger("recipexp", this.recipeXP);
/* 1404 */     if (this.recipePlayer != null) {
/* 1405 */       nbt.setUniqueId("recipeplayer", this.recipePlayer.getUniqueID());
     }
 
     
/* 1409 */     nbt.setBoolean("expunge", this.expunge);
     
/* 1411 */     if (this.recipe != null) {
/* 1412 */       nbt.setString("recipe", ((ResourceLocation)((Map.Entry)IsorropiaAPI.creatureInfusionRecipes
/* 1413 */           .entrySet().stream()
/* 1414 */           .filter(entry -> (IsorropiaAPI.creatureInfusionRecipes.get(entry.getKey()) == this.recipe))
/* 1415 */           .findFirst().get()).getKey()).toString());
     }
     
/* 1418 */     return nbt;
   }
 
   
   public void invalidate() {
/* 1423 */     super.invalidate();
/* 1424 */     if (getEntityContained() != null) {
/* 1425 */       this.world.removeEntity((Entity)this.entityContained);
     }
   }
 
   
   public boolean doesContainerAccept(Aspect paramAspect) {
/* 1431 */     return false;
   }
 
   
   public boolean takeFromContainer(Aspect paramAspect, int paramInt) {
/* 1436 */     return false;
   }
 
   
   public boolean takeFromContainer(AspectList paramAspectList) {
/* 1441 */     return false;
   }
 
   
   public boolean doesContainerContainAmount(Aspect paramAspect, int paramInt) {
/* 1446 */     return false;
   }
 
   
   public boolean doesContainerContain(AspectList paramAspectList) {
/* 1451 */     return false;
   }
 
   
   public AspectList getAspects() {
/* 1456 */     return getEssentiaNeeded();
   }
 
 
   
   public void setAspects(AspectList paramAspectList) {}
 
 
   
   public int containerContains(Aspect tag) {
/* 1466 */     return this.essentiaNeeded.getAmount(tag);
   }
   
   public Aspect getSuctionType() {
/* 1470 */     return this.currentlySucking;
   }
   
   public int addEssentia(Aspect aspect, int amount, EnumFacing face) {
/* 1474 */     return amount - addToContainer(aspect, amount);
   }
 
 
   
   public void addStability() {}
 
   
   public int getMode() {
/* 1483 */     return this.mode;
   }
   
   public void setMode(int mode) {
/* 1487 */     this.mode = mode;
   }
   
   public int getStabilityCap() {
/* 1491 */     return this.stabilityCap;
   }
   
   public void setStabilityCap(int stabilityCap) {
/* 1495 */     this.stabilityCap = stabilityCap;
   }
   
   public float getStartUp() {
/* 1499 */     return this.startUp;
   }
   
   public void setStartUp(float startUp) {
/* 1503 */     this.startUp = startUp;
   }
   
   public Map<String, SourceFX> getSourceFX() {
/* 1507 */     return this.sourceFX;
   }
   
   public void setSourceFX(Map<String, SourceFX> sourceFX) {
/* 1511 */     this.sourceFX = sourceFX;
   }
   
   public boolean isActive() {
/* 1515 */     return this.active;
   }
   
   public void setActive(boolean active) {
/* 1519 */     this.active = active;
   }
   
   public float getCostMult() {
/* 1523 */     return this.costMult;
   }
   
   public void setCostMult(float costMult) {
/* 1527 */     this.costMult = costMult;
   }
   
   public int getCycleTime() {
/* 1531 */     return this.cycleTime;
   }
   
   public void setCycleTime(int cycleTime) {
/* 1535 */     this.cycleTime = cycleTime;
   }
   
   public float getSymmetryStability() {
/* 1539 */     return this.stabilityReplenish;
   }
   
   public void setSymmetryStability(float symmetryStability) {
/* 1543 */     this.stabilityReplenish = symmetryStability;
   }
   
   public AspectList getEssentiaNeeded() {
/* 1547 */     return this.essentiaNeeded;
   }
   
   public void setEssentiaNeeded(AspectList essentiaNeeded) {
/* 1551 */     this.essentiaNeeded = essentiaNeeded;
   }
   
   public int getCount() {
/* 1555 */     return this.count;
   }
   
   public void setCount(int count) {
/* 1559 */     this.count = count;
   }
   
   public int getCraftCount() {
/* 1563 */     return this.craftCount;
   }
   
   public void setCraftCount(int craftCount) {
/* 1567 */     this.craftCount = craftCount;
   }
   
   public void setOptionalComponents(List<ItemStack> array) {
/* 1571 */     this.optionalComponents = array;
   }
   
   public List<ItemStack> getOptionalComponents() {
/* 1575 */     return this.optionalComponents;
   }
   
   public int getCountDelay() {
/* 1579 */     return this.countDelay;
   }
   
   public void setCountDelay(int countDelay) {
/* 1583 */     this.countDelay = countDelay;
   }
   
   public int getItemCount() {
/* 1587 */     return this.itemCount;
   }
   
   public void setItemCount(int itemCount) {
/* 1591 */     this.itemCount = itemCount;
   }
   
   public boolean isInfusing() {
/* 1595 */     return this.infusing;
   }
   
   public void setInfusing(boolean infusing) {
/* 1599 */     this.infusing = infusing;
   }
   
   public CurativeInfusionRecipe getRecipe() {
/* 1603 */     return this.recipe;
   }
   
   public void setRecipe(CurativeInfusionRecipe recipe) {
/* 1607 */     this.recipe = recipe;
   }
   
   public List<ItemStack> getRecipeIngredients() {
/* 1611 */     return this.recipeIngredients;
   }
   
   public void setRecipeIngredients(ArrayList<ItemStack> recipeIngredients) {
/* 1615 */     this.recipeIngredients = recipeIngredients;
   }
   
   public EntityPlayer getRecipePlayer() {
/* 1619 */     return this.recipePlayer;
   }
   
   public void setRecipePlayer(EntityPlayer recipePlayer) {
/* 1623 */     this.recipePlayer = recipePlayer;
   }
   
   public int getRecipeXP() {
/* 1627 */     return this.recipeXP;
   }
   
   public void setRecipeXP(int recipeXP) {
/* 1631 */     this.recipeXP = recipeXP;
   }
   
   public float getRawStability() {
/* 1635 */     return this.stability;
   }
   
   public void setStability(float stability) {
/* 1639 */     this.stability = stability;
   }
   
   public Aspect getCurrentlySucking() {
/* 1643 */     return this.currentlySucking;
   }
   
   public float getStabilityReplenish() {
/* 1647 */     return this.stabilityReplenish;
   }
   
   public void setStabilityReplenish(float stabilityReplenish) {
/* 1651 */     this.stabilityReplenish = stabilityReplenish;
   }
   
   public ICelestialBody getCelestialBody() {
/* 1655 */     return this.celestialBody;
   }
   
   public void setType(ICelestialBody celestialBody) {
/* 1659 */     if (!this.celestialBody.equals(celestialBody)) {
/* 1660 */       if (!this.celestialBody.equals(CelestialBody.NONE) && 
/* 1661 */         !this.celestialBody.isAuraEquals(this.recipePlayer, this.world, celestialBody))
/* 1662 */         dischargeCelestialAura(); 
/* 1663 */       this.celestialBody = celestialBody;
     } 
   }
   
   public float getCelestialAura() {
/* 1668 */     return this.celestialAura;
   }
   
   public void setCelestialAura(int celestialAura) {
/* 1672 */     this.celestialAura = celestialAura;
   }
   
   public int getCelestialEnergyCap() {
/* 1676 */     return this.celestialAuraCap;
   }
   
   public void setCelestialEnergyCap(int celestialEnergyCap) {
/* 1680 */     this.celestialAuraCap = celestialEnergyCap;
   }
   
   public float getCelestialAuraNeeded() {
/* 1684 */     return this.celestialAuraNeeded;
   }
   
   public void setCelestialAuraNeeded(int celestialAuraNeeded) {
/* 1688 */     this.celestialAuraNeeded = celestialAuraNeeded;
   }
   
   public void setCurrentlySucking(Aspect currentlySucking) {
/* 1692 */     this.currentlySucking = currentlySucking;
   }
   
   public float getTotalInstability() {
/* 1696 */     return this.totalInstability;
   }
   
   public void setTotalInstability(float totalInstability) {
/* 1700 */     this.totalInstability = totalInstability;
   }
   
   public Set<TilePedestal> getPedestals() {
/* 1704 */     return this.pedestals;
   }
   
   public void setPedestals(Set<TilePedestal> pedestals) {
/* 1708 */     this.pedestals = pedestals;
   }
   
   public float getFluxStocked() {
/* 1712 */     return this.fluxStocked;
   }
   
   public void setFluxStocked(int fluxStocked) {
/* 1716 */     this.fluxStocked = fluxStocked;
   }
   
   public UUID getEntityTag() {
/* 1720 */     return this.entityUUID;
   }
   
   public void setEntityUUID(UUID entityTag) {
/* 1724 */     this.entityUUID = entityTag;
   }
   
   public int getCelestialAuraCap() {
/* 1728 */     return this.celestialAuraCap;
   }
   
   public void setCelestialAuraCap(int celestialAuraCap) {
/* 1732 */     this.celestialAuraCap = celestialAuraCap;
   }
   
   public void setCelestialBody(ICelestialBody celestialBody) {
/* 1736 */     this.celestialBody = celestialBody;
   }
   
   public void setCelestialAura(float celestialAura) {
/* 1740 */     this.celestialAura = celestialAura;
   }
   
   public void setCelestialAuraNeeded(float celestialAuraNeeded) {
/* 1744 */     this.celestialAuraNeeded = celestialAuraNeeded;
   }
   
   public boolean isExpunging() {
/* 1748 */     return this.expunge;
   }
   
   public int getCurativeCount() {
/* 1752 */     return this.curativeCount;
   }
   
   public void setCurativeCount(int curativeCount) {
/* 1756 */     this.curativeCount = curativeCount;
   }
   
   public boolean isExpunge() {
/* 1760 */     return this.expunge;
   }
   
   public void setExpunge(boolean expunge) {
/* 1764 */     this.expunge = expunge;
   }
   
   public float getSoundExpunge() {
/* 1768 */     return this.soundExpunge;
   }
   
   public void setSoundExpunge(float soundExpunge) {
/* 1772 */     this.soundExpunge = soundExpunge;
   }
   
   public NonNullList<ItemStack> getStacksUsed() {
/* 1776 */     return this.stacksUsed;
   }
   
   public void setStacksUsed(NonNullList<ItemStack> stacksUsed) {
/* 1780 */     this.stacksUsed = stacksUsed;
   }
   
   public ICurativeEffectProvider getCurativeEffect() {
/* 1784 */     return this.curativeEffect;
   }
   
   public void setCurativeEffect(ICurativeEffectProvider curativeEffect) {
/* 1788 */     this.curativeEffect = curativeEffect;
   }
   
   public List<ItemStack> getLoots() {
/* 1792 */     return this.loots;
   }
   
   public void setLoots(List<ItemStack> loots) {
/* 1796 */     this.loots = loots;
   }
   
   public UUID getEntityUUID() {
/* 1800 */     return this.entityUUID;
   }
   
   public void setFluxStocked(float fluxStocked) {
/* 1804 */     this.fluxStocked = fluxStocked;
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\tiles\TileVat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */