package br.toe.framework.logging;

public interface Logger {

    void warn(Object message, Object ... args);
    void info(Object message, Object ... args);
    void debug(Object message, Object ... args);
    void trace(Object message, Object ... args);
    void error(Object message, Object ... args);
    void fatal(Object message, Object ... args);

    void error(Throwable exception);
    void fatal(Throwable exception);

}
