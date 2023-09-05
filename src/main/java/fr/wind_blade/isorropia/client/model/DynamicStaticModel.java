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
/* 34 */     return this.oldModel.getQuads(state, side, rand);
   }


   public boolean isAmbientOcclusion() {
/* 39 */     return this.oldModel.isAmbientOcclusion();
   }


   public boolean isGui3d() {
/* 44 */     return this.oldModel.isGui3d();
   }

   public IBakedModel getInternal() {
/* 48 */     return this.oldModel;
   }


   public boolean isBuiltInRenderer() {
/* 53 */     return true;
   }


   public TextureAtlasSprite getParticleTexture() {
/* 58 */     return this.oldModel.getParticleTexture();
   }


   public ItemOverrideList getOverrides() {
/* 63 */     return ItemOverrideList.NONE;
   }


   public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType type) {
/* 68 */     ((RenderCustomItem)this.item.getTileEntityItemStackRenderer()).transform = type;
/* 69 */     return Pair.of(this, this.oldModel.handlePerspective(type).getRight());
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\client\model\DynamicStaticModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */