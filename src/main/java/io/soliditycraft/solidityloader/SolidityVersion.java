package io.soliditycraft.solidityloader;

public class SolidityVersion {

    private static final String VERSION = "DEV";

    public static boolean isDevelopment() {
        return VERSION.equals("DEV");
    }

}
