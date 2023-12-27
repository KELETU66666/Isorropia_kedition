package fr.wind_blade.isorropia.common.lenses;

import fr.wind_blade.isorropia.common.items.misc.ItemLens;
import fr.wind_blade.isorropia.common.libs.helpers.IRMathHelper;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;




public class AirLens
        extends Lens
{
    public static final byte AIR_LENS_DIST_CAP = 24;
    public static final ResourceLocation TEX = new ResourceLocation("isorropia", "textures/fx/ripple.png");
    public static final ResourceLocation TEX_UNDEAD = new ResourceLocation("isorropia", "textures/fx/vortex.png");
    public static final ResourceLocation TEX_ELDRITCH = new ResourceLocation("thaumcraft", "textures/misc/vortex.png");


    public AirLens(ItemLens lensIn) {
        super(lensIn);
    }




    public void handleTicks(World worldIn, EntityPlayer playerIn, boolean doubleLens) {}




    public void handleRenderGameOverlay(World worldIn, EntityPlayer playerIn, ScaledResolution resolution, boolean doubleLens, float partialTicks) {}



    public void handleRenderWorldLast(World worldIn, EntityPlayer playerIn, boolean doubleLens, float partialTicks) {
        if ((Minecraft.getMinecraft()).gameSettings.thirdPersonView > 0) {
            return;
        }
        List<EntityLivingBase> base = playerIn.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(playerIn.posX - 24.0D, playerIn.posY - 24.0D, playerIn.posZ - 24.0D, playerIn.posX + 24.0D, playerIn.posY + 24.0D, playerIn.posZ + 24.0D), e -> (e != playerIn));


        for (EntityLivingBase e : base) {


            double playerOffX = playerIn.prevPosX + (playerIn.posX - playerIn.prevPosX) * partialTicks;
            double playerOffY = playerIn.prevPosY + (playerIn.posY - playerIn.prevPosY) * partialTicks;
            double playerOffZ = playerIn.prevPosZ + (playerIn.posZ - playerIn.prevPosZ) * partialTicks;

            double eOffX = e.prevPosX + (e.posX - e.prevPosX) * partialTicks;
            double eOffY = e.prevPosY + (e.posY - e.prevPosY) * partialTicks;
            double eOffZ = e.prevPosZ + (e.posZ - e.prevPosZ) * partialTicks;

            double scale = e.getPositionVector().subtract(playerIn.getPositionVector()).length();
            float sizeOffset = (e.ticksExisted % 16);

            double cappedTchebychevDist = Math.min(IRMathHelper.getTchebychevDistance((Entity)e, (Entity)playerIn), 24.0D);
            float alpha = (float)(1.0D - cappedTchebychevDist / 24.0D);
            float size = Math.min(e.height, e.width);

            if (e instanceof thaumcraft.api.entities.IEldritchMob) {
                GlStateManager.pushMatrix();
                GL11.glDisable(2929);
                size = (float)(size * 0.8D);

                GlStateManager.translate(-playerOffX, -playerOffY, -playerOffZ);
                GlStateManager.translate(eOffX, eOffY + (e.height / 2.0F), eOffZ);
                GlStateManager.rotate(-playerIn.rotationYaw, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(playerIn.rotationPitch, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate((e.ticksExisted % 360), 0.0F, 0.0F, 1.0F);
                UtilsFX.renderQuadCentered(TEX_ELDRITCH, size, 1.0F, 1.0F, 1.0F, -99, 771, alpha);
                GlStateManager.rotate((-(e.ticksExisted % 360) * 2), 0.0F, 0.0F, 1.0F);
                size *= 2.0F;

                UtilsFX.renderQuadCentered(TEX_ELDRITCH, size, 1.0F, 1.0F, 1.0F, -99, 771, alpha);

                GL11.glEnable(2929);
                GlStateManager.popMatrix(); continue;
            }
            if (e.isEntityUndead()) {
                size = (float)(size * 1.3D);
                GlStateManager.pushMatrix();
                GL11.glDisable(2929);

                GlStateManager.translate(-playerOffX, -playerOffY, -playerOffZ);
                GlStateManager.translate(eOffX, eOffY + (e.height / 2.0F), eOffZ);
                GlStateManager.rotate(-playerIn.rotationYaw, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(playerIn.rotationPitch, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(-(e.ticksExisted % 360), 0.0F, 0.0F, 1.0F);
                UtilsFX.renderQuadCentered(TEX_UNDEAD, size, 1.0F, 1.0F, 1.0F, -99, 771, alpha);
                size *= 2.0F;
                GlStateManager.rotate((e.ticksExisted % 360 * 2), 0.0F, 0.0F, 1.0F);

                UtilsFX.renderQuadCentered(TEX_UNDEAD, size, 1.0F, 1.0F, 1.0F, -99, 771, alpha);

                GL11.glEnable(2929);
                GlStateManager.popMatrix(); continue;
            }
            double numbers = Math.min(48.0D / scale + 1.0D, 4.0D);

            for (int i = 0; i < numbers; i++) {
                float numSize = (float)(((i * 16) / (numbers + 1.0D) + sizeOffset) % 16.0D / 12.0D) * size;

                GlStateManager.pushMatrix();
                GL11.glDisable(2929);

                GlStateManager.translate(-playerOffX, -playerOffY, -playerOffZ);
                GlStateManager.translate(eOffX, eOffY + (e.height / 2.0F), eOffZ);
                GlStateManager.rotate(-playerIn.rotationYaw, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(playerIn.rotationPitch, 1.0F, 0.0F, 0.0F);
                UtilsFX.renderQuadCentered(TEX, numSize, 1.0F, 1.0F, 1.0F, -99, 771, alpha);

                GL11.glEnable(2929);
                GlStateManager.popMatrix();
            }
        }
    }

    public void handleRemoval(World worldIn, EntityPlayer playerIn) {}
}