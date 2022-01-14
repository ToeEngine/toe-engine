package br.toe.game;

import br.toe.engine.event.*;
import br.toe.engine.platform.*;
import br.toe.engine.platform.linux.*;
import br.toe.engine.platform.window.*;
import br.toe.engine.platform.window.event.*;
import br.toe.engine.renderer.*;
import br.toe.engine.renderer.layer.*;
import br.toe.engine.renderer.provider.*;
import br.toe.engine.threads.*;
import br.toe.framework.cdi.*;
import br.toe.framework.logging.*;
import br.toe.framework.reflection.*;
import br.toe.utils.*;
import net.bytebuddy.agent.*;
import org.joml.*;

import java.util.concurrent.*;

public abstract class AbstractGame {
    static {
        if (Platform.IS_LINUX)
            Registry.register(LogWriter.class, LinuxLogWriter.class);


        final var logger = LoggerFactory.getLogger(AbstractGame.class);
        logger.debug("Static Initializer BEGIN");
        ByteBuddyAgent.install();
        Layers.push(new WorldLayer());
        logger.debug("Static Initializer END");
    }

    protected AbstractGame() {
        final var logger = LoggerFactory.getLogger(AbstractGame.class);
        logger.debug("<cinit> BEGIN");
        setDefaults();
        registerDefaults();
        registerRendererProvider();

        EventBus.get().callback(WindowClosedEvent.class, event ->  {
            GameState.running = false;
            event.setHandled();
        });

        EventBus.get().callback(event -> {
            final var layers = Layers.layers();
            for (Layer layer : layers) {
                final var overlays = layer.overlays();

                for (Overlay overlay : overlays) {
                    overlay.handle(event);
                    if (event.isHandled())
                        break;
                }

                if (event.isHandled())
                    break;

                layer.handle(event);
                if (event.isHandled())
                    break;
            }
        });
        logger.debug("<cinit> END");
    }

    private void setDefaults() {
        GameSettings.set("game.build", ReleaseType.DEVELOPMENT);
        GameSettings.set("platform.window.title", "Default Window Title");
        GameSettings.set("platform.window.size", new Vector2i(800, 600));

        GameSettings.set("renderer.frame.rate", 60);
        GameSettings.set("renderer.mode.debug", false);
        GameSettings.set("renderer.background.color", new Vector4f(.72f, 0f, .72f, 1f));
    }

    private void registerDefaults() {
        Registry.register(EventBus.class, DefaultEventBus.class);
        Registry.register(Monitor.class, GLFWMonitor.class);
    }

    private void registerRendererProvider() {
        var klass = ClassUtils.forName("br.toe.engine.renderer.provider.GLProvider");
        if (klass != null)
            ((RendererProvider) ClassUtils.create(klass)).initialize();

        if (klass == null) {
            klass = ClassUtils.forName("br.toe.engine.renderer.provider.VKProvider");
            if (klass != null)
                ( (RendererProvider) ClassUtils.create(klass) ).initialize();
        }

        if (klass == null)
            throw new GeneralException("No RendererApi Implementation Found");
    }

    public final void run () {
        final var logger = LoggerFactory.getLogger(AbstractGame.class);
        logger.info("Initializing");

        logger.debug("------ Game Settings ------");
        GameSettings.known().forEach(pair -> logger.debug(pair.toString()));
        logger.debug("------ Game Settings ------");

//        Layers.push(new ImGuiLayer());

        logger.info("Launching");
        GameState.running = true;

        final var executor = Executors.newCachedThreadPool();
        executor.execute(new RenderThread());
        executor.execute(new GameThread());
        executor.execute(new AudioThread());
        executor.execute(new NetworkThread());
        executor.shutdown();
    }

    protected void push(Layer layer) {
        Layers.push(layer);
    }

}
