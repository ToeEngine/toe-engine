package br.toe.engine.renderer.provider.opengl.executor;

import br.toe.engine.renderer.*;
import br.toe.engine.renderer.provider.*;
import br.toe.framework.logging.*;
import br.toe.framework.reflection.*;
import org.lwjgl.opengl.*;

abstract class AbstractGLExecutor implements GLExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(GLExecutor.class);
    private static final String RENDERER_NAME = Renderer.class.getSimpleName();


    @Override
    public <T> T call (String target, Object... args) {
        // ------------------ Find Method ------------------
        final var method = ClassUtils.getMethod(GL46C.class, target, args);
        if (method == null)
            throw new RendererProviderException("Method not found");

        // ------------------ Invoke Method ------------------
        final var owner = method.getDeclaringClass();

//        final var stack = CurrentThread.stack();
//        if (stack.stream().noneMatch(s -> s.contains(RENDERER_NAME)))
//            LOGGER.trace("(%s) %s.%s(%s)", stack.get(1), owner.getSimpleName(), target, args.length == 0 ? "" : "...");

        return (T) MethodUtils.invoke(owner, method, args);
    }
}
