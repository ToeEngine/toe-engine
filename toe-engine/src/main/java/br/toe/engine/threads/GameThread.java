package br.toe.engine.threads;

import br.toe.*;
import br.toe.engine.platform.logger.*;
import br.toe.engine.platform.window.*;
import br.toe.engine.renderer.*;
import br.toe.engine.renderer.layer.*;

import java.util.*;

public final class GameThread extends SharedThread {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameThread.class);

    private Window window;
    private Renderer renderer;

    @Override
    protected void doInitialize () {
        window = Window.get();
        window.initialize();

        renderer = new Renderer();
        renderer.initialize();

        Arrays.stream(Layers.layers()).forEach(Layer::initialize);

        window.show();
    }

    @Override
    protected void perFrame () {
        renderer.clear();
        renderer.render();

        Arrays.stream(Layers.layers()).forEach(Layer::update);

        window.refresh();
    }

    @Override
    protected void perSecond () {

    }

    @Override
    protected void doDestroy () {
        if (window != null) {
            window.hide();
            try {window.destroy();} catch (ToeException ignored){}
        }

        Arrays.stream(Layers.layers()).forEach(Layer::destroy);

        if (renderer != null)
            try {renderer.destroy();} catch (ToeException ignored){}
    }
}
