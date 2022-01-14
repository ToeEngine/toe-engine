package br.toe.engine.threads;

import br.toe.*;
import br.toe.engine.platform.window.*;
import br.toe.engine.renderer.provider.*;
import br.toe.framework.logging.*;

public final class RenderThread extends SharedThread {
    private static final Logger LOGGER = LoggerFactory.getLogger(RenderThread.class);

    private Window window;
    private RendererApi api;

    @Override
    protected void doInitialize () {
        window = Window.get();
        window.initialize();

        api = RendererApi.create();
        api.initialize();


        window.show();
    }

    @Override
    protected void perFrame () {
        frame++;

        api.clear();

        window.refresh();
    }

    @Override
    protected void perSecond () {
        frame = 0;

    }

    @Override
    protected void doDestroy () {
        if (window != null) {
            window.hide();
            try {window.destroy();} catch (ToeException ignored){}
        }

        if (api != null)
            try {api.destroy();} catch (ToeException ignored){}

    }

}
