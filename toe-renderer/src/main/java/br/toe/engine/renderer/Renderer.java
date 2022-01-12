package br.toe.engine.renderer;

import br.toe.engine.renderer.provider.*;
import br.toe.utils.*;

public final class Renderer implements Lifecycle {
    private RendererApi api;

    @Override
    public void initialize () {
        api = RendererApi.create();
        api.initialize();
    }

    public void clear() {
        api.clear();
    }

    public void render() {

    }

    @Override
    public void destroy () {
        api.destroy();
    }
}
