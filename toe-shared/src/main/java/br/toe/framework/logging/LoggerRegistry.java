package br.toe.framework.logging;

import lombok.*;

public abstract class LoggerRegistry {

    @Getter
    @Setter
    private static volatile LogLevel level = LogLevel.ALL;

}
