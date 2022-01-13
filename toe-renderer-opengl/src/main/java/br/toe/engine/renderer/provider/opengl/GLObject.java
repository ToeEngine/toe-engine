package br.toe.engine.renderer.provider.opengl;

import br.toe.engine.platform.*;
import br.toe.engine.utils.*;
import br.toe.framework.logging.*;
import br.toe.game.*;
import br.toe.utils.*;
import net.bytebuddy.*;
import net.bytebuddy.asm.*;
import net.bytebuddy.dynamic.*;
import net.bytebuddy.dynamic.loading.*;
import net.bytebuddy.pool.*;

import static net.bytebuddy.matcher.ElementMatchers.*;

public abstract class GLObject extends External<Integer> {

    static {
        if (GameSettings.get("game.build", ReleaseType.class) == ReleaseType.DEVELOPMENT) {
            final var logger = LoggerFactory.getLogger(GLObject.class);
            logger.trace("Instrumenting OpenGL API BEGIN");
            final var loader = TypePool.Default.ofSystemLoader();
            final var classFileLoader = ClassFileLocator.ForClassLoader.ofSystemLoader();
            final var classLoader = CurrentThread.classLoader();
            final var strategy = ClassReloadingStrategy.fromInstalledAgent();

            final var byteBudy = new ByteBuddy();
            for (var name : new String[]{
                    "GL11C", "GL12C", "GL13C", "GL14C", "GL15C",
                    "GL20C", "GL21C",
                    "GL30C", "GL31C", "GL32C", "GL33C",
                    "GL41C", "GL42C", "GL43C", "GL44"
            }) {
                final var start = System.nanoTime();
                byteBudy
                        .redefine(
                                loader.describe("org.lwjgl.opengl.%s".formatted(name)).resolve(),
                                classFileLoader)
                        .visit(
                                Advice.to(GLInterceptor.class).on(nameStartsWith("gl")))
                        .make()
                        .load(
                                classLoader,
                                strategy);
                logger.trace("Instrumented org.lwjgl.opengl.%s.formatted(name) in %d ms", name, Time.toMillis(System.nanoTime() - start));
            }

            logger.trace("Instrumenting OpenGL API END");
        }
    }

}
