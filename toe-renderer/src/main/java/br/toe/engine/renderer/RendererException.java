package br.toe.engine.renderer;

import br.toe.engine.*;

public class RendererException extends EngineException {
    public RendererException (String message) {
        super(message);
    }

    public RendererException (String message, Throwable cause) {
        super(message, cause);
    }
}
