package fr.wind_blade.isorropia.client.renderer.tiles;

import fr.wind_blade.isorropia.Isorropia;
import fr.wind_blade.isorropia.common.tiles.TileVat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderVatSlave extends TileEntitySpecialRenderer<TileVat> {

    ModelBiped corpse;
    static String tx1;
    static String tx2;
    EntityItem stack;

    public RenderVatSlave() {
        this.corpse = new ModelBiped();
        this.stack = null;
    }

    public void render(final TileVat tco, final double x, final double y, final double z, final float f, int destroyStage, float alpha) {
        GL11.glPushMatrix();
        if (tco.getMode() == 3) {
            GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
            GL11.glTranslatef(
                    (float) (-x) - 0.5f,
                    (float) (-y) - 0.5f + 0.1f * (float) Math.cos(Math.toRadians(Minecraft.getMinecraft().player.ticksExisted)),
                    (float) z + 0.5f);
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Isorropia.MODID, RenderVatSlave.tx1));
            this.corpse.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.1f);
        } else if (tco.getMode() == 4 || (tco.getMode() == 2 && tco.recipeType == 1)) {
            GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
            GL11.glTranslatef(
                    (float) (-x) - 0.5f,
                    (float) (-y) - 0.5f + 0.1f * (float) Math.cos(Math.toRadians(Minecraft.getMinecraft().player.ticksExisted)),
                    (float) z + 0.5f);
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Isorropia.MODID, RenderVatSlave.tx2));
            this.corpse.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.1f);
        }
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    static {
        RenderVatSlave.tx1 = "textures/models/corpseeffigy.png";
        RenderVatSlave.tx2 = "textures/models/corpseeffigyrevived.png";
    }
}