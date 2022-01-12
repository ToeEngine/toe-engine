package br.toe.game;

import br.toe.utils.*;

import java.util.*;

public abstract class GameSettings {
    private static final Map<String, Object> settings = new TreeMap<>();

    private GameSettings (){}

    public synchronized static void set(String name, Object value) {
        settings.put(name, value);
    }

    public static boolean getBoolean(String name) {
        return get(name, Boolean.class);
    }

    public static int getInteger(String name) {
        return get(name, Integer.class);
    }

    public static float getFloat(String name) {
        return get(name, Float.class);
    }

    public static long getLong(String name) {
        return get(name, Long.class);
    }

    public static String getString(String name) {
        return get(name, String.class);
    }

    public static <T> T get(String name, Class<T> type) {
        return type.cast(settings.get(name));
    }

    public static boolean isNull(String name) {
        return ! settings.containsKey(name);
    }

    public static List<Pair<String, Object>> known() {
        final var result = new ArrayList<Pair<String, Object>>();

        for (var entry : settings.entrySet())
            result.add(new Pair<>(entry.getKey(), entry.getValue()));

        return Collections.unmodifiableList(result);
    }
}
