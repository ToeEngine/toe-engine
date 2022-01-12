package br.toe.engine.platform;

public abstract class Platform {
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private Platform (){}

    public static final String OS_NAME = System.getProperty("os.name").toUpperCase();

    private static final String OS_PREFIX_LINUX = "LINUX";
    private static final String OS_PREFIX_WINDOWS = "WINDOWS";
    private static final String OS_PREFIX_MAC = "MAC";
    private static final String OS_PREFIX_MAC_OSX = OS_PREFIX_MAC + " OS X";

    public static final boolean IS_WINDOWS = OS_NAME.startsWith(OS_PREFIX_WINDOWS);
    public static final boolean IS_MAC = OS_NAME.startsWith(OS_PREFIX_MAC);
    public static final boolean IS_MAC_OSX = OS_NAME.startsWith(OS_PREFIX_MAC_OSX);
    public static final boolean IS_LINUX = OS_NAME.startsWith(OS_PREFIX_LINUX);
}
