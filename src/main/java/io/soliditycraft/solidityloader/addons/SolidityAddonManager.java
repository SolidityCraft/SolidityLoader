package io.soliditycraft.solidityloader.addons;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.soliditycraft.solidityloader.SolidityLoader;
import io.soliditycraft.solidityloader.SolidityLogger;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * The SolidityAddonManager class is responsible for managing addons within the Solidity ecosystem.
 * It handles loading, enabling, and disabling of addons from JAR files and maintains the state
 * of loaded and enabled addons.
 */
@Getter
public class SolidityAddonManager {

    private final Map<String, SolidityAddon> loadedAddons = new HashMap<>();
    private final Map<String, SolidityAddon> enabledAddons = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SolidityLoader loader;
    private final SolidityLogger logger = SolidityLogger.getLogger();
    private final PluginManager pluginManager;

    public SolidityAddonManager(SolidityLoader loader) {
        this.loader = loader;
        pluginManager = Bukkit.getPluginManager();
    }

    /**
     * Loads an addon from a specified JAR file.
     *
     * @param jarFile The JAR file containing the addon.
     * @throws Exception If an error occurs while loading the addon, such as
     *                   missing metadata or class loading issues.
     */
    public void loadAddon(@NotNull File jarFile) throws Exception {
        logger.info("Loading Solidity Addon: " + jarFile.getName());
        try (JarFile jar = new JarFile(jarFile)) {
            JarEntry entry = jar.getJarEntry("solidity.addon.json");
            if (entry == null) {
                throw new IllegalArgumentException("solidity.addon.json not found in the JAR: " + jarFile.getName());
            }

            InputStream inputStream = jar.getInputStream(entry);
            SolidityAddonData addonInfo = objectMapper.readValue(inputStream, SolidityAddonData.class);
            String name = addonInfo.getName();
            String version = addonInfo.getVersion();
            String id = addonInfo.getId();
            String mainClassName = addonInfo.getMain();
            boolean loadPlugin = addonInfo.isLoadPlugin();

            if (mainClassName == null || name == null || version == null || id == null) {
                throw new IllegalArgumentException("The addon data provided within the addon jar is corrupted. Please check: " + jarFile.getName());
            }

            URL jarUrl = jarFile.toURI().toURL();
            URLClassLoader classLoader = new URLClassLoader(new URL[]{jarUrl}, getClass().getClassLoader());

            Class<?> mainClass = classLoader.loadClass(mainClassName);
            if (!SolidityAddon.class.isAssignableFrom(mainClass)) {
                throw new IllegalArgumentException("The main class does not extend SolidityAddon: " + mainClassName);
            }

            SolidityAddon addon = (SolidityAddon) mainClass.getDeclaredConstructor().newInstance();
            File addonDataFolder = new File(SolidityLoader.SOLIDITY_ADDON_FOLDER, id);
            addon.initialize(this.loader, addonInfo, addonDataFolder);
            loadedAddons.put(name, addon);
            addon.onLoad();

            if (loadPlugin) {
                Plugin plugin = getPluginManager().loadPlugin(jarFile);

                if (plugin != null) {
                    getPluginManager().enablePlugin(plugin);
                }
            }

            logger.info("Loaded Solidity Addon: " + name + " (" + id + ") v" + version);
        }
    }

    public void unloadAddon(String name) {
        if (!loadedAddons.containsKey(name)) return;
        SolidityAddon addon = loadedAddons.remove(name);
        if (enabledAddons.containsKey(name)) enabledAddons.remove(name);
        if (addon != null) {
            addon.onDisable();
        }
    }

    /**
     * Loads all addons from a specified directory containing JAR files.
     *
     * @param directory The directory to search for JAR files.
     * @throws IllegalArgumentException If the specified path is not a directory.
     */
    public void loadAddonsFromDirectory(@NotNull File directory) {
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("The specified path is not a directory: " + directory.getAbsolutePath());
        }

        File[] jarFiles = directory.listFiles((dir, name) -> name.endsWith(".jar"));
        if (jarFiles != null) {
            for (File jarFile : jarFiles) {
                try {
                    loadAddon(jarFile);
                } catch (Exception e) {
                    getLogger().error("Unable to load Addon due to: \n" + e);
                }
            }
        }
    }

    /**
     * Automatically loads and enables all addons from the predefined addon directory.
     * This method utilizes the addon folder specified in the SolidityLoader class.
     */
    public void automatic() {
        loadAddonsFromDirectory(SolidityLoader.SOLIDITY_ADDON_FOLDER);
        enableAllAddons();
    }

    /**
     * Retrieves a loaded addon by its name.
     *
     * @param name The name of the addon to retrieve.
     * @return The corresponding SolidityAddon, or null if not found.
     */
    public SolidityAddon getAddon(String name) {
        return loadedAddons.get(name);
    }

    /**
     * Enables a specified addon, allowing it to perform its functionality.
     *
     * @param name The name of the addon to enable.
     */
    public void enableAddon(String name) {
        SolidityAddon addon = loadedAddons.get(name);
        if (addon != null) {
            addon.onEnable();
            enabledAddons.put(name, addon);
        } else {
            // Optionally log or throw an error if the addon is not found.
        }
    }

    /**
     * Disables a specified addon, preventing it from performing any further functionality.
     *
     * @param name The name of the addon to disable.
     */
    public void disableAddon(String name) {
        SolidityAddon addon = enabledAddons.remove(name);
        if (addon != null) {
            addon.onDisable();
        } else {
            // Optionally log or throw an error if the addon is not found or already disabled.
        }
    }

    /**
     * Enables all loaded addons, allowing them to perform their functionality.
     */
    public void enableAllAddons() {
        for (SolidityAddon addon : this.enabledAddons.values()) {
            this.enableAddon(addon.getInfo().getName());
        }
    }

    /**
     * Disables all currently enabled addons, preventing them from performing any further functionality.
     */
    public void disableAllAddons() {
        for (SolidityAddon addon : this.enabledAddons.values()) {
            this.disableAddon(addon.getInfo().getName());
        }
    }

    public boolean isLoaded(String addonName) {
        return loadedAddons.containsKey(addonName);
    }
}
