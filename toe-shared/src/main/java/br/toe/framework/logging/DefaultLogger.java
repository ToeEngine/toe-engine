package br.toe.framework.logging;

import br.toe.engine.platform.*;
import br.toe.framework.cdi.*;
import br.toe.utils.*;
import lombok.*;

@RequiredArgsConstructor
final class DefaultLogger implements Logger{
    private static final LogWriter WRITER = Factory.create(LogWriter.class);

    private final String logger;

    @Override
    public void info (Object message, Object... args) {
        append(LogLevel.INFO, message.toString().formatted(args));
    }

    @Override
    public void warn (Object message, Object... args) {
        append(LogLevel.WARNING, message.toString().formatted(args));

    }

    @Override
    public void debug (Object message, Object... args) {
        append(LogLevel.DEBUG, message.toString().formatted(args));

    }

    @Override
    public void error (Object message, Object... args) {
        append(LogLevel.ERROR, message.toString().formatted(args));
    }

    @Override
    public void error (Throwable throwable) {
        exception(LogLevel.ERROR, throwable);
    }

    @Override
    public void trace (Object message, Object... args) {
        append(LogLevel.TRACE, message.toString().formatted(args));
    }

    @Override
    public void fatal (Object message, Object... args) {
        append(LogLevel.FATAL, message.toString().formatted(args));
    }

    @Override
    public void fatal (Throwable throwable) {
        exception(LogLevel.FATAL, throwable);
    }

    private void exception(LogLevel level, Throwable throwable) {
        append(
                level,
                "%n%nException: %s%n  Message: %s%n%nStack Trace:%n%s"
                        .formatted(
                                throwable.getClass().getTypeName(),
                                throwable.getLocalizedMessage(),
                                buildStackTrace(throwable))
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

    private void append(LogLevel level, String message) {
        if (level.code() > LoggerRegistry.getLevel().code())
            return;

        WRITER.write(level, System.currentTimeMillis(), CurrentThread.getName(), logger, message);
    }
}
