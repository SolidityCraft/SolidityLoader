package io.soliditycraft.solidityloader.api;

import io.soliditycraft.solidityloader.SolidityLoader;
import io.soliditycraft.solidityloader.SolidityVersion;
import io.soliditycraft.solidityloader.addons.SolidityAddonManager;
import io.soliditycraft.solidityloader.commands.SolidityCommandManager;
import io.soliditycraft.solidityloader.sender.SolidityCommandSender;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class Solidity {

    @Getter
    private static SolidityAddonManager addonManager;
    @Getter
    private static SolidityCommandManager commandManager;
    @Getter
    private static SolidityCommandSender consoleCommandSender;
    @Getter
    private static String solidityVersion;

    public static void initialize(@NotNull SolidityLoader loader) {
        addonManager = loader.getAddonManager();
        commandManager = loader.getCommandManager();
        consoleCommandSender = SolidityCommandSender.from(Bukkit.getConsoleSender());
        solidityVersion = SolidityVersion.getVersion();
    }

}
