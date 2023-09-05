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
/*  25 */   public static final ResourceLocation TEX = new ResourceLocation("isorropia", "textures/fx/ripple.png");
/*  26 */   public static final ResourceLocation TEX_UNDEAD = new ResourceLocation("isorropia", "textures/fx/vortex.png");
/*  27 */   public static final ResourceLocation TEX_ELDRITCH = new ResourceLocation("thaumcraft", "textures/misc/vortex.png");
 
   
   public AirLens(ItemLens lensIn) {
/*  31 */     super(lensIn);
   }
 
 
 
   
   public void handleTicks(World worldIn, EntityPlayer playerIn, boolean doubleLens) {}
 
 
 
   
   public void handleRenderGameOverlay(World worldIn, EntityPlayer playerIn, ScaledResolution resolution, boolean doubleLens, float partialTicks) {}
 
 
   
   public void handleRenderWorldLast(World worldIn, EntityPlayer playerIn, boolean doubleLens, float partialTicks) {
/*  47 */     if ((Minecraft.getMinecraft()).gameSettings.thirdPersonView > 0) {
       return;
     }
/*  50 */     List<EntityLivingBase> base = playerIn.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(playerIn.posX - 24.0D, playerIn.posY - 24.0D, playerIn.posZ - 24.0D, playerIn.posX + 24.0D, playerIn.posY + 24.0D, playerIn.posZ + 24.0D), e -> (e != playerIn));
 
 
 
 
     
/*  56 */     for (EntityLivingBase e : base) {
       
/*  58 */       double playerOffX = playerIn.prevPosX + (playerIn.posX - playerIn.prevPosX) * partialTicks;
/*  59 */       double playerOffY = playerIn.prevPosY + (playerIn.posY - playerIn.prevPosY) * partialTicks;
/*  60 */       double playerOffZ = playerIn.prevPosZ + (playerIn.posZ - playerIn.prevPosZ) * partialTicks;
       
/*  62 */       double eOffX = e.prevPosX + (e.posX - e.prevPosX) * partialTicks;
/*  63 */       double eOffY = e.prevPosY + (e.posY - e.prevPosY) * partialTicks;
/*  64 */       double eOffZ = e.prevPosZ + (e.posZ - e.prevPosZ) * partialTicks;
       
/*  66 */       double scale = e.getPositionVector().subtract(playerIn.getPositionVector()).length();
/*  67 */       float sizeOffset = (e.ticksExisted % 16);
       
/*  69 */       double cappedTchebychevDist = Math.min(IRMathHelper.getTchebychevDistance((Entity)e, (Entity)playerIn), 24.0D);
/*  70 */       float alpha = (float)(1.0D - cappedTchebychevDist / 24.0D);
/*  71 */       float size = Math.min(e.height, e.width);
       
/*  73 */       if (e instanceof thaumcraft.api.entities.IEldritchMob) {
/*  74 */         GlStateManager.pushMatrix();
/*  75 */         GL11.glDisable(2929);
/*  76 */         size = (float)(size * 0.8D);
         
/*  78 */         GlStateManager.translate(-playerOffX, -playerOffY, -playerOffZ);
/*  79 */         GlStateManager.translate(eOffX, eOffY + (e.height / 2.0F), eOffZ);
/*  80 */         GlStateManager.rotate(-playerIn.rotationYaw, 0.0F, 1.0F, 0.0F);
/*  81 */         GlStateManager.rotate(playerIn.rotationPitch, 1.0F, 0.0F, 0.0F);
/*  82 */         GlStateManager.rotate((e.ticksExisted % 360), 0.0F, 0.0F, 1.0F);
/*  83 */         UtilsFX.renderQuadCentered(TEX_ELDRITCH, size, 1.0F, 1.0F, 1.0F, -99, 771, alpha);
/*  84 */         GlStateManager.rotate((-(e.ticksExisted % 360) * 2), 0.0F, 0.0F, 1.0F);
/*  85 */         size *= 2.0F;
         
/*  87 */         UtilsFX.renderQuadCentered(TEX_ELDRITCH, size, 1.0F, 1.0F, 1.0F, -99, 771, alpha);
         
/*  89 */         GL11.glEnable(2929);
/*  90 */         GlStateManager.popMatrix(); continue;
       } 
/*  92 */       if (e.isEntityUndead()) {
/*  93 */         size = (float)(size * 1.3D);
/*  94 */         GlStateManager.pushMatrix();
/*  95 */         GL11.glDisable(2929);
         
         GlStateManager.translate(-playerOffX, -playerOffY, -playerOffZ);
         GlStateManager.translate(eOffX, eOffY + (e.height / 2.0F), eOffZ);
/*  99 */         GlStateManager.rotate(-playerIn.rotationYaw, 0.0F, 1.0F, 0.0F);
/* 100 */         GlStateManager.rotate(playerIn.rotationPitch, 1.0F, 0.0F, 0.0F);
/* 101 */         GlStateManager.rotate(-(e.ticksExisted % 360), 0.0F, 0.0F, 1.0F);
/* 102 */         UtilsFX.renderQuadCentered(TEX_UNDEAD, size, 1.0F, 1.0F, 1.0F, -99, 771, alpha);
/* 103 */         size *= 2.0F;
/* 104 */         GlStateManager.rotate((e.ticksExisted % 360 * 2), 0.0F, 0.0F, 1.0F);
         
/* 106 */         UtilsFX.renderQuadCentered(TEX_UNDEAD, size, 1.0F, 1.0F, 1.0F, -99, 771, alpha);
         
/* 108 */         GL11.glEnable(2929);
/* 109 */         GlStateManager.popMatrix(); continue;
       } 
/* 111 */       double numbers = Math.min(48.0D / scale + 1.0D, 4.0D);
       
/* 113 */       for (int i = 0; i < numbers; i++) {
/* 114 */         float numSize = (float)(((i * 16) / (numbers + 1.0D) + sizeOffset) % 16.0D / 12.0D) * size;
         
/* 116 */         GlStateManager.pushMatrix();
/* 117 */         GL11.glDisable(2929);
         
/* 119 */         GlStateManager.translate(-playerOffX, -playerOffY, -playerOffZ);
/* 120 */         GlStateManager.translate(eOffX, eOffY + (e.height / 2.0F), eOffZ);
/* 121 */         GlStateManager.rotate(-playerIn.rotationYaw, 0.0F, 1.0F, 0.0F);
/* 122 */         GlStateManager.rotate(playerIn.rotationPitch, 1.0F, 0.0F, 0.0F);
/* 123 */         UtilsFX.renderQuadCentered(TEX, numSize, 1.0F, 1.0F, 1.0F, -99, 771, alpha);
         
/* 125 */         GL11.glEnable(2929);
/* 126 */         GlStateManager.popMatrix();
       } 
     } 
   }
   
   public void handleRemoval(World worldIn, EntityPlayer playerIn) {}
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\lenses\AirLens.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */