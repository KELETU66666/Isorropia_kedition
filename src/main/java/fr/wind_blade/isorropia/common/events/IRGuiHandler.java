 package fr.wind_blade.isorropia.common.events;
 
 import fr.wind_blade.isorropia.client.gui.GuiCelestialMagnet;
 import fr.wind_blade.isorropia.common.tiles.TileCelestialMagnet;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.tileentity.TileEntity;
 import net.minecraft.util.math.BlockPos;
 import net.minecraft.world.World;
 import net.minecraftforge.fml.common.network.IGuiHandler;
 
 
 
 
 
 public class IRGuiHandler
   implements IGuiHandler
 {
   public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
     return null;
   }
 
   
   public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
/* 24 */     if (ID == 0) {
       TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        if (te instanceof TileCelestialMagnet)
          return new GuiCelestialMagnet((TileCelestialMagnet)te); 
     } 
      return null;
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\events\IRGuiHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */