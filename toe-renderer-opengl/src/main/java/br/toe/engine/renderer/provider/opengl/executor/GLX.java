package br.toe.engine.renderer.provider.opengl.executor;

import org.lwjgl.opengl.*;

public abstract class GLX {
    private static final GLExecutor executor;

    static {
        final var capabilities = GL.getCapabilities();
        if (capabilities.OpenGL43)
            executor = new GL43PExecutor();

        else
            executor = new GL42LExecutor();
    }

    public static <T> T call(String target, Object ... args) {
        return executor.call(target, args);
    }
}
