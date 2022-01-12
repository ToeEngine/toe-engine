package br.toe.framework.reflection;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;

public abstract class ClassUtils {

    private ClassUtils(){}

    public static boolean isAbstract(Class<?> klass) {
        return Modifier.isAbstract(klass.getModifiers());
    }

    public static boolean isPublic(Class<?> klass) {
        return Modifier.isPublic(klass.getModifiers());
    }

    public static boolean isLocal(Class<?> klass) {
        return klass.getTypeName().startsWith("org.openengine");
    }

    public static Class<?> forName(String typeName) {
        try {
            return Class.forName(typeName);
        } catch (Exception ex) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T create (Class<T> klass, Object ... parameters) {
        if (klass.getEnclosingClass() != null)
            throw new ReflectionException("Oh no!! InnerClass?!");

        for (var constructor: klass.getDeclaredConstructors())
            if (constructor.getParameterCount() == parameters.length)
                try {
                    ConstructorUtils.makeAccessible(constructor);
                    return (T) constructor.newInstance(parameters);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new ReflectionException("Unable to create [%s] instance".formatted(klass.getTypeName()), e);
                }

        throw new ReflectionException("Constructor for [%s] not found".formatted(klass.getCanonicalName()));
    }

    public static List<Method> getMethods(Object object, Class<? extends Annotation> annotation) {
        return getMethods(object.getClass(), annotation);
    }

    public static List<Method> getMethods(Class<?> klass, Class<? extends Annotation> annotation) {

        return getAllMethods(klass)
                .stream()
                .filter(m -> m.isAnnotationPresent(annotation))
                .toList();
    }

    public static Optional<Method> getMethod(Class<?> klass, Class<? extends Annotation> annotation) {
        final var methods = getMethods(klass, annotation);

        if (methods.isEmpty())
            return Optional.empty();

        if (methods.size() > 1)
            throw new ReflectionException("Found [%s] methods with the annotation [%s]".formatted(methods.size(), annotation.getTypeName()));

        return Optional.of(methods.get(0));
    }

    public static Method getMethod(Class<?> klass, String name) {
        try {
            return klass.getDeclaredMethod(name);
        } catch (Exception ignored) {
            return null;
        }
    }

    public static List<Method> getAllMethods(Class<?> klass) {
        return getAllMethods(klass, true);
    }

    public static Method getMethod(Class<?> target, String name, Object ... parameters) {
        for (var method : getAllMethods(target)) {
            if (!method.getName().equals(name))
                continue;

            final var args = method.getParameterTypes();
            if (args.length != parameters.length)
                continue;

            final var arg2 = new Class<?>[parameters.length];
            for (int i = 0 ; i < parameters.length ; i++) {
                final var parameter = parameters[i];

                final var klass = parameter.getClass();
                arg2[i] = switch (klass.getSimpleName()) {
                    case "Byte" -> byte.class;
                    case "Short" -> short.class;
                    case "Integer" -> int.class;
                    case "Long" -> long.class;
                    case "Float" -> float.class;
                    case "Double" -> double.class;
                    case "Boolean" -> boolean.class;
                    default -> klass;
                };
            }

            var found = true;
            for (int i = 0; i < args.length; i++) {
                if ( ! args[i].isAssignableFrom(arg2[i]) ) {
                    found = false;
                    break;
                }
            }

            if (found)
                return method;
        }

        return null;
    }

    private static final ConcurrentMap<String, List<Method>> methods = new ConcurrentHashMap<>();
    public static List<Method> getAllMethods(Class<?> klass, boolean cache) {
        final var typeName = klass.getTypeName();
        if (typeName.startsWith("java"))
            return new ArrayList<>();

        if (cache && methods.containsKey(typeName))
            return methods.get(typeName);

        final var buffer = new ArrayList<Method>();
        final var parent = klass.getSuperclass();

        if (parent != null) {
            final var parentTypeName = parent.getTypeName();

            if (!parentTypeName.startsWith("java")) {
                if (methods.containsKey(parentTypeName))
                    buffer.addAll(methods.get(parentTypeName));

                else
                    buffer.addAll(getAllMethods(parent));
            }
        }

        buffer.addAll(Arrays.asList(klass.getDeclaredMethods()));
        methods.put(typeName, buffer);

        return buffer;
    }
}