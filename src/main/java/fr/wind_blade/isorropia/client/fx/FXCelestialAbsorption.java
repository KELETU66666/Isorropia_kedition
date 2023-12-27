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
    public float length = 1.0F;
    public int particle = 16;


    public FXCelestialAbsorption(World par1World, double x, double y, double z, float red, float green, float blue, boolean absorb) {
        super(par1World, x, y, z, 0.0D, 0.0D, 0.0D);
        this.particleRed = red;
        this.particleGreen = green;
        this.particleBlue = blue;
        setSize(0.02F, 0.02F);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.particleMaxAge = 3;
        int c = 0;
        while (c < 50) {
            FXGeneric fb;
            boolean sp = (this.rand.nextFloat() < 0.2D);

            if (absorb) {
                fb = new FXGeneric(par1World, x, y, z, 0.0D, 0.0D, 0.0D);
                fb.setLoop(true);
                fb.setGravity(sp ? 0.0F : 0.125F);
                fb.setLayer(0);
                fb.setSlowDown(0.995D);
            } else {
                fb = new FXGeneric(par1World, x + this.rand.nextFloat() - 0.5D, y, z - this.rand.nextFloat() + 0.5D, 0.0D, 0.0D, 0.0D);

                fb.setParticles(sp ? 320 : 512, 16, 1);
                fb.setGravity(-0.1F);
                fb.setSlowDown(0.95D);
            }

            int age = 30 + this.rand.nextInt(20);
            fb.setMaxAge(age);
            fb.setRBGColorF(red, blue, green);





            float[] alphas = new float[6 + this.rand.nextInt(age / 3)];
            for (int a = 1; a < alphas.length - 1; a++)
                alphas[a] = this.rand.nextFloat();
            alphas[0] = 1.0F;
            fb.setAlphaF(alphas);
            fb.setParticles(sp ? 320 : 512, 16, 1);
            fb.setScale(new float[] { 0.5F, 0.125F });
            fb.setRandomMovementScale(0.0025F, 0.001F, 0.0025F);

            ParticleEngine.addEffectWithDelay(par1World, (Particle)fb, 2 + this.rand.nextInt(3));
            c++;
        }
    }


    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.particleAge++ >= this.particleMaxAge) {
            setExpired();
        }
    }

    public void setRGB(float r, float g, float b) {
        this.particleRed = r;
        this.particleGreen = g;
        this.particleBlue = b;
    }

    public void renderParticle(BufferBuilder wr, Entity entity, float partialTicks, float rotX, float rotY, float rotZ, float f4, float f5) {}
}