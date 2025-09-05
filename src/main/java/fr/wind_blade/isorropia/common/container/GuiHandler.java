package fr.wind_blade.isorropia.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler {
    @Nullable
    @Override
    public Object getServerGuiElement(int i, EntityPlayer player, World world, int i1, int i2, int i3) {
        if(i == 9){
            return new ContainerFingers(player.inventory);
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int i, EntityPlayer player, World world, int i1, int i2, int i3) {
        if(i == 9){
            return new GuiFingers(player.inventory);
        }
        return null;
    }
}
