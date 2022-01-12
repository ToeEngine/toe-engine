package br.toe.framework.cdi;

import br.toe.framework.cdi.annotation.*;

import java.util.*;
import java.util.concurrent.*;

public abstract class Registry {
    static final Map<Class<?>, Class<?>> database = new ConcurrentHashMap<>();
    static final Map<Class<?>, Object> singletons = new ConcurrentHashMap<>();

    private Registry(){}

    public static boolean contains(Class<?> type) {
        if (type == null )
            throw new CDIException("Type is null");

        return database.containsKey(type);
    }

    public static <T> void register(Class<T> type, Class<? extends T> implementation) {
        if (type == null || implementation == null)
            throw new CDIException("Type or Implementation is null");

        if (!type.isAssignableFrom(implementation))
            throw new CDIException("Implementation isn't of given type");

        if (database.containsKey(type)) {
            final var registered = database.get(type);
            if (registered.getAnnotation(Overridable.class) == null)
                throw new CDIException("Type [%s] cannot be overwritten".formatted(type.getTypeName()));
        }

        database.put(type, implementation);
    }
}
