package io.soliditycraft.solidityloader.configuration;

import io.soliditycraft.solidityloader.addons.SolidityAddon;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
@Getter
public class SolidityConfiguration {

    private final JavaPlugin plugin;
    private final String fileName;
    private File configFile;
    private FileConfiguration config;

    public SolidityConfiguration(JavaPlugin plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
    }

    // Save the default configuration if the file does not exist
    public void saveDefaultConfig() {
        configFile = new File(plugin.getDataFolder(), fileName);
        if (!configFile.exists()) {
            plugin.saveResource(fileName, false); // Saves from the plugin's resources
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    // Save the configuration to file
    public void saveConfig() {
        if (config == null || configFile == null) return;
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Reload the configuration from file
    public void reloadConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), fileName);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    // Get the configuration object
    public FileConfiguration getConfig() {
        if (config == null) {
            reloadConfig();
        }
        return config;
    }

}
