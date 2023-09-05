 package fr.wind_blade.isorropia.client.libs;
 
 import java.awt.image.BufferedImage;
 import java.io.Closeable;
 import java.io.IOException;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.renderer.texture.AbstractTexture;
 import net.minecraft.client.renderer.texture.TextureUtil;
 import net.minecraft.client.resources.IResource;
 import net.minecraft.client.resources.IResourceManager;
 import net.minecraft.util.ResourceLocation;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 import org.apache.commons.io.IOUtils;
 
 
 @SideOnly(Side.CLIENT)
 public class CombinedLayeredColorMaskTexture
   extends AbstractTexture
 {
   private final ResourceLocation location;
   private final ResourceLocation location2;
   private final int progression;
   private final int max;
   private boolean isPlayer;
   
   public CombinedLayeredColorMaskTexture(ResourceLocation location, ResourceLocation location2, int progression, int max, boolean isPlayer) {
/*  28 */     this.location = location;
/*  29 */     this.location2 = location2;
/*  30 */     this.progression = progression;
/*  31 */     this.max = max;
/*  32 */     this.isPlayer = isPlayer;
   }
 
   
   public void loadTexture(IResourceManager resourceManager) throws IOException {
/*  37 */     deleteGlTexture();
     
/*  39 */     IResource iresource = null;
/*  40 */     BufferedImage bufferedimage1 = null;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
     
/*  58 */     IResource iresource2 = Minecraft.getMinecraft().getResourceManager().getResource(this.location2);
/*  59 */     BufferedImage bufferedimage2 = TextureUtil.readBufferedImage(iresource2.getInputStream());
/*  60 */     int i = bufferedimage1.getType();
     
/*  62 */     if (i == 0) {
/*  63 */       i = 6;
     }
     
/*  66 */     BufferedImage bufferedimage = new BufferedImage(bufferedimage1.getWidth(), bufferedimage1.getHeight(), i);
/*  67 */     for (int y = 0; y < bufferedimage1.getHeight(); y++) {
/*  68 */       for (int x = 0; x < bufferedimage1.getWidth(); x++) {
/*  69 */         int j1 = bufferedimage1.getRGB(x, y);
/*  70 */         if ((j1 & 0xFF000000) != 0) {
/*  71 */           int modelColor = bufferedimage1.getRGB(x, y);
           
/*  73 */           if (y <= this.progression * bufferedimage1.getWidth() / this.max) {
/*  74 */             int material = bufferedimage2.getRGB(x % bufferedimage2.getWidth(), y % bufferedimage2
/*  75 */                 .getHeight());
/*  76 */             int model_red = (modelColor & 0xFF0000) >> 16;
/*  77 */             int material_red = (material & 0xFF0000) >> 16;
/*  78 */             int model_green = (modelColor & 0xFF00) >> 8;
/*  79 */             int material_green = (material & 0xFF00) >> 8;
/*  80 */             int model_blue = (modelColor & 0xFF) >> 0;
/*  81 */             int material_blue = (material & 0xFF) >> 0;
/*  82 */             double blackAndWhite = 0.3D * model_red + 0.59D * model_green + 0.11D * model_blue;
             
/*  84 */             blackAndWhite = 255.0D - (255.0D - blackAndWhite) / 1.0D;
/*  85 */             int bake_red = (int)((float)blackAndWhite * material_red / 255.0F);
/*  86 */             int bake_green = (int)((float)blackAndWhite * material_green / 255.0F);
/*  87 */             int bake_blue = (int)((float)blackAndWhite * material_blue / 255.0F);
/*  88 */             int bake_color = (modelColor & 0xFF000000 | bake_red << 16 | bake_green << 8 | bake_blue) & 0xFFFFFF;
             
/*  90 */             bufferedimage.setRGB(x, y, 0xFF000000 | bake_color);
           } else {
/*  92 */             bufferedimage.setRGB(x, y, 0xFF000000 | modelColor);
           } 
         } 
       } 
     } 
     
     TextureUtil.uploadTextureImage(getGlTextureId(), bufferedimage);
     
/* 100 */     if (iresource != null) {
/* 101 */       IOUtils.closeQuietly((Closeable)iresource);
     }
/* 103 */     IOUtils.closeQuietly((Closeable)iresource2);
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\client\libs\CombinedLayeredColorMaskTexture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */