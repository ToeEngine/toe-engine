package br.toe.framework.reflection;

import br.toe.utils.*;

import java.lang.reflect.*;

public class Callback extends Pair<Object, Method> {

    public Callback (Object key, Method value) {
        super(key, value);
    }


    public String toString() {
        return "%s.%s(%s)".formatted(
                getKey().getClass().getSimpleName(),
                getValue().getName(),
                getValue().getParameterCount() == 0 ? "" : "..."
        );
    }
}
