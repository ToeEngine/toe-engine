package br.toe.engine.platform.logger;

import br.toe.framework.cdi.*;

public abstract class LoggerFactory {
    private LoggerFactory() {}

    public static Logger getLogger(Class<?> klass) {
        final var logger = Factory.create(Logger.class);

        if ( ! (logger instanceof AbstractLogger)) {
            final var typeName = logger.getClass().getTypeName();
            final var ownName = AbstractLogger.class.getTypeName();

            throw new LoggerException("%s does not extends %s".formatted(typeName, ownName));
        }

        ((AbstractLogger) logger).setLoggerName(klass.getTypeName());
        return logger;
    }
}
