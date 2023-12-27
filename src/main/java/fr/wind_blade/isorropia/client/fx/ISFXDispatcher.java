package fr.wind_blade.isorropia.client.fx;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ISFXDispatcher
{
    public static void elemental(World worldIn, double x, double y, double z) {
        (Minecraft.getMinecraft()).effectRenderer.addEffect(new FXElementalWisp(worldIn, x, y, z));
    }

    public static void fxAbsorption(double x, double y, double z, float r, float g, float b, boolean absorption) {
        FXCelestialAbsorption efa = new FXCelestialAbsorption((World)(Minecraft.getMinecraft()).world, x, y, z, r, g, b, absorption);

        (Minecraft.getMinecraft()).effectRenderer.addEffect(efa);
    }

    public static void fluxExplosion(World world, double x, double y, double z) {
        FXFluxExplosion fx = new FXFluxExplosion(world, x, y, z);
        (Minecraft.getMinecraft()).effectRenderer.addEffect(fx);
    }
}
