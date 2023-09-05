 package fr.wind_blade.isorropia.client.renderer.tiles;
 
 import fr.wind_blade.isorropia.common.tiles.TileModifiedMatrix;
 import fr.wind_blade.isorropia.common.tiles.TileVat;
 import java.util.Random;
 import net.minecraft.block.state.IBlockState;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.particle.Particle;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.OpenGlHelper;
 import net.minecraft.client.renderer.RenderHelper;
 import net.minecraft.client.renderer.Tessellator;
 import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
 import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
 import net.minecraft.tileentity.TileEntity;
 import net.minecraft.util.ResourceLocation;
 import net.minecraft.util.math.MathHelper;
 import net.minecraftforge.fml.client.FMLClientHandler;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 import org.lwjgl.opengl.GL11;
 import thaumcraft.api.blocks.BlocksTC;
 import thaumcraft.client.fx.ParticleEngine;
 import thaumcraft.client.fx.particles.FXGeneric;
 import thaumcraft.client.renderers.models.ModelCube;
 
 @SideOnly(Side.CLIENT)
 public class RenderModifiedMatrix
   extends TileEntitySpecialRenderer<TileModifiedMatrix>
 {
/*  31 */   private final ModelCube model = new ModelCube(0);
/*  32 */   private final ModelCube model_over = new ModelCube(32);
/*  33 */   private static final ResourceLocation tex1 = new ResourceLocation("thaumcraft", "textures/blocks/infuser_normal.png");
   
/*  35 */   private static final ResourceLocation tex2 = new ResourceLocation("thaumcraft", "textures/blocks/infuser_ancient.png");
   
/*  37 */   private static final ResourceLocation tex3 = new ResourceLocation("thaumcraft", "textures/blocks/infuser_eldritch.png");
 
   
   private void drawHalo(TileEntity is, double x, double y, double z, float par8, int count) {
/*  41 */     GL11.glPushMatrix();
/*  42 */     GL11.glTranslated(x + 0.5D, y + 0.5D, z + 0.5D);
/*  43 */     int q = !(FMLClientHandler.instance().getClient()).gameSettings.fancyGraphics ? 10 : 20;
/*  44 */     Tessellator tessellator = Tessellator.getInstance();
/*  45 */     RenderHelper.disableStandardItemLighting();
/*  46 */     float f1 = count / 500.0F;
/*  47 */     float f2 = 0.0F;
/*  48 */     Random random = new Random(245L);
/*  49 */     GL11.glDisable(3553);
/*  50 */     GL11.glShadeModel(7425);
/*  51 */     GL11.glEnable(3042);
/*  52 */     GL11.glBlendFunc(770, 1);
/*  53 */     GL11.glDisable(3008);
/*  54 */     GL11.glEnable(2884);
/*  55 */     GL11.glDepthMask(false);
/*  56 */     GL11.glPushMatrix();
/*  57 */     for (int i = 0; i < q; i++) {
/*  58 */       GL11.glRotatef(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
/*  59 */       GL11.glRotatef(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
/*  60 */       GL11.glRotatef(random.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
/*  61 */       GL11.glRotatef(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
/*  62 */       GL11.glRotatef(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
/*  63 */       GL11.glRotatef(random.nextFloat() * 360.0F + f1 * 360.0F, 0.0F, 0.0F, 1.0F);
/*  64 */       tessellator.getBuffer().begin(6, DefaultVertexFormats.POSITION_COLOR);
/*  65 */       float fa = random.nextFloat() * 20.0F + 5.0F + f2 * 10.0F;
/*  66 */       float f4 = random.nextFloat() * 2.0F + 1.0F + f2 * 2.0F;
/*  67 */       tessellator.getBuffer().pos(0.0D, 0.0D, 0.0D).color(255, 255, 255, (int)(255.0F * (1.0F - f1))).endVertex();
/*  68 */       tessellator.getBuffer().pos(-0.866D * f4, (fa /= 20.0F / Math.min(count, 50) / 50.0F), (-0.5F * (f4 /= 20.0F / 
/*  69 */           Math.min(count, 50) / 50.0F))).color(255, 0, 255, 0).endVertex();
/*  70 */       tessellator.getBuffer().pos(0.866D * f4, fa, (-0.5F * f4)).color(255, 0, 255, 0).endVertex();
/*  71 */       tessellator.getBuffer().pos(0.0D, fa, (f4)).color(255, 0, 255, 0).endVertex();
/*  72 */       tessellator.getBuffer().pos(-0.866D * f4, fa, (-0.5F * f4)).color(255, 0, 255, 0).endVertex();
/*  73 */       tessellator.draw();
     } 
/*  75 */     GL11.glPopMatrix();
/*  76 */     GL11.glDepthMask(true);
/*  77 */     GL11.glDisable(2884);
/*  78 */     GL11.glDisable(3042);
/*  79 */     GL11.glShadeModel(7424);
/*  80 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/*  81 */     GL11.glEnable(3553);
/*  82 */     GL11.glEnable(3008);
/*  83 */     RenderHelper.enableStandardItemLighting();
/*  84 */     GL11.glBlendFunc(770, 771);
/*  85 */     GL11.glPopMatrix();
   }
   
/*  88 */   float dt = 0.1F;
 
 
 
 
   
   public void renderInfusionMatrix(TileModifiedMatrix te, double par2, double par4, double par6, float par8, int destroyStage) {
/*  95 */     GL11.glPushMatrix();
/*  96 */     ResourceLocation t = tex1;
     GL11.glTranslatef((float)par2 + 0.5F, (float)par4 + 0.5F, (float)par6 + 0.5F);
     float ticks = (Minecraft.getMinecraft().getRenderViewEntity()).ticksExisted + par8;
/*  99 */     float inst = 0.0F;
/* 100 */     int craftcount = 0;
/* 101 */     float startup = 0.0F;
/* 102 */     boolean active = false;
/* 103 */     boolean infusing = false;
/* 104 */     if (te.getMaster() != null && te.getMaster().getWorld() != null) {
       
/* 106 */       IBlockState bs = te.getMaster().getWorld().getBlockState(te.getMaster().getPos().add(-1, -2, -1));
/* 107 */       if (bs.getBlock() == BlocksTC.pillarAncient) {
/* 108 */         t = tex2;
       }
/* 110 */       if (bs.getBlock() == BlocksTC.pillarEldritch) {
/* 111 */         t = tex3;
       }
       
/* 114 */       TileVat curative = te.getMaster();
/* 115 */       inst = curative.getRawStability();
/* 116 */       craftcount = curative.getCraftCount();
/* 117 */       startup = curative.getStartUp();
/* 118 */       active = curative.isActive();
/* 119 */       infusing = curative.isInfusing();
       
/* 121 */       GL11.glRotatef(ticks % 360.0F * startup, 0.0F, 1.0F, 0.0F);
/* 122 */       GL11.glRotatef(35.0F * startup, 1.0F, 0.0F, 0.0F);
/* 123 */       GL11.glRotatef(45.0F * startup, 0.0F, 0.0F, 1.0F);
       
/* 125 */       if (te.getMaster().isExpunging()) {
/* 126 */         GL11.glRotatef((ticks + par8) * 30.0F, 0.0F, 1.0F, 0.0F);
/* 127 */         float x = te.getPos().getX() + 0.2F + (te.getWorld()).rand.nextFloat() * 0.6F;
/* 128 */         float y = te.getPos().getY() + 0.2F + (te.getWorld()).rand.nextFloat() * 0.6F;
/* 129 */         float z = te.getPos().getZ() + 0.2F + (te.getWorld()).rand.nextFloat() * 0.6F;
         
/* 131 */         FXGeneric fb = new FXGeneric(getWorld(), x, y, z);
/* 132 */         fb.setMaxAge(100 + (te.getWorld()).rand.nextInt(60));
         fb.setRBGColorF(1.0F, 0.3F, 0.9F);
/* 134 */         fb.setAlphaF(0.5F, 0.0F);
/* 135 */         fb.setGridSize(16);
/* 136 */         fb.setParticles(56, 1, 1);
/* 137 */         fb.setScale(2.0F, 5.0F);
/* 138 */         fb.setLayer(1);
/* 139 */         fb.setSlowDown(1.0D);
/* 140 */         fb.setWind(0.001D);
/* 141 */         fb.setRotationSpeed((te.getWorld()).rand.nextFloat(), (getWorld()).rand.nextBoolean() ? -1.0F : 1.0F);
/* 142 */         ParticleEngine.addEffect(getWorld(), fb);
       } 
     } 
     
/* 146 */     bindTexture(t);
/* 147 */     if (destroyStage >= 0) {
/* 148 */       bindTexture(DESTROY_STAGES[destroyStage]);
/* 149 */       GlStateManager.matrixMode(5890);
/* 150 */       GlStateManager.pushMatrix();
/* 151 */       GlStateManager.scale(4.0F, 4.0F, 1.0F);
/* 152 */       GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
/* 153 */       GlStateManager.matrixMode(5888);
     } 
     
/* 156 */     float instability = Math.min(6.0F, 1.0F + ((inst < 0.0F) ? (-inst * 0.66F) : 1.0F) * 
/* 157 */         Math.min(craftcount, 50) / 50.0F);
/* 158 */     float b1 = 0.0F;
/* 159 */     float b2 = 0.0F;
/* 160 */     float b3 = 0.0F;
/* 161 */     int aa = 0;
/* 162 */     int bb = 0;
/* 163 */     int cc = 0; int a;
/* 164 */     for (a = 0; a < 2; a++) {
/* 165 */       for (int b = 0; b < 2; b++) {
/* 166 */         for (int c = 0; c < 2; c++) {
/* 167 */           if (active) {
/* 168 */             b1 = MathHelper.sin((ticks + (a * 10)) / 15.0F) * 0.01F * startup * instability;
/* 169 */             b2 = MathHelper.sin((ticks + (b * 10)) / 14.0F) * 0.01F * startup * instability;
/* 170 */             b3 = MathHelper.sin((ticks + (c * 10)) / 13.0F) * 0.01F * startup * instability;
           } 
/* 172 */           aa = (a == 0) ? -1 : 1;
/* 173 */           bb = (b == 0) ? -1 : 1;
/* 174 */           cc = (c == 0) ? -1 : 1;
/* 175 */           GL11.glPushMatrix();
/* 176 */           GL11.glTranslatef(b1 + aa * 0.25F, b2 + bb * 0.25F, b3 + cc * 0.25F);
/* 177 */           if (a > 0) {
/* 178 */             GL11.glRotatef(90.0F, a, 0.0F, 0.0F);
           }
/* 180 */           if (b > 0) {
/* 181 */             GL11.glRotatef(90.0F, 0.0F, b, 0.0F);
           }
/* 183 */           if (c > 0) {
/* 184 */             GL11.glRotatef(90.0F, 0.0F, 0.0F, c);
           }
/* 186 */           GL11.glScaled(0.45D, 0.45D, 0.45D);
/* 187 */           this.model.render();
/* 188 */           GL11.glPopMatrix();
         } 
       } 
     } 
/* 192 */     if (active) {
/* 193 */       GL11.glPushMatrix();
/* 194 */       GL11.glAlphaFunc(516, 0.003921569F);
/* 195 */       GL11.glEnable(3042);
/* 196 */       GL11.glBlendFunc(770, 1);
/* 197 */       for (a = 0; a < 2; a++) {
/* 198 */         for (int b = 0; b < 2; b++) {
/* 199 */           for (int c = 0; c < 2; c++) {
/* 200 */             b1 = MathHelper.sin((ticks + (a * 10)) / 15.0F) * 0.01F * startup * instability;
/* 201 */             b2 = MathHelper.sin((ticks + (b * 10)) / 14.0F) * 0.01F * startup * instability;
/* 202 */             b3 = MathHelper.sin((ticks + (c * 10)) / 13.0F) * 0.01F * startup * instability;
/* 203 */             aa = (a == 0) ? -1 : 1;
/* 204 */             bb = (b == 0) ? -1 : 1;
/* 205 */             cc = (c == 0) ? -1 : 1;
/* 206 */             GL11.glPushMatrix();
/* 207 */             GL11.glTranslatef(b1 + aa * 0.25F, b2 + bb * 0.25F, b3 + cc * 0.25F);
/* 208 */             if (a > 0) {
/* 209 */               GL11.glRotatef(90.0F, a, 0.0F, 0.0F);
             }
/* 211 */             if (b > 0) {
/* 212 */               GL11.glRotatef(90.0F, 0.0F, b, 0.0F);
             }
/* 214 */             if (c > 0) {
/* 215 */               GL11.glRotatef(90.0F, 0.0F, 0.0F, c);
             }
/* 217 */             GL11.glScaled(0.45D, 0.45D, 0.45D);
/* 218 */             int j = 15728880;
/* 219 */             int k = j % 65536;
/* 220 */             int l = j / 65536;
/* 221 */             OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, k, l);
/* 222 */             GL11.glColor4f(0.8F, 0.1F, 1.0F, (
/* 223 */                 MathHelper.sin((ticks + (a * 2) + (b * 3) + (c * 4)) / 4.0F) * 0.1F + 0.2F) * startup);
             
/* 225 */             this.model_over.render();
/* 226 */             GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 227 */             GL11.glPopMatrix();
           } 
         } 
       } 
/* 231 */       GL11.glBlendFunc(770, 771);
/* 232 */       GL11.glDisable(3042);
/* 233 */       GL11.glAlphaFunc(516, 0.1F);
/* 234 */       GL11.glPopMatrix();
     } 
/* 236 */     if (destroyStage >= 0) {
/* 237 */       GlStateManager.matrixMode(5890);
/* 238 */       GlStateManager.popMatrix();
/* 239 */       GlStateManager.matrixMode(5888);
     } 
/* 241 */     GL11.glPopMatrix();
/* 242 */     if (infusing) {
/* 243 */       drawHalo(te, par2, par4, par6, par8, craftcount);
     }
/* 245 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
   }
 
 
   
   public void render(TileModifiedMatrix te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
/* 251 */     super.render(te, x, y, z, partialTicks, destroyStage, alpha);
/* 252 */     renderInfusionMatrix(te, x, y, z, partialTicks, destroyStage);
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\client\renderer\tiles\RenderModifiedMatrix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */