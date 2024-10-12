package io.soliditycraft.solidityloader.addons;

import io.soliditycraft.solidityloader.SolidityLoader;
import io.soliditycraft.solidityloader.SolidityLogger;
import io.soliditycraft.solidityloader.commands.SolidityCommand;
import io.soliditycraft.solidityloader.commands.SolidityCommandManager;
import io.soliditycraft.solidityloader.configuration.SolidityAddonConfiguration;
import io.soliditycraft.solidityloader.listener.SolidityAddonListener;
import io.soliditycraft.solidityloader.listener.SolidityListenerManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Getter
public abstract class SolidityAddon {

    private SolidityLoader loader;
    private SolidityAddonData info;
    private File addonDataFolder;
    private SolidityLogger logger;
    private SolidityAddonConfiguration configuration;

    synchronized void initialize(SolidityLoader loader, SolidityAddonData info, File addonDataFolder) {
        this.setLoader(loader);
        this.info = info;
        this.addonDataFolder = addonDataFolder;
        this.logger = SolidityLogger.getLogger(getInfo().getName());
        this.configuration = new SolidityAddonConfiguration(this, "config.yml");
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

    public void registerListener(@NotNull SolidityAddonListener listener) {
        SolidityListenerManager.register(listener, this);
    }

    public void registerCommand(SolidityCommand command) {
        this.getCommandManager().registerCommand(command);
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

    public String getName() {
        return this.getInfo().getName();
    }

    public void saveResource(String name, boolean replace) {
        // Create the destination file
        File file = new File(this.getAddonDataFolder(), name);

        if (file.exists() && !replace) return;

        InputStream resourceStream = this.getResource(name);
        if (resourceStream == null) {
            getLogger().warn("Resource " + name + " not found in the addon.");
            return;
        }

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try {
            java.nio.file.Files.copy(resourceStream, file.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            getLogger().error("SolidityAddon saveResource has thrown an error while trying to save a resource: " + e);
        }
    }

    public InputStream getResource(String name) {
        return this.getClass().getClassLoader().getResourceAsStream(name);
    }

    public void saveDefaultConfig() {
        configuration.saveDefaultConfig();
    }

    public FileConfiguration getConfiguration() {
        return this.configuration.getConfig();
    }

    public void saveConfig() {
        this.configuration.saveConfig();
    }

    public void reloadConfig() {
        this.configuration.reloadConfig();
    }
}
