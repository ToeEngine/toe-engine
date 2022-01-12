package br.toe.engine.platform;

import br.toe.*;

public abstract class PlatformException extends ToeException {

    public PlatformException (String message) {
        this(message, null);
    }

    public PlatformException (String message, Throwable cause) {
        super(message, cause);
    }
}
