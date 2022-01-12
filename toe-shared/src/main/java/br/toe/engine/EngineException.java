package br.toe.engine;

import br.toe.*;

public class EngineException extends ToeException {

    public EngineException (String message) {
        super(message);
    }

    public EngineException (String message, Throwable cause) {
        super(message, cause);
    }

}
