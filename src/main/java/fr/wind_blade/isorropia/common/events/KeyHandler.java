package fr.wind_blade.isorropia.common.events;

import fr.wind_blade.isorropia.Isorropia;
import fr.wind_blade.isorropia.common.Common;
import fr.wind_blade.isorropia.common.lenses.LensManager;
import fr.wind_blade.isorropia.common.network.LensRemoveMessageSP;
import fr.wind_blade.isorropia.common.network.PacketFingersToServer;
import fr.wind_blade.isorropia.common.network.PacketToggleClimbToServer;
import fr.wind_blade.isorropia.common.network.PacketToggleInvisibleToServer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class KeyHandler {
    public KeyBinding keyLens;
    public KeyBinding keyC;
    public KeyBinding keyX;
    public KeyBinding keyMorphic;
    public static long lastPressV = 0L;
    public static long lastPressM = 0L;
    private boolean keyPressedM = false;
    public static long lastPressX = 0l;
    public static long lastPressC = 0l;
    private boolean keyPressedX = false;
    private boolean keyPressedC = false;
    public static boolean radialActive;
    public static boolean radialLock;

    public KeyHandler() {
        this.keyLens = new KeyBinding("Change Goggles Lens", 36, "key.categories.misc");
        this.keyMorphic = new KeyBinding("Activate Morphic Fingers", Keyboard.KEY_M, "key.categories.misc");
        this.keyC = new KeyBinding("Toggle Spider Climb", Keyboard.KEY_NONE, "key.categories.misc");
        this.keyX = new KeyBinding("Toggle Chameleon Skin", Keyboard.KEY_NONE, "key.categories.misc");

        ClientRegistry.registerKeyBinding(this.keyLens);
        ClientRegistry.registerKeyBinding(this.keyMorphic);
        ClientRegistry.registerKeyBinding(this.keyC);
        ClientRegistry.registerKeyBinding(this.keyX);
    }

    @SubscribeEvent
    public void keyEvent(InputEvent.KeyInputEvent event) {
        if (this.keyLens.isKeyDown()) {
            if ((FMLClientHandler.instance().getClient()).inGameHasFocus) {
                EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
                if (entityPlayerSP != null) {
                    if (entityPlayerSP.isSneaking()) {
                        Common.INSTANCE.sendToServer(new LensRemoveMessageSP());
                        return;
                    }
                    lastPressV = System.currentTimeMillis();
                    radialLock = false;
                    if (!radialLock && !LensManager.getRevealer(entityPlayerSP).isEmpty()) {
                        radialActive = true;
                    }
                }
            }
            lastPressV = System.currentTimeMillis();
        } else {
            radialActive = false;
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void playerTick(final TickEvent.PlayerTickEvent event) {
        if (event.side == Side.SERVER) {
            return;
        }

        if (this.keyMorphic.isPressed()) {
            if (FMLClientHandler.instance().getClient().inGameHasFocus) {
                final EntityPlayer player = Minecraft.getMinecraft().player;
                if (player != null) {
                    if (!this.keyPressedM) {
                        KeyHandler.lastPressM = System.currentTimeMillis();
                    }
                    if (Common.getCap(player).hasPlayerInfusion(2) && !this.keyPressedM) {
                        player.openGui(
                                Isorropia.MODID,
                                9,
                                player.world,
                                (int) player.posX,
                                (int) player.posY,
                                (int) player.posZ);
                        Common.INSTANCE.sendToServer(new PacketFingersToServer(player, player.dimension));
                    }
                }
                this.keyPressedM = true;
            }
        } else {
            if (this.keyPressedM) {
                KeyHandler.lastPressM = System.currentTimeMillis();
            }
            this.keyPressedM = false;
        }
        if (event.phase == TickEvent.Phase.START) {
            if (this.keyC.isPressed()) {
                if (FMLClientHandler.instance().getClient().inGameHasFocus) {
                    EntityPlayerSP player = Minecraft.getMinecraft().player;
                    if (player != null) {
                        if (!this.keyPressedC) {
                            KeyHandler.lastPressC = System.currentTimeMillis();
                        }
                        if (Common.getCap(player)
                                .hasPlayerInfusion(9) && !this.keyPressedC) {
                            Common.getCap(player).toggleClimb = !Common.getCap(player).toggleClimb;
                            if (Common.getCap(player).toggleClimb) {
                                player.sendMessage(
                                        new TextComponentString(
                                                TextFormatting.ITALIC + ""
                                                        + TextFormatting.GRAY
                                                        + "Spider Climb disabled."));
                            } else {
                                player.sendMessage(
                                        new TextComponentString(
                                                TextFormatting.ITALIC + ""
                                                        + TextFormatting.GRAY
                                                        + "Spider Climb enabled."));
                            }
                            Common.INSTANCE
                                    .sendToServer(new PacketToggleClimbToServer(player, player.dimension));
                        }
                    }
                    this.keyPressedC = true;
                }
            } else {
                if (this.keyPressedC) {
                    KeyHandler.lastPressC = System.currentTimeMillis();
                }
                this.keyPressedC = false;
            }
            if (this.keyX.isPressed()) {
                if (FMLClientHandler.instance().getClient().inGameHasFocus) {
                    EntityPlayerSP player = Minecraft.getMinecraft().player;
                    if (player != null) {
                        if (!this.keyPressedX) {
                            KeyHandler.lastPressX = System.currentTimeMillis();
                        }
                        if (Common.getCap(player)
                                .hasPlayerInfusion(10) && !this.keyPressedX) {
                            Common.getCap(player).toggleInvisible = !Common.getCap(player).toggleInvisible;
                            if (Common.getCap(player).toggleInvisible) {
                                player.removeActivePotionEffect(MobEffects.INVISIBILITY);
                                player.setInvisible(false);
                                player.sendMessage(
                                        new TextComponentString(
                                                TextFormatting.ITALIC + ""
                                                        + TextFormatting.GRAY
                                                        + "Chameleon Skin disabled."));
                            } else {
                                final PotionEffect effect = new PotionEffect(
                                        MobEffects.INVISIBILITY,
                                        Integer.MAX_VALUE,
                                        0,
                                        true, false);
                                effect.setCurativeItems(new ArrayList<>());
                                player.addPotionEffect(effect);
                                player.setInvisible(true);
                                player.sendMessage(
                                        new TextComponentString(
                                                TextFormatting.ITALIC + ""
                                                        + TextFormatting.GRAY
                                                        + "Chameleon Skin enabled."));
                            }
                            Common.INSTANCE
                                    .sendToServer(new PacketToggleInvisibleToServer(player, player.dimension));
                        }
                    }
                    this.keyPressedX = true;
                }
            } else {
                if (this.keyPressedX) {
                    KeyHandler.lastPressX = System.currentTimeMillis();
                }
                this.keyPressedX = false;
            }
        }
    }
}