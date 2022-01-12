package br.toe.framework.reflection;

import br.toe.*;

import java.lang.reflect.*;

public abstract class MethodUtils {
    private MethodUtils(){}

    public static Object invoke(Object target, Method method, Object ... parameters) {
        try {
            method.setAccessible(true);
            return method.invoke(target, parameters);

        } catch (Exception ex) {
            if (ex.getCause() instanceof ToeException cause)
                throw cause;

            throw new ReflectionException(ex.getMessage(), ex);
        }
    }
}