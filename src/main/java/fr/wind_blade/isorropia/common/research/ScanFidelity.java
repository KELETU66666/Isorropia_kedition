 package fr.wind_blade.isorropia.common.research;
 
 import java.util.HashMap;
 import java.util.UUID;
 import net.minecraft.entity.passive.EntityTameable;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.util.text.ITextComponent;
 import net.minecraft.util.text.TextComponentString;
 import net.minecraft.util.text.TextComponentTranslation;
 import net.minecraft.util.text.TextFormatting;
 import thaumcraft.api.capabilities.ThaumcraftCapabilities;
 import thaumcraft.api.research.IScanThing;
 
 public class ScanFidelity
   implements IScanThing {
/* 16 */   public static HashMap<UUID, UUID> memory = new HashMap<>();
 
   
   public boolean checkThing(EntityPlayer var1, Object obj) {
/* 20 */     if (!(obj instanceof EntityTameable) || 
/* 21 */       !ThaumcraftCapabilities.getKnowledge(var1).isResearchKnown("INSTILLEDFIDELITY@0") || 
/* 22 */       ThaumcraftCapabilities.getKnowledge(var1).isResearchKnown("!scan.fidelity"))
/* 23 */       return false; 
/* 24 */     return !((EntityTameable)obj).isTamed();
   }
 
   
   public void onSuccess(EntityPlayer player, Object obj) {
      EntityTameable tame = (EntityTameable)obj;
     
/* 31 */     memory.put(player.getUniqueID(), tame.getUniqueID());
/* 32 */     player.sendMessage((ITextComponent)new TextComponentString(TextFormatting.DARK_PURPLE + (new TextComponentTranslation("scan.fidelity", new Object[0]))
/* 33 */           .getFormattedText()));
/* 34 */     ThaumcraftCapabilities.getKnowledge(player).removeResearch("!scan.pretame");
   }
 
   
   public String getResearchKey(EntityPlayer var1, Object var2) {
/* 39 */     return "!scan.pretame";
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\research\ScanFidelity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */