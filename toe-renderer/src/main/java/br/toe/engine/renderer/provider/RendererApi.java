package br.toe.engine.renderer.provider;

import br.toe.engine.renderer.*;
import br.toe.framework.cdi.*;
import br.toe.utils.*;

public interface RendererApi extends Lifecycle {

    static RendererApi create () {
        return Factory.create(RendererApi.class);
    }

    void setWireframe(boolean desired);

    void clear();
    void draw(Mesh mesh, Shader shader);
}
