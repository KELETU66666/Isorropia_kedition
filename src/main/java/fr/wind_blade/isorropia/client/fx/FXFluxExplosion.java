package fr.wind_blade.isorropia.client.fx;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXGeneric;

import java.util.Random;

@SideOnly(value=Side.CLIENT)
public class FXFluxExplosion
        extends Particle {
    public FXFluxExplosion(World world, double x, double y, double z) {
        super(world, x, y, z);
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 180; ++j) {
                boolean sp = (double)this.rand.nextFloat() < 0.2;
                FXGeneric fb = new FXGeneric(world, x, y, z, this.getRandomSign(this.rand), this.getRandomSign(this.rand), this.getRandomSign(this.rand));
                fb.setRBGColorF(MathHelper.clamp(384.0f, 0.0f, 1.0f), 0.0f, MathHelper.clamp(384.0f, 0.0f, 1.0f), this.rand.nextFloat(), this.rand.nextFloat(), this.rand.nextFloat());
                fb.setLoop(true);
                fb.setLayer(0);
                fb.setSlowDown(0.955);
                int age = 30 + this.rand.nextInt(20);
                fb.setMaxAge(age);
                float[] alphas = new float[6 + this.rand.nextInt(age / 3)];
                for (int a = 1; a < alphas.length - 1; ++a) {
                    alphas[a] = this.rand.nextFloat();
                }
                alphas[0] = 1.0f;
                fb.setAlphaF(alphas);
                fb.setParticles(sp ? 320 : 512, 16, 1);
                fb.setScale(0.5f, 0.125f);
                fb.setRandomMovementScale(0.0025f, 0.001f, 0.0025f);
                ParticleEngine.addEffectWithDelay(world, fb, 2 + this.rand.nextInt(3));
            }
        }
    }

    public void onUpdate() {
        super.onUpdate();
    }

    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    }

    public float getRandomSign(Random rand) {
        return (rand.nextBoolean() ? rand.nextFloat() : -rand.nextFloat()) / 1.5f;
    }
}
