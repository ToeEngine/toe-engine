package br.toe.engine.utils;

import br.toe.engine.renderer.provider.*;
import net.bytebuddy.asm.*;
import org.lwjgl.opengl.*;

public class GLInterceptor {

    @Advice.OnMethodExit
    public static void intercept() {
        if (!GL.getCapabilities().OpenGL43)
            return;

        final var builder = new StringBuilder();

        var code = 0;
        while (( code = GL11C.glGetError() ) != GL11C.GL_NO_ERROR) {
            builder.append("  - ");
            builder.append(switch (code) {
                case GL11C.GL_INVALID_ENUM -> "Invalid Enum";
                case GL11C.GL_INVALID_OPERATION -> "Invalid Operation";
                case GL11C.GL_INVALID_VALUE -> "Invalid Value";
                case GL11C.GL_STACK_OVERFLOW -> "Stack Overflow";
                case GL11C.GL_STACK_UNDERFLOW -> "Stack Underflow";
                case GL11C.GL_OUT_OF_MEMORY -> "Out Of Memory";
                case GL30C.GL_INVALID_FRAMEBUFFER_OPERATION -> "Invalid Framebuffer Operation";
                default -> "???";
            });

            builder.append("\n");
        }

        if (builder.length() > 0)
            throw new RendererProviderException("OpenGL ERRORS:\n\n%s".formatted(builder.toString()));
    }
}
