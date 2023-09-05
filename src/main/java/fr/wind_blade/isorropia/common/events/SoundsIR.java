 package fr.wind_blade.isorropia.common.events;
 
 import net.minecraft.util.ResourceLocation;
 import net.minecraft.util.SoundEvent;
 import net.minecraftforge.event.RegistryEvent;
 import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
 import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
 import net.minecraftforge.registries.IForgeRegistryEntry;
 
 
 @EventBusSubscriber
 public class SoundsIR
 {
   public static final float CURATIVE_EXPUNGE_FLUX_START_LENGHT = 1.853F;
   public static final float CURATIVE_EXPUNGE_FLUX_LOOP_LENGTH = 5.821F;
   public static final float CURATIVE_EXPUNGE_FLUX_END_LENGHT = 1.747F;
   public static SoundEvent curative_infusion_start;
   public static SoundEvent curative_infusion_loop;
   public static SoundEvent curative_expunge_flux_start;
   public static SoundEvent curative_expunge_flux_loop;
   public static SoundEvent curative_expunge_flux_end;
   
   @SubscribeEvent
   public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
     curative_infusion_start = getSound(event, "curative_infusion_start");
      curative_infusion_loop = getSound(event, "curative_infusion_loop");
      curative_expunge_flux_start = getSound(event, "curative_expunge_flux_start");
      curative_expunge_flux_loop = getSound(event, "curative_expunge_flux_loop");
      curative_expunge_flux_end = getSound(event, "curative_expunge_flux_end");
   }
   
   private static SoundEvent getSound(RegistryEvent.Register<SoundEvent> event, String registryName) {
/* 33 */     ResourceLocation loc = new ResourceLocation("isorropia", registryName);
/* 34 */     SoundEvent sound = new SoundEvent(loc);
/* 35 */     sound.setRegistryName(loc);
/* 36 */     event.getRegistry().register(sound);
/* 37 */     return sound;
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\events\SoundsIR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */