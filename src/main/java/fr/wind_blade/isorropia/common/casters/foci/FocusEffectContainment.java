// Decompiled with: CFR 0.152
// Class Version: 8
package fr.wind_blade.isorropia.common.casters.foci;

import fr.wind_blade.isorropia.common.libs.helpers.IsorropiaHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
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
        return "FOCUSSPELLBAT";
    }

    public String getKey() {
        return "isorropia.FOCUSCONTAINMENT";
    }

    public Aspect getAspect() {
        return Aspect.TRAP;
    }

    public int getComplexity() {
        return this.getSettingValue("power") * 3;
    }

    public boolean execute(RayTraceResult trace, Trajectory var2, float var3, int var4) {
        EntityLivingBase base = this.getPackage().getCaster();
        if (trace.entityHit instanceof MultiPartEntityPart && ((MultiPartEntityPart) trace.entityHit).parent instanceof Entity) {
            trace.entityHit = (Entity) ((MultiPartEntityPart) trace.entityHit).parent;
        }
        if (!(trace.entityHit instanceof EntityLiving) || !IsorropiaHelper.doPlayerHaveJar((EntityPlayer)base, false)) {
            return false;
        }
        if (!IsorropiaHelper.canEntityBeJarred((EntityLiving)trace.entityHit)) {
            base.sendMessage(new TextComponentString(String.valueOf(TextFormatting.ITALIC) + TextFormatting.GRAY + I18n.format("isorropia.containment.fail")));
            return false;
        }
        if (IsorropiaHelper.containEntity(base, (EntityLivingBase)trace.entityHit, this.getSettingValue("power"))) {
            IsorropiaHelper.playerJarEntity((EntityPlayer)base, (EntityLiving)trace.entityHit);
        }
        return true;
    }

    public void renderParticleFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
    }

    public NodeSetting[] createSettings() {
        return new NodeSetting[]{new NodeSetting("power", "focus.common.power", new NodeSetting.NodeSettingIntRange(1, 5))};
    }
}
