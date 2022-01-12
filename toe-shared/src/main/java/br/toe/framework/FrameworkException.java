package br.toe.framework;

import br.toe.*;

public abstract class FrameworkException extends ToeException {

    public FrameworkException (String message) {
        this(message, null);
    }

    public FrameworkException (String message, Throwable cause) {
        super(message, cause);
    }
}
