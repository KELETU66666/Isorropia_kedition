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
 @Mod(modid = "isorropia", name = "isorropia", version = "0.1.13", acceptedMinecraftVersions = "[1.12.2]", dependencies = "required-after:thaumcraft@[6.1.BETA26]")
 public class Isorropia {
     public static final String MODID = "isorropia";
     public static final String NAME = "isorropia";
     public static final String VERSION = "0.1.13";
     @Instance("isorropia")
     public static Isorropia instance;
     @SidedProxy(clientSide = "fr.wind_blade.isorropia.client.Client", serverSide = "fr.wind_blade.isorropia.server.Server")
     public static Common proxy;
     public static Logger logger;

     @EventHandler
     public void preInit(FMLPreInitializationEvent event) {
         /* 32 */
         logger = event.getModLog();
         /* 33 */
         proxy.preInit(event);
     }

     @EventHandler
     public void init(FMLInitializationEvent event) {
         /* 38 */
         proxy.init(event);
     }

     @EventHandler
     public void postInit(FMLPostInitializationEvent event) {
         /* 43 */
         proxy.postInit(event);
     }
 }