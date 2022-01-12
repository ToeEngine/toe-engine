package br.toe.utils;

import java.io.*;
import java.util.*;

public abstract class CurrentThread {
    private static final String SEPARATOR = System.getProperty("line.separator");

    private CurrentThread(){}

    public static ClassLoader classLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static InputStream get(String resource) {
        final var stream = classLoader().getResourceAsStream(resource);

        if (stream == null)
            throw new GeneralException("Resource [%s] not found.".formatted(resource));

        try {
            return stream;
        } catch (Exception ex) {
            throw new GeneralException("Unable to load resource [%s].".formatted(resource), ex);
        }
    }

    public static String read(String resource) {
        final var stream = get(resource);

        try (final var reader = new BufferedReader(new InputStreamReader(stream))) {
            final var buffer = new StringBuilder();

            var line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
                buffer.append(SEPARATOR);
            }

            return buffer.toString();
        } catch (Exception ex) {
            throw new GeneralException("Unable to load resource [%s].".formatted(resource), ex);
        }
    }

    public static void sleep(int millis) {
        sleep((long) millis);
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {}
    }

    public static void busy(long delay) {
        if (delay < 0)
            throw new GeneralException("Delay is negative");

        final var start = System.nanoTime();
        while(System.nanoTime() - start < delay);
    }

    public static String getName() {
        return Thread.currentThread().getName();
    }
    public static void setName(String name) {
        Thread.currentThread().setName(name);
    }

    public static void setPriority(int priority) {
        Thread.currentThread().setPriority(priority);
    }

    public static List<String> stack() {
        final var walker = StackWalker.getInstance();
        final var stack = new ArrayList<String>();

        walker.forEach(frame -> stack.add("%s:%s".formatted(frame.getFileName(), frame.getLineNumber())));
        stack.remove(0);

        return stack;
    }
}
