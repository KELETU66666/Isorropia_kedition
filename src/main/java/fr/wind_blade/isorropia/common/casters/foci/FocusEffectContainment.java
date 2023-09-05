 package fr.wind_blade.isorropia.common.casters.foci;
 
 import fr.wind_blade.isorropia.common.libs.helpers.IsorropiaHelper;
 import net.minecraft.client.resources.I18n;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.EntityLiving;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.entity.MultiPartEntityPart;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.util.math.RayTraceResult;
 import net.minecraft.util.text.ITextComponent;
 import net.minecraft.util.text.TextComponentString;
 import net.minecraft.util.text.TextFormatting;
 import net.minecraft.world.World;
 import thaumcraft.api.aspects.Aspect;
 import thaumcraft.api.casters.FocusEffect;
 import thaumcraft.api.casters.NodeSetting;
 import thaumcraft.api.casters.Trajectory;
 
 public class FocusEffectContainment
   extends FocusEffect {
   public String getResearch() {
/* 23 */     return "FOCUSSPELLBAT";
   }
 
   
   public String getKey() {
      return "isorropia.FOCUSCONTAINMENT";
   }
 
   
   public Aspect getAspect() {
/* 33 */     return Aspect.TRAP;
   }
 
   
   public int getComplexity() {
/* 38 */     return getSettingValue("power") * 3;
   }
 
   
   public boolean execute(RayTraceResult trace, Trajectory var2, float var3, int var4) {
/* 43 */     EntityLivingBase base = getPackage().getCaster();
 
 
 
 
     
/* 49 */     if (trace.entityHit instanceof MultiPartEntityPart && ((MultiPartEntityPart)trace.entityHit).parent instanceof Entity)
     {
/* 51 */       trace.entityHit = (Entity)((MultiPartEntityPart)trace.entityHit).parent;
     }
 
 
 
 
     
/* 58 */     if (!(trace.entityHit instanceof EntityLiving) || 
/* 59 */       !IsorropiaHelper.doPlayerHaveJar((EntityPlayer)base, false)) {
/* 60 */       return false;
     }
 
     
/* 64 */     if (!IsorropiaHelper.canEntityBeJarred((EntityLiving)trace.entityHit)) {
/* 65 */       ((EntityPlayer)base).sendMessage((ITextComponent)new TextComponentString(TextFormatting.ITALIC + "" + TextFormatting.GRAY + 
/* 66 */             I18n.format("isorropia.containment.fail", new Object[0])));
/* 67 */       return false;
     } 
 
 
 
 
     
/* 74 */     if (IsorropiaHelper.containEntity(base, (EntityLivingBase)trace.entityHit, getSettingValue("power"))) {
/* 75 */       IsorropiaHelper.playerJarEntity((EntityPlayer)base, (EntityLiving)trace.entityHit);
     }
     
/* 78 */     return true;
   }
 
 
   
   public void renderParticleFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {}
 
 
   
   public NodeSetting[] createSettings() {
/* 88 */     return new NodeSetting[] { new NodeSetting("power", "focus.common.power", (NodeSetting.INodeSettingType)new NodeSetting.NodeSettingIntRange(1, 5)) };
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\casters\foci\FocusEffectContainment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */