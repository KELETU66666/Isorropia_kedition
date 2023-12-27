package fr.wind_blade.isorropia.common.lenses;

import fr.wind_blade.isorropia.common.items.misc.ItemLens;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ScanningManager;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.client.lib.events.RenderEventHandler;
import thaumcraft.common.lib.utils.EntityUtils;

public class OrdoLens extends Lens {
    private static float nameSize;

    public OrdoLens(ItemLens lensIn) {
        super(lensIn);
    }


    public void handleTicks(World worldIn, EntityPlayer playerIn, boolean doubleLens) {
        if (worldIn.isRemote) {
            return;
        }
        Entity target = EntityUtils.getPointedEntity(worldIn, (Entity)playerIn, 1.0D, 5.0D, 0.0F, true);

        if (target != null && ScanningManager.isThingStillScannable(playerIn, target)) {
            ScanningManager.scanTheThing(playerIn, target);
        } else {

            RayTraceResult mop = rayTrace(worldIn, playerIn, true);
            if (mop != null && mop.getBlockPos() != null &&
                    ScanningManager.isThingStillScannable(playerIn, mop.getBlockPos())) {
                ScanningManager.scanTheThing(playerIn, mop.getBlockPos());
            }
        }
    }





    @SideOnly(Side.CLIENT)
    public void handleRenderGameOverlay(World worldIn, EntityPlayer playerIn, ScaledResolution resolution, boolean doubleLens, float partialTicks) {
        if ((Minecraft.getMinecraft()).gameSettings.thirdPersonView != 0) {
            return;
        }
        Entity target = EntityUtils.getPointedEntity(worldIn, (Entity)playerIn, 1.0D, 5.0D, 0.0F, true);
        if (target != null) {
            Entity entity = RenderEventHandler.thaumTarget;
            if (entity == null || entity != target) {
                RenderEventHandler.tagscale = 0.0F;
                nameSize = 0.0F;
            }
            RenderEventHandler.thaumTarget = target;

            if (ScanningManager.isThingStillScannable(playerIn, target)) {
                FXDispatcher.INSTANCE.scanHighlight(target);
            }

            return;
        }
        RayTraceResult mop = rayTrace(worldIn, playerIn, true);
        if (mop != null && mop.getBlockPos() != null && mop.typeOfHit == RayTraceResult.Type.BLOCK) {





            List<EntityItem> itemstacks = (Minecraft.getMinecraft()).world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(new BlockPos(mop
                    .getBlockPos().getX(), mop.getBlockPos().getY() + 1, mop
                    .getBlockPos().getZ())));
            if (!itemstacks.isEmpty() && !((EntityItem)itemstacks.get(0)).getItem().isEmpty()) {
                renderNameAndAspects(resolution, AspectHelper.getObjectAspects(((EntityItem)itemstacks.get(0)).getItem()), ((EntityItem)itemstacks
                        .get(0)).getItem().getDisplayName());

                if (ScanningManager.isThingStillScannable(playerIn, itemstacks.get(0))) {
                    FXDispatcher.INSTANCE.scanHighlight((Entity)itemstacks.get(0));
                }
            }
            else {

                RayTraceResult mob = playerIn.rayTrace(5.0D, partialTicks);
                if (mob != null && mop.getBlockPos() != null) {
                    Block block = worldIn.getBlockState(mop.getBlockPos()).getBlock();
                    renderNameAndAspects(resolution, AspectHelper.getObjectAspects(new ItemStack(block)), block
                            .getLocalizedName());

                    if (ScanningManager.isThingStillScannable(playerIn, mob.getBlockPos())) {
                        FXDispatcher.INSTANCE.scanHighlight(mop.getBlockPos());
                    }
                }
            }
        }
    }




    @SideOnly(Side.CLIENT)
    public void handleRenderWorldLast(World worldIn, EntityPlayer playerIn, boolean doubleLens, float partialTicks) {
        Entity entity = RenderEventHandler.thaumTarget;
        if (entity != null) {

            if (entity.isDead) {
                RenderEventHandler.thaumTarget = null;

                return;
            }
            double playerOffX = playerIn.prevPosX + (playerIn.posX - playerIn.prevPosX) * partialTicks;
            double playerOffY = playerIn.prevPosY + (playerIn.posY - playerIn.prevPosY) * partialTicks;
            double playerOffZ = playerIn.prevPosZ + (playerIn.posZ - playerIn.prevPosZ) * partialTicks;

            double eOffX = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
            double eOffY = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks;
            double eOffZ = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;
            AspectList aspects = AspectHelper.getEntityAspects(entity);

            GlStateManager.pushMatrix();

            GlStateManager.translate(-playerOffX, -playerOffY, -playerOffZ);
            GlStateManager.translate(eOffX, eOffY + entity.height + ((aspects != null && aspects.size() > 0) ? 1.1D : 0.6D), eOffZ);


            GlStateManager.rotate(-playerIn.rotationYaw, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);

            if (nameSize < 1.0F)
                nameSize += 0.031F;
            float size = 0.03F * nameSize;
            GlStateManager.scale(size, size, size);


            String name = (entity instanceof EntityItem) ? ((EntityItem)entity).getItem().getDisplayName() : entity.getName();
            (Minecraft.getMinecraft()).fontRenderer.drawString(name, 1 -
                    (Minecraft.getMinecraft()).fontRenderer.getStringWidth(name) / 2, 1, Color.WHITE.getRGB());

            GlStateManager.popMatrix();
        }
    }


    public void handleRemoval(World worldIn, EntityPlayer playerIn) {
        if (worldIn.isRemote) {
            RenderEventHandler.thaumTarget = null;
        }
    }

    @SideOnly(Side.CLIENT)
    public static void renderNameAndAspects(ScaledResolution resolution, AspectList aspects, String text) {
        int w = resolution.getScaledWidth();
        int h = resolution.getScaledHeight();

        if (aspects != null && aspects.size() > 0) {
            int num = 0;
            int yOff = 0;
            int thisRow = 0;
            int size = 18;
            if (aspects.size() - num < 5) {
                thisRow = aspects.size() - num;
            } else {
                thisRow = 5;
            }

            for (Aspect aspect : aspects.getAspects()) {
                yOff = num / 5 * size;
                drawAspectTag(aspect, aspects.getAmount(aspect), w / 2 - size * thisRow / 2 + size * num % 5, h / 2 + 16 + yOff);

                if (++num % 5 == 0)
                {

                    if (aspects.size() - num < 5) {
                        thisRow = aspects.size() - num;
                    } else {
                        thisRow = 5;
                    }
                }
            }
        }
        if (text.length() > 0) {
            (Minecraft.getMinecraft()).ingameGUI.drawString((Minecraft.getMinecraft()).fontRenderer, text, w / 2 -
                    (Minecraft.getMinecraft()).fontRenderer.getStringWidth(text) / 2, h / 2 - 16, 16777215);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void drawAspectTag(Aspect aspect, int amount, int x, int y) {
        Color color = new Color(aspect.getColor());
        GL11.glPushMatrix();
        GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 0.5F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(aspect.getImage());
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        buffer.putColorRGB_F(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 0);
        buffer.pos(x, y, 0.0D).tex(0.0D, 0.0D)
                .color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F)
                .endVertex();
        buffer.pos(x, y + 16.0D, 0.0D).tex(0.0D, 1.0D)
                .color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F)
                .endVertex();
        buffer.pos(x + 16.0D, y + 16.0D, 0.0D).tex(1.0D, 1.0D)
                .color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F)
                .endVertex();
        buffer.pos(x + 16.0D, y, 0.0D).tex(1.0D, 0.0D)
                .color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F)
                .endVertex();
        tessellator.draw();
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        DecimalFormat myFormatter = new DecimalFormat("#######.##");
        String am = myFormatter.format(amount);
        (Minecraft.getMinecraft()).fontRenderer.drawString(am, 24 + x * 2, 32 -
                (Minecraft.getMinecraft()).fontRenderer.FONT_HEIGHT + y * 2, 16777215);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    public static RayTraceResult rayTrace(World worldIn, EntityPlayer playerIn, boolean useLiquids) {
        float f = playerIn.rotationPitch;
        float f1 = playerIn.rotationYaw;
        double d0 = playerIn.posX;
        double d1 = playerIn.posY + playerIn.getEyeHeight();
        double d2 = playerIn.posZ;
        Vec3d vec3d = new Vec3d(d0, d1, d2);
        float f2 = MathHelper.cos(-f1 * 0.017453292F - 3.1415927F);
        float f3 = MathHelper.sin(-f1 * 0.017453292F - 3.1415927F);
        float f4 = -MathHelper.cos(-f * 0.017453292F);
        float f5 = MathHelper.sin(-f * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d3 = playerIn.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
        Vec3d vec3d1 = vec3d.add(f6 * d3, f5 * d3, f7 * d3);
        return worldIn.rayTraceBlocks(vec3d, vec3d1, useLiquids, !useLiquids, false);
    }
}