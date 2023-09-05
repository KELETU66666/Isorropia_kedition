 package fr.wind_blade.isorropia.client.fx;
 
 import net.minecraft.client.particle.Particle;
 import net.minecraft.client.renderer.BufferBuilder;
 import net.minecraft.entity.Entity;
 import net.minecraft.world.World;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 import thaumcraft.client.fx.ParticleEngine;
 import thaumcraft.client.fx.particles.FXGeneric;
 
 @SideOnly(Side.CLIENT)
 public class FXCelestialAbsorption
   extends Particle {
   public int blendmode = 1;
/* 16 */   public float length = 1.0F;
/* 17 */   public int particle = 16;
 
   
   public FXCelestialAbsorption(World par1World, double x, double y, double z, float red, float green, float blue, boolean absorb) {
/* 21 */     super(par1World, x, y, z, 0.0D, 0.0D, 0.0D);
/* 22 */     this.particleRed = red;
/* 23 */     this.particleGreen = green;
/* 24 */     this.particleBlue = blue;
     setSize(0.02F, 0.02F);
      this.motionX = 0.0D;
      this.motionY = 0.0D;
      this.motionZ = 0.0D;
      this.particleMaxAge = 3;
/* 30 */     int c = 0;
/* 31 */     while (c < 50) {
       FXGeneric fb;
/* 33 */       boolean sp = (this.rand.nextFloat() < 0.2D);
       
/* 35 */       if (absorb) {
/* 36 */         fb = new FXGeneric(par1World, x, y, z, 0.0D, 0.0D, 0.0D);
/* 37 */         fb.setLoop(true);
/* 38 */         fb.setGravity(sp ? 0.0F : 0.125F);
/* 39 */         fb.setLayer(0);
/* 40 */         fb.setSlowDown(0.995D);
       } else {
/* 42 */         fb = new FXGeneric(par1World, x + this.rand.nextFloat() - 0.5D, y, z - this.rand.nextFloat() + 0.5D, 0.0D, 0.0D, 0.0D);
         
/* 44 */         fb.setParticles(sp ? 320 : 512, 16, 1);
/* 45 */         fb.setGravity(-0.1F);
/* 46 */         fb.setSlowDown(0.95D);
       } 
       
/* 49 */       int age = 30 + this.rand.nextInt(20);
/* 50 */       fb.setMaxAge(age);
/* 51 */       fb.setRBGColorF(red, blue, green);
 
 
 
 
       
/* 57 */       float[] alphas = new float[6 + this.rand.nextInt(age / 3)];
/* 58 */       for (int a = 1; a < alphas.length - 1; a++)
/* 59 */         alphas[a] = this.rand.nextFloat(); 
/* 60 */       alphas[0] = 1.0F;
/* 61 */       fb.setAlphaF(alphas);
/* 62 */       fb.setParticles(sp ? 320 : 512, 16, 1);
/* 63 */       fb.setScale(new float[] { 0.5F, 0.125F });
/* 64 */       fb.setRandomMovementScale(0.0025F, 0.001F, 0.0025F);
       
/* 66 */       ParticleEngine.addEffectWithDelay(par1World, (Particle)fb, 2 + this.rand.nextInt(3));
/* 67 */       c++;
     } 
   }
 
   
   public void onUpdate() {
/* 73 */     this.prevPosX = this.posX;
/* 74 */     this.prevPosY = this.posY;
/* 75 */     this.prevPosZ = this.posZ;
/* 76 */     if (this.particleAge++ >= this.particleMaxAge) {
/* 77 */       setExpired();
     }
   }
   
   public void setRGB(float r, float g, float b) {
/* 82 */     this.particleRed = r;
/* 83 */     this.particleGreen = g;
/* 84 */     this.particleBlue = b;
   }
   
   public void renderParticle(BufferBuilder wr, Entity entity, float partialTicks, float rotX, float rotY, float rotZ, float f4, float f5) {}
}