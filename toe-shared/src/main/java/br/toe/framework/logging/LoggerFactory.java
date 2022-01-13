package br.toe.framework.logging;

public class LoggerFactory {

    public static Logger getLogger(Class<?> klass) {
        return new DefaultLogger(klass.getTypeName());
    }

}
