package io.soliditycraft.solidityloader.listener;

import io.soliditycraft.solidityloader.SolidityLoader;
import io.soliditycraft.solidityloader.addons.SolidityAddon;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class SolidityListenerManager {

    public static void register(@NotNull SolidityListener listener) {
        listener.initialize(SolidityLoader.getInstance());
        Bukkit.getPluginManager().registerEvents(listener, SolidityLoader.getInstance());
    }

    public static void register(@NotNull SolidityAddonListener listener, SolidityAddon addon) {
        listener.initialize(addon);
        Bukkit.getPluginManager().registerEvents(listener, SolidityLoader.getInstance());
    }

}
