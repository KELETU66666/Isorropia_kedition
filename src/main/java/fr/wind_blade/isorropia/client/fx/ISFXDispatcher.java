 package fr.wind_blade.isorropia.client.fx;
 
 import net.minecraft.client.Minecraft;
 import net.minecraft.world.World;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 
 @SideOnly(Side.CLIENT)
 public class ISFXDispatcher
 {
   public static void elemental(World worldIn, double x, double y, double z) {
/* 12 */     (Minecraft.getMinecraft()).effectRenderer.addEffect(new FXElementalWisp(worldIn, x, y, z));
   }
   
   public static void fxAbsorption(double x, double y, double z, float r, float g, float b, boolean absorption) {
/* 16 */     FXCelestialAbsorption efa = new FXCelestialAbsorption((World)(Minecraft.getMinecraft()).world, x, y, z, r, g, b, absorption);
     
/* 18 */     (Minecraft.getMinecraft()).effectRenderer.addEffect(efa);
   }
   
   public static void fluxExplosion(World world, double x, double y, double z) {
/* 22 */     FXFluxExplosion fx = new FXFluxExplosion(world, x, y, z);
/* 23 */     (Minecraft.getMinecraft()).effectRenderer.addEffect(fx);
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\client\fx\ISFXDispatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */