package br.toe.engine.platform.window;

import br.toe.engine.platform.*;

public final class WindowException extends PlatformException {
    public WindowException (String message) {
        super(message);
    }

    public WindowException (String message, Throwable cause) {
        super(message, cause);
    }
}
