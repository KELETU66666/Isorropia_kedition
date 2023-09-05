 package fr.wind_blade.isorropia.client.libs;
 
 import fr.wind_blade.isorropia.client.model.DynamicStaticModel;
 import fr.wind_blade.isorropia.common.Common;
 import fr.wind_blade.isorropia.common.IsorropiaAPI;
 import fr.wind_blade.isorropia.common.blocks.BlocksIS;
 import fr.wind_blade.isorropia.common.capabilities.LivingBaseCapability;
 import fr.wind_blade.isorropia.common.events.KeyHandler;
 import fr.wind_blade.isorropia.common.items.misc.ItemLens;
 import fr.wind_blade.isorropia.common.lenses.Lens;
 import fr.wind_blade.isorropia.common.lenses.LensManager;
 import fr.wind_blade.isorropia.common.network.LensChangeMessage;
 import fr.wind_blade.isorropia.common.research.recipes.CurativeInfusionRecipe;
 import java.lang.reflect.Field;
 import java.lang.reflect.Method;
 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.HashMap;
 import java.util.LinkedHashMap;
 import java.util.List;
 import java.util.TreeMap;
 import net.minecraft.block.Block;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.entity.EntityPlayerSP;
 import net.minecraft.client.gui.GuiScreen;
 import net.minecraft.client.multiplayer.WorldClient;
 import net.minecraft.client.renderer.BufferBuilder;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.RenderHelper;
 import net.minecraft.client.renderer.RenderItem;
 import net.minecraft.client.renderer.Tessellator;
 import net.minecraft.client.renderer.block.model.IBakedModel;
 import net.minecraft.client.renderer.block.model.ModelResourceLocation;
 import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
 import net.minecraft.client.resources.I18n;
 import net.minecraft.client.util.ITooltipFlag;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.item.Item;
 import net.minecraft.item.ItemStack;
 import net.minecraft.util.ResourceLocation;
 import net.minecraft.util.math.MathHelper;
 import net.minecraft.world.World;
 import net.minecraftforge.client.event.GuiScreenEvent;
 import net.minecraftforge.client.event.ModelBakeEvent;
 import net.minecraftforge.client.event.RenderGameOverlayEvent;
 import net.minecraftforge.client.event.RenderHandEvent;
 import net.minecraftforge.client.event.RenderLivingEvent;
 import net.minecraftforge.client.event.RenderWorldLastEvent;
 import net.minecraftforge.event.entity.player.ItemTooltipEvent;
 import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
 import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
 import net.minecraftforge.fml.relauncher.ReflectionHelper;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 import org.lwjgl.input.Mouse;
 import org.lwjgl.opengl.Display;
 import org.lwjgl.opengl.GL11;
 import thaumcraft.client.gui.GuiResearchBrowser;
 import thaumcraft.client.gui.GuiResearchPage;
 import thaumcraft.client.lib.UtilsFX;
 
 
 
 
 @SideOnly(Side.CLIENT)
 public class RenderEventHandler
 {
/*  69 */   public static final ResourceLocation LIGO_TEX = new ResourceLocation("isorropia", "textures/misc/ligo.png");
   
/*  71 */   public static IBakedModel jar_soul = null;
   
   private static Lens theRightLens;
/*  74 */   private static final fociHUD fociLeft = new fociHUD(LensManager.LENSSLOT.LEFT); private static Lens theLeftLens; private static float radialHudScale;
/*  75 */   private static final fociHUD fociRight = new fociHUD(LensManager.LENSSLOT.RIGHT);
   
/*  77 */   public static ResourceLocation TEX_VIS = new ResourceLocation("thaumcraft", "textures/gui/gui_researchbook_overlay.png");
   
/*  79 */   public static ResourceLocation TEX_PAR = new ResourceLocation("thaumcraft", "textures/misc/particles.png");
/*  80 */   ResourceLocation tex4 = new ResourceLocation("thaumcraft", "textures/gui/paper.png");
/*  81 */   public static List<String> texts = Arrays.asList("wandtable.text1");
   
   public static Method tip;
   
   @SubscribeEvent
   public static void onModelBakeEvent(ModelBakeEvent event) {
       ModelResourceLocation mrl = new ModelResourceLocation(BlocksIS.blockJarSoul.getRegistryName(), "inventory");
       jar_soul = event.getModelRegistry().getObject(mrl);
       event.getModelRegistry().putObject(mrl, new DynamicStaticModel(jar_soul, Item.getItemFromBlock(BlocksIS.blockJarSoul)));
   }

   @SubscribeEvent
   public static void onOverlay(RenderGameOverlayEvent.Pre event) {
       if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
           fociRight.handleFociRadial(Minecraft.getMinecraft(), System.nanoTime() / 1000000L, event);
           fociLeft.handleFociRadial(Minecraft.getMinecraft(), System.nanoTime() / 1000000L, event);
     }
     if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
         WorldClient worldClient = (Minecraft.getMinecraft()).world;
         EntityPlayerSP entityPlayerSP = (Minecraft.getMinecraft()).player;
         ItemStack revealer = LensManager.getRevealer(entityPlayerSP);

         if (!revealer.isEmpty() && revealer.hasTagCompound() && revealer.getTagCompound().getString("LeftLens") != null) {
             theLeftLens = LensManager.getLens(revealer, LensManager.LENSSLOT.LEFT);
             theRightLens = LensManager.getLens(revealer, LensManager.LENSSLOT.RIGHT);
             boolean doubleLens = (theRightLens != null && theRightLens.equals(theLeftLens));
             if (theLeftLens != null) {
                 theLeftLens.handleRenderGameOverlay(worldClient, entityPlayerSP, event.getResolution(), doubleLens, event
                         .getPartialTicks());
             }
             if (!doubleLens && theRightLens != null) {
                 theRightLens.handleRenderGameOverlay(worldClient, entityPlayerSP, event.getResolution(), false, event
                         .getPartialTicks());
         }
       }
       LivingBaseCapability cap = Common.getCap(entityPlayerSP);
if (cap.petrification > 0) {
    float flag = 1.0F - (100.0F - cap.petrification) / 100.0F;
    float width = (float)event.getResolution().getScaledWidth_double();
    float height = (float)event.getResolution().getScaledHeight_double();
    float f2 = height / 34.0F;
    float f3 = width / 34.0F;
    GlStateManager.pushMatrix();
    GlStateManager.blendFunc(770, 771);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, flag);
    (Minecraft.getMinecraft()).renderEngine
            .bindTexture(new ResourceLocation("textures/blocks/cobblestone.png"));
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder buffer = tessellator.getBuffer();
    buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
    buffer.color(1.0F, 1.0F, 1.0F, 0.1F);
    buffer.pos(0.0D, f2, 1.0D).tex(0.0D, height).endVertex();
    buffer.pos(f3, f2, 1.0D).tex(width, height).endVertex();
    buffer.pos(f3, 0.0D, 1.0D).tex(width, 0.0D).endVertex();
    buffer.pos(0.0D, 0.0D, 1.0D).tex(0.0D, 0.0D).endVertex();
    tessellator.draw();
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    GlStateManager.popMatrix();
       } 
     } 
   }
   
   @SideOnly(Side.CLIENT)
   @SubscribeEvent
   public static void onDrawGui(GuiScreenEvent.DrawScreenEvent event) {
       GuiScreen screen = event.getGui();
       if (screen instanceof GuiResearchPage) {
           Field index = ReflectionHelper.findField(GuiResearchPage.class, "recipePage");
           Field recipes = ReflectionHelper.findField(GuiResearchPage.class, "recipeLists");
           Field shown = ReflectionHelper.findField(GuiResearchPage.class, "shownRecipe");
       
       try {
/* 173 */         int recipePage = ((Integer)index.get(screen)).intValue();
/* 174 */         LinkedHashMap maps = (LinkedHashMap)recipes.get(screen);
/* 175 */         ResourceLocation loc = (ResourceLocation)shown.get(screen);
         
/* 177 */         if (maps == null || loc == null) {
           return;
         }
/* 180 */         List<Object> list = (List<Object>)maps.get(loc);
         
/* 182 */         if (list != null && !list.isEmpty()) {
/* 183 */           Object recipe = list.get(recipePage % list.size());
           
/* 185 */           if (recipe instanceof CurativeInfusionRecipe) {
/* 186 */             drawCurativeInfusionRecipe((CurativeInfusionRecipe)recipe, event);
           }
         }
       
/* 190 */       } catch (IllegalArgumentException|IllegalAccessException e) {
/* 191 */         e.printStackTrace();
       } 
     } 
   }
   
   @SubscribeEvent
   public static void onToolTip(ItemTooltipEvent event) {
/* 198 */     ArrayList<String> tooltip = (ArrayList<String>)event.getToolTip();
/* 199 */     ItemStack stack = event.getItemStack();
     
/* 201 */     if (stack.hasTagCompound() && (stack.getItem() instanceof thaumcraft.api.items.IGoggles || stack.getItem() instanceof thaumcraft.api.items.IRevealer)) {
/* 202 */       String lens = stack.getTagCompound().getString(LensManager.LENSSLOT.LEFT.getName());
       
/* 204 */       if (!lens.isEmpty()) {
/* 205 */         tooltip.add(1, "§a" + I18n.format("lens." + IsorropiaAPI.lensRegistry
/* 206 */               .getValue(new ResourceLocation(lens)).getTranslationKey()));
       }
       
/* 209 */       lens = stack.getTagCompound().getString(LensManager.LENSSLOT.RIGHT.getName());
       
/* 211 */       if (!lens.isEmpty()) {
/* 212 */         tooltip.add(1, "§a" + I18n.format("lens." + IsorropiaAPI.lensRegistry
/* 213 */               .getValue(new ResourceLocation(lens)).getTranslationKey()));
       }
     } 
   }
   
   public static void drawCurativeInfusionRecipe(CurativeInfusionRecipe recipe, GuiScreenEvent.DrawScreenEvent event) {
/* 219 */     GuiScreen screen = event.getGui();
     
/* 221 */     int x = (screen.width - 256) / 2 + 128;
/* 222 */     int y = (screen.height - 256) / 2 + 128;
     
/* 224 */     if (recipe.getVis() > 0.0F) {
/* 225 */       GL11.glPushMatrix();
/* 226 */       GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 227 */       String text = String.valueOf((int) recipe.getVis());
/* 228 */       int offset = (Minecraft.getMinecraft()).fontRenderer.getStringWidth(text);
/* 229 */       (Minecraft.getMinecraft()).fontRenderer.drawString(text, x - offset / 2 + 70, y - 45, 5263440);
/* 230 */       (Minecraft.getMinecraft()).renderEngine.bindTexture(TEX_VIS);
/* 231 */       GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.4F);
/* 232 */       GL11.glTranslatef(x, (y - 55), 0.0F);
/* 233 */       GL11.glScalef(2.0F, 2.0F, 1.0F);
/* 234 */       screen.drawTexturedModalRect(30, 0, 68, 76, 12, 12);
/* 235 */       GL11.glPopMatrix();
     } 
     
