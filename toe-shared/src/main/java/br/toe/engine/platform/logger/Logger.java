package br.toe.engine.platform.logger;

public interface Logger {

    void warn(String message, Object ... args);
    void info(String message, Object ... args);
    void debug(String message, Object ... args);
    void trace(String message, Object ... args);
    void error(String message, Object ... args);
    void fatal(String message, Object ... args);

    void error(Throwable exception);
    void fatal(Throwable exception);

}
