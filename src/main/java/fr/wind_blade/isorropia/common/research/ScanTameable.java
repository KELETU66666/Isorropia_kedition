 package fr.wind_blade.isorropia.common.research;
 
 import java.util.UUID;
 import net.minecraft.entity.passive.EntityTameable;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.util.text.ITextComponent;
 import net.minecraft.util.text.TextComponentString;
 import net.minecraft.util.text.TextComponentTranslation;
 import net.minecraft.util.text.TextFormatting;
 import thaumcraft.api.capabilities.ThaumcraftCapabilities;
 import thaumcraft.api.research.IScanThing;
 
 public class ScanTameable implements IScanThing {
   public boolean checkThing(EntityPlayer var1, Object obj) {
     if (!(obj instanceof EntityTameable) || 
/* 16 */       !ThaumcraftCapabilities.getKnowledge(var1).isResearchKnown("INSTILLEDFIDELITY")) {
/* 17 */       return false;
     }
     EntityTameable tame = (EntityTameable)obj;
     
/* 21 */     if (tame.isTamed() && ScanFidelity.memory.containsKey(var1.getUniqueID()) && ((UUID)ScanFidelity.memory
/* 22 */       .get(var1.getUniqueID())).equals(tame.getUniqueID()))
/* 23 */       return true; 
/* 24 */     return false;
   }
 
   
   public void onSuccess(EntityPlayer player, Object object) {
      player.sendMessage((ITextComponent)new TextComponentString(TextFormatting.DARK_PURPLE + (new TextComponentTranslation("scan.pretame", new Object[0]))
/* 30 */           .getFormattedText()));
/* 31 */     ScanFidelity.memory.remove(player.getUniqueID());
   }
 
   
   public String getResearchKey(EntityPlayer var1, Object var2) {
/* 36 */     return "!scan.fidelity";
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\research\ScanTameable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */