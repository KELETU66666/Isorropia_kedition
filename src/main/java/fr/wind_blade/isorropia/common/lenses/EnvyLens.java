 package fr.wind_blade.isorropia.common.lenses;
 
 import fr.wind_blade.isorropia.common.Common;
 import fr.wind_blade.isorropia.common.IsorropiaAPI;
 import fr.wind_blade.isorropia.common.capabilities.LivingCapability;
 import fr.wind_blade.isorropia.common.items.misc.ItemLens;
 import fr.wind_blade.isorropia.common.network.CapabilityMessage;
 import java.awt.Color;
 import java.lang.reflect.Field;
 import java.lang.reflect.Method;
 import java.util.List;
 import java.util.Random;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.gui.ScaledResolution;
 import net.minecraft.client.renderer.BufferBuilder;
 import net.minecraft.client.renderer.Tessellator;
 import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.EntityLiving;
 import net.minecraft.entity.EntityLivingBase;
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
 import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 import org.lwjgl.opengl.GL11;
 import thaumcraft.common.lib.utils.EntityUtils;
 
 
 public class EnvyLens
   extends Lens
 {
/*  42 */   public static final ResourceLocation ENVY = new ResourceLocation("isorropia", "textures/aspects/invidia_hud.png");
   
/*  44 */   private static final Method getLootTable = ObfuscationReflectionHelper.findMethod(EntityLiving.class, "func_184647_J", ResourceLocation.class, new Class[0]);
   
/*  46 */   private static final Field getPool = ObfuscationReflectionHelper.findField(LootTable.class, "field_186466_c");
   
   public EnvyLens(ItemLens lensIn) {
/*  49 */     super(lensIn);
   }
 
   
   public void handleTicks(World worldIn, EntityPlayer playerIn, boolean doubleLens) {
/*  54 */     if (!worldIn.isRemote) {
/*  55 */       Entity entity = EntityUtils.getPointedEntity(worldIn, (Entity)playerIn, 0.0D, 1.5D, 5.0F, true);
/*  56 */       if (entity instanceof EntityLiving) {
/*  57 */         EntityLiving living = (EntityLiving)entity;
/*  58 */         LivingCapability cap = (LivingCapability)living.getCapability(Common.LIVING_CAPABILITY, null);
         
/*  60 */         if (cap.hasLooted)
           return; 
         try {
/*  63 */           ResourceLocation lootTableLocation = (ResourceLocation)getLootTable.invoke(living, new Object[0]);
/*  64 */           if (lootTableLocation == null) {
/*  65 */             cap.hasLooted = true;
/*  66 */             Common.INSTANCE.sendTo((IMessage)new CapabilityMessage((EntityLivingBase)living, cap.serializeNBT()), (EntityPlayerMP)playerIn);
             
             return;
           } 
           
/*  71 */           LootTableManager manager = worldIn.getLootTableManager();
           
/*  73 */           if (manager == null) {
             return;
           }
/*  76 */           LootTable lootTable = manager.getLootTableFromLocation(lootTableLocation);
           
/*  78 */           if (lootTable == null) {
/*  79 */             cap.hasLooted = true;
/*  80 */             Common.INSTANCE.sendTo((IMessage)new CapabilityMessage((EntityLivingBase)living, cap.serializeNBT()), (EntityPlayerMP)playerIn);
             
             return;
           } 
           
/*  85 */           getPool.setAccessible(true);
/*  86 */           List<LootPool> pools = (List<LootPool>)getPool.get(lootTable);
           
/*  88 */           if (pools == null || pools.isEmpty()) {
/*  89 */             cap.hasLooted = true;
/*  90 */             Common.INSTANCE.sendTo((IMessage)new CapabilityMessage((EntityLivingBase)living, cap.serializeNBT()), (EntityPlayerMP)playerIn);
             
             return;
           } 
           
/*  95 */           cap.envy++;
/*  96 */           if (cap.envy > living.getHealth() * 20.0F) {
             
             LootContext.Builder builder = (new LootContext.Builder((WorldServer)worldIn)).withLootedEntity((Entity)living);
/*  99 */             for (ItemStack stack : lootTable.generateLootForPools(new Random(), builder.build())) {
/* 100 */               living.entityDropItem(stack, 0.0F);
             }
             
/* 103 */             cap.hasLooted = true;
           } 
/* 105 */           Common.INSTANCE.sendTo((IMessage)new CapabilityMessage((EntityLivingBase)living, cap.serializeNBT()), (EntityPlayerMP)playerIn);
         }
/* 107 */         catch (IllegalAccessException|IllegalArgumentException|java.lang.reflect.InvocationTargetException e) {
/* 108 */           e.printStackTrace();
         } 
       } 
     } 
   }
 
 
   
   @SideOnly(Side.CLIENT)
   public void handleRenderGameOverlay(World worldIn, EntityPlayer playerIn, ScaledResolution resolution, boolean doubleLens, float partialTicks) {
/* 118 */     Entity entity = EntityUtils.getPointedEntity(worldIn, (Entity)playerIn, 1.0D, 5.0D, 5.0F, true);
/* 119 */     if (entity instanceof EntityLiving) {
/* 120 */       EntityLiving living = (EntityLiving)entity;
/* 121 */       LivingCapability cap = (LivingCapability)living.getCapability(Common.LIVING_CAPABILITY, null);
       
/* 123 */       if (!cap.hasLooted && resolution != null) {
/* 124 */         renderEnvyHUD(resolution, entity.getEntityId());
       }
     } 
   }
 
 
   
   public void handleRenderWorldLast(World worldIn, EntityPlayer playerIn, boolean doubleLens, float partialTicks) {}
 
 
   
   public void handleRemoval(World worldIn, EntityPlayer playerIn) {}
 
 
   
   @SideOnly(Side.CLIENT)
   private void renderEnvyHUD(ScaledResolution scaledResolution, int entityId) {
/* 141 */     Entity entity = (Minecraft.getMinecraft()).world.getEntityByID(entityId);
/* 142 */     if (entity instanceof EntityLiving) {
/* 143 */       double x = scaledResolution.getScaledWidth_double() - 16.0D;
/* 144 */       double y = scaledResolution.getScaledHeight_double() - 16.0D;
       
/* 146 */       double pourcentage = ((((LivingCapability)entity.getCapability(Common.LIVING_CAPABILITY, null)).envy * 100) / ((EntityLiving)entity).getHealth() * 20.0F);
/* 147 */       if (pourcentage <= 100.0D) {
/* 148 */         double p = pourcentage * 8.0D / 100.0D;
/* 149 */         double u = 8.0D - p;
/* 150 */         double i = 1.0D - pourcentage / 100.0D;
/* 151 */         Color color = new Color(IsorropiaAPI.ENVY.getColor());
/* 152 */         GL11.glPushMatrix();
/* 153 */         GL11.glAlphaFunc(516, 0.003921569F);
/* 154 */         GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 0.5F);
/* 155 */         Minecraft.getMinecraft().getTextureManager().bindTexture(ENVY);
/* 156 */         Tessellator tessellator = Tessellator.getInstance();
/* 157 */         BufferBuilder buffer = tessellator.getBuffer();
/* 158 */         buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 159 */         buffer.putColorRGB_F(1.0F, 1.0F, 1.0F, 0);
/* 160 */         buffer.pos(x, y, 0.0D).tex(0.0D, 0.0D).color(1.0F, 1.0F, 1.0F, 0.5F).endVertex();
/* 161 */         buffer.pos(x, y + 8.0D, 0.0D).tex(0.0D, 1.0D).color(1.0F, 1.0F, 1.0F, 0.5F).endVertex();
/* 162 */         buffer.pos(x + 16.0D, y + 8.0D, 0.0D).tex(1.0D, 1.0D).color(1.0F, 1.0F, 1.0F, 0.5F).endVertex();
/* 163 */         buffer.pos(x + 16.0D, y, 0.0D).tex(1.0D, 0.0D).color(1.0F, 1.0F, 1.0F, 0.5F).endVertex();
/* 164 */         tessellator.draw();
/* 165 */         tessellator = Tessellator.getInstance();
/* 166 */         buffer = tessellator.getBuffer();
/* 167 */         buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 168 */         buffer.putColorRGB_F(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 0);
         
/* 170 */         buffer.pos(x, y + u, 0.0D).tex(0.0D, i).color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color
/* 171 */             .getBlue() / 255.0F, color.getAlpha() / 255.0F).endVertex();
/* 172 */         buffer.pos(x, y + 8.0D, 0.0D).tex(0.0D, 1.0D).color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color
/* 173 */             .getBlue() / 255.0F, color.getAlpha() / 255.0F).endVertex();
/* 174 */         buffer.pos(x + 16.0D, y + 8.0D, 0.0D).tex(1.0D, 1.0D).color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color
/* 175 */             .getBlue() / 255.0F, color.getAlpha() / 255.0F).endVertex();
/* 176 */         buffer.pos(x + 16.0D, y + u, 0.0D).tex(1.0D, i).color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color
/* 177 */             .getBlue() / 255.0F, color.getAlpha() / 255.0F).endVertex();
/* 178 */         tessellator.draw();
/* 179 */         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 180 */         GL11.glAlphaFunc(516, 0.003921569F);
/* 181 */         GL11.glPopMatrix();
       } 
     } 
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\lenses\EnvyLens.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */