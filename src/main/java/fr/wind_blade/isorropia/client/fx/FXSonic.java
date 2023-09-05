 package fr.wind_blade.isorropia.client.fx;
 
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.particle.Particle;
 import net.minecraft.client.particle.ParticleManager;
 import net.minecraft.client.renderer.BufferBuilder;
 import net.minecraft.client.renderer.OpenGlHelper;
 import net.minecraft.client.renderer.Tessellator;
 import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
 import net.minecraft.entity.Entity;
 import net.minecraft.util.ResourceLocation;
 import net.minecraft.world.World;
 import net.minecraftforge.fml.relauncher.ReflectionHelper;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 import org.lwjgl.opengl.GL11;
 import thaumcraft.client.lib.obj.AdvancedModelLoader;
 import thaumcraft.client.lib.obj.IModelCustom;
 
 @SideOnly(Side.CLIENT)
 public class FXSonic
   extends Particle
 {
/*  24 */   public Entity target = null;
/*  25 */   public float yaw = 0.0F;
/*  26 */   public float pitch = 0.0F;
   public IModelCustom hemisphere;
/*  28 */   public static final ResourceLocation MODEL = new ResourceLocation("thaumcraft", "models/obj/hemis.obj");
   
   public FXSonic(World world, double d, double d1, double d2, Entity target, int age) {
/*  31 */     super(world, d, d1, d2, 0.0D, 0.0D, 0.0D);
/*  32 */     this.particleRed = 0.0F;
/*  33 */     this.particleGreen = 1.0F;
/*  34 */     this.particleBlue = 1.0F;
/*  35 */     this.particleGravity = 0.0F;
/*  36 */     this.motionZ = 0.0D;
/*  37 */     this.motionY = 0.0D;
/*  38 */     this.motionX = 0.0D;
/*  39 */     this.particleMaxAge = age + this.rand.nextInt(age / 2);
/*  40 */     this.canCollide = false;
/*  41 */     setSize(0.01F, 0.01F);
/*  42 */     this.canCollide = true;
/*  43 */     this.particleScale = 1.0F;
/*  44 */     this.target = target;
/*  45 */     this.yaw = target.getRotationYawHead();
/*  46 */     this.pitch = target.rotationPitch;
/*  47 */     this.prevPosX = this.posX = target.posX;
/*  48 */     this.prevPosY = this.posY = target.posY + target.getEyeHeight();
/*  49 */     this.prevPosZ = this.posZ = target.posZ;
   }
 
 
   
   public void renderParticle(BufferBuilder buffer, Entity entityIn, float f, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
/*  55 */     Tessellator.getInstance().draw();
/*  56 */     GL11.glPushMatrix();
/*  57 */     GL11.glDepthMask(false);
/*  58 */     GL11.glEnable(3042);
/*  59 */     GL11.glBlendFunc(770, 771);
/*  60 */     if (this.hemisphere == null) {
/*  61 */       this.hemisphere = AdvancedModelLoader.loadModel(MODEL);
     }
/*  63 */     float fade = (this.particleAge + f) / this.particleMaxAge;
/*  64 */     float xx = (float)(this.prevPosX + (this.posX - this.prevPosX) * f - interpPosX);
/*  65 */     float yy = (float)(this.prevPosY + (this.posY - this.prevPosY) * f - interpPosY);
/*  66 */     float zz = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * f - interpPosZ);
/*  67 */     GL11.glTranslated(xx, yy, zz);
/*  68 */     float b = 1.0F;
/*  69 */     int frame = Math.min(15, (int)(14.0F * fade) + 1);
/*  70 */     (Minecraft.getMinecraft()).renderEngine
/*  71 */       .bindTexture(new ResourceLocation("thaumcraft", "textures/models/ripple" + frame + ".png"));
/*  72 */     b = 0.5F;
/*  73 */     int i = 220;
/*  74 */     int j = i % 65536;
/*  75 */     int k = i / 65536;
/*  76 */     OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
/*  77 */     GL11.glRotatef(-this.yaw, 0.0F, 1.0F, 0.0F);
/*  78 */     GL11.glRotatef(this.pitch, 1.0F, 0.0F, 0.0F);
/*  79 */     GL11.glTranslated(0.0D, 0.0D, (2.0F * this.target.height + this.target.width / 2.0F));
/*  80 */     GL11.glScaled(0.25D * this.target.height, 0.25D * this.target.height, (-1.0F * this.target.height));
     
/*  82 */     GL11.glColor4f(0.0F, b, b, 1.0F);
/*  83 */     this.hemisphere.renderAll();
/*  84 */     GL11.glDisable(3042);
/*  85 */     GL11.glDepthMask(true);
/*  86 */     GL11.glPopMatrix();
/*  87 */     (Minecraft.getMinecraft()).renderEngine.bindTexture(getParticleTexture());
/*  88 */     buffer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
   }
 
   
   public void onUpdate() {
/*  93 */     this.prevPosX = this.posX;
/*  94 */     this.prevPosY = this.posY;
/*  95 */     this.prevPosZ = this.posZ;
/*  96 */     if (this.particleAge++ >= this.particleMaxAge) {
       setExpired();
     }
   }
   
   public static ResourceLocation getParticleTexture() {
     try {
/* 103 */       return (ResourceLocation)ReflectionHelper.getPrivateValue(ParticleManager.class, null, new String[] { "PARTICLE_TEXTURES", "b", "field_110737_b" });
     }
/* 105 */     catch (Exception e) {
/* 106 */       return null;
     } 
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\client\fx\FXSonic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */