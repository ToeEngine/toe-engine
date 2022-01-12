package br.toe.engine.renderer.layer;

import br.toe.engine.platform.logger.*;

import java.util.*;
import java.util.concurrent.*;

public abstract class Layer implements Base {
    private static final Logger LOGGER = LoggerFactory.getLogger(Layer.class);

    private final Queue<Overlay> overlays = new ConcurrentLinkedQueue<>();

    protected abstract void doInitialize();
    protected abstract void doDestroy();

    @Override
    public final void initialize () {
        LOGGER.debug("Initializing Layer [%s]", getName());
        doInitialize();
    }

    @Override
    public final void destroy () {
        LOGGER.debug("Destroying Layer [%s]", getName());
        doDestroy();
    }

    public final void push(Overlay overlay) {
        overlays.add(overlay);
    }

    public final Overlay pop() {
        return overlays.remove();
    }

    public final Overlay[] overlays() {
        return overlays.toArray(new Overlay[0]);
    }
}
