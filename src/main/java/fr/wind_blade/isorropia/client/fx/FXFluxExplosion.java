 package fr.wind_blade.isorropia.client.fx;

 import java.util.Random;
 import net.minecraft.client.particle.Particle;
 import net.minecraft.client.renderer.BufferBuilder;
 import net.minecraft.entity.Entity;
 import net.minecraft.util.math.MathHelper;
 import net.minecraft.world.World;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 import thaumcraft.client.fx.ParticleEngine;
 import thaumcraft.client.fx.particles.FXGeneric;

 @SideOnly(Side.CLIENT)
 public class FXFluxExplosion
   extends Particle
 {
   public FXFluxExplosion(World world, double x, double y, double z) {
     super(world, x, y, z);
/* 20 */     for (int i = 0; i < 3; i++) {
/* 21 */       for (int j = 0; j < 180; j++) {

/* 23 */         boolean sp = (this.rand.nextFloat() < 0.2D);

         FXGeneric fb = new FXGeneric(world, x, y, z, getRandomSign(this.rand), getRandomSign(this.rand), getRandomSign(this.rand));
          fb.setRBGColorF(MathHelper.clamp(384.0F, 0.0F, 1.0F), 0.0F, MathHelper.clamp(384.0F, 0.0F, 1.0F), this.rand
              .nextFloat(), this.rand.nextFloat(), this.rand.nextFloat());

          fb.setLoop(true);
/* 30 */         fb.setLayer(0);
/* 31 */         fb.setSlowDown(0.955D);
/* 32 */         int age = 30 + this.rand.nextInt(20);
/* 33 */         fb.setMaxAge(age);
/* 34 */         float[] alphas = new float[6 + this.rand.nextInt(age / 3)];
/* 35 */         for (int a = 1; a < alphas.length - 1; a++)
/* 36 */           alphas[a] = this.rand.nextFloat(); 
/* 37 */         alphas[0] = 1.0F;
/* 38 */         fb.setAlphaF(alphas);
/* 39 */         fb.setParticles(sp ? 320 : 512, 16, 1);
/* 40 */         fb.setScale(new float[] { 0.5F, 0.125F });
          fb.setRandomMovementScale(0.0025F, 0.001F, 0.0025F);

/* 43 */         ParticleEngine.addEffectWithDelay(world, (Particle)fb, 2 + this.rand.nextInt(3));
       }
     }
   }


   public void onUpdate() {
/* 50 */     super.onUpdate();
   }



   public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
/* 56 */     super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
   }

   public float getRandomSign(Random rand) {
/* 60 */     return (rand.nextBoolean() ? rand.nextFloat() : -rand.nextFloat()) / 1.5F;
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\client\fx\FXFluxExplosion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */