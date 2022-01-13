package br.toe.framework.logging;

public interface LogWriter {

    void write(LogLevel level, long timestamp, String thread, String logger, String message);

}
