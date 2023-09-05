 package fr.wind_blade.isorropia.common.libs;
 
 import fr.wind_blade.isorropia.client.libs.CombinedTextureManager;
 import java.util.HashMap;
 import java.util.Map;
 import net.minecraft.client.renderer.texture.TextureManager;
 
 
 public class IsorropiaCache
 {
   private static final Map<TextureManager, CombinedTextureManager> DELEGATE_TEXTURE_ENGINE_CACHE = new HashMap<>();
   
   public static CombinedTextureManager loadStatueTextureManager(TextureManager delegate) {
/* 14 */     return loadStatueTextureManager(delegate, 100, 100);
   }
   
   public static CombinedTextureManager loadStatueTextureManager(TextureManager delegate, int progression, int max) {
/* 18 */     if (DELEGATE_TEXTURE_ENGINE_CACHE.containsKey(delegate)) {
       CombinedTextureManager manager = DELEGATE_TEXTURE_ENGINE_CACHE.get(delegate);
/* 20 */       manager.setProgression(progression);
/* 21 */       manager.setMax(max);
/* 22 */       return manager;
     } 
 
     
      return DELEGATE_TEXTURE_ENGINE_CACHE.put(delegate, new CombinedTextureManager(delegate, CombinedTextureManager.COBBLESTONE_LOCATION, progression, max));
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\libs\IsorropiaCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */