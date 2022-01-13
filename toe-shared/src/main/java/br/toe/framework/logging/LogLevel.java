package br.toe.framework.logging;

public enum LogLevel {
    OFF(-1, ""),
    FATAL(0, "FATAL  "),
    ERROR(1, "ERROR  "),
    WARNING(2, "WARNING"),
    INFO(3, "INFO   "),
    DEBUG(4, "DEBUG  "),
    TRACE(5, "TRACE  "),
    ALL(5, "");

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
