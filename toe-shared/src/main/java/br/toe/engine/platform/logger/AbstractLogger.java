package br.toe.engine.platform.logger;

import java.time.format.*;
import java.util.*;

public class AbstractLogger {
    private String loggerName;
    protected final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss.SSS", Locale.getDefault());

    final void setLoggerName(String desired) {
        if (loggerName != null)
            throw new LoggerException("Logger Name already set");

        loggerName = desired;
    }

    protected String getLoggerName() {
        return loggerName;
    }
}
