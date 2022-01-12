package br.toe;

public abstract class ToeException extends RuntimeException {
    public ToeException (String message) {
        this(message, null);
    }

    public ToeException (String message, Throwable cause) {
        super(message, cause);
    }
}
