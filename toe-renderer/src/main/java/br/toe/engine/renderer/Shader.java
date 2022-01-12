package br.toe.engine.renderer;

import br.toe.framework.cdi.*;
import br.toe.utils.*;

public interface Shader extends Lifecycle, Bindable {

    static Shader create() {
        return Factory.create(Shader.class);
    }

    boolean has(ShaderType type);
    void attach(ShaderType type, String source);

}
