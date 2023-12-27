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
 import net.minecraft.item.Item;
 import net.minecraft.item.ItemStack;
 import net.minecraft.potion.PotionEffect;
 import net.minecraft.util.DamageSource;
 import net.minecraft.util.EnumFacing;
 import net.minecraft.util.ResourceLocation;
 import net.minecraft.util.text.ITextComponent;
 import net.minecraft.util.text.TextComponentString;
 import net.minecraft.util.text.TextFormatting;
 import net.minecraft.world.IBlockAccess;
 import net.minecraft.world.World;
 import net.minecraftforge.common.IShearable;
 import net.minecraftforge.common.capabilities.ICapabilityProvider;
 import net.minecraftforge.event.AttachCapabilitiesEvent;
 import net.minecraftforge.event.entity.living.*;
 import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
 import net.minecraftforge.event.entity.player.PlayerEvent;
 import net.minecraftforge.event.entity.player.PlayerInteractEvent;
 import net.minecraftforge.fml.client.event.ConfigChangedEvent;
 import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
 import net.minecraftforge.fml.common.gameevent.TickEvent;
 import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
 import thaumcraft.api.aspects.AspectList;
 import thaumcraft.api.aspects.IEssentiaContainerItem;
 import thaumcraft.api.capabilities.IPlayerKnowledge;
 import thaumcraft.api.capabilities.ThaumcraftCapabilities;
 import thaumcraft.api.items.IRevealer;
 import thaumcraft.api.items.ItemsTC;
 import thaumcraft.api.research.ResearchEvent;
 import thaumcraft.client.fx.FXDispatcher;
 import thaumcraft.common.items.tools.ItemThaumometer;
 import thaumcraft.common.lib.research.ResearchManager;

 import java.util.ArrayList;
 import java.util.Random;
 
 
 
 
 public class EntityEventHandler
 {
    private static final ItemStack SHEARS = new ItemStack((Item)Items.SHEARS);
   private Lens theRightLens;
/*  80 */   private static Random randy = new Random();
   private Lens theLeftLens;
   
   @SubscribeEvent
   public void playerTick(TickEvent.PlayerTickEvent event) {
      if (event.phase == TickEvent.Phase.START) {
        World world = event.player.world;
        ItemStack revealer = LensManager.getRevealer(event.player);
       
        if (!revealer.isEmpty() && revealer.hasTagCompound() && revealer
          .getTagCompound().getString("LeftLens") != null) {
          this.theLeftLens = LensManager.getLens(revealer, LensManager.LENSSLOT.LEFT);
         this.theRightLens = LensManager.getLens(revealer, LensManager.LENSSLOT.RIGHT);
        boolean doubleLens = (this.theRightLens != null && this.theRightLens.equals(this.theLeftLens));
 
         
/*  96 */         if (this.theLeftLens != null) {
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
     Entity entity = (Entity)event.getObject();
      if (entity instanceof EntityLiving) {
        event.addCapability(new ResourceLocation("isorropia", "LIVING_CAPABILITY"), (ICapabilityProvider)new LivingCapability((EntityLiving)event
/* 112 */             .getObject()));
   } else if (entity instanceof EntityLivingBase) {
/* 114 */       event.addCapability(new ResourceLocation("isorropia", "LIVING_BASE_CAPABILITY"), (ICapabilityProvider)new LivingBaseCapability((EntityLivingBase)event
/* 115 */             .getObject()));
     } 
   }
 
 
 
   
   @SubscribeEvent
   public void onRevealerRemoved(LivingEquipmentChangeEvent event) {
      ItemStack stack = event.getFrom();
     
/* 126 */     if (!(event.getEntityLiving() instanceof EntityPlayer))
       return; 
/* 128 */     if (stack.getItem() instanceof IRevealer && ((IRevealer)stack
/* 129 */       .getItem()).showNodes(stack, event.getEntityLiving())) {
/* 130 */       Lens lens = LensManager.getLens(stack, LensManager.LENSSLOT.LEFT);
/* 131 */       Lens lens2 = LensManager.getLens(stack, LensManager.LENSSLOT.RIGHT);
       
       if (lens != null)
/* 134 */         lens.handleRemoval((event.getEntityLiving()).world, (EntityPlayer)event.getEntityLiving()); 
/* 135 */       if (lens2 != null)
/* 136 */         lens2.handleRemoval((event.getEntityLiving()).world, (EntityPlayer)event.getEntityLiving()); 
/* 137 */       Common.INSTANCE.sendTo((IMessage)new LensRemoveMessage(lens, lens2), (EntityPlayerMP)event.getEntityLiving());
     } 
   }
   
   @SubscribeEvent
   public void onRevealerDestroy(PlayerDestroyItemEvent event) {
/* 143 */     ItemStack stack = event.getOriginal();
/* 144 */     EntityPlayer player = event.getEntityPlayer();
     
/* 146 */     if (stack.getItem() instanceof IRevealer && ((IRevealer)stack
/* 147 */       .getItem()).showNodes(stack, event.getEntityLiving())) {
/* 148 */       Lens lens = LensManager.getLens(stack, LensManager.LENSSLOT.LEFT);
/* 149 */       Lens lens2 = LensManager.getLens(stack, LensManager.LENSSLOT.RIGHT);
       
/* 151 */       if (lens != null && 
/* 152 */         !player.inventory.addItemStackToInventory(new ItemStack((Item)lens.getItemLens())))
/* 153 */         player.dropItem(new ItemStack((Item)lens.getItemLens()), false); 
/* 154 */       if (lens2 != null && 
/* 155 */         !player.inventory.addItemStackToInventory(new ItemStack((Item)lens.getItemLens()))) {
/* 156 */         player.dropItem(new ItemStack((Item)lens.getItemLens()), false);
       }
     } 
   }
 
 
 
 
 
 
 
 
 
   
   @SubscribeEvent
   public void onLivingDeath(LivingDeathEvent event) {}
 
 
 
 
 
 
 
 
   
   @SubscribeEvent
   public void onLivingUpdateEvent(LivingEvent.LivingUpdateEvent event) {
/* 183 */     EntityLivingBase entity = event.getEntityLiving();
/* 184 */     LivingBaseCapability cap = Common.getCap(entity);
     
/* 186 */     cap.update();
/* 187 */     if (entity.isInsideOfMaterial(MaterialIR.LIQUID_VAT)) {
/* 188 */       entity.setAir(300);
     }
/* 190 */     if (!entity.world.isRemote) {
/* 191 */       if (entity instanceof IShearable) {
/* 192 */         IShearable shear = (IShearable)entity;
         
/* 194 */         if (entity.ticksExisted % 100 == 0 && cap.infusions.containsKey(new ResourceLocation("isorropia:selfshearing")) && shear.isShearable(SHEARS, (IBlockAccess)entity.world, entity.getPosition())) {
/* 195 */           ArrayList<ItemStack> drops = (ArrayList<ItemStack>)shear.onSheared(SHEARS, (IBlockAccess)entity.world, entity
/* 196 */               .getPosition(), 0);
/* 197 */           Random rand = new Random();
/* 198 */           for (ItemStack stack : drops) {
             
/* 200 */             EntityItem entityDropItem = entity.entityDropItem(stack, 1.0F), ent = entityDropItem;
/* 201 */             entityDropItem.motionY += (rand.nextFloat() * 0.05F);
/* 202 */             EntityItem entityItem = ent;
/* 203 */             entityItem.motionX += ((rand.nextFloat() - rand.nextFloat()) * 0.1F);
/* 204 */             EntityItem entityItem2 = ent;
/* 205 */             entityItem2.motionZ += ((rand.nextFloat() - rand.nextFloat()) * 0.1F);
           } 
         } 
       } 
/* 209 */       if (entity.ticksExisted % 50 == 0 && cap.entityHasOrgan(OrganCurativeInfusionRecipe.Organ.BLOOD, "isorropia:awakened_blood") && 
/* 210 */         entity.getHealth() < entity.getMaxHealth())
/* 211 */         entity.heal(1.0F); 
     } 
   }
   
   @SubscribeEvent
   public void onStartTrack(PlayerEvent.StartTracking event) {
/* 217 */     if (event.getTarget() instanceof EntityLivingBase) {
/* 218 */       Common.getCap((EntityLivingBase)event.getTarget())
/* 219 */         .synchStartTracking((EntityPlayerMP)event.getEntityPlayer());
     }
   }
   
   @SubscribeEvent
   public void onPlayerJoin(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event) {
/* 225 */     Common.getCap((EntityLivingBase)event.player).synchStartTracking((EntityPlayerMP)event.player);
   }
   
   @SubscribeEvent
   public void onPlayerBlockInterract(PlayerInteractEvent.RightClickBlock event) {
/* 256 */     ItemStack stack = event.getEntityPlayer().getHeldItem(event.getHand());
/* 257 */     World world = event.getWorld();
/* 258 */     if (!world.isRemote && stack.getItem() == ItemsTC.label && 
/* 259 */       ThaumcraftCapabilities.getKnowledge(event.getEntityPlayer()).isResearchKnown("HANGINGLABEL") && 
/* 260 */       !(world.getBlockState(event.getPos()).getBlock() instanceof thaumcraft.api.blocks.ILabelable) && event
/* 261 */       .getFace() != EnumFacing.DOWN && event.getFace() != EnumFacing.UP && event.getEntityPlayer()
/* 262 */       .canPlayerEdit(event.getPos().offset(event.getFace()), event.getFace(), stack)) {
/* 263 */       AspectList aspects = ((IEssentiaContainerItem)stack.getItem()).getAspects(stack);
 
       
/* 266 */       EntityHangingLabel entityMessage = new EntityHangingLabel(world, true, event.getPos().offset(event.getFace()), event.getFace(), (aspects != null) ? aspects.getAspects()[0] : null);
       
/* 268 */       if (entityMessage.onValidSurface()) {
/* 269 */         entityMessage.playPlaceSound();
/* 270 */         world.spawnEntity((Entity)entityMessage);
/* 271 */         stack.shrink(1);
       } 
     } 
   }
   
   @SubscribeEvent
   public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
/* 278 */     ItemStack stack = event.getItemStack();
     
/* 280 */     if (event.getEntityPlayer() != null && event.getTarget() instanceof EntityZombieVillager) {
/* 281 */       EntityZombieVillager entity = (EntityZombieVillager)event.getTarget();
       
/* 283 */       if (stack.getItem() == Items.GOLDEN_APPLE && stack.getMetadata() == 0 && entity
/* 284 */         .isPotionActive(MobEffects.WEAKNESS) && !entity.isConverting()) {
/* 285 */         IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(event.getEntityPlayer());
/* 286 */         if (!knowledge.isResearchKnown("!experiment.villager_zombie")) {
/* 287 */           knowledge.addResearch("!experiment.villager_zombie");
/* 288 */           knowledge.sync((EntityPlayerMP)event.getEntity());
/* 289 */           ((EntityPlayer)event.getEntity()).sendStatusMessage((ITextComponent)new TextComponentString(TextFormatting.DARK_PURPLE + 
                 
/* 291 */                 I18n.format("research.experiment.villager_zombie.text", new Object[0])), false);
         } 
       } 
     } 
   }
 
 
   
   @SubscribeEvent
   public void onItemRightClick(PlayerInteractEvent.RightClickItem event) {
/* 301 */     ItemStack stack = event.getItemStack();
     
/* 303 */     if (!(event.getWorld()).isRemote && (stack.getItem() instanceof IRevealer || stack.getItem() instanceof thaumcraft.api.items.IGoggles) && stack
/* 304 */       .hasTagCompound()) {
/* 305 */       String lens = stack.getTagCompound().getString(LensManager.LENSSLOT.LEFT.getName());
       
/* 307 */       if (!lens.equals("isorropia:ordo_lens")) {
/* 308 */         lens = stack.getTagCompound().getString(LensManager.LENSSLOT.RIGHT.getName());
       }
       
/* 311 */       if (lens.equals("isorropia:ordo_lens")) {
/* 312 */         ((ItemThaumometer)ItemsTC.thaumometer).doScan(event.getWorld(), event.getEntityPlayer());
       }
     } 
   }
   
   @SubscribeEvent
   public void onAttack(LivingAttackEvent event) {
/* 319 */     EntityLivingBase base = event.getEntityLiving();
/* 320 */     DamageSource source = event.getSource();
/* 321 */     LivingBaseCapability cap = Common.getCap(base);
     
/* 323 */     if (cap.entityHasOrgan(OrganCurativeInfusionRecipe.Organ.HEART, "isorropia:enderheart") && !base.isEntityInvulnerable(source) && source instanceof net.minecraft.util.EntityDamageSourceIndirect) {
/* 324 */       base.world.playSound((EntityPlayer)null, base.prevPosX, base.prevPosY, base.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, base.getSoundCategory(), 1.0F, 1.0F);
/* 325 */       base.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
/* 326 */       base.attemptTeleport(base.posX, base.posY, base.posZ);
/* 327 */       base.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 3));
/* 328 */       event.setCanceled(true);
     } 
   }
   
   @SubscribeEvent
   public void onAttack(LivingDamageEvent event) {
/* 334 */     EntityLivingBase base = event.getEntityLiving();
/* 335 */     LivingBaseCapability cap = Common.getCap(base);
/* 336 */     Entity target = event.getSource().getTrueSource();
     
/* 338 */     if (target instanceof EntityLivingBase && cap.entityHasOrgan(OrganCurativeInfusionRecipe.Organ.SKIN, "isorropia:shockskin")) {
/* 339 */       FXDispatcher.INSTANCE.arcLightning(base.posX, base.posY, base.posZ, target.posX, target.posY, target.posZ, 0.2F, 0.8F, 0.8F, 1.0F);
/* 340 */       target.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 4.0F);
     } 
   }
 
 
   
   @SubscribeEvent
   public void onPlayerResearch(ResearchEvent.Research event) {}
 
 
   
   private boolean hardCodedAntiRecursive = false;
 
   
   @SubscribeEvent
   public void onPlayerGetKnowledge(ResearchEvent.Knowledge event) {
/* 356 */     ItemStack stack = event.getPlayer().getItemStackFromSlot(EntityEquipmentSlot.HEAD);
     
/* 358 */     if (stack.getItem() != ItemsIS.itemSomaticBrain) {
/* 359 */       stack = BaublesApi.getBaublesHandler(event.getPlayer()).getStackInSlot(4);
     }
     
/* 362 */     if (stack.getItem() == ItemsIS.itemSomaticBrain && !this.hardCodedAntiRecursive) {
/* 363 */       this.hardCodedAntiRecursive = true;
/* 364 */       ResearchManager.addKnowledge(event.getPlayer(), event.getType(), event.getCategory(), 
/* 365 */           (int)Math.round(event.getAmount() * ((event.getType() == IPlayerKnowledge.EnumKnowledgeType.THEORY) ? 0.3D : 0.6D)));
/* 366 */       this.hardCodedAntiRecursive = false;
     } 
   }
   
   @SubscribeEvent
   public void onExperienceDrop(LivingExperienceDropEvent event) {
/* 372 */     EntityPlayer player = event.getAttackingPlayer();
     
/* 374 */     if (player != null) {
/* 375 */       ItemStack stack = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
       
/* 377 */       if (stack.getItem() != ItemsIS.itemSomaticBrain) {
/* 378 */         stack = BaublesApi.getBaublesHandler(player).getStackInSlot(4);
       }
/* 380 */       if (stack.getItem() == ItemsIS.itemSomaticBrain) {
/* 381 */         event.setDroppedExperience(event.getDroppedExperience() * 2);
       }
     } 
   }
   
   public void addDrop(LivingDropsEvent event, ItemStack drop) {
/* 387 */     EntityItem entityitem = new EntityItem((event.getEntityLiving()).world, (event.getEntityLiving()).posX, (event.getEntityLiving()).posY, (event.getEntityLiving()).posZ, drop);
/* 388 */     entityitem.setPickupDelay(10);
/* 389 */     event.getDrops().add(entityitem);
   }
   
   @SubscribeEvent
   public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
/* 394 */     if (eventArgs.getModID().equals("Thaumic Isorropia")) {
/* 395 */       Config.syncConfigurable();
/* 396 */       if (Config.config != null && Config.config.hasChanged())
/* 397 */         Config.save(); 
     } 
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\events\EntityEventHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */