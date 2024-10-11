package io.soliditycraft.solidityloader;

import io.soliditycraft.solidityloader.addons.SolidityAddonManager;
import io.soliditycraft.solidityloader.commands.SolidityCommandManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Getter
public final class SolidityLoader extends JavaPlugin {

    public static File SOLIDITY_ADDON_FOLDER;
    @Getter
    private static SolidityLoader instance;

    /* - */
    private final SolidityLogger slogger = SolidityLogger.getLogger();
    private SolidityAddonManager addonManager;
    private SolidityCommandManager commandManager;

    @Override
    public void onLoad() {
        instance = this;
        slogger.line(64);
        slogger.info("&6> &5Solidity &6version: " + getDescription().getVersion());
        saveDefaultConfig();
        SOLIDITY_ADDON_FOLDER = new File(getDataFolder(), "addons");
        if (!SOLIDITY_ADDON_FOLDER.exists()) SOLIDITY_ADDON_FOLDER.mkdir();

        /* Class Initialization */
        addonManager = new SolidityAddonManager(this);
        commandManager = new SolidityCommandManager(this);
        slogger.line(64);
    }

    @Override
    public void onEnable() {
        slogger.line(64);
        slogger.info("Loading Solidity Addons...");
        addonManager.automatic();
        slogger.info("Loading Solidity Base Command (/solidity)...");
        commandManager.loadDefaultCommands();
        commandManager.loadBaseCommand();

        /* Final log message */
        slogger.line(64);
        slogger.info("&5Solidity &6has been enabled successfully!");
        slogger.empty();
        slogger.info("&6> Solidity has loaded addons successfully and has successfully enabled them!");
        slogger.info("&6> Currently you are using &5Solidity &6v" + SolidityVersion.getVersion());
        slogger.line(64);

    }

    @Override
    public void onDisable() {
        addonManager.disableAllAddons();

        /* Final log message */
        slogger.line(64);
        slogger.info("&5Solidity &6has been disabled successfully!");
        slogger.empty();
        slogger.info("&6> Solidity has disabled addons successfully and has successfully unloaded them!");
        slogger.info("&6> Thanks for using &5Solidity &6" + (SolidityVersion.isDevelopment() ? "Development" : "Free") + " version.");
        slogger.line(64);
    }
}
