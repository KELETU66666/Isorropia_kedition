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
/* 24 */     this.keyLens = new KeyBinding("Change Goggles Lens", 47, "key.categories.misc");
     ClientRegistry.registerKeyBinding(this.keyLens);
   }
   
   @SubscribeEvent
   public void keyEvent(InputEvent.KeyInputEvent event) {
/* 30 */     if (this.keyLens.isKeyDown()) {
/* 31 */       if ((FMLClientHandler.instance().getClient()).inGameHasFocus) {
/* 32 */         EntityPlayerSP entityPlayerSP = (Minecraft.getMinecraft()).player;
/* 33 */         if (entityPlayerSP != null) {
/* 34 */           if (entityPlayerSP.isSneaking()) {
/* 35 */             Common.INSTANCE.sendToServer((IMessage)new LensRemoveMessageSP());
             return;
           } 
/* 38 */           lastPressV = System.currentTimeMillis();
/* 39 */           radialLock = false;
/* 40 */           if (!radialLock && !LensManager.getRevealer((EntityPlayer)entityPlayerSP).isEmpty()) {
              radialActive = true;
           }
         } 
       } 
/* 45 */       lastPressV = System.currentTimeMillis();
     } else {
/* 47 */       radialActive = false;
     } 
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\events\KeyHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */