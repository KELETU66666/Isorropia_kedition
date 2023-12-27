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
public class CombinedTextureManager extends TextureManager
{
    public static ResourceLocation COBBLESTONE_LOCATION = new ResourceLocation("textures/blocks/cobblestone.png");
    public static ResourceLocation PLACEHOLDER_LOCATION = new ResourceLocation("isorropia", "textures/blocks/statue.png");

    protected final TextureManager textureManager;

    protected ResourceLocation materialLocation = COBBLESTONE_LOCATION;
    protected int progression;
    protected int max;

    public CombinedTextureManager(TextureManager textureManager, ResourceLocation material) {
        this(textureManager, material, 100, 100);
    }

    public CombinedTextureManager(TextureManager textureManager, ResourceLocation material, int progression, int max) {
        super(null);
        this.textureManager = checkManager(textureManager);
        this.progression = progression;
        this.max = max;
    }

    private TextureManager checkManager(TextureManager textureManager) {
        return (textureManager instanceof CombinedTextureManager) ?
                checkManager(((CombinedTextureManager)textureManager).textureManager) : textureManager;
    }



    public void bindTexture(ResourceLocation resource) {
        this.textureManager.loadTexture(PLACEHOLDER_LOCATION, (ITextureObject)new CombinedLayeredColorMaskTexture(resource, this.materialLocation, this.progression, this.max, resource
                .getPath().startsWith("skins/")));
        this.textureManager.bindTexture(PLACEHOLDER_LOCATION);
    }


    public boolean loadTickableTexture(ResourceLocation textureLocation, ITickableTextureObject textureObj) {
        return this.textureManager.loadTickableTexture(textureLocation, textureObj);
    }


    public boolean loadTexture(ResourceLocation textureLocation, ITextureObject textureObj) {
        return this.textureManager.loadTexture(textureLocation, textureObj);
    }


    public ITextureObject getTexture(ResourceLocation textureLocation) {
        return this.textureManager.getTexture(textureLocation);
    }


    public ResourceLocation getDynamicTextureLocation(String name, DynamicTexture texture) {
        return this.textureManager.getDynamicTextureLocation(name, texture);
    }


    public void tick() {
        this.textureManager.tick();
    }


    public void deleteTexture(ResourceLocation textureLocation) {
        this.textureManager.deleteTexture(textureLocation);
    }


    public void onResourceManagerReload(IResourceManager resourceManager) {
        this.textureManager.onResourceManagerReload(resourceManager);
    }

    public ResourceLocation getMaterialLocation() {
        return this.materialLocation;
    }

    public void setMaterialLocation(ResourceLocation materialLocation) {
        this.materialLocation = materialLocation;
    }

    public TextureManager getTextureManager() {
        return this.textureManager;
    }

    public int getProgression() {
        return this.progression;
    }

    public void setProgression(int progression) {
        this.progression = progression;
    }

    public int getMax() {
        return this.max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}