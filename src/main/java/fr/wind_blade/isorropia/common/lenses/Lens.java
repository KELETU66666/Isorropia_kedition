 package fr.wind_blade.isorropia.common.lenses;
 
 import fr.wind_blade.isorropia.common.items.misc.ItemLens;
 import net.minecraft.client.gui.ScaledResolution;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.world.World;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 import net.minecraftforge.registries.IForgeRegistryEntry;
 
 public abstract class Lens
   extends IForgeRegistryEntry.Impl<Lens> {
   private final ItemLens lens;
   
   public Lens(ItemLens lensIn) {
/* 16 */     this.lens = lensIn;
/* 17 */     lensIn.setLens(this);
   }
   
   public ItemLens getItemLens() {
/* 21 */     return this.lens;
   }
   
   public String getTranslationKey() {
     return getRegistryName().getPath();
   }
   
   public abstract void handleTicks(World paramWorld, EntityPlayer paramEntityPlayer, boolean paramBoolean);
   
   @SideOnly(Side.CLIENT)
   public abstract void handleRenderGameOverlay(World paramWorld, EntityPlayer paramEntityPlayer, ScaledResolution paramScaledResolution, boolean paramBoolean, float paramFloat);
   
   @SideOnly(Side.CLIENT)
   public abstract void handleRenderWorldLast(World paramWorld, EntityPlayer paramEntityPlayer, boolean paramBoolean, float paramFloat);
   
   public abstract void handleRemoval(World paramWorld, EntityPlayer paramEntityPlayer);
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\lenses\Lens.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */