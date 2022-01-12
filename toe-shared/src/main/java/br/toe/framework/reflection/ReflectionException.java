package br.toe.framework.reflection;

import br.toe.framework.*;

public final class ReflectionException extends FrameworkException {

    ReflectionException(String message) {
        this(message, null);
    }

    ReflectionException(String message, Throwable cause) {
        super(message, cause);
    }
}