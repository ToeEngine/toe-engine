package br.toe.engine.platform.linux;

import br.toe.framework.logging.*;
import br.toe.utils.*;

import java.text.*;
import java.util.*;

public final class LinuxLogWriter implements LogWriter {
    private static final ThreadLocal<SimpleDateFormat> DTF = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS"));

    private static final String RESET = "\u001B[0m";

    private static final String GREEN = "\u001B[32m";
    private static final String BLACK = "\u001B[30m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String WHITE = "\u001B[37m";

    private LinuxLogWriter(){}

    @Override
    public void write (LogLevel level, long timestamp, String thread, String logger, String message) {
        System.out.printf(
                "%s%s [%s] %s %s - %s%s%n",
                switch (level) {
                    case FATAL, ERROR -> RED;
                    case WARNING -> YELLOW;
                    case INFO -> GREEN;
                    case DEBUG -> CYAN;
                    case TRACE -> PURPLE;
                    default -> BLUE;
                },
                DTF.get().format(new Date(timestamp)),
                CurrentThread.getName(),
                level.formatted(),
                logger,
                message,
                RESET
        );
    }

}
