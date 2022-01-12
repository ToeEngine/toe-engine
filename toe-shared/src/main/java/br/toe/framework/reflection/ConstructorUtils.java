package br.toe.framework.reflection;

import java.lang.reflect.*;

public abstract class ConstructorUtils {

    private ConstructorUtils(){}

    public static void makeAccessible(Constructor<?> constructor) {
        constructor.setAccessible(true);
    }
}