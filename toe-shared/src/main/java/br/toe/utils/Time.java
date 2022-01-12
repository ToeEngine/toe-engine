package br.toe.utils;

import java.util.concurrent.*;

public abstract class Time {
    private Time(){}

    public static final long NANOS = 1000000000L;
    public static final long MILLIS = 1000L;

    public static final float NANO_TO_MILLIS = (float) 10e-6;

    public static long nanos() {
        return System.nanoTime();
    }

    public static long millis() {
        return System.currentTimeMillis();
    }

    public static long toMillis(long nanos) {
        return TimeUnit.NANOSECONDS.toMillis(nanos);
    }
}
