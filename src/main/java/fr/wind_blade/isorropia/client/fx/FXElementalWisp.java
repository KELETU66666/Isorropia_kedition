 package fr.wind_blade.isorropia.client.fx;
 
 import java.util.Random;
 import net.minecraft.client.particle.Particle;
 import net.minecraft.util.math.MathHelper;
 import net.minecraft.world.World;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 import thaumcraft.client.fx.ParticleEngine;
 import thaumcraft.client.fx.particles.FXGeneric;
 
 @SideOnly(Side.CLIENT)
 public class FXElementalWisp
   extends Particle
 {
   public FXElementalWisp(World worldIn, double x, double y, double z) {
/* 17 */     super(worldIn, x, y, z);
     
     for (int i = 0; i < 10; i++) {
       
/* 21 */       float a = getRandomSign(this.rand);
/* 22 */       float b = getRandomSign(this.rand);
/* 23 */       float c = getRandomSign(this.rand);
/* 24 */       float slow = 20.0F;
       FXGeneric fb = new FXGeneric(this.world, x + a, y + b, z + c, (-a / slow), (-b / slow), (-c / slow));
        fb.setRBGColorF(MathHelper.clamp(384.0F, 0.0F, 1.0F), 0.0F, MathHelper.clamp(384.0F, 0.0F, 1.0F));
        boolean sp = (this.rand.nextFloat() < 0.2D);
        int age = 30 + this.rand.nextInt(20);
        fb.setMaxAge(age);
/* 30 */       fb.setParticles(sp ? 320 : 512, 16, 1);
/* 31 */       fb.setScale(new float[] { 0.5F, 0.125F });
/* 32 */       ParticleEngine.addEffect(this.world, (Particle)fb);
     } 
   }
   
   public float getRandomSign(Random rand) {
/* 37 */     return rand.nextBoolean() ? rand.nextFloat() : -rand.nextFloat();
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\client\fx\FXElementalWisp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */