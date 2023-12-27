package fr.wind_blade.isorropia.client.model;

import fr.wind_blade.isorropia.client.renderer.RenderCustomItem;
import java.util.List;
import javax.vecmath.Matrix4f;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;



@SideOnly(Side.CLIENT)
public class DynamicStaticModel
        implements IBakedModel
{
    private final IBakedModel oldModel;
    private final Item item;

    public DynamicStaticModel(IBakedModel internal, Item item) {
        this.oldModel = internal;
        this.item = item;
    }


    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
        return this.oldModel.getQuads(state, side, rand);
    }


    public boolean isAmbientOcclusion() {
        return this.oldModel.isAmbientOcclusion();
    }


    public boolean isGui3d() {
        return this.oldModel.isGui3d();
    }

    public IBakedModel getInternal() {
        return this.oldModel;
    }


    public boolean isBuiltInRenderer() {
        return true;
    }


    public TextureAtlasSprite getParticleTexture() {
        return this.oldModel.getParticleTexture();
    }


    public ItemOverrideList getOverrides() {
        return ItemOverrideList.NONE;
    }


    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType type) {
        ((RenderCustomItem)this.item.getTileEntityItemStackRenderer()).transform = type;
        return Pair.of(this, this.oldModel.handlePerspective(type).getRight());
    }
}