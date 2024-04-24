package fr.wind_blade.isorropia.client.renderer.tiles;

import fr.wind_blade.isorropia.common.blocks.BlockJarSoul;
import fr.wind_blade.isorropia.common.tiles.TileJarSoul;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderJarSoul extends TileEntitySpecialRenderer<TileJarSoul> {
    public static ResourceLocation fx3 = new ResourceLocation("isorropia", "textures/misc/soul.png");


    public void render(TileJarSoul te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (te.entity != null) {
            GL11.glPushMatrix();
            GL11.glDisable(2884);
            GL11.glTranslatef((float) x + 0.5F, (float) y + 0.01F, (float) z + 0.5F);
            GL11.glRotated((((EnumFacing) te.getWorld().getBlockState(te.getPos()).getValue((IProperty) BlockJarSoul.FACING)).getHorizontalIndex() * -90 + 180), 0.0D, 1.0D, 0.0D);


            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            float f2 = 0.25F;
            GL11.glScalef(0.25F, 0.25F, 0.25F);
            te.entity.setLocationAndAngles(te.getPos().getX() + 0.5D, te.getPos().getY() + 0.5D, (te.getPos().getZ()), 0.0F, 0.0F);

            Render<Entity> render = Minecraft.getMinecraft().getRenderManager().getEntityRenderObject(te.entity);
            if (render != null) {
                render.doRender(te.entity, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
            }
            GL11.glEnable(2884);
            GL11.glPopMatrix();
        }

        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
    }
}