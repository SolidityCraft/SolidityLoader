package io.soliditycraft.solidityloader.commands;

import io.soliditycraft.solidityloader.SolidityLoader;
import io.soliditycraft.solidityloader.commands.cmds.BaseSolidityCommand;
import io.soliditycraft.solidityloader.commands.cmds.SolidityAddonsCommand;
import io.soliditycraft.solidityloader.commands.cmds.SolidityHelpCommand;
import io.soliditycraft.solidityloader.commands.cmds.SolidityReloadConfigs;
import io.soliditycraft.solidityloader.utils.SolUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.SimplePluginManager;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Getter
public class SolidityCommandManager {

    private final ISolidityCMDExecutor baseSolidityCommand = new BaseSolidityCommand(this);
    private final List<SolidityCommand> commands = new ArrayList<>();
    private final SolidityLoader loader;

    public SolidityCommandManager(SolidityLoader loader) {
        this.loader = loader;
    }

    public void registerCommand(@NotNull SolidityCommand command) {
        command.initialize(this.loader, this);
        commands.add(command);
    }

    public boolean isCommandRegistered(String name) {
        return SolUtils.find(getCommands(), (v) -> v.getName().equals(name)) != null;
    }

    public void loadBaseCommand() {
        PluginCommand command = getLoader().getCommand("solidity");
        assert command != null;
        command.setExecutor(baseSolidityCommand);
        command.setTabCompleter(baseSolidityCommand);
    }

    public void registerGlobalCommand(SolidityGlobalCommand command) {

        SolidityLoader.getInstance().reloadConfig();
        FileConfiguration configuration = SolidityLoader.getInstance().getConfig();
        boolean registerGlobalCommands = configuration.getBoolean("registering_global_commands", true);

        if (!registerGlobalCommands) return; // Disables global command registering through the configuration.

        try {
            command.initialize();
            Field commandMapField = SimplePluginManager.class.getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer().getPluginManager());

            commandMap.register(SolidityLoader.getInstance().getName(), command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadDefaultCommands() {
        this.registerCommand(new SolidityHelpCommand());
        this.registerCommand(new SolidityReloadConfigs());
        this.registerCommand(new SolidityAddonsCommand());
    }
}
