package fr.wind_blade.isorropia.client.libs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;


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
        this.location = location;
        this.location2 = location2;
        this.progression = progression;
        this.max = max;
        this.isPlayer = isPlayer;
    }


    public void loadTexture(IResourceManager resourceManager) throws IOException {
        deleteGlTexture();

        IResource iresource = null;
        BufferedImage bufferedimage1 = null;
        IResource iresource2 = Minecraft.getMinecraft().getResourceManager().getResource(this.location2);
        BufferedImage bufferedimage2 = TextureUtil.readBufferedImage(iresource2.getInputStream());
        int i = bufferedimage1.getType();

        if (i == 0) {
            i = 6;
        }

        BufferedImage bufferedimage = new BufferedImage(bufferedimage1.getWidth(), bufferedimage1.getHeight(), i);
        for (int y = 0; y < bufferedimage1.getHeight(); y++) {
            for (int x = 0; x < bufferedimage1.getWidth(); x++) {
                int j1 = bufferedimage1.getRGB(x, y);
                if ((j1 & 0xFF000000) != 0) {
                    int modelColor = bufferedimage1.getRGB(x, y);

                    if (y <= this.progression * bufferedimage1.getWidth() / this.max) {
                        int material = bufferedimage2.getRGB(x % bufferedimage2.getWidth(), y % bufferedimage2
                                .getHeight());
                        int model_red = (modelColor & 0xFF0000) >> 16;
                        int material_red = (material & 0xFF0000) >> 16;
                        int model_green = (modelColor & 0xFF00) >> 8;
                        int material_green = (material & 0xFF00) >> 8;
                        int model_blue = (modelColor & 0xFF) >> 0;
                        int material_blue = (material & 0xFF) >> 0;
                        double blackAndWhite = 0.3D * model_red + 0.59D * model_green + 0.11D * model_blue;

                        blackAndWhite = 255.0D - (255.0D - blackAndWhite) / 1.0D;
                        int bake_red = (int)((float)blackAndWhite * material_red / 255.0F);
                        int bake_green = (int)((float)blackAndWhite * material_green / 255.0F);
                        int bake_blue = (int)((float)blackAndWhite * material_blue / 255.0F);
                        int bake_color = (modelColor & 0xFF000000 | bake_red << 16 | bake_green << 8 | bake_blue) & 0xFFFFFF;

                        bufferedimage.setRGB(x, y, 0xFF000000 | bake_color);
                    } else {
                        bufferedimage.setRGB(x, y, 0xFF000000 | modelColor);
                    }
                }
            }
        }

        TextureUtil.uploadTextureImage(getGlTextureId(), bufferedimage);

        if (iresource != null) {
            IOUtils.closeQuietly((Closeable)iresource);
        }
        IOUtils.closeQuietly((Closeable)iresource2);
    }
}