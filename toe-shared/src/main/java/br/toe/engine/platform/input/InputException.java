package br.toe.engine.platform.input;

import br.toe.engine.platform.*;

public class InputException extends PlatformException {
    public InputException (String message) {
        super(message);
    }

    public InputException (String message, Throwable cause) {
        super(message, cause);
    }
}
