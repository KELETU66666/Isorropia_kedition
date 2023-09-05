 package fr.wind_blade.isorropia.client.fx;
 
 import java.awt.Color;
 import net.minecraft.client.particle.Particle;
 import net.minecraft.client.renderer.BufferBuilder;
 import net.minecraft.entity.Entity;
 import net.minecraft.util.math.MathHelper;
 import net.minecraft.world.World;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 import org.lwjgl.opengl.GL11;
 
 
 @SideOnly(Side.CLIENT)
 public class FXEssentiaBubble
   extends Particle
 {
/* 18 */   private int count = 0;
   private int delay = 0;
/* 20 */   public int particle = 24;
 
   
   public FXEssentiaBubble(World par1World, double par2, double par4, double par6, int count, int color, float scale, int delay) {
/* 24 */     super(par1World, par2, par4, par6, 0.0D, 0.0D, 0.0D);
     this.particleBlue = 0.6F;
      this.particleGreen = 0.6F;
      this.particleRed = 0.6F;
      this.particleScale = (MathHelper.sin(count / 2.0F) * 0.1F + 1.0F) * scale;
      this.delay = delay;
/* 30 */     this.count = count;
/* 31 */     this.particleMaxAge = 20 + this.rand.nextInt(20);
/* 32 */     this.motionY = (0.025F + MathHelper.sin(count / 3.0F) * 0.002F);
/* 33 */     this.motionZ = 0.0D;
/* 34 */     this.motionX = 0.0D;
/* 35 */     Color c = new Color(color);
/* 36 */     float mr = c.getRed() / 255.0F * 0.2F;
/* 37 */     float mg = c.getGreen() / 255.0F * 0.2F;
/* 38 */     float mb = c.getBlue() / 255.0F * 0.2F;
/* 39 */     this.particleRed = c.getRed() / 255.0F - mr + this.rand.nextFloat() * mr;
/* 40 */     this.particleGreen = c.getGreen() / 255.0F - mg + this.rand.nextFloat() * mg;
      this.particleBlue = c.getBlue() / 255.0F - mb + this.rand.nextFloat() * mb;
/* 42 */     this.particleGravity = 0.2F;
/* 43 */     this.canCollide = false;
   }
 
 
   
   public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
/* 49 */     if (this.delay > 0) {
       return;
     }
     
/* 53 */     float t2 = 0.5625F;
/* 54 */     float t3 = 0.625F;
/* 55 */     float t4 = 0.0625F;
/* 56 */     float t5 = 0.125F;
/* 57 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
/* 58 */     int part = this.particle + this.particleAge % 16;
/* 59 */     float s = MathHelper.sin((this.particleAge - this.count) / 5.0F) * 0.25F + 1.0F;
/* 60 */     float var12 = 0.1F * this.particleScale * s;
/* 61 */     float var13 = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX);
/* 62 */     float var14 = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY);
/* 63 */     float var15 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ);
/* 64 */     float var16 = 1.0F;
     
/* 66 */     buffer.color(this.particleRed * var16, this.particleGreen * var16, this.particleBlue * var16, 0.5F);
     
/* 68 */     buffer.pos((var13 - rotationX * var12 - rotationXY * var12), (var14 - rotationZ * var12), (var15 - rotationYZ * var12 - rotationXZ * var12))
/* 69 */       .tex(t2, t5).endVertex();
/* 70 */     buffer.pos((var13 - rotationX * var12 + rotationXY * var12), (var14 + rotationZ * var12), (var15 - rotationYZ * var12 + rotationXZ * var12))
/* 71 */       .tex(t3, t5).endVertex();
/* 72 */     buffer.pos((var13 + rotationX * var12 + rotationXY * var12), (var14 + rotationZ * var12), (var15 + rotationYZ * var12 + rotationXZ * var12))
/* 73 */       .tex(t2, t4).endVertex();
/* 74 */     buffer.pos((var13 + rotationX * var12 - rotationXY * var12), (var14 - rotationZ * var12), (var15 + rotationXY * var12 - rotationXZ * var12))
/* 75 */       .tex(t2, t4).endVertex();
   }
 
   
   public int getFXLayer() {
/* 80 */     return 1;
   }
 
   
   public void onUpdate() {
/* 85 */     this.prevPosX = this.posX;
/* 86 */     this.prevPosY = this.posY;
/* 87 */     this.prevPosZ = this.posZ;
/* 88 */     if (this.delay > 0) {
/* 89 */       this.delay--;
       return;
     } 
/* 92 */     if (this.particleAge++ >= this.particleMaxAge) {
/* 93 */       setExpired();
       return;
     } 
/* 96 */     this.motionY += 0.00125D;
/* 97 */     this.particleScale *= 1.05F;
/* 98 */     move(this.motionX, this.motionY, this.motionZ);
/* 99 */     this.motionY *= 0.985D;
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\client\fx\FXEssentiaBubble.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */