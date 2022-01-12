package br.toe.engine.platform.logger;

public enum LogLevel {
    FATAL(0, "FATAL  "),
    ERROR(1, "ERROR  "),
    WARNING(2, "WARNING"),
    INFO(3, "INFO   "),
    DEBUG(4, "DEBUG  "),
    TRACE(5, "TRACE  ");

    private final int code;
    private final String formatted;

    LogLevel(int code, String formatted) {
        this.code = code;
        this.formatted = formatted;
    }

    public int code() {
        return code;
    }

    public String formatted() {
        return formatted;
    }
}
