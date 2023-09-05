 package fr.wind_blade.isorropia.client.libs;
 
 import net.minecraft.client.renderer.texture.DynamicTexture;
 import net.minecraft.client.renderer.texture.ITextureObject;
 import net.minecraft.client.renderer.texture.ITickableTextureObject;
 import net.minecraft.client.renderer.texture.TextureManager;
 import net.minecraft.client.resources.IResourceManager;
 import net.minecraft.util.ResourceLocation;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 
 @SideOnly(Side.CLIENT)
 public class CombinedTextureManager
   extends TextureManager
 {
/*  16 */   public static ResourceLocation COBBLESTONE_LOCATION = new ResourceLocation("textures/blocks/cobblestone.png");
/*  17 */   public static ResourceLocation PLACEHOLDER_LOCATION = new ResourceLocation("isorropia", "textures/blocks/statue.png");
   
   protected final TextureManager textureManager;
   
/*  21 */   protected ResourceLocation materialLocation = COBBLESTONE_LOCATION;
   protected int progression;
   protected int max;
   
   public CombinedTextureManager(TextureManager textureManager, ResourceLocation material) {
/*  26 */     this(textureManager, material, 100, 100);
   }
   
   public CombinedTextureManager(TextureManager textureManager, ResourceLocation material, int progression, int max) {
/*  30 */     super(null);
/*  31 */     this.textureManager = checkManager(textureManager);
/*  32 */     this.progression = progression;
/*  33 */     this.max = max;
   }
   
   private TextureManager checkManager(TextureManager textureManager) {
/*  37 */     return (textureManager instanceof CombinedTextureManager) ? 
/*  38 */       checkManager(((CombinedTextureManager)textureManager).textureManager) : textureManager;
   }
 
 
   
   public void bindTexture(ResourceLocation resource) {
/*  44 */     this.textureManager.loadTexture(PLACEHOLDER_LOCATION, (ITextureObject)new CombinedLayeredColorMaskTexture(resource, this.materialLocation, this.progression, this.max, resource
/*  45 */           .getPath().startsWith("skins/")));
/*  46 */     this.textureManager.bindTexture(PLACEHOLDER_LOCATION);
   }
 
   
   public boolean loadTickableTexture(ResourceLocation textureLocation, ITickableTextureObject textureObj) {
/*  51 */     return this.textureManager.loadTickableTexture(textureLocation, textureObj);
   }
 
   
   public boolean loadTexture(ResourceLocation textureLocation, ITextureObject textureObj) {
/*  56 */     return this.textureManager.loadTexture(textureLocation, textureObj);
   }
 
   
   public ITextureObject getTexture(ResourceLocation textureLocation) {
/*  61 */     return this.textureManager.getTexture(textureLocation);
   }
 
   
   public ResourceLocation getDynamicTextureLocation(String name, DynamicTexture texture) {
/*  66 */     return this.textureManager.getDynamicTextureLocation(name, texture);
   }
 
   
   public void tick() {
/*  71 */     this.textureManager.tick();
   }
 
   
   public void deleteTexture(ResourceLocation textureLocation) {
/*  76 */     this.textureManager.deleteTexture(textureLocation);
   }
 
   
   public void onResourceManagerReload(IResourceManager resourceManager) {
/*  81 */     this.textureManager.onResourceManagerReload(resourceManager);
   }
   
   public ResourceLocation getMaterialLocation() {
/*  85 */     return this.materialLocation;
   }
   
   public void setMaterialLocation(ResourceLocation materialLocation) {
/*  89 */     this.materialLocation = materialLocation;
   }
   
   public TextureManager getTextureManager() {
/*  93 */     return this.textureManager;
   }
   
   public int getProgression() {
     return this.progression;
   }
   
   public void setProgression(int progression) {
/* 101 */     this.progression = progression;
   }
   
   public int getMax() {
/* 105 */     return this.max;
   }
   
   public void setMax(int max) {
/* 109 */     this.max = max;
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\client\libs\CombinedTextureManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */