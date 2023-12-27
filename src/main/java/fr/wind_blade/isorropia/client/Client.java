package fr.wind_blade.isorropia.client;

import fr.wind_blade.isorropia.client.libs.RenderEventHandler;
import fr.wind_blade.isorropia.client.renderer.RenderCustomItem;
import fr.wind_blade.isorropia.client.renderer.entities.*;
import fr.wind_blade.isorropia.client.renderer.tiles.RenderJarSoul;
import fr.wind_blade.isorropia.client.renderer.tiles.RenderModifiedMatrix;
import fr.wind_blade.isorropia.common.Common;
import fr.wind_blade.isorropia.common.blocks.BlocksIS;
import fr.wind_blade.isorropia.common.blocks.IItemStackRenderProvider;
import fr.wind_blade.isorropia.common.entities.*;
import fr.wind_blade.isorropia.common.entities.projectile.EntityEmber;
import fr.wind_blade.isorropia.common.entities.projectile.EntityIncubatedEgg;
import fr.wind_blade.isorropia.common.events.KeyHandler;
import fr.wind_blade.isorropia.common.items.ItemsIS;
import fr.wind_blade.isorropia.common.tiles.TileJarSoul;
import fr.wind_blade.isorropia.common.tiles.TileModifiedMatrix;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderOcelot;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaContainerItem;

@SideOnly(value=Side.CLIENT)
public class Client
        extends Common {
 @Override
 public void preInit(FMLPreInitializationEvent event) {
  super.preInit(event);
  MinecraftForge.EVENT_BUS.register(RenderEventHandler.class);
  RenderingRegistry.registerEntityRenderingHandler(EntityIncubatedEgg.class, renderManager -> new RenderSnowball(renderManager, ItemsIS.itemIncubatedEgg, Minecraft.getMinecraft().getRenderItem()));
  ClientRegistry.bindTileEntitySpecialRenderer(TileJarSoul.class, new RenderJarSoul());
  ClientRegistry.bindTileEntitySpecialRenderer(TileModifiedMatrix.class, new RenderModifiedMatrix());
  RenderingRegistry.registerEntityRenderingHandler(EntitySaehrimnir.class, RenderSaehrimnir::new);
  RenderingRegistry.registerEntityRenderingHandler(EntitySaehrimnirReborn.class, RenderSaehrimnirReborn::new);
  RenderingRegistry.registerEntityRenderingHandler(EntityTaintPig.class, RenderTaintPig::new);
  RenderingRegistry.registerEntityRenderingHandler(EntityGravekeeper.class, RenderOcelot::new);
  RenderingRegistry.registerEntityRenderingHandler(EntitySheeder.class, RenderSheeder::new);
  RenderingRegistry.registerEntityRenderingHandler(EntityOrePig.class, RenderOrePig::new);
  RenderingRegistry.registerEntityRenderingHandler(EntityScholarChicken.class, RenderScholarChicken::new);
  RenderingRegistry.registerEntityRenderingHandler(EntityJellyRabbit.class, RenderJellyRabbit::new);
  RenderingRegistry.registerEntityRenderingHandler(EntityHangingLabel.class, RenderHangingLabel::new);
  RenderingRegistry.registerEntityRenderingHandler(EntityHellHound.class, RenderHellHound::new);
  RenderingRegistry.registerEntityRenderingHandler(EntityEmber.class, RenderEmber::new);
  RenderingRegistry.registerEntityRenderingHandler(EntityGoldenChicken.class, RenderGoldChicken::new);
  RenderingRegistry.registerEntityRenderingHandler(EntityChocow.class, RenderChocolateCow::new);
  OBJLoader.INSTANCE.addDomain("isorropia");
 }

 @Override
 public void init(FMLInitializationEvent event) {
  super.init(event);
  ItemsIS.items.stream().filter(item -> item instanceof IItemStackRenderProvider).forEach(item -> item.setTileEntityItemStackRenderer(new RenderCustomItem()));
  BlocksIS.blocks.stream().filter(block -> Item.getItemFromBlock((Block)block) instanceof IItemStackRenderProvider).forEach(block -> Item.getItemFromBlock((Block)block).setTileEntityItemStackRenderer(new RenderCustomItem()));
 }

 @Override
 public void postInit(FMLPostInitializationEvent event) {
  super.postInit(event);
  IItemColor itemEssentiaColourHandler = (stack, tintIndex) -> {
   IEssentiaContainerItem item = (IEssentiaContainerItem)((Object)stack.getItem());
   if (item != null && item.getAspects(stack) != null) {
    return item.getAspects(stack).getAspects()[0].getColor();
   }
   return Aspect.SENSES.getColor();
  };
  Minecraft.getMinecraft().getItemColors().registerItemColorHandler(itemEssentiaColourHandler, new Item[]{ItemsIS.itemJelly});
  MinecraftForge.EVENT_BUS.register(new KeyHandler());
  RenderEventHandler.tip = ReflectionHelper.findMethod(Render.class, "getEntityTexture", "func_110775_a", new Class[]{Entity.class});
 }
}
