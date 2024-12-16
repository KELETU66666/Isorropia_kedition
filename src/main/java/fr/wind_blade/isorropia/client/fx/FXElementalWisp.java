package fr.wind_blade.isorropia.client.fx;

import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXGeneric;

import java.util.Random;

@SideOnly(Side.CLIENT)
public class FXElementalWisp
        extends Particle
{
    public FXElementalWisp(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);

        for (int i = 0; i < 10; i++) {

            float a = getRandomSign(this.rand);
            float b = getRandomSign(this.rand);
            float c = getRandomSign(this.rand);
            float slow = 20.0F;
            FXGeneric fb = new FXGeneric(this.world, x + a, y + b, z + c, (-a / slow), (-b / slow), (-c / slow));
            fb.setRBGColorF(MathHelper.clamp(384.0F, 0.0F, 1.0F), 0.0F, MathHelper.clamp(384.0F, 0.0F, 1.0F));
            boolean sp = (this.rand.nextFloat() < 0.2D);
            int age = 30 + this.rand.nextInt(20);
            fb.setMaxAge(age);
            fb.setParticles(sp ? 320 : 512, 16, 1);
            fb.setScale(new float[] { 0.5F, 0.125F });
            ParticleEngine.addEffect(this.world, (Particle)fb);
        }
    }

    public float getRandomSign(Random rand) {
        return rand.nextBoolean() ? rand.nextFloat() : -rand.nextFloat();
    }
}