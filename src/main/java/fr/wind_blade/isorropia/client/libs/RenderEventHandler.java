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
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.gui.GuiResearchBrowser;
import thaumcraft.client.gui.GuiResearchPage;
import thaumcraft.client.lib.UtilsFX;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;


@SideOnly(Side.CLIENT)
public class RenderEventHandler {
    public static final ResourceLocation LIGO_TEX = new ResourceLocation("isorropia", "textures/misc/ligo.png");

    public static IBakedModel jar_soul = null;

    private static Lens theRightLens;
    private static final fociHUD fociLeft = new fociHUD(LensManager.LENSSLOT.LEFT);
    private static Lens theLeftLens;
    private static float radialHudScale;
    private static final fociHUD fociRight = new fociHUD(LensManager.LENSSLOT.RIGHT);

    public static ResourceLocation TEX_VIS = new ResourceLocation("thaumcraft", "textures/gui/gui_researchbook_overlay.png");

    public static ResourceLocation TEX_PAR = new ResourceLocation("thaumcraft", "textures/misc/particles.png");
    ResourceLocation tex4 = new ResourceLocation("thaumcraft", "textures/gui/paper.png");
    public static List<String> texts = Arrays.asList("wandtable.text1");

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
                float width = (float) event.getResolution().getScaledWidth_double();
                float height = (float) event.getResolution().getScaledHeight_double();
                float f2 = height / 34.0F;
                float f3 = width / 34.0F;
                GlStateManager.pushMatrix();
                GlStateManager.blendFunc(770, 771);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, flag);
                (Minecraft.getMinecraft()).renderEngine.bindTexture(new ResourceLocation("textures/blocks/cobblestone.png"));
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
                int recipePage = ((Integer) index.get(screen)).intValue();
                LinkedHashMap maps = (LinkedHashMap) recipes.get(screen);
                ResourceLocation loc = (ResourceLocation) shown.get(screen);

                if (maps == null || loc == null) {
                    return;
                }
                List<Object> list = (List<Object>) maps.get(loc);

                if (list != null && !list.isEmpty()) {
                    Object recipe = list.get(recipePage % list.size());

                    if (recipe instanceof CurativeInfusionRecipe) {
                        drawCurativeInfusionRecipe((CurativeInfusionRecipe) recipe, event);
                    }
                }

            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @SubscribeEvent
    public static void onToolTip(ItemTooltipEvent event) {
        ArrayList<String> tooltip = (ArrayList<String>) event.getToolTip();
        ItemStack stack = event.getItemStack();

        if (stack.hasTagCompound() && (stack.getItem() instanceof thaumcraft.api.items.IGoggles || stack.getItem() instanceof thaumcraft.api.items.IRevealer)) {
            String lens = stack.getTagCompound().getString(LensManager.LENSSLOT.LEFT.getName());

            if (!lens.isEmpty()) {
                tooltip.add(1, "§a" + I18n.format("lens." + IsorropiaAPI.lensRegistry.getValue(new ResourceLocation(lens)).getTranslationKey()));
            }

            lens = stack.getTagCompound().getString(LensManager.LENSSLOT.RIGHT.getName());

            if (!lens.isEmpty()) {
                tooltip.add(1, "§a" + I18n.format("lens." + IsorropiaAPI.lensRegistry.getValue(new ResourceLocation(lens)).getTranslationKey()));
            }
        }
    }

    public static void drawCurativeInfusionRecipe(CurativeInfusionRecipe recipe, GuiScreenEvent.DrawScreenEvent event) {
        GuiScreen screen = event.getGui();

        int x = (screen.width - 256) / 2 + 128;
        int y = (screen.height - 256) / 2 + 128;

        if (recipe.getVis() > 0.0F) {
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            String text = String.valueOf((int) recipe.getVis());
            int offset = (Minecraft.getMinecraft()).fontRenderer.getStringWidth(text);
            (Minecraft.getMinecraft()).fontRenderer.drawString(text, x - offset / 2 + 70, y - 45, 5263440);
            (Minecraft.getMinecraft()).renderEngine.bindTexture(TEX_VIS);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.4F);
            GL11.glTranslatef(x, (y - 55), 0.0F);
            GL11.glScalef(2.0F, 2.0F, 1.0F);
            screen.drawTexturedModalRect(30, 0, 68, 76, 12, 12);
            GL11.glPopMatrix();
        }

        if (recipe.getCelestialAura() != 0) {
            GL11.glPushMatrix();
            (Minecraft.getMinecraft()).renderEngine.bindTexture(recipe.getCelestialBody().getTex());
            GL11.glTranslatef((x + 56), (y - 17), 0.0F);
            GL11.glScalef(0.12F, 0.12F, 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7F);
            screen.drawTexturedModalRect(30, 0, 46, 45, 161, 162);
            GL11.glPopMatrix();
        }
        if (recipe.getFluxRejection() > 0.0F) {
            GuiResearchBrowser.drawForbidden(x, (y - 70));
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void renderLast(RenderWorldLastEvent event) {
        EntityPlayerSP player = (Minecraft.getMinecraft()).player;
        ItemStack revealer = LensManager.getRevealer(player);

        if (!revealer.isEmpty() && revealer.hasTagCompound() && revealer
                .getTagCompound().getString("LeftLens") != null) {
            theLeftLens = LensManager.getLens(revealer, LensManager.LENSSLOT.LEFT);
            theRightLens = LensManager.getLens(revealer, LensManager.LENSSLOT.RIGHT);
            boolean doubleLens = (theRightLens != null && theRightLens.equals(theLeftLens));


            if (theLeftLens != null) {
                theLeftLens.handleRenderWorldLast(player.world, player, doubleLens, event.getPartialTicks());
            }
            if (!doubleLens && theRightLens != null) {
                theRightLens.handleRenderWorldLast(player.world, player, false, event.getPartialTicks());
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void renderPlayerEvent(RenderHandEvent event) {
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void preRenderLiving(RenderLivingEvent.Pre<EntityLivingBase> event) {
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void postRenderLiving(RenderLivingEvent.Post<EntityLivingBase> event) {
    }

    protected static float interpolateRotation(float prevYawOffset, float yawOffset, float partialTicks) {
        float f;
        for (f = yawOffset - prevYawOffset; f < -180.0F; f += 360.0F) ;
        while (f >= 180.0F) {
            f -= 360.0F;
        }

        return prevYawOffset + partialTicks * f;
    }

    public static class fociHUD {
        private static final ResourceLocation radial = new ResourceLocation("thaumcraft", "textures/misc/radial.png");
        private static final ResourceLocation radial2 = new ResourceLocation("thaumcraft", "textures/misc/radial2.png");
        static final TreeMap<ResourceLocation, Integer> foci = new TreeMap<>();
        static final HashMap<ResourceLocation, ItemStack> lensStack = new HashMap<>();
        HashMap<ResourceLocation, Boolean> lensHover;
        HashMap<ResourceLocation, Float> fociScale = new HashMap<>();
        static final HashMap<ResourceLocation, Integer> lensSlot = new HashMap<>();
        final LensManager.LENSSLOT type;
        long lastTime;
        boolean lastState;

        public fociHUD(LensManager.LENSSLOT type) {
            this.lensHover = new HashMap<>();
            this.type = type;
            this.lastTime = 0L;
            this.lastState = false;
        }

        @SideOnly(Side.CLIENT)
        public void handleFociRadial(Minecraft mc, long time, RenderGameOverlayEvent event) {
            if (KeyHandler.radialActive || RenderEventHandler.radialHudScale > 0.0F) {
                if (KeyHandler.radialActive) {
                    if (mc.currentScreen != null) {
                        KeyHandler.radialActive = false;
                        KeyHandler.radialLock = true;
                        mc.setIngameFocus();
                        mc.setIngameNotInFocus();
                        return;
                    }
                    if (RenderEventHandler.radialHudScale == 0.0F) {
                        foci.clear();
                        lensStack.clear();
                        this.lensHover.clear();
                        this.fociScale.clear();
                        lensSlot.clear();
                        ItemStack item = null;
                        for (int a = 0; a < mc.player.inventory.mainInventory.size(); a++) {
                            item = mc.player.inventory.mainInventory.get(a);
                            if (!item.isEmpty() && item.getItem() instanceof ItemLens) {
                                ItemLens lens = (ItemLens) item.getItem();
                                foci.put(lens.getLens().getRegistryName(), Integer.valueOf(a));
                                lensStack.put(lens.getLens().getRegistryName(), item.copy());
                                this.fociScale.put(lens.getLens().getRegistryName(), Float.valueOf(1.0F));
                                this.lensHover.put(lens.getLens().getRegistryName(), Boolean.valueOf(false));
                                lensSlot.put(lens.getLens().getRegistryName(), Integer.valueOf(a));
                            }
                            if (foci.size() > 0 && mc.inGameHasFocus) {
                                mc.inGameHasFocus = false;
                                mc.mouseHelper.ungrabMouseCursor();
                            }
                        }
                    }
                } else if (mc.currentScreen == null && this.lastState) {
                    if (Display.isActive() && !mc.inGameHasFocus) {
                        mc.inGameHasFocus = true;
                        mc.mouseHelper.grabMouseCursor();
                    }
                    this.lastState = false;
                }
                renderFocusRadialHUD(event.getResolution().getScaledWidth_double(), event
                        .getResolution().getScaledHeight_double(), time, event.getPartialTicks());
                if (time > this.lastTime) {
                    for (ResourceLocation key : this.lensHover.keySet()) {
                        if (this.lensHover.get(key).booleanValue()) {
                            if (!KeyHandler.radialActive && !KeyHandler.radialLock) {
                                if (lensSlot.containsKey(key)) {
                                    Lens lens = IsorropiaAPI.lensRegistry.getValue(key);
                                    LensManager.putLens((Minecraft.getMinecraft()).player, lens, this.type);
                                    Common.INSTANCE
                                            .sendToServer(new LensChangeMessage(lens, lensSlot.get(key).intValue(), this.type));
                                }
                                KeyHandler.radialLock = true;
                            }
                            if (this.fociScale.get(key).floatValue() >= 1.3F) {
                                continue;
                            }
                            this.fociScale.put(key, Float.valueOf(this.fociScale.get(key).floatValue() + 0.025F));
                            continue;
                        }
                        if (this.fociScale.get(key).floatValue() <= 1.0F) {
                            continue;
                        }
                        this.fociScale.put(key, Float.valueOf(this.fociScale.get(key).floatValue() - 0.025F));
                    }
                    if (!KeyHandler.radialActive) {
                        RenderEventHandler.radialHudScale = RenderEventHandler.radialHudScale - 0.05F;
                    } else if (RenderEventHandler.radialHudScale < 1.0F) {
                        RenderEventHandler.radialHudScale = Math.min(RenderEventHandler.radialHudScale = RenderEventHandler.radialHudScale + 0.05F, 1.0F);
                    }

                    if (RenderEventHandler.radialHudScale < 0.0F) {
                        RenderEventHandler.radialHudScale = 0.0F;
                        KeyHandler.radialLock = false;
                    }

                    this.lastTime = time + 5L;
                    this.lastState = KeyHandler.radialActive;
                }
            }
        }

        public void renderFocusRadialHUD(double sw, double sh, long time, float partialTicks) {
            Minecraft mc = Minecraft.getMinecraft();
            RenderItem ri = Minecraft.getMinecraft().getRenderItem();
            ItemStack goggles = LensManager.getRevealer(mc.player);
            if (goggles == null)
                return;
            Lens lens = null;
            if (goggles.getTagCompound() != null) {
                lens = IsorropiaAPI.lensRegistry.getValue(new ResourceLocation(goggles.getTagCompound().getString(this.type.getName())));
            }
            int i = (int) (Mouse.getEventX() * sw / mc.displayWidth);
            int j = (int) (sh - Mouse.getEventY() * sh / mc.displayHeight - 1.0D);
            int k = Mouse.getEventButton();
            if (lensStack.size() == 0) {
                return;
            }
            GL11.glPushMatrix();
            GL11.glClear(256);
            GL11.glMatrixMode(5889);
            GL11.glLoadIdentity();
            GL11.glOrtho(0.0D, sw, sh, 0.0D, 1000.0D, 3000.0D);
            GL11.glMatrixMode(5888);
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            GL11.glPushMatrix();
            GL11.glTranslated(sw / 2.0D, sh / 2.0D, 0.0D);
            ItemStack tt = null;
            float width = 16.0F + lensStack.size() * 2.5F;
            GL11.glTranslatef(this.type.getAngle(), 0.0F, 0.0F);
            mc.renderEngine.bindTexture(radial);
            GL11.glPushMatrix();
            if (this.type.equals(LensManager.LENSSLOT.LEFT)) {
                GL11.glRotatef(-(partialTicks + (mc.player.ticksExisted % 720) / 2.0F), 0.0F, 0.0F, 1.0F);
            } else {
                GL11.glRotatef(partialTicks + (mc.player.ticksExisted % 720) / 2.0F, 0.0F, 0.0F, 1.0F);
            }
            GL11.glAlphaFunc(516, 0.003921569F);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            UtilsFX.renderQuadCentered(1, 1, 0, width * 2.75F * RenderEventHandler.radialHudScale, 0.5F, 0.5F, 0.5F, 200, 771, 0.5F);
            GL11.glDisable(3042);
            GL11.glAlphaFunc(516, 0.1F);
            GL11.glPopMatrix();
            mc.renderEngine.bindTexture(radial2);
            GL11.glPushMatrix();
            if (this.type.equals(LensManager.LENSSLOT.LEFT)) {
                GL11.glRotatef(partialTicks + (mc.player.ticksExisted % 720) / 2.0F, 0.0F, 0.0F, 1.0F);
            } else {
                GL11.glRotatef(-(partialTicks + (mc.player.ticksExisted % 720) / 2.0F), 0.0F, 0.0F, 1.0F);
            }
            GL11.glAlphaFunc(516, 0.003921569F);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            UtilsFX.renderQuadCentered(1, 1, 0, width * 2.55F * RenderEventHandler.radialHudScale, 0.5F, 0.5F, 0.5F, 200, 771, 0.5F);
            GL11.glDisable(3042);
            GL11.glAlphaFunc(516, 0.1F);
            GL11.glPopMatrix();
            if (lens != null) {
                GL11.glPushMatrix();
                GL11.glEnable(32826);
                RenderHelper.enableGUIStandardItemLighting();
                ItemLens itemLens = lens.getItemLens();
                ItemStack item = new ItemStack(itemLens);
                item.setTagCompound(null);
                ri.renderItemIntoGUI(item, -8, -8);
                RenderHelper.disableStandardItemLighting();
                GL11.glDisable(32826);
                GL11.glPopMatrix();
                int mx = (int) (i - sw / 2.0D);
                int my = (int) (j - sh / 2.0D);
                if (mx >= -10 && mx <= 10 && my >= -10 && my <= 10) {
                    tt = new ItemStack(lens.getItemLens());
                }
            }
            GL11.glScaled(RenderEventHandler.radialHudScale, RenderEventHandler.radialHudScale, RenderEventHandler
                    .radialHudScale);
            float currentRot = -90.0F * RenderEventHandler.radialHudScale;
            float pieSlice = 360.0F / lensStack.size();
            ResourceLocation key = foci.firstKey();
            for (int a = 0; a < lensStack.size(); a++) {
                double xx = (MathHelper.cos(currentRot / 180.0F * 3.141593F) * width);
                double yy = (MathHelper.sin(currentRot / 180.0F * 3.141593F) * width);
                currentRot += pieSlice;
                GL11.glPushMatrix();
                if (this.type.equals(LensManager.LENSSLOT.LEFT)) {
                    GL11.glTranslated(xx, yy, 100.0D);
                } else {
                    GL11.glTranslated(-xx, yy, 100.0D);
                }
                if (this.fociScale.get(key) == null)
                    this.fociScale.put(key, Float.valueOf(1.0F));
                GL11.glScalef(this.fociScale.get(key).floatValue(), this.fociScale.get(key).floatValue(), this.fociScale.get(key).floatValue());
                GL11.glEnable(32826);
                RenderHelper.enableGUIStandardItemLighting();
                ItemStack item2 = lensStack.get(key).copy();
                item2.setTagCompound(null);
                ri.renderItemIntoGUI(item2, -8, -8);
                RenderHelper.disableStandardItemLighting();
                GL11.glDisable(32826);
                GL11.glPopMatrix();
                if (!KeyHandler.radialLock && KeyHandler.radialActive) {
                    int mx2;

                    if (this.type.equals(LensManager.LENSSLOT.LEFT)) {
                        mx2 = (int) (i - sw / 2.0D - xx + -this.type.getAngle());
                    } else {
                        mx2 = (int) (i - sw / 2.0D - -xx + -this.type.getAngle());
                    }
                    int my2 = (int) (j - sh / 2.0D - yy);
                    if (mx2 >= -10 && mx2 <= 10 && my2 >= -10 && my2 <= 10) {
                        this.lensHover.put(key, Boolean.valueOf(true));
                        tt = lensStack.get(key);
                        if (k == 0) {
                            KeyHandler.radialActive = false;
                            KeyHandler.radialLock = true;
                            if (lensSlot.containsKey(key)) {
                                Lens lens1 = IsorropiaAPI.lensRegistry.getValue(key);
                                LensManager.putLens((Minecraft.getMinecraft()).player, lens1, this.type);
                                Common.INSTANCE
                                        .sendToServer(new LensChangeMessage(lens1, lensSlot.get(key).intValue(), this.type));
                            }
                            break;
                        }
                    } else {
                        this.lensHover.put(key, Boolean.valueOf(false));
                    }
                }
                key = foci.higherKey(key);
            }
            GL11.glPopMatrix();
            if (tt != null)
                UtilsFX.drawCustomTooltip(mc.currentScreen, mc.fontRenderer, tt
                        .getTooltip(mc.player, ITooltipFlag.TooltipFlags.ADVANCED), 0, 20, 11);
            GL11.glDepthMask(true);
            GL11.glEnable(2929);
            GL11.glDisable(3042);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }
    }
}