/* 238 */     if (recipe.getCelestialAura() != 0) {
/* 239 */       GL11.glPushMatrix();
/* 240 */       (Minecraft.getMinecraft()).renderEngine.bindTexture(recipe.getCelestialBody().getTex());
/* 241 */       GL11.glTranslatef((x + 56), (y - 17), 0.0F);
/* 242 */       GL11.glScalef(0.12F, 0.12F, 1.0F);
/* 243 */       GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7F);
/* 244 */       screen.drawTexturedModalRect(30, 0, 46, 45, 161, 162);
/* 245 */       GL11.glPopMatrix();
     } 
     
/* 248 */     if (recipe.getFluxRejection() > 0.0F) {
/* 249 */       GuiResearchBrowser.drawForbidden(x, (y - 70));
     }
   }
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
   
   @SideOnly(Side.CLIENT)
   @SubscribeEvent
   public static void renderLast(RenderWorldLastEvent event) {
/* 276 */     EntityPlayerSP player = (Minecraft.getMinecraft()).player;
/* 277 */     ItemStack revealer = LensManager.getRevealer(player);
     
/* 279 */     if (!revealer.isEmpty() && revealer.hasTagCompound() && revealer
/* 280 */       .getTagCompound().getString("LeftLens") != null) {
/* 281 */       theLeftLens = LensManager.getLens(revealer, LensManager.LENSSLOT.LEFT);
/* 282 */       theRightLens = LensManager.getLens(revealer, LensManager.LENSSLOT.RIGHT);
/* 283 */       boolean doubleLens = (theRightLens != null && theRightLens.equals(theLeftLens));
 
       
/* 286 */       if (theLeftLens != null) {
/* 287 */         theLeftLens.handleRenderWorldLast(player.world, player, doubleLens, event.getPartialTicks());
       }
/* 289 */       if (!doubleLens && theRightLens != null) {
/* 290 */         theRightLens.handleRenderWorldLast(player.world, player, false, event.getPartialTicks());
       }
     } 
   }
 
 
 
 
 
   
   @SideOnly(Side.CLIENT)
   @SubscribeEvent
   public static void renderPlayerEvent(RenderHandEvent event) {}
 
 
 
 
   
   @SideOnly(Side.CLIENT)
   @SubscribeEvent
   public static void preRenderLiving(RenderLivingEvent.Pre<EntityLivingBase> event) {}
 
 
 
 
   
   @SideOnly(Side.CLIENT)
   @SubscribeEvent
   public static void postRenderLiving(RenderLivingEvent.Post<EntityLivingBase> event) {}
 
 
 
 
   
   protected static float interpolateRotation(float prevYawOffset, float yawOffset, float partialTicks) {
     float f;
/* 326 */     for (f = yawOffset - prevYawOffset; f < -180.0F; f += 360.0F);
 
 
     
/* 330 */     while (f >= 180.0F) {
/* 331 */       f -= 360.0F;
     }
     
/* 334 */     return prevYawOffset + partialTicks * f;
   }
   
   public static class fociHUD
   {
/* 339 */     private static final ResourceLocation radial = new ResourceLocation("thaumcraft", "textures/misc/radial.png");
/* 340 */     private static final ResourceLocation radial2 = new ResourceLocation("thaumcraft", "textures/misc/radial2.png");
/* 341 */     static final TreeMap<ResourceLocation, Integer> foci = new TreeMap<>();
/* 342 */     static final HashMap<ResourceLocation, ItemStack> lensStack = new HashMap<>();
     HashMap<ResourceLocation, Boolean> lensHover;
/* 344 */     HashMap<ResourceLocation, Float> fociScale = new HashMap<>();
/* 345 */     static final HashMap<ResourceLocation, Integer> lensSlot = new HashMap<>();
     final LensManager.LENSSLOT type;
     long lastTime;
     boolean lastState;
     
     public fociHUD(LensManager.LENSSLOT type) {
/* 351 */       this.lensHover = new HashMap<>();
/* 352 */       this.type = type;
/* 353 */       this.lastTime = 0L;
/* 354 */       this.lastState = false;
     }
     
     @SideOnly(Side.CLIENT)
     public void handleFociRadial(Minecraft mc, long time, RenderGameOverlayEvent event) {
/* 359 */       if (KeyHandler.radialActive || RenderEventHandler.radialHudScale > 0.0F) {
/* 360 */         if (KeyHandler.radialActive) {
/* 361 */           if (mc.currentScreen != null) {
/* 362 */             KeyHandler.radialActive = false;
/* 363 */             KeyHandler.radialLock = true;
/* 364 */             mc.setIngameFocus();
/* 365 */             mc.setIngameNotInFocus();
             return;
           } 
/* 368 */           if (RenderEventHandler.radialHudScale == 0.0F) {
/* 369 */             foci.clear();
/* 370 */             lensStack.clear();
/* 371 */             this.lensHover.clear();
/* 372 */             this.fociScale.clear();
/* 373 */             lensSlot.clear();
/* 374 */             ItemStack item = null;
 
 
 
 
 
 
 
 
 
 
 
 
 
             
/* 389 */             for (int a = 0; a < mc.player.inventory.mainInventory.size(); a++) {
/* 390 */               item = mc.player.inventory.mainInventory.get(a);
/* 391 */               if (!item.isEmpty() && item.getItem() instanceof ItemLens) {
/* 392 */                 ItemLens lens = (ItemLens)item.getItem();
/* 393 */                 foci.put(lens.getLens().getRegistryName(), Integer.valueOf(a));
/* 394 */                 lensStack.put(lens.getLens().getRegistryName(), item.copy());
/* 395 */                 this.fociScale.put(lens.getLens().getRegistryName(), Float.valueOf(1.0F));
/* 396 */                 this.lensHover.put(lens.getLens().getRegistryName(), Boolean.valueOf(false));
/* 397 */                 lensSlot.put(lens.getLens().getRegistryName(), Integer.valueOf(a));
               } 
 
 
 
 
 
 
 
 
               
/* 408 */               if (foci.size() > 0 && mc.inGameHasFocus) {
/* 409 */                 mc.inGameHasFocus = false;
/* 410 */                 mc.mouseHelper.ungrabMouseCursor();
               } 
             } 
           } 
/* 414 */         } else if (mc.currentScreen == null && this.lastState) {
/* 415 */           if (Display.isActive() && !mc.inGameHasFocus) {
/* 416 */             mc.inGameHasFocus = true;
/* 417 */             mc.mouseHelper.grabMouseCursor();
           } 
/* 419 */           this.lastState = false;
         } 
/* 421 */         renderFocusRadialHUD(event.getResolution().getScaledWidth_double(), event
/* 422 */             .getResolution().getScaledHeight_double(), time, event.getPartialTicks());
/* 423 */         if (time > this.lastTime) {
/* 424 */           for (ResourceLocation key : this.lensHover.keySet()) {
/* 425 */             if (this.lensHover.get(key).booleanValue()) {
/* 426 */               if (!KeyHandler.radialActive && !KeyHandler.radialLock) {
/* 427 */                 if (lensSlot.containsKey(key)) {
/* 428 */                   Lens lens = IsorropiaAPI.lensRegistry.getValue(key);
/* 429 */                   LensManager.putLens((Minecraft.getMinecraft()).player, lens, this.type);
/* 430 */                   Common.INSTANCE
/* 431 */                     .sendToServer(new LensChangeMessage(lens, lensSlot.get(key).intValue(), this.type));
                 } 
/* 433 */                 KeyHandler.radialLock = true;
               } 
/* 435 */               if (this.fociScale.get(key).floatValue() >= 1.3F) {
                 continue;
               }
/* 438 */               this.fociScale.put(key, Float.valueOf(this.fociScale.get(key).floatValue() + 0.025F)); continue;
             } 
/* 440 */             if (this.fociScale.get(key).floatValue() <= 1.0F) {
               continue;
             }
/* 443 */             this.fociScale.put(key, Float.valueOf(this.fociScale.get(key).floatValue() - 0.025F));
           } 
 
           
/* 447 */           if (!KeyHandler.radialActive) {
/* 448 */             RenderEventHandler.radialHudScale = RenderEventHandler.radialHudScale - 0.05F;
/* 449 */           } else if (RenderEventHandler.radialHudScale < 1.0F) {
/* 450 */             RenderEventHandler.radialHudScale = Math.min(RenderEventHandler.radialHudScale = RenderEventHandler.radialHudScale + 0.05F, 1.0F);
           } 
           
/* 453 */           if (RenderEventHandler.radialHudScale < 0.0F) {
/* 454 */             RenderEventHandler.radialHudScale = 0.0F;
/* 455 */             KeyHandler.radialLock = false;
           } 
           
/* 458 */           this.lastTime = time + 5L;
/* 459 */           this.lastState = KeyHandler.radialActive;
         } 
       } 
     }
     
     public void renderFocusRadialHUD(double sw, double sh, long time, float partialTicks) {
/* 465 */       Minecraft mc = Minecraft.getMinecraft();
/* 466 */       RenderItem ri = Minecraft.getMinecraft().getRenderItem();
/* 467 */       ItemStack goggles = LensManager.getRevealer(mc.player);
/* 468 */       if (goggles == null)
         return; 
/* 470 */       Lens lens = null;
/* 471 */       if (goggles.getTagCompound() != null)
       {
/* 473 */         lens = IsorropiaAPI.lensRegistry.getValue(new ResourceLocation(goggles.getTagCompound().getString(this.type.getName()))); }
/* 474 */       int i = (int)(Mouse.getEventX() * sw / mc.displayWidth);
/* 475 */       int j = (int)(sh - Mouse.getEventY() * sh / mc.displayHeight - 1.0D);
/* 476 */       int k = Mouse.getEventButton();
/* 477 */       if (lensStack.size() == 0) {
         return;
       }
/* 480 */       GL11.glPushMatrix();
/* 481 */       GL11.glClear(256);
/* 482 */       GL11.glMatrixMode(5889);
/* 483 */       GL11.glLoadIdentity();
/* 484 */       GL11.glOrtho(0.0D, sw, sh, 0.0D, 1000.0D, 3000.0D);
/* 485 */       GL11.glMatrixMode(5888);
/* 486 */       GL11.glLoadIdentity();
/* 487 */       GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
/* 488 */       GL11.glDisable(2929);
/* 489 */       GL11.glDepthMask(false);
/* 490 */       GL11.glPushMatrix();
/* 491 */       GL11.glTranslated(sw / 2.0D, sh / 2.0D, 0.0D);
/* 492 */       ItemStack tt = null;
/* 493 */       float width = 16.0F + lensStack.size() * 2.5F;
/* 494 */       GL11.glTranslatef(this.type.getAngle(), 0.0F, 0.0F);
/* 495 */       mc.renderEngine.bindTexture(radial);
/* 496 */       GL11.glPushMatrix();
/* 497 */       if (this.type.equals(LensManager.LENSSLOT.LEFT)) {
/* 498 */         GL11.glRotatef(-(partialTicks + (mc.player.ticksExisted % 720) / 2.0F), 0.0F, 0.0F, 1.0F);
       } else {
/* 500 */         GL11.glRotatef(partialTicks + (mc.player.ticksExisted % 720) / 2.0F, 0.0F, 0.0F, 1.0F);
/* 501 */       }  GL11.glAlphaFunc(516, 0.003921569F);
/* 502 */       GL11.glEnable(3042);
/* 503 */       GL11.glBlendFunc(770, 771);
/* 504 */       UtilsFX.renderQuadCentered(1, 1, 0, width * 2.75F * RenderEventHandler.radialHudScale, 0.5F, 0.5F, 0.5F, 200, 771, 0.5F);
/* 505 */       GL11.glDisable(3042);
/* 506 */       GL11.glAlphaFunc(516, 0.1F);
/* 507 */       GL11.glPopMatrix();
/* 508 */       mc.renderEngine.bindTexture(radial2);
/* 509 */       GL11.glPushMatrix();
/* 510 */       if (this.type.equals(LensManager.LENSSLOT.LEFT)) {
/* 511 */         GL11.glRotatef(partialTicks + (mc.player.ticksExisted % 720) / 2.0F, 0.0F, 0.0F, 1.0F);
       } else {
/* 513 */         GL11.glRotatef(-(partialTicks + (mc.player.ticksExisted % 720) / 2.0F), 0.0F, 0.0F, 1.0F);
/* 514 */       }  GL11.glAlphaFunc(516, 0.003921569F);
/* 515 */       GL11.glEnable(3042);
/* 516 */       GL11.glBlendFunc(770, 771);
/* 517 */       UtilsFX.renderQuadCentered(1, 1, 0, width * 2.55F * RenderEventHandler.radialHudScale, 0.5F, 0.5F, 0.5F, 200, 771, 0.5F);
/* 518 */       GL11.glDisable(3042);
/* 519 */       GL11.glAlphaFunc(516, 0.1F);
/* 520 */       GL11.glPopMatrix();
/* 521 */       if (lens != null) {
/* 522 */         GL11.glPushMatrix();
/* 523 */         GL11.glEnable(32826);
/* 524 */         RenderHelper.enableGUIStandardItemLighting();
/* 525 */         ItemLens itemLens = lens.getItemLens();
/* 526 */         ItemStack item = new ItemStack(itemLens);
/* 527 */         item.setTagCompound(null);
/* 528 */         ri.renderItemIntoGUI(item, -8, -8);
/* 529 */         RenderHelper.disableStandardItemLighting();
/* 530 */         GL11.glDisable(32826);
/* 531 */         GL11.glPopMatrix();
/* 532 */         int mx = (int)(i - sw / 2.0D);
/* 533 */         int my = (int)(j - sh / 2.0D);
/* 534 */         if (mx >= -10 && mx <= 10 && my >= -10 && my <= 10) {
/* 535 */           tt = new ItemStack(lens.getItemLens());
         }
       } 
/* 538 */       GL11.glScaled(RenderEventHandler.radialHudScale, RenderEventHandler.radialHudScale, RenderEventHandler
/* 539 */           .radialHudScale);
/* 540 */       float currentRot = -90.0F * RenderEventHandler.radialHudScale;
/* 541 */       float pieSlice = 360.0F / lensStack.size();
/* 542 */       ResourceLocation key = foci.firstKey();
/* 543 */       for (int a = 0; a < lensStack.size(); a++) {
/* 544 */         double xx = (MathHelper.cos(currentRot / 180.0F * 3.141593F) * width);
/* 545 */         double yy = (MathHelper.sin(currentRot / 180.0F * 3.141593F) * width);
/* 546 */         currentRot += pieSlice;
/* 547 */         GL11.glPushMatrix();
/* 548 */         if (this.type.equals(LensManager.LENSSLOT.LEFT)) {
/* 549 */           GL11.glTranslated(xx, yy, 100.0D);
         } else {
/* 551 */           GL11.glTranslated(-xx, yy, 100.0D);
/* 552 */         }  if (this.fociScale.get(key) == null)
/* 553 */           this.fociScale.put(key, Float.valueOf(1.0F)); 
/* 554 */         GL11.glScalef(this.fociScale.get(key).floatValue(), this.fociScale.get(key).floatValue(), this.fociScale.get(key).floatValue());
/* 555 */         GL11.glEnable(32826);
/* 556 */         RenderHelper.enableGUIStandardItemLighting();
/* 557 */         ItemStack item2 = lensStack.get(key).copy();
/* 558 */         item2.setTagCompound(null);
/* 559 */         ri.renderItemIntoGUI(item2, -8, -8);
/* 560 */         RenderHelper.disableStandardItemLighting();
/* 561 */         GL11.glDisable(32826);
/* 562 */         GL11.glPopMatrix();
/* 563 */         if (!KeyHandler.radialLock && KeyHandler.radialActive) {
           int mx2;
           
/* 566 */           if (this.type.equals(LensManager.LENSSLOT.LEFT)) {
/* 567 */             mx2 = (int)(i - sw / 2.0D - xx + -this.type.getAngle());
           } else {
/* 569 */             mx2 = (int)(i - sw / 2.0D - -xx + -this.type.getAngle());
/* 570 */           }  int my2 = (int)(j - sh / 2.0D - yy);
/* 571 */           if (mx2 >= -10 && mx2 <= 10 && my2 >= -10 && my2 <= 10) {
/* 572 */             this.lensHover.put(key, Boolean.valueOf(true));
/* 573 */             tt = lensStack.get(key);
/* 574 */             if (k == 0) {
/* 575 */               KeyHandler.radialActive = false;
/* 576 */               KeyHandler.radialLock = true;
/* 577 */               if (lensSlot.containsKey(key)) {
/* 578 */                 Lens lens1 = IsorropiaAPI.lensRegistry.getValue(key);
/* 579 */                 LensManager.putLens((Minecraft.getMinecraft()).player, lens1, this.type);
/* 580 */                 Common.INSTANCE
/* 581 */                   .sendToServer(new LensChangeMessage(lens1, lensSlot.get(key).intValue(), this.type));
               } 
               break;
             } 
           } else {
/* 586 */             this.lensHover.put(key, Boolean.valueOf(false));
           } 
/* 588 */         }  key = foci.higherKey(key);
       } 
/* 590 */       GL11.glPopMatrix();
/* 591 */       if (tt != null)
/* 592 */         UtilsFX.drawCustomTooltip(mc.currentScreen, mc.fontRenderer, tt
/* 593 */             .getTooltip(mc.player, ITooltipFlag.TooltipFlags.ADVANCED), 0, 20, 11);
/* 594 */       GL11.glDepthMask(true);
/* 595 */       GL11.glEnable(2929);
/* 596 */       GL11.glDisable(3042);
/* 597 */       GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 598 */       GL11.glPopMatrix();
     }
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\client\libs\RenderEventHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */