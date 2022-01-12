package br.toe.utils;

import br.toe.*;

public final class GeneralException extends ToeException {

    public GeneralException (String message) {
        super(message);
    }

    public GeneralException (String message, Throwable cause) {
        super(message, cause);
    }
}
