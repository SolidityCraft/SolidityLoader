package io.soliditycraft.solidityloader.addons;

import io.soliditycraft.solidityloader.SolidityLoader;
import io.soliditycraft.solidityloader.commands.SolidityCommandManager;
import io.soliditycraft.solidityloader.listener.SolidityListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class SolidityAddon {

    private SolidityLoader loader;
    private SolidityAddonData info;

    synchronized void initialize(SolidityLoader loader, SolidityAddonData info) {
        this.setLoader(loader);
        this.info = info;
    }

    /**
     * Event emitted on load when the addon loads
     */
    public abstract void onLoad();

    /**
     * Event emitted on enable when the addon enables
     */
    public abstract void onEnable();

    /**
     * Event emitted on disable when the addon disable
     */
    public abstract void onDisable();

    public SolidityCommandManager getCommandManager() {
        return getLoader().getCommandManager();
    }

    public void registerListener(@NotNull SolidityListener listener) {
        listener.initialize(this);
        getPluginManager().registerEvents(listener, getLoader());
    }

    public PluginManager getPluginManager() {
        return getServer().getPluginManager();
    }

    public Server getServer() {
        return Bukkit.getServer();
    }

    private void setLoader(SolidityLoader loader) {
        this.loader = loader;
    }
}
