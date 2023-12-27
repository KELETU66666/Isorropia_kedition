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
        int vertId = 0;
        int fragId = 0;
        int program = 0;
        if (vert != null) {
            vertId = createShader("/assets/" + vert.getNamespace() + "/" + vert.getPath(), 35633);
        }
        if (frag != null) {
            fragId = createShader("/assets/" + frag.getNamespace() + "/" + frag.getPath(), 35632);
        }
        if ((program = ARBShaderObjects.glCreateProgramObjectARB()) == 0) {
            return 0;
        }
        if (vert != null) {
            ARBShaderObjects.glAttachObjectARB(program, vertId);
        }
        if (frag != null) {
            ARBShaderObjects.glAttachObjectARB(program, fragId);
        }
        ARBShaderObjects.glLinkProgramARB(program);
        if (ARBShaderObjects.glGetObjectParameteriARB(program, 35714) == 0) {
            return 0;
        }
        ARBShaderObjects.glValidateProgramARB(program);
        if (ARBShaderObjects.glGetObjectParameteriARB(program, 35715) == 0) {
            return 0;
        }
        return program;
    }

    public static int createShader(String filename, int shaderType) {
        int shader = 0;
        try {
            shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
            if (shader == 0) {
                return 0;
            }
            ARBShaderObjects.glShaderSourceARB(shader, readFileAsString(filename));
            ARBShaderObjects.glCompileShaderARB(shader);
            if (ARBShaderObjects.glGetObjectParameteriARB(shader, 35713) == 0) {
                throw new RuntimeException("Error creating shader: " + getLogInfo(shader));
            }
            return shader;
        } catch (Exception e) {
            ARBShaderObjects.glDeleteObjectARB(shader);
            e.printStackTrace();
            return -1;
        }
    }

    public static String getLogInfo(int obj) {
        return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, 35716));
    }



    private static String readFileAsString(String filename) throws Exception {
        StringBuilder source = new StringBuilder();
        InputStream in = ShaderHelper.class.getResourceAsStream(filename);
        Exception exception = null;
        if (in == null) {
            return "";
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            Exception innerExc = null;
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    source.append(line).append('\n');
                }
            } catch (Exception exc) {
                exception = exc;
            } finally {
                try {
                    reader.close();
                } catch (Exception exc) {
                    if (innerExc == null) {
                        innerExc = exc;
                    }
                    exc.printStackTrace();
                }
            }
            if (innerExc != null) {
                throw innerExc;
            }
        } catch (Exception exc) {
            exception = exc;
            String str = exception.toString();
        } finally {
            Exception exception1 = null; try {
                in.close();
            } catch (Exception exc) {
                if (exception == null) {
                    exception = exc;
                }
                exc.printStackTrace();
            }
        }




        return source.toString();
    }

    public static void initShaders() {}
}