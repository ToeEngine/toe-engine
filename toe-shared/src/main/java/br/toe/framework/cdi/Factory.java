package br.toe.framework.cdi;

import br.toe.framework.cdi.annotation.*;
import br.toe.framework.reflection.*;

public abstract class Factory {

    private Factory(){}

    public static <T> T create (Class<T> type) {
        if (!Registry.database.containsKey(type))
            throw new CDIException("No implementation found for [%s]".formatted(type.getTypeName()));

        if (Registry.singletons.containsKey(type))
            return type.cast(Registry.singletons.get(type));

        final var implementation = Registry.database.get(type);
        final var result = type.cast(ClassUtils.create(implementation));

        if (implementation.getAnnotation(Singleton.class) != null)
            Registry.singletons.put(type, result);

        return result;
    }
}
