package fr.wind_blade.isorropia.common.events;

import fr.wind_blade.isorropia.common.Common;
import fr.wind_blade.isorropia.common.lenses.LensManager;
import fr.wind_blade.isorropia.common.network.LensRemoveMessageSP;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class KeyHandler
{
    public KeyBinding keyLens;
    public static long lastPressV = 0L;
    public static boolean radialActive;
    public static boolean radialLock;

    public KeyHandler() {
        this.keyLens = new KeyBinding("Change Goggles Lens", 47, "key.categories.misc");
        ClientRegistry.registerKeyBinding(this.keyLens);
    }

    @SubscribeEvent
    public void keyEvent(InputEvent.KeyInputEvent event) {
        if (this.keyLens.isKeyDown()) {
            if ((FMLClientHandler.instance().getClient()).inGameHasFocus) {
                EntityPlayerSP entityPlayerSP = (Minecraft.getMinecraft()).player;
                if (entityPlayerSP != null) {
                    if (entityPlayerSP.isSneaking()) {
                        Common.INSTANCE.sendToServer((IMessage)new LensRemoveMessageSP());
                        return;
                    }
                    lastPressV = System.currentTimeMillis();
                    radialLock = false;
                    if (!radialLock && !LensManager.getRevealer((EntityPlayer)entityPlayerSP).isEmpty()) {
                        radialActive = true;
                    }
                }
            }
            lastPressV = System.currentTimeMillis();
        } else {
            radialActive = false;
        }
    }
}