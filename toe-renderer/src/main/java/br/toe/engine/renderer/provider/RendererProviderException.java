package br.toe.engine.renderer.provider;

import br.toe.engine.*;

public final class RendererProviderException extends EngineException {
    public RendererProviderException (String message) {
        super(message);
    }

    public RendererProviderException (String message, Throwable cause) {
        super(message, cause);
    }
}
