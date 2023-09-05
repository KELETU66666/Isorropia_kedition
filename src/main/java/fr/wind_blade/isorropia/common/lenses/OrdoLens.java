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
 
 public class OrdoLens
   extends Lens
 {
   private static float nameSize;
   
   public OrdoLens(ItemLens lensIn) {
/*  41 */     super(lensIn);
   }
 
   
   public void handleTicks(World worldIn, EntityPlayer playerIn, boolean doubleLens) {
/*  46 */     if (worldIn.isRemote) {
       return;
     }
/*  49 */     Entity target = EntityUtils.getPointedEntity(worldIn, (Entity)playerIn, 1.0D, 5.0D, 0.0F, true);
     
/*  51 */     if (target != null && ScanningManager.isThingStillScannable(playerIn, target)) {
/*  52 */       ScanningManager.scanTheThing(playerIn, target);
     } else {
       
/*  55 */       RayTraceResult mop = rayTrace(worldIn, playerIn, true);
/*  56 */       if (mop != null && mop.getBlockPos() != null && 
/*  57 */         ScanningManager.isThingStillScannable(playerIn, mop.getBlockPos())) {
/*  58 */         ScanningManager.scanTheThing(playerIn, mop.getBlockPos());
       }
     } 
   }
 
 
 
 
   
   @SideOnly(Side.CLIENT)
   public void handleRenderGameOverlay(World worldIn, EntityPlayer playerIn, ScaledResolution resolution, boolean doubleLens, float partialTicks) {
/*  69 */     if ((Minecraft.getMinecraft()).gameSettings.thirdPersonView != 0) {
       return;
     }
/*  72 */     Entity target = EntityUtils.getPointedEntity(worldIn, (Entity)playerIn, 1.0D, 5.0D, 0.0F, true);
/*  73 */     if (target != null) {
/*  74 */       Entity entity = RenderEventHandler.thaumTarget;
/*  75 */       if (entity == null || entity != target) {
/*  76 */         RenderEventHandler.tagscale = 0.0F;
/*  77 */         nameSize = 0.0F;
       } 
/*  79 */       RenderEventHandler.thaumTarget = target;
       
/*  81 */       if (ScanningManager.isThingStillScannable(playerIn, target)) {
/*  82 */         FXDispatcher.INSTANCE.scanHighlight(target);
       }
       
       return;
     } 
/*  87 */     RayTraceResult mop = rayTrace(worldIn, playerIn, true);
/*  88 */     if (mop != null && mop.getBlockPos() != null && mop.typeOfHit == RayTraceResult.Type.BLOCK) {
 
 
 
 
       
/*  94 */       List<EntityItem> itemstacks = (Minecraft.getMinecraft()).world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(new BlockPos(mop
/*  95 */               .getBlockPos().getX(), mop.getBlockPos().getY() + 1, mop
/*  96 */               .getBlockPos().getZ())));
       if (!itemstacks.isEmpty() && !((EntityItem)itemstacks.get(0)).getItem().isEmpty()) {
         renderNameAndAspects(resolution, AspectHelper.getObjectAspects(((EntityItem)itemstacks.get(0)).getItem()), ((EntityItem)itemstacks
/*  99 */             .get(0)).getItem().getDisplayName());
         
/* 101 */         if (ScanningManager.isThingStillScannable(playerIn, itemstacks.get(0))) {
/* 102 */           FXDispatcher.INSTANCE.scanHighlight((Entity)itemstacks.get(0));
         }
       }
       else {
         
/* 107 */         RayTraceResult mob = playerIn.rayTrace(5.0D, partialTicks);
/* 108 */         if (mob != null && mop.getBlockPos() != null) {
/* 109 */           Block block = worldIn.getBlockState(mop.getBlockPos()).getBlock();
/* 110 */           renderNameAndAspects(resolution, AspectHelper.getObjectAspects(new ItemStack(block)), block
/* 111 */               .getLocalizedName());
           
/* 113 */           if (ScanningManager.isThingStillScannable(playerIn, mob.getBlockPos())) {
/* 114 */             FXDispatcher.INSTANCE.scanHighlight(mop.getBlockPos());
           }
         } 
       } 
     } 
   }
 
 
 
   
   @SideOnly(Side.CLIENT)
   public void handleRenderWorldLast(World worldIn, EntityPlayer playerIn, boolean doubleLens, float partialTicks) {
/* 126 */     Entity entity = RenderEventHandler.thaumTarget;
/* 127 */     if (entity != null) {
       
/* 129 */       if (entity.isDead) {
/* 130 */         RenderEventHandler.thaumTarget = null;
         
         return;
       } 
/* 134 */       double playerOffX = playerIn.prevPosX + (playerIn.posX - playerIn.prevPosX) * partialTicks;
/* 135 */       double playerOffY = playerIn.prevPosY + (playerIn.posY - playerIn.prevPosY) * partialTicks;
/* 136 */       double playerOffZ = playerIn.prevPosZ + (playerIn.posZ - playerIn.prevPosZ) * partialTicks;
       
/* 138 */       double eOffX = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
/* 139 */       double eOffY = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks;
/* 140 */       double eOffZ = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;
/* 141 */       AspectList aspects = AspectHelper.getEntityAspects(entity);
       
/* 143 */       GlStateManager.pushMatrix();
       
/* 145 */       GlStateManager.translate(-playerOffX, -playerOffY, -playerOffZ);
/* 146 */       GlStateManager.translate(eOffX, eOffY + entity.height + ((aspects != null && aspects.size() > 0) ? 1.1D : 0.6D), eOffZ);
 
       
/* 149 */       GlStateManager.rotate(-playerIn.rotationYaw, 0.0F, 1.0F, 0.0F);
/* 150 */       GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
       
/* 152 */       if (nameSize < 1.0F)
/* 153 */         nameSize += 0.031F; 
/* 154 */       float size = 0.03F * nameSize;
/* 155 */       GlStateManager.scale(size, size, size);
 
       
/* 158 */       String name = (entity instanceof EntityItem) ? ((EntityItem)entity).getItem().getDisplayName() : entity.getName();
/* 159 */       (Minecraft.getMinecraft()).fontRenderer.drawString(name, 1 - 
/* 160 */           (Minecraft.getMinecraft()).fontRenderer.getStringWidth(name) / 2, 1, Color.WHITE.getRGB());
       
/* 162 */       GlStateManager.popMatrix();
     } 
   }
 
   
   public void handleRemoval(World worldIn, EntityPlayer playerIn) {
/* 168 */     if (worldIn.isRemote) {
/* 169 */       RenderEventHandler.thaumTarget = null;
     }
   }
   
   @SideOnly(Side.CLIENT)
   public static void renderNameAndAspects(ScaledResolution resolution, AspectList aspects, String text) {
/* 175 */     int w = resolution.getScaledWidth();
/* 176 */     int h = resolution.getScaledHeight();
     
/* 178 */     if (aspects != null && aspects.size() > 0) {
/* 179 */       int num = 0;
/* 180 */       int yOff = 0;
/* 181 */       int thisRow = 0;
/* 182 */       int size = 18;
/* 183 */       if (aspects.size() - num < 5) {
/* 184 */         thisRow = aspects.size() - num;
       } else {
/* 186 */         thisRow = 5;
       } 
       
/* 189 */       for (Aspect aspect : aspects.getAspects()) {
/* 190 */         yOff = num / 5 * size;
/* 191 */         drawAspectTag(aspect, aspects.getAmount(aspect), w / 2 - size * thisRow / 2 + size * num % 5, h / 2 + 16 + yOff);
         
/* 193 */         if (++num % 5 == 0)
         {
           
/* 196 */           if (aspects.size() - num < 5) {
/* 197 */             thisRow = aspects.size() - num;
           } else {
/* 199 */             thisRow = 5;
           } 
         }
       } 
     } 
/* 204 */     if (text.length() > 0) {
/* 205 */       (Minecraft.getMinecraft()).ingameGUI.drawString((Minecraft.getMinecraft()).fontRenderer, text, w / 2 - 
/* 206 */           (Minecraft.getMinecraft()).fontRenderer.getStringWidth(text) / 2, h / 2 - 16, 16777215);
     }
   }
   
   @SideOnly(Side.CLIENT)
   public static void drawAspectTag(Aspect aspect, int amount, int x, int y) {
/* 212 */     Color color = new Color(aspect.getColor());
/* 213 */     GL11.glPushMatrix();
/* 214 */     GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 0.5F);
/* 215 */     Minecraft.getMinecraft().getTextureManager().bindTexture(aspect.getImage());
/* 216 */     Tessellator tessellator = Tessellator.getInstance();
/* 217 */     BufferBuilder buffer = tessellator.getBuffer();
/* 218 */     buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 219 */     buffer.putColorRGB_F(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 0);
/* 220 */     buffer.pos(x, y, 0.0D).tex(0.0D, 0.0D)
/* 221 */       .color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F)
/* 222 */       .endVertex();
/* 223 */     buffer.pos(x, y + 16.0D, 0.0D).tex(0.0D, 1.0D)
/* 224 */       .color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F)
/* 225 */       .endVertex();
/* 226 */     buffer.pos(x + 16.0D, y + 16.0D, 0.0D).tex(1.0D, 1.0D)
/* 227 */       .color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F)
/* 228 */       .endVertex();
/* 229 */     buffer.pos(x + 16.0D, y, 0.0D).tex(1.0D, 0.0D)
/* 230 */       .color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F)
/* 231 */       .endVertex();
/* 232 */     tessellator.draw();
/* 233 */     GL11.glScalef(0.5F, 0.5F, 0.5F);
/* 234 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 235 */     DecimalFormat myFormatter = new DecimalFormat("#######.##");
/* 236 */     String am = myFormatter.format(amount);
/* 237 */     (Minecraft.getMinecraft()).fontRenderer.drawString(am, 24 + x * 2, 32 - 
/* 238 */         (Minecraft.getMinecraft()).fontRenderer.FONT_HEIGHT + y * 2, 16777215);
/* 239 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 240 */     GL11.glPopMatrix();
   }
   
   public static RayTraceResult rayTrace(World worldIn, EntityPlayer playerIn, boolean useLiquids) {
/* 244 */     float f = playerIn.rotationPitch;
/* 245 */     float f1 = playerIn.rotationYaw;
/* 246 */     double d0 = playerIn.posX;
/* 247 */     double d1 = playerIn.posY + playerIn.getEyeHeight();
/* 248 */     double d2 = playerIn.posZ;
/* 249 */     Vec3d vec3d = new Vec3d(d0, d1, d2);
/* 250 */     float f2 = MathHelper.cos(-f1 * 0.017453292F - 3.1415927F);
/* 251 */     float f3 = MathHelper.sin(-f1 * 0.017453292F - 3.1415927F);
/* 252 */     float f4 = -MathHelper.cos(-f * 0.017453292F);
/* 253 */     float f5 = MathHelper.sin(-f * 0.017453292F);
/* 254 */     float f6 = f3 * f4;
/* 255 */     float f7 = f2 * f4;
/* 256 */     double d3 = playerIn.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
/* 257 */     Vec3d vec3d1 = vec3d.add(f6 * d3, f5 * d3, f7 * d3);
/* 258 */     return worldIn.rayTraceBlocks(vec3d, vec3d1, useLiquids, !useLiquids, false);
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\lenses\OrdoLens.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */