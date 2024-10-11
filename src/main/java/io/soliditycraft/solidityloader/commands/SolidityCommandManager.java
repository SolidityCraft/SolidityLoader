package io.soliditycraft.solidityloader.commands;

import io.soliditycraft.solidityloader.SolidityLoader;
import io.soliditycraft.solidityloader.commands.cmds.BaseSolidityCommand;
import io.soliditycraft.solidityloader.commands.cmds.SolidityAddonsCommand;
import io.soliditycraft.solidityloader.commands.cmds.SolidityHelpCommand;
import io.soliditycraft.solidityloader.commands.cmds.SolidityReloadConfigs;
import io.soliditycraft.solidityloader.utils.SolUtils;
import lombok.Getter;
import org.bukkit.command.PluginCommand;
import org.jetbrains.annotations.NotNull;

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

    public void loadDefaultCommands() {
        this.registerCommand(new SolidityHelpCommand());
        this.registerCommand(new SolidityReloadConfigs());
        this.registerCommand(new SolidityAddonsCommand());
    }
}
