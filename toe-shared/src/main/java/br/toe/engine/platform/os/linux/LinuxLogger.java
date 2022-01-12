package br.toe.engine.platform.os.linux;

import br.toe.engine.platform.*;
import br.toe.engine.platform.logger.*;
import br.toe.utils.*;

import java.time.*;

public class LinuxLogger extends AbstractLogger implements Logger {

    private LinuxLogger (){}

    private void log(LogLevel level, String message, String color, Object... args) {
        System.out.printf(
                "%s%s [%s] %s %s - %s%s%n",
                color,
                dateTimeFormatter.format(LocalDateTime.now()),
                CurrentThread.getName(),
                level.formatted(),
                getLoggerName(),
                message.formatted(args),
                AnsiColor.RESET
        );
    }

    @Override
    public void fatal(String message, Object... args) {
        log(LogLevel.FATAL, message, AnsiColor.RED, args);
    }

    @Override
    public void error(String message, Object... args) {
        log(LogLevel.ERROR, message, AnsiColor.RED, args);
    }

    @Override
    public void error(Throwable throwable) {
        error(
                "%n%nException: %s%n  Message: %s%n%nStack Trace:%n%s",
                throwable.getClass().getTypeName(),
                throwable.getLocalizedMessage(),
                buildStackTrace(throwable)
        );
    }

    @Override
    public void fatal(Throwable throwable) {
        fatal(
                "%n%nException: %s%n  Message: %s%n%nStack Trace:%n%s",
                throwable.getClass().getTypeName(),
                throwable.getLocalizedMessage(),
                buildStackTrace(throwable)
        );
    }

    private String buildStackTrace(Throwable throwable) {
        final var builder = new StringBuilder();
        for (var element : throwable.getStackTrace()) {
            builder
                    .append("  at ")
                    .append(element.getClassName())
                    .append(".")
                    .append(element.getMethodName())
                    .append("(")
                    .append(element.getFileName())
                    .append(":")
                    .append(element.getLineNumber())
                    .append(")")
                    .append(Platform.LINE_SEPARATOR);
        }

        return builder.toString();
    }

    @Override
    public void warn(String message, Object... args) {
        log(LogLevel.WARNING, message, AnsiColor.YELLOW, args);
    }

    @Override
    public void info(String message, Object... args) {
        log(LogLevel.INFO, message, AnsiColor.GREEN, args);
    }

    @Override
    public void debug(String message, Object... args) {
        log(LogLevel.DEBUG, message, AnsiColor.CYAN, args);
    }

    @Override
    public void trace(String message, Object... args) {
        log(LogLevel.TRACE, message, AnsiColor.PURPLE, args);
    }
}
