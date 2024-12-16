package fr.wind_blade.isorropia.common.lenses;

import fr.wind_blade.isorropia.common.Common;
import fr.wind_blade.isorropia.common.capabilities.LivingCapability;
import fr.wind_blade.isorropia.common.items.misc.ItemLens;
import fr.wind_blade.isorropia.common.network.CapabilityMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.lib.utils.EntityUtils;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;


public class EnvyLens extends Lens {
    public static final ResourceLocation ENVY = new ResourceLocation("isorropia", "textures/aspects/invidia_hud.png");

    private static final Method getLootTable = ObfuscationReflectionHelper.findMethod(EntityLiving.class, "func_184647_J", ResourceLocation.class);

    private static final Field getPool = ObfuscationReflectionHelper.findField(LootTable.class, "field_186466_c");

    public EnvyLens(ItemLens lensIn) {
        super(lensIn);
    }


    public void handleTicks(World worldIn, EntityPlayer playerIn, boolean doubleLens) {
        if (!worldIn.isRemote) {
            Entity entity = EntityUtils.getPointedEntity(worldIn, playerIn, 0.0D, 1.5D, 5.0F, true);
            if (entity instanceof EntityLiving) {
                EntityLiving living = (EntityLiving) entity;
                LivingCapability cap = living.getCapability(Common.LIVING_CAPABILITY, null);

                if (cap.hasLooted)
                    return;
                try {
                    ResourceLocation lootTableLocation = (ResourceLocation) getLootTable.invoke(living, new Object[0]);
                    if (lootTableLocation == null) {
                        cap.hasLooted = true;
                        Common.INSTANCE.sendTo(new CapabilityMessage(living, cap.serializeNBT()), (EntityPlayerMP) playerIn);

                        return;
                    }

                    LootTableManager manager = worldIn.getLootTableManager();

                    if (manager == null) {
                        return;
                    }
                    LootTable lootTable = manager.getLootTableFromLocation(lootTableLocation);

                    if (lootTable == null) {
                        cap.hasLooted = true;
                        Common.INSTANCE.sendTo(new CapabilityMessage(living, cap.serializeNBT()), (EntityPlayerMP) playerIn);

                        return;
                    }

                    getPool.setAccessible(true);
                    List<LootPool> pools = (List<LootPool>) getPool.get(lootTable);

                    if (pools == null || pools.isEmpty()) {
                        cap.hasLooted = true;
                        Common.INSTANCE.sendTo(new CapabilityMessage(living, cap.serializeNBT()), (EntityPlayerMP) playerIn);

                        return;
                    }

                    cap.envy++;
                    if (cap.envy > living.getHealth() * 20.0F) {

                        LootContext.Builder builder = (new LootContext.Builder((WorldServer) worldIn)).withLootedEntity(living);
                        for (ItemStack stack : lootTable.generateLootForPools(new Random(), builder.build())) {
                            living.entityDropItem(stack, 0.0F);
                        }

                        cap.hasLooted = true;
                    }
                    Common.INSTANCE.sendTo(new CapabilityMessage(living, cap.serializeNBT()), (EntityPlayerMP) playerIn);
                } catch (IllegalAccessException | IllegalArgumentException |
                         java.lang.reflect.InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @SideOnly(Side.CLIENT)
    public void handleRenderGameOverlay(World worldIn, EntityPlayer playerIn, ScaledResolution resolution, boolean doubleLens, float partialTicks) {
        Entity entity = EntityUtils.getPointedEntity(worldIn, playerIn, 1.0D, 5.0D, 5.0F, true);
        if (entity instanceof EntityLiving) {
            EntityLiving living = (EntityLiving) entity;
            LivingCapability cap = living.getCapability(Common.LIVING_CAPABILITY, null);

            if (!cap.hasLooted && resolution != null) {
                renderEnvyHUD(resolution, entity.getEntityId());
            }
        }
    }


    public void handleRenderWorldLast(World worldIn, EntityPlayer playerIn, boolean doubleLens, float partialTicks) {
    }


    public void handleRemoval(World worldIn, EntityPlayer playerIn) {
    }


    @SideOnly(Side.CLIENT)
    private void renderEnvyHUD(ScaledResolution scaledResolution, int entityId) {
        Entity entity = Minecraft.getMinecraft().world.getEntityByID(entityId);
        if (entity instanceof EntityLiving) {
            double x = scaledResolution.getScaledWidth_double() - 16.0D;
            double y = scaledResolution.getScaledHeight_double() - 16.0D;

            double pourcentage = entity.getCapability(Common.LIVING_CAPABILITY, null).envy / ((EntityLiving) entity).getHealth() * 5.15;
            if (pourcentage <= 100.0D) {
                double p = pourcentage * 8.0D / 100.0D;
                double u = 8.0D - p;
                double i = 1.0D - pourcentage / 100.0D;
                Color color = new Color(0x00ba00);
                GL11.glPushMatrix();
                GL11.glAlphaFunc(516, 0.003921569F);
                GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 0.5F);
                Minecraft.getMinecraft().getTextureManager().bindTexture(ENVY);
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder buffer = tessellator.getBuffer();
                buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                buffer.putColorRGB_F(1.0F, 1.0F, 1.0F, 0);
                buffer.pos(x, y, 0.0D).tex(0.0D, 0.0D).color(1.0F, 1.0F, 1.0F, 0.5F).endVertex();
                buffer.pos(x, y + 8.0D, 0.0D).tex(0.0D, 1.0D).color(1.0F, 1.0F, 1.0F, 0.5F).endVertex();
                buffer.pos(x + 16.0D, y + 8.0D, 0.0D).tex(1.0D, 1.0D).color(1.0F, 1.0F, 1.0F, 0.5F).endVertex();
                buffer.pos(x + 16.0D, y, 0.0D).tex(1.0D, 0.0D).color(1.0F, 1.0F, 1.0F, 0.5F).endVertex();
                tessellator.draw();

                Tessellator tessellator1 = Tessellator.getInstance();
                BufferBuilder buffer1 = tessellator1.getBuffer();
                buffer1.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                buffer1.putColorRGB_F(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 0);
                buffer1.pos(x, y + u, 0.0D).tex(0.0D, i).color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F).endVertex();
                buffer1.pos(x, y + 8.0D, 0.0D).tex(0.0D, 1.0D).color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F).endVertex();
                buffer1.pos(x + 16.0D, y + 8.0D, 0.0D).tex(1.0D, 1.0D).color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F).endVertex();
                buffer1.pos(x + 16.0D, y + u, 0.0D).tex(1.0D, i).color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F).endVertex();
                tessellator1.draw();

                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glAlphaFunc(516, 0.003921569F);
                GL11.glPopMatrix();
            }
        }
    }
}