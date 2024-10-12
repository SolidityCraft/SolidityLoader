package io.soliditycraft.solidityloader;

public class SolidityVersion {

    private static final String VERSION = "1.0.0-B003";

    public static boolean isDevelopment() {
        return VERSION.equals("DEV");
    }

    public static String getVersion() {
        return isDevelopment() ? "Development" : VERSION;
    }
}
