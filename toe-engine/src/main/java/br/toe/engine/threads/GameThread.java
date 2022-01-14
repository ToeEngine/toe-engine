package br.toe.engine.threads;

import br.toe.engine.renderer.layer.*;
import br.toe.framework.logging.*;

import java.util.*;

public final class GameThread extends SharedThread {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameThread.class);

    @Override
    protected void doInitialize () {
        Arrays.stream(Layers.layers()).forEach(Layer::initialize);
    }

    @Override
    protected void perFrame () {
        Arrays.stream(Layers.layers()).forEach(Layer::update);
        Arrays.stream(Layers.layers()).forEach(Layer::render);
    }

    @Override
    protected void perSecond () {
    }

    @Override
    protected void doDestroy () {
        Arrays.stream(Layers.layers()).forEach(Layer::destroy);
    }
}
