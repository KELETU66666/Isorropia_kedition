package fr.wind_blade.isorropia;

import fr.wind_blade.isorropia.common.Common;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@EventBusSubscriber
@Mod(modid = Isorropia.MODID, name = Isorropia.NAME, version = Isorropia.VERSION, acceptedMinecraftVersions = "[1.12.2]", dependencies = "required-after:thaumcraft@[6.1.BETA26]")
public class Isorropia {
    public static final String MODID = "isorropia";
    public static final String NAME = "isorropia";
    public static final String VERSION = "1.0.4";
    @Instance("isorropia")
    public static Isorropia instance;
    @SidedProxy(clientSide = "fr.wind_blade.isorropia.client.Client", serverSide = "fr.wind_blade.isorropia.server.Server")
    public static Common proxy;
    public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}