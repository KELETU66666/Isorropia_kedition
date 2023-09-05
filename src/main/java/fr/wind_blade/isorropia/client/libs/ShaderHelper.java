 package fr.wind_blade.isorropia.client.libs;
 
 import java.io.BufferedReader;
 import java.io.InputStream;
 import java.io.InputStreamReader;
 import net.minecraft.util.ResourceLocation;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 import org.lwjgl.opengl.ARBShaderObjects;
 
 
 
 @SideOnly(Side.CLIENT)
 public class ShaderHelper
 {
   public static int createProgram(ResourceLocation vert, ResourceLocation frag) {
/*  17 */     int vertId = 0;
/*  18 */     int fragId = 0;
/*  19 */     int program = 0;
/*  20 */     if (vert != null) {
/*  21 */       vertId = createShader("/assets/" + vert.getNamespace() + "/" + vert.getPath(), 35633);
     }
/*  23 */     if (frag != null) {
/*  24 */       fragId = createShader("/assets/" + frag.getNamespace() + "/" + frag.getPath(), 35632);
     }
/*  26 */     if ((program = ARBShaderObjects.glCreateProgramObjectARB()) == 0) {
/*  27 */       return 0;
     }
/*  29 */     if (vert != null) {
/*  30 */       ARBShaderObjects.glAttachObjectARB(program, vertId);
     }
/*  32 */     if (frag != null) {
/*  33 */       ARBShaderObjects.glAttachObjectARB(program, fragId);
     }
/*  35 */     ARBShaderObjects.glLinkProgramARB(program);
/*  36 */     if (ARBShaderObjects.glGetObjectParameteriARB(program, 35714) == 0) {
/*  37 */       return 0;
     }
/*  39 */     ARBShaderObjects.glValidateProgramARB(program);
/*  40 */     if (ARBShaderObjects.glGetObjectParameteriARB(program, 35715) == 0) {
/*  41 */       return 0;
     }
/*  43 */     return program;
   }
   
   public static int createShader(String filename, int shaderType) {
/*  47 */     int shader = 0;
     try {
/*  49 */       shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
/*  50 */       if (shader == 0) {
/*  51 */         return 0;
       }
/*  53 */       ARBShaderObjects.glShaderSourceARB(shader, readFileAsString(filename));
/*  54 */       ARBShaderObjects.glCompileShaderARB(shader);
/*  55 */       if (ARBShaderObjects.glGetObjectParameteriARB(shader, 35713) == 0) {
/*  56 */         throw new RuntimeException("Error creating shader: " + getLogInfo(shader));
       }
/*  58 */       return shader;
/*  59 */     } catch (Exception e) {
/*  60 */       ARBShaderObjects.glDeleteObjectARB(shader);
/*  61 */       e.printStackTrace();
/*  62 */       return -1;
     } 
   }
   
   public static String getLogInfo(int obj) {
/*  67 */     return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, 35716));
   }
 
 
   
   private static String readFileAsString(String filename) throws Exception {
/*  73 */     StringBuilder source = new StringBuilder();
/*  74 */     InputStream in = ShaderHelper.class.getResourceAsStream(filename);
/*  75 */     Exception exception = null;
/*  76 */     if (in == null) {
/*  77 */       return "";
     }
     
     try {
/*  81 */       BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
/*  82 */       Exception innerExc = null;
       try {
         String line;
/*  85 */         while ((line = reader.readLine()) != null) {
/*  86 */           source.append(line).append('\n');
         }
/*  88 */       } catch (Exception exc) {
/*  89 */         exception = exc;
       } finally {
         try {
/*  92 */           reader.close();
/*  93 */         } catch (Exception exc) {
/*  94 */           if (innerExc == null) {
/*  95 */             innerExc = exc;
           }
           exc.printStackTrace();
         } 
       } 
/* 100 */       if (innerExc != null) {
/* 101 */         throw innerExc;
       }
/* 103 */     } catch (Exception exc) {
/* 104 */       exception = exc;
/* 105 */       String str = exception.toString();
     } finally {
/* 107 */       Exception exception1 = null; try {
/* 108 */         in.close();
/* 109 */       } catch (Exception exc) {
/* 110 */         if (exception == null) {
/* 111 */           exception = exc;
         }
/* 113 */         exc.printStackTrace();
       } 
     } 
 
 
 
     
/* 120 */     return source.toString();
   }
   
   public static void initShaders() {}
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\client\libs\ShaderHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